package org.dahotre.web.controller;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.userstore.PublicUserInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.dahotre.web.common.EvernoteData;
import org.dahotre.web.common.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controls home page as well as other one-off endpoints that won't need CRUD actions
 */
@Controller
@RequestMapping("/")
public class HomeController {

  @Autowired
  NoteStoreClient noteStoreClient;

  @Autowired
  PublicUserInfo publicUserInfo;

  @RequestMapping("")
  public ModelAndView getHome(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page
      , @RequestParam(name = "search", required = false) String query
      , @RequestParam(name = "tagId", required = false) String tagId
      , @RequestParam(name = "tag", required = false) String tag) throws Exception {

    boolean isHomePage = true;  //This is used to decide if the request is for the home page.
    // The results will be cached in a static variable if this remains true.
    if (page != 1) {
      isHomePage = false;
    }

    final Notebook defaultNotebook = noteStoreClient.getDefaultNotebook();
    NoteFilter noteFilter = new NoteFilter();
    noteFilter.setNotebookGuid(defaultNotebook.getGuid());
    noteFilter.setAscending(false);
    if (StringUtils.isNotBlank(query)) {
      noteFilter.setWords(query);
      isHomePage = false;
    }
    if (StringUtils.isNotBlank(tagId)) {
      noteFilter.setTagGuids(Lists.newArrayList(tagId));
      isHomePage = false;
    }
    final Integer noteCount = noteStoreClient.findNoteCounts(noteFilter, false)
        .getNotebookCounts().values().stream()
        .findFirst().get();

    NoteList noteList;
    if (isHomePage) {
      final int liveUpdateCount = noteStoreClient.getSyncState().getUpdateCount();
      if (liveUpdateCount > EvernoteData.updateCount || EvernoteData.homePageNoteList == null) {
        EvernoteData.updateCount = liveUpdateCount;
        EvernoteData.homePageNoteList = noteStoreClient.findNotes(noteFilter, page - 1, 20);
      }
      noteList = EvernoteData.homePageNoteList;
    }
    else {
      noteList = noteStoreClient.findNotes(noteFilter, page - 1, 20);
    }

    final Map<String, String> noteToImgUrlMap = noteList.getNotes().parallelStream()
        .filter(note -> note != null && note.getResources() != null && note.getResourcesIterator().hasNext())
        .collect(Collectors.toMap(Note::getGuid
                , note -> String.format("%sres/%s", publicUserInfo.getWebApiUrlPrefix(), note.getResourcesIterator().next().getGuid()))
        );

    return new ModelAndView(ViewNames.HOME)
        .addObject("hello", "Hello world!")
        .addObject("noteCount", noteCount)
        .addObject("notes", noteList.getNotes())
        .addObject("noteToImgUrlMap", noteToImgUrlMap)
        .addObject("page", page)
        .addObject("totalPages", (noteCount/21) + 1)
        .addObject("search", query)
        .addObject("tag", tag);
  }
}
