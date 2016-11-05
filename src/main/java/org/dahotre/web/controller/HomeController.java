package org.dahotre.web.controller;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.dahotre.web.common.EvernoteData;
import org.dahotre.web.common.ViewNames;
import org.dahotre.web.helper.EvernoteSyncClient;
import org.dahotre.web.helper.S3Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controls home page as well as other one-off endpoints that won't need CRUD actions
 */
@Controller
@RequestMapping("/")
public class HomeController {

  public static final int PAGE_SIZE = 20;
  private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
  @Autowired
  EvernoteSyncClient evernoteSyncClient;
  @Autowired
  S3Helper s3Helper;

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

    final Notebook defaultNotebook = evernoteSyncClient.getDefaultNotebook();
    NoteFilter noteFilter = new NoteFilter();
    noteFilter.setNotebookGuid(defaultNotebook.getGuid());
    noteFilter.setOrder(NoteSortOrder.CREATED.getValue());
    noteFilter.setAscending(false);
    if (StringUtils.isNotBlank(query)) {
      noteFilter.setWords(query);
      isHomePage = false;
    }
    if (StringUtils.isNotBlank(tagId)) {
      noteFilter.setTagGuids(Lists.newArrayList(tagId));
      isHomePage = false;
    }
    final Integer noteCount = evernoteSyncClient.findNoteCounts(noteFilter, false)
        .getNotebookCounts().values().stream()
        .findFirst().get();

    NoteList noteList;
    if (isHomePage) {
      final int liveUpdateCount = evernoteSyncClient.getSyncState().getUpdateCount();
      if (liveUpdateCount > EvernoteData.updateCount || EvernoteData.homePageNoteList == null) {
        EvernoteData.updateCount = liveUpdateCount;
        EvernoteData.homePageNoteList = findNotes(page, noteFilter);
      }
      noteList = EvernoteData.homePageNoteList;
    }
    else {
      noteList = findNotes(page, noteFilter);
    }

    final List<Note> notes = noteList.getNotes();
    notes.stream()
        .filter(eachNote -> eachNote.getResourcesSize() > 0)
        .forEach(
            note -> note.getResources().forEach(
                resource -> {
                  try {
                    s3Helper.checkAndPut(resource);
                  } catch (EDAMUserException | EDAMSystemException | EDAMNotFoundException | TException e) {
                    LOG.error("Problem in checkAndPut resource " + resource.getGuid(), e);
                  }
                }
            )
        );
    final Map<String, String> noteToImgUrlMap = notes.parallelStream()
        .filter(note -> note != null && note.getResources() != null && note.getResourcesIterator().hasNext())
        .collect(Collectors.toMap(Note::getGuid
            , note -> s3Helper.generateS3ImageUrl(note.getResourcesIterator().next().getGuid()))
        );

    return new ModelAndView(ViewNames.HOME)
        .addObject("hello", "Hello world!")
        .addObject("noteCount", noteCount)
        .addObject("notes", notes)
        .addObject("noteToImgUrlMap", noteToImgUrlMap)
        .addObject("page", page)
        .addObject("totalPages", (noteCount/(PAGE_SIZE + 1)) + 1)
        .addObject("search", query)
        .addObject("tag", tag);
  }

  private NoteList findNotes(@RequestParam(name = "page", required = false, defaultValue = "1") Integer page, NoteFilter noteFilter) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException {
    return evernoteSyncClient.findNotes(noteFilter, (page - 1) * PAGE_SIZE, PAGE_SIZE);
  }
}
