package AI.hotel.controller;


import AI.hotel.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static AI.hotel.utils.FileUtils.getStaticPath;
import static AI.hotel.utils.toJson.toJsonArray;
import static AI.hotel.utils.toJson.toJsonString;

@RestController
@Slf4j
public class FileController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadImg")
    public String uploadImg(@RequestParam("file") MultipartFile file) {
        return uploadImg(file, "faceImgs");
    }


    @PostMapping("/uploadHotelImg")
    public String uploadHotelImg(@RequestParam("file") MultipartFile file) {
        return uploadImg(file, "imgs");
    }

    @PostMapping("/uploadMenuImg")
    public String uploadMenuImg(@RequestParam("file") MultipartFile file) {
        return uploadImg(file, "DishImgs");
    }

    @GetMapping(value = "/static/{url}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable String url, HttpServletResponse response) throws IOException {
        File file = FileUtils.getNewFile(url);
        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes, 0, inputStream.available());
                return bytes;
            } catch (IOException e) {
                log.error("picture exists but error!!!! fileName" + url);
                return null;
            }
        }
        response.sendError(404);
        return null;
    }

    String uploadImg(@RequestParam("file") MultipartFile file, String site) {
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));

                fileName = UUID.randomUUID() + suffixName;
                File dest = new File(getStaticPath(site) + fileName);
                file.transferTo(dest);// 文件写入
                if (!site.equals("faceImgs")){
                    Thumbnails.of(getStaticPath(site) + fileName)
                            .scale(1f)
                            .outputQuality(0.25f)
                            .outputFormat("jpg")
                            .toFile(getStaticPath(site) + fileName);
                }
                log.info("文件上传成功:" + fileName);
                return toJsonArray(200, "上传文件成功", fileName);
            } catch (IOException e) {
                log.info("上传文件异常：" + e.getMessage());
                return toJsonString(100, "上传文件异常", null);
            }
        } else {
            return toJsonString(100, "文件无效", null);
        }
    }


    /**
     * 二次开发，获取当前年份的所有已生成客流分析-xmls
     *
     * @param
     * @return
     */
    @GetMapping("/getCFStatus")
    public String getCFStatus() {
        if (redisTemplate.hasKey("FileListDetail")) {
            return toJsonArray(200, "获取文件细节成功", redisTemplate.opsForValue().get("FileListDetail"));
        }
        return toJsonArray(200, "获取文件细节成功", update());
    }

    @Async
    public Map<String, Object> update() {
        Map<String, Object> dirs = new HashMap<>();
        for (String str : new File(getStaticPath("passengerAnalysis")).list()) {
            dirs.put(str, new File(getStaticPath("passengerAnalysis") + str).list());
        }
        redisTemplate.opsForValue().set("FileListDetail", dirs);
        return dirs;
    }


    @GetMapping("/downAnalysis/{year}/{fileName}")
    public String downAnalysis(@PathVariable String year, @PathVariable String fileName,
                               HttpServletResponse response) throws IOException {
        File file = new File(getStaticPath("passengerAnalysis") + year, fileName);
        if (file.exists()) {
            String filePath = getStaticPath("passengerAnalysis") + year + File.separator + fileName;
            filePath = URLDecoder.decode(filePath, "UTF-8");
            OutputStream os = response.getOutputStream();
            InputStream in = new FileInputStream(filePath);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "");
            response.setHeader("Content-Length", String.valueOf(in.available()));
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(in, os);
            os.flush();
            log.info("下载文件成功，文件名：" + fileName);
        }
        return toJsonString(100, "文件不存在", null);
    }

}




