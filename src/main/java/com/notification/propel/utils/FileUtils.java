package com.notification.propel.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notification.propel.messages.CustomMessages;

/**
 * @author aishwaryt
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
        throw new IllegalStateException(CustomMessages.UTILITY_CLASS);
    }

    /**
     * This method will return the file size for a file present at URL
     * {@code fileURL}.<br>
     * Note: If the transfer-encoding is set to 'chunked', you will get -1.
     * 
     * @param fileURL
     * @return file size
     */
    public static long getFileSizeFromURL(final String fileURL) {
        URLConnection conn = null;
        try {
            URL url = new URL(fileURL);
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLengthLong();
        } catch (IOException exception) {
            logger.error(
                        CustomMessages.REQUEST_FAILED_FOR_FILE_SIZE_FETCH.concat(
                                    CustomMessages.PLACEHOLDER),
                        exception.getMessage());
            throw new RuntimeException(
                        CustomMessages.REQUEST_FAILED_FOR_FILE_SIZE_FETCH);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    /**
     * This method will return the total file sizes for all the files present at
     * their respective URL<br>
     * Note: If the transfer-encoding is set to 'chunked', you will get -1.
     * 
     * @return total file size
     */
    public static long getFileSizeFromURL(final String... fileURLs) {
        long totalSize = 0;
        for (String file : fileURLs) {
            totalSize += getFileSizeFromURL(file);
        }
        return totalSize;
    }

}
