package com.comp680backend.util;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CloudStorageUtil {
    public static final String BUCKET_NAME = "comp583";

    // [START gcs]
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
    // [END gcs]

    public String processImageUploadRequest(HttpServletRequest req) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        final String fileName = filePart.getSubmittedFileName();
        // Check extension of file
        if (fileName != null && fileName.length() > 0 && fileName.contains(".")) {
            final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            String[] allowedExt = {"jpg", "jpeg", "png", "gif"};
            for (String s : allowedExt) {
                if (extension.equals(s)) {
                    return uploadImage(filePart);
                }
            }
        }
        return null;
    }

    private String uploadImage(Part filePart) throws IOException {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYY-MM-dd-HHmmssSSS");
        DateTime dt = DateTime.now(DateTimeZone.UTC);
        String dtString = dt.toString(dtf);
        String fileExtension = filePart.getSubmittedFileName().substring(filePart.getSubmittedFileName().indexOf(".") + 1);

        final String fileName = dtString + "." + fileExtension;

        ByteArrayOutputStream byteArrayOutputStream = IOUtils.toByteArrayOutputStream(filePart.getInputStream());
        if (byteArrayOutputStream != null) {
            gcsService.createOrReplace(
                    new GcsFilename(BUCKET_NAME, fileName),
                    new GcsFileOptions.Builder().mimeType("image/"+fileExtension).build(),
                    ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));

            ImagesService imagesService = ImagesServiceFactory.getImagesService();
//            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
//            BlobKey blobKey = blobstoreService.createGsBlobKey("/gs/" + BUCKET_NAME + "/" + fileName);

            ServingUrlOptions options = ServingUrlOptions.Builder
                    .withGoogleStorageFileName("/gs/" + BUCKET_NAME + "/" + fileName)
                    .secureUrl(false);
            return imagesService.getServingUrl(options);
        }
        return null;
    }

}
