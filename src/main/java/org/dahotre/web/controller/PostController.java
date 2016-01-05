package org.dahotre.web.controller;

import com.evernote.edam.type.Note;
import com.evernote.edam.type.Resource;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.dahotre.web.common.ViewNames;
import org.dahotre.web.helper.EvernoteSyncClient;
import org.dahotre.web.helper.S3Helper;
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

  public static final boolean WITH_CONTENT = true;
  public static final boolean WITHOUT_RESOURCES_DATA = false;
  public static final boolean WITHOUT_RESOURCES_RECOGNITION = false;
  public static final boolean WITHOUT_RESOURCES_ALTERNATE_DATA = false;
  Logger LOG = LoggerFactory.getLogger(PostController.class);

  @Autowired
  EvernoteSyncClient evernoteSyncClient;

  @Autowired
  S3Helper s3Helper;


  @RequestMapping("/{guid}")
  public ModelAndView show(@PathVariable String guid) throws Exception {
    final Note note = evernoteSyncClient.getNote(guid, WITH_CONTENT, WITHOUT_RESOURCES_DATA, WITHOUT_RESOURCES_RECOGNITION, WITHOUT_RESOURCES_ALTERNATE_DATA);

    //Check if the note's resource are already uploaded by looking up in S3. If not, then upload it in the site bucket.
    if (note.getResourcesSize() > 0) {
      for (Resource resource : note.getResources()) {
        s3Helper.checkAndPut(resource);
      }
    }
    return new ModelAndView(ViewNames.POSTS_SHOW)
        .addObject("title", note.getTitle())
        .addObject("content", convertToHtml(note))
        .addObject("noteTagIds", note.getTagGuids())
        .addObject("guid", guid);
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
    enNoteElement.attr("class", "col-sm-12");

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
          mediaElem.attr("src", s3Helper.generateS3ImageUrl(resource.getGuid()));

          Double height = new Double(resource.getHeight());
          Double width = new Double(resource.getWidth());
          if (width > 640d) {
            height = height * 640d / width;
            width = 640d;
          }

          mediaElem.attr("height", String.valueOf(height.shortValue()));
          mediaElem.attr("width", String.valueOf(width.shortValue()));
          mediaElem.attr("class", "img-responsive center-block");
        }
      });
    }
    return enNoteElement.outerHtml();
  }
}
