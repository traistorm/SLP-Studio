package traistorm.slpstudio.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;

public class ResourceUtils {
    public static File loadFileFromPathInResource(String path) throws IOException {
        ClassLoader classLoader = ResourceUtils.class.getClassLoader();

        File file = new File(path);

        String parent = file.getParent(); // Lấy phần path (thư mục cha)
        String filename = file.getName(); // Lấy phần file name (tên tệp)
        return new File(classLoader.getResource(parent).getPath() + "/" + filename);
    }
    public static File loadFileInResource(String filename) throws IOException {
        ClassLoader classLoader = ResourceUtils.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }
    public void test() {
    }
}
