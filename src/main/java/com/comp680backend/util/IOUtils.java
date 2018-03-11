package com.comp680backend.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {
    public static ByteArrayOutputStream toByteArrayOutputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                baos.write(data, 0, nRead);
            }

            baos.flush();
        } catch (IOException e) {
            return null;
        }
        return baos;
    }

}
