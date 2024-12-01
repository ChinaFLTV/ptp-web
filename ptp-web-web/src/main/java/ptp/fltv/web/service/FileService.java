package ptp.fltv.web.service;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Lenovo/LiGuanda
 * @version 1.0.0
 * @date 2024/11/8 PM 3:14:27
 * @description 文件读写服务接口
 * @filename FileService.java
 */

public interface FileService {


    /**
     * @param relativePath 用户期望上传的文件所保存的相对路径(相对于当前Web程序运行所在的路径)(路径需要包含文件完整的名称)(不要以斜杠开头)
     * @param file         用户上传到服务器端的文件
     * @return 如果文件保存成功 , 则返回对应的云端资源访问直链 ; 否则 , 保存失败则返回null
     * @throws IOException 在进行IO流读写时 , 出现异常时将会抛出此异常
     * @author Lenovo/LiGuanda
     * @date 2024/11/8 PM 3:25:04
     * @version 1.0.0
     * @description 保存单个文件到指定相对路径上
     * @filename FileService.java
     */
    String uploadSingleFile(@Nonnull String relativePath, @Nonnull MultipartFile file);


    /**
     * @param relativePaths 用户期望上传的多个文件所保存的对应的相对路径(相对于当前Web程序运行所在的路径)(路径需要包含文件完整的名称)(不要以斜杠开头)
     * @param files         用户上传到服务器端的多个文件
     * @return 如果文件保存成功 , 则返回对应的云端资源访问直链列表 ; 否则 , 保存失败则返回null
     * @throws IOException 在进行IO流读写时 , 出现异常时将会抛出此异常
     * @author Lenovo/LiGuanda
     * @date 2024/11/16 PM 7:30:39
     * @version 1.0.0
     * @description 保存多个文件到指定相对路径上
     * @filename FileService.java
     */
    List<String> uploadMultipleFile(@Nonnull List<String> relativePaths, @Nonnull MultipartFile[] files);


    /**
     * @param response     HTTP响应
     * @param relativePath 待下载的文件的云端相对路径(即该文件上传时选择的相对存放路径)(路径要求见上传文件时的相对路径的要求)
     * @author Lenovo/LiGuanda
     * @date 2024/12/1 PM 10:40:53
     * @version 1.0.0
     * @description 预览指定相对路径上的单个文件
     * @filename FileService.java
     */
    void previewSingleFile(HttpServletResponse response, String relativePath) throws IOException;


    /**
     * @param response     HTTP响应
     * @param relativePath 待下载的文件的云端相对路径(即该文件上传时选择的相对存放路径)(路径要求见上传文件时的相对路径的要求)
     * @author Lenovo/LiGuanda
     * @date 2024/11/8 PM 5:15:37
     * @version 1.0.0
     * @description 下载指定相对路径上的单个文件
     * @filename FileService.java
     */
    void downloadSingleFile(@Nonnull HttpServletResponse response, @Nonnull String relativePath) throws IOException;


    /**
     * @param relativePath 待删除的文件的云端相对路径(即该文件上传时选择的相对存放路径)(路径要求见上传文件时的相对路径的要求)
     * @return 是否成功删除指定相对路径上的单个文件
     * @author Lenovo/LiGuanda
     * @date 2024/11/9 PM 7:10:49
     * @version 1.0.0
     * @description 删除指定相对路径上的单个文件数据
     * @filename FileService.java
     */
    boolean deleteSingleFile(@Nonnull String relativePath);


}