package org.dahotre.web.controller;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;
import com.evernote.edam.userstore.PublicUserInfo;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.dahotre.web.common.ViewNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Displays posts
 */
@Controller
@RequestMapping("/posts")
public class PostController {

  Logger LOG = LoggerFactory.getLogger(PostController.class);

  @Autowired
  NoteStoreClient noteStoreClient;

  @Autowired
  PublicUserInfo publicUserInfo;

  @RequestMapping("/{guid}")
  public ModelAndView show(@PathVariable String guid) throws Exception {
    final Note note = noteStoreClient.getNote(guid, true, false, false, false);
    return new ModelAndView(ViewNames.POSTS_SHOW)
        .addObject("title", note.getTitle())
        .addObject("content", convertToHtml(note))
        .addObject("tags", note.getTagNames());
  }

  @RequestMapping("/{guid}/title/{urlTitle}")
  public ModelAndView vanityShow(@PathVariable String guid, @PathVariable String urlTitle) throws Exception {
    return show(guid);
  }

  public String convertToHtml(final Note note) {
    Document.OutputSettings outputSettings = new Document.OutputSettings();
    outputSettings.indentAmount(0);

    Document document = Jsoup.parse(note.getContent()).outputSettings(outputSettings);
    Element enNoteElement = document.getElementsByTag("en-note").get(0);

    enNoteElement.tagName("div");

    enNoteElement.getElementsByTag("en-todo").forEach(todo -> {
      todo.tagName("input");
      todo.attr("type", "checkbox");
    });

    if (note.getResources() != null) {
      //Map resource's hash to itself for easy access
      final Map<String, Resource> imageHashToResourceMap = note.getResources().parallelStream()
          .collect(
              Collectors.toMap(
                  resource -> Hex.encodeHexString(resource.getData().getBodyHash())
                  , Function.<Resource>identity())
          );

      //Convert en-media tag to img
      enNoteElement.getElementsByTag("en-media").forEach(mediaElem -> {
        String type = mediaElem.attr("type");
        String hash = mediaElem.attr("hash");

        if (StringUtils.isNotBlank(type)  //has attribute type
            && StringUtils.containsIgnoreCase(type, "image")  // resource is of type image
            && StringUtils.isNotBlank(hash)) {  // has attr hash
          final Resource resource = imageHashToResourceMap.get(hash);

          mediaElem.tagName("img");
          mediaElem.attr("src", String.format("%sres/%s", publicUserInfo.getWebApiUrlPrefix(), resource.getGuid()));
          mediaElem.attr("height", String.valueOf(resource.getHeight()));
          mediaElem.attr("width", String.valueOf(resource.getWidth()));
        }
      });
    }
    return enNoteElement.outerHtml();
  }
}
