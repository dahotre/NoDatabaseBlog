package org.dahotre.web.helper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Resource;
import com.evernote.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Helper utils for S3
 */
@Component
public class S3Helper {

  private static final Logger LOG = LoggerFactory.getLogger(S3Helper.class);
  @Autowired
  EvernoteSyncClient evernoteSyncClient;
  @Autowired
  AmazonS3 s3;
  @Value("#{systemEnvironment['AWS_SITE_BUCKET']}")
  String siteBucket;

  public void checkAndPut(Resource resource) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
    String mime = resource.getMime();
    String resourceGuid = resource.getGuid();
    String s3ImageKey = String.format("images/%s", resourceGuid);
    try {
      s3.getObjectMetadata(siteBucket, s3ImageKey);
    } catch (AmazonS3Exception s3Exception) {
      byte[] imageBytes = evernoteSyncClient.getImageBytes(resourceGuid);
      InputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);

      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(mime);
      metadata.setContentLength(imageBytes.length);
      metadata.setHeader("x-amz-acl", "public-read");

      s3.putObject(siteBucket, s3ImageKey, byteArrayInputStream, metadata);
    }
  }

  public String generateS3ImageUrl(final String guid) {
    return String.format("https://s3.amazonaws.com/%s/images/%s", siteBucket, guid);
  }
}
