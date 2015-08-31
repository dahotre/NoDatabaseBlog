package org.dahotre.web.common;

import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Tag;

import java.util.List;

/**
 * Holds expensive Evernote data.
 */
public class EvernoteData {
  public static int updateCount = -1;
  public static List<Tag> globalTags;
  public static NoteList homePageNoteList;
  private EvernoteData() {
  }
}
