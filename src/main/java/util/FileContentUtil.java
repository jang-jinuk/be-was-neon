package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public class FileContentUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileContentUtil.class);

    public static String getExtension(String url) {
        String[] tokens = url.split("\\.");
        return tokens[1];
    }

    public static Optional<byte[]> getFileContent(String path) {
        Optional<byte[]> body = Optional.empty();
        try (FileInputStream fis = new FileInputStream("src/main/resources/" + path)) {
            byte[] data = fis.readAllBytes();
            body = Optional.of(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.debug("=====================request file path: {}===================", path);

        logger.debug("====================={}===================", new String(body.get()));
        return body;
    }

}
