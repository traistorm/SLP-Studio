package traistorm.slpstudio.utils;

import java.io.File;
import java.net.URL;

public class ResourceUtils {
    public static File loadFileFromResource(String filename) {
        ClassLoader classLoader = ResourceUtils.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(filename);
        if (resourceUrl == null) {
            throw new IllegalArgumentException("Không tìm thấy tệp trong resource: " + filename);
        }

        // Tạo đối tượng File từ URL
        return new File(resourceUrl.getFile());
    }
}
