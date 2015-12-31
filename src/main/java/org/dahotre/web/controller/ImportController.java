package org.dahotre.web.controller;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Data;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.ResourceAttributes;
import com.evernote.thrift.TException;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * TODO javadocs.
 */
@Controller
@RequestMapping("/import")
public class ImportController {

  @Autowired
  NoteStoreClient noteStoreClient;

  @RequestMapping(method = RequestMethod.GET)
  public String getImportForm(@RequestParam(required = true) String tumblrBlogUrl) throws IOException, EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException, InterruptedException {
    final Map<String, Object> objectMap = getStringObjectMap(0, tumblrBlogUrl);
    int totalPosts = (Integer) objectMap.get("posts-total");
    System.out.println("totalPosts = " + totalPosts);

    for (int i = 185; i < totalPosts; i++) {
      final Map<String, Object> stringObjectMap = getStringObjectMap(i, tumblrBlogUrl);
      final List<Map<String, Object>> posts = (List<Map<String, Object>>) stringObjectMap.get("posts");
      final Map<String, Object> post = posts.get(0);

      StringBuilder noteBodyBuilder = new StringBuilder();
      noteBodyBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      noteBodyBuilder.append("<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">");
      noteBodyBuilder.append("<en-note>");
      Note note = new Note();

      String nTitle;
      if (post.get("regular-title") != null) {
        nTitle = (String) post.get("regular-title");
      }
      else {
        nTitle = ((String)post.get("slug")).replace("-", " ");
      }

      note.setTitle(StringUtils.capitalize(nTitle).trim());

      if (StringUtils.equals((String) post.get("type"), "photo")) {
        StringBuilder imgMD5 = new StringBuilder();
        Data imgdata = new Data();

        // 1. get image URL
        Object imgURLObj;
        if ((imgURLObj = post.get("photo-url-1280")) != null
            || (imgURLObj = post.get("photo-url-500")) != null
            || (imgURLObj = post.get("photo-url-400")) != null
            || (imgURLObj = post.get("photo-url-250")) != null
            ) {
          String imgURL = (String) imgURLObj;

          // 2. get image bytes
          try (InputStream inputStream = new URL(imgURL.trim()).openStream()) {
            byte[] bytes = ByteStreams.toByteArray(inputStream);
            imgdata.setBody(bytes);

            // 3. md5 hash image bytes
            imgMD5.append(DigestUtils.md5Hex(bytes));
          }


        }
        noteBodyBuilder.append(
            String.format("<en-media type=\"image/jpg\" hash=\"%s\" /><br />", imgMD5.toString())
        );
        noteBodyBuilder.append((String) post.get("photo-caption"));

        // 4. note.setResources
        Resource resource = new Resource();
        resource.setData(imgdata);
        resource.setMime("image/jpeg");
        ResourceAttributes attributes = new ResourceAttributes();
        attributes.setFileName(nTitle.replace(" ", "-") + ".jpg");
        resource.setAttributes(attributes);
        note.setResources(Lists.newArrayList(resource));
      }
      else {
        noteBodyBuilder.append((String) post.get("regular-body"));
      }

      noteBodyBuilder.append("</en-note>");
      note.setContent(noteBodyBuilder.toString());

      if (post.get("tags") != null) {
        List<String> tags = (List<String>) post.get("tags");
        if (!tags.isEmpty()) {
          note.setTagNames(tags);
        }
      }

      long timestamp = ((Integer) post.get("unix-timestamp")).longValue() * 1000;
      note.setCreated(timestamp);
      note.setUpdated(timestamp);

      System.out.println("note = " + note);
      try {
        Note createdNote = noteStoreClient.createNote(note);
        System.out.println("createdNote = " + createdNote);
      }
      catch (Exception e) {
        LoggerFactory.getLogger(ImportController.class).error("problem with note", e);
      }
      Thread.sleep(7000l);
    }


    return "import";
  }

  public Map<String, Object> getStringObjectMap(int i, String tumblrBlogUrl) throws IOException {
    String tumblrReadURLFormat = "http://" + tumblrBlogUrl + "/api/read/json?start=%d&num=1&debug=1";
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(new URL(String.format(tumblrReadURLFormat, i)), Map.class);
  }
}
