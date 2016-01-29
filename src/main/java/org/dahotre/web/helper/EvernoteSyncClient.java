package org.dahotre.web.helper;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteCollectionCounts;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.SyncState;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Tag;
import com.evernote.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Restricts parallel access to Evernote note client to avoid out-of-sequence request exception.
 */
@Component
public class EvernoteSyncClient {

  @Autowired
  NoteStoreClient noteStoreClient;

  @Value("#{systemEnvironment['EVERNOTE_NOTEBOOK_NAME']}")
  private String evernoteNotebookName;

  public synchronized byte[] getImageBytes(String guid) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
    return noteStoreClient.getResourceData(guid);
  }

  public synchronized SyncState getSyncState() throws TException, EDAMUserException, EDAMSystemException {
    return noteStoreClient.getSyncState();
  }

  public synchronized List<Tag> listTags() throws TException, EDAMUserException, EDAMSystemException, EDAMNotFoundException {
    return noteStoreClient.listTagsByNotebook(getDefaultNotebook().getGuid());
  }

  public synchronized NoteList findNotes(NoteFilter noteFilter, int offset, int pageSize) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
    return noteStoreClient.findNotes(noteFilter, offset, pageSize);
  }

  public synchronized NoteCollectionCounts findNoteCounts(NoteFilter noteFilter, boolean withTrash) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
    return noteStoreClient.findNoteCounts(noteFilter, withTrash);
  }

  public synchronized Notebook getDefaultNotebook() throws TException, EDAMUserException, EDAMSystemException {
    return noteStoreClient.listNotebooks().stream()
        .filter(notebook -> notebook.getName().equalsIgnoreCase(evernoteNotebookName))
        .findFirst().get();
  }

  public synchronized Note getNote(String guid, boolean withContent, boolean withResourcesData, boolean withResourcesRecognition, boolean withResourcesAlternateData) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
    return noteStoreClient.getNote(guid, withContent, withResourcesData, withResourcesRecognition, withResourcesAlternateData);
  }
}
