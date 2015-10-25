package org.dahotre.web.controller;

import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Gets images from Evernote
 */
@Controller
@RequestMapping("/images")
public class ImageController {

  @Autowired
  EvernoteSyncClient evernoteSyncClient;

  @ResponseBody
  @RequestMapping(value = "/{guid}", produces = MediaType.IMAGE_JPEG_VALUE)
  public byte[] getImage(@PathVariable String guid) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
    return evernoteSyncClient.getImageBytes(guid);
  }
}
