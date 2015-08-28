package org.dahotre.web.controller;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NotesMetadataList;
import com.evernote.edam.notestore.NotesMetadataResultSpec;
import com.evernote.edam.type.Notebook;
import org.dahotre.web.common.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controls home page as well as other one-off endpoints that won't need CRUD actions
 */
@Controller
@RequestMapping("/")
public class HomeController {

  @Autowired
  NoteStoreClient noteStoreClient;

  @RequestMapping("")
  public ModelAndView getHome(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page) throws Exception {
    final Notebook defaultNotebook = noteStoreClient.getDefaultNotebook();
    NoteFilter noteFilter = new NoteFilter();
    noteFilter.setNotebookGuid(defaultNotebook.getGuid());
    noteFilter.setAscending(false);
    final Integer noteCount = noteStoreClient.findNoteCounts(noteFilter, false)
        .getNotebookCounts().values().stream()
        .findFirst().get();

    NotesMetadataResultSpec metadataResultSpec = new NotesMetadataResultSpec();
    metadataResultSpec.setIncludeTitle(true);
    metadataResultSpec.setIncludeTagGuids(true);
    metadataResultSpec.setIncludeCreated(true);
    NotesMetadataList metadataList = noteStoreClient.findNotesMetadata(noteFilter, page, 20, metadataResultSpec);

    return new ModelAndView(ViewNames.HOME)
        .addObject("hello", "Hello world!")
        .addObject("noteCount", noteCount)
        .addObject("notes", metadataList.getNotes());
  }
}
