package AI.hotel.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class FileUtils {

    public static File getNewFile(String url) {
        File file2 = new File(getStaticPath("OmitImg") + url);
        if (file2.exists()) return file2;
        File file = new File(getStaticPath("imgs") + url);
        if (file.exists()) return file;
        file = new File(getStaticPath("DishImgs") + url);
        if (file.exists()) return file;
        return file;
    }

    public static String getStaticPath(String d) {
        try {
            File file = new File("");
            File dir = new File("src/main/resources/static/" + d);
            return file.getCanonicalPath() + File.separator + dir + File.separator;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(String path, String name) {
        File file = new File(getStaticPath(path) + name);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
        log.info("删除文件：" + name);
    }

}
