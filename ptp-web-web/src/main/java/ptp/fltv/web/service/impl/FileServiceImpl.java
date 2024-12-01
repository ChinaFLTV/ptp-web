package ptp.fltv.web.service.impl;

import cn.hutool.core.net.URLEncodeUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pfp.fltv.common.exceptions.PtpException;
import ptp.fltv.web.service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/8 PM 3:15:01
 * @description 文件读写服务接口的实现类
 * @filename FileServiceImpl.java
 */

@Slf4j
@Service
public class FileServiceImpl implements FileService {


    // 2024-11-9  21:27-所有上传文件被存放的路径的基础路径
    @Value("${upload.file.base-path}")
    private String BASE_PATH;
    // 2024-11-8  16:58-本机的真实物理IP
    @Value("${ip.physical.self-host:127.0.0.1}")
    private String SELF_HOST_IP;


    @Override
    public void previewSingleFile(HttpServletResponse response, String relativePath) throws IOException {

        // 2024-12-1  22:42-解决通过API fox发送HTTP请求时 , relativePath字段总是以逗号结尾的问题
        if (relativePath.endsWith(",")) {

            relativePath = relativePath.substring(0, relativePath.length() - 1);

        }

        String absolutePath = (BASE_PATH + File.separator + relativePath).replace("/", File.separator)
                .replace("\\", File.separator); // 2024-12-1  22:42-解决用户给出的路径中采用了可能与应用运行平台不一致的路径分隔符的问题以避免路径解析异常
        String extension = absolutePath.substring(absolutePath.lastIndexOf(".") + 1).toLowerCase();
        String contentType = extension2ContentType(extension);

        File targetFile = new File(absolutePath);
        if (targetFile.exists()) {

            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline;filename=" + URLEncodeUtil.encode(getFilenameFromPath(absolutePath), StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(Files.readAllBytes(Path.of(absolutePath)));
            outputStream.flush();
            outputStream.close();

        } else {

            throw new PtpException(818, "不存在指定的文件资源!");

        }

    }


    @Override
    public void downloadSingleFile(@Nonnull HttpServletResponse response, @Nonnull String relativePath) throws IOException {

        // 2024-11-8  19:02-解决通过API fox发送HTTP请求时 , relativePath字段总是以逗号结尾的问题
        if (relativePath.endsWith(",")) {

            relativePath = relativePath.substring(0, relativePath.length() - 1);

        }

        String absolutePath = (BASE_PATH + File.separator + relativePath).replace("/", File.separator)
                .replace("\\", File.separator); // 2024-11-8  17:17-解决用户给出的路径中采用了可能与应用运行平台不一致的路径分隔符的问题以避免路径解析异常

        File targetFile = new File(absolutePath);
        if (targetFile.exists()) {

            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncodeUtil.encode(getFilenameFromPath(absolutePath), StandardCharsets.UTF_8));
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(Files.readAllBytes(Path.of(absolutePath)));
            outputStream.flush();
            outputStream.close();

        } else {

            throw new PtpException(818, "不存在指定的文件资源!");

        }

    }


    @Override
    public String uploadSingleFile(@Nonnull String relativePath, @Nonnull MultipartFile file) {

        if (!file.isEmpty()) {

            // 2024-11-8  19:02-解决通过API fox发送HTTP请求时 , relativePath字段总是以逗号结尾的问题
            if (relativePath.endsWith(",")) {

                relativePath = relativePath.substring(0, relativePath.length() - 1);

            }

            String absolutePath = (BASE_PATH + File.separator + relativePath).replace("/", File.separator)
                    .replace("\\", File.separator); // 2024-11-8  15:52-解决用户给出的路径中采用了可能与应用运行平台不一致的路径分隔符的问题以避免路径解析异常

            // 2024-11-3  15:42-处理用户期望的文件存放所在目录可能会不存在的情况
            File destFile = new File(absolutePath);

            // 2024-11-8  17:54-如果已存在同名同路径的文件资源 , 则拒绝本次文件上传操作
            if (destFile.exists()) {

                throw new PtpException(818, "已存在目标资源!");

            }

            if (!destFile.getParentFile().exists()) {

                boolean isCreated = destFile.getParentFile().mkdirs();
                if (!isCreated) {

                    return null; // 2024-11-8  15:45-如果父级目录都创建失败了 , 那么也就没有继续进行下去的必要了 , 因此直接返回null以失败告终

                }

            }

            try {

                file.transferTo(destFile);

            } catch (IOException ex) {

                log.error("[{}] : {} occurred : {}", "ptp-web-web : " + FileServiceImpl.class.getCanonicalName(), ex.getClass().getName(), ex.getCause() == null ? ex.getLocalizedMessage() : ex.getCause().getMessage());
                return null; // 2024-11-8  15:47-只要在传输文件到服务器的过程中出现IO异常 , 则也要直接返回null以失败告终

            }

            return "http://" + SELF_HOST_IP + ":8150/api/v1/web/resource/file/download/single/" + relativePath.replace("\\", "/"); // 2024-11-8  17:14-这里需要将全部的文件分隔符转换为URL路径的分隔符(Linux样式的文件分隔符无需替换)

        }

        return null;

    }


    @Override
    public List<String> uploadMultipleFile(@Nonnull List<String> relativePaths, @Nonnull MultipartFile[] files) {


        int length = Math.min(relativePaths.size(), files.length); // 2024-11-16  19:32-如果真的出现用户上传的文件路径列表和文件列表数量不一致的情况 , 则取二者长度的较小值作为最终生产的直链列表的容量

        List<String> urls = new ArrayList<>();

        for (int i = 0; i < length; i++) {

            urls.add(uploadSingleFile(relativePaths.get(i), files[i])); // 2024-11-16  19:34-这里为了简化业务逻辑 , 便直接采用现成的上传文件的API接口

        }

        return urls;

    }


    /**
     * @param path 文件路径
     * @return 提取出来的文件名称 , 如果提取失败则返回null
     * @author Lenovo/LiGuanda
     * @date 2024/11/8 PM 5:35:06
     * @version 1.0.0
     * @description 从所给的文件路径中
     * @filename FileServiceImpl.java
     */
    private String getFilenameFromPath(@Nonnull String path) {

        if (StringUtils.hasLength(path)) {

            String filename = path.substring(path.lastIndexOf(File.separator) + 1);
            if (!StringUtils.hasLength(filename)) {

                filename = path.substring(path.lastIndexOf("/") + 1); // 2024-11-8  17:40-这里的操作主要用于解决URI中提取文件名称的情况

            }

            return filename;

        }

        return null;

    }


    @Override
    public boolean deleteSingleFile(@Nonnull String relativePath) {

        // 2024-11-9  19:13-解决通过API fox发送HTTP请求时 , relativePath字段总是以逗号结尾的问题
        if (relativePath.endsWith(",")) {

            relativePath = relativePath.substring(0, relativePath.length() - 1);

        }

        String absolutePath = (BASE_PATH + File.separator + relativePath).replace("/", File.separator)
                .replace("\\", File.separator); // 2024-11-9  19:13-解决用户给出的路径中采用了可能与应用运行平台不一致的路径分隔符的问题以避免路径解析异常

        File targetFile = new File(absolutePath);
        if (targetFile.exists()) {

            return targetFile.delete();

        } else {

            throw new PtpException(818, "不存在指定的文件资源!");

        }

    }


    /**
     * @param extension 文件的后缀名
     * @return 与该后缀名对应的内容类型
     * @author Lenovo/LiGuanda
     * @date 2024/12/1 PM 10:46:05
     * @version 1.0.0
     * @description 根据文件后缀名转换为对应的响应内容类型
     * @filename FileServiceImpl.java
     */
    private String extension2ContentType(String extension) {

        if (!StringUtils.hasLength(extension)) {

            return "application/octet-stream";

        }

        return switch (extension.toLowerCase()) {

            case "html", "htm" -> "text/html";
            case "jpg", "jpeg", "png", "webp" -> "image/jpeg";
            case "mp3" -> "audio/mpeg";
            case "mp4", "avi" -> "video/mp4";
            case "xml", "xhtml" -> "application/xhtml+xml";
            case "json" -> "application/json";
            case "txt", "md" -> "text/plain";
            case "zip" -> "application/zip";
            case "gz" -> "application/x-gzip";
            case "bz2" -> "application/x-bzip2";
            default -> "application/octet-stream";

        };

    }

}