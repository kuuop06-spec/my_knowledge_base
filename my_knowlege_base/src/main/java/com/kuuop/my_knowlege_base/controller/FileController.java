package com.kuuop.my_knowlege_base.controller;

import com.kuuop.my_knowlege_base.common.Result;
import com.kuuop.my_knowlege_base.entity.KbFile;
import com.kuuop.my_knowlege_base.service.KbFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/files")
public class FileController {
    @Autowired
    private KbFileService kbFileService;

    // 文件存储路径（项目根目录下的 uploads 文件夹）
    private static final String UPLOAD_DIR = "uploads/";

    // 后台：获取所有文件列表
    @GetMapping
    public Result<List<KbFile>> listFiles() {
        return Result.success(kbFileService.list());
    }

    // 后台：上传文件
    @PostMapping("/upload")
    public Result<KbFile> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            // 创建 uploads 目录
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一的文件名，避免覆盖
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID() + extension;
            String filePath = UPLOAD_DIR + newFilename;

            // 保存文件到磁盘
            file.transferTo(new File(filePath));

            // 保存文件信息到数据库
            KbFile kbFile = new KbFile();
            kbFile.setFileName(originalFilename);
            kbFile.setFileUrl("/uploads/" + newFilename);  // 前端可通过这个 URL 访问
            kbFile.setFileType(extension.substring(1));    // 去掉点号
            kbFile.setFileSize(file.getSize());

            kbFileService.save(kbFile);

            return Result.success(kbFile);
        } catch (Exception e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    // 后台：删除文件
    @DeleteMapping("/{id}")
    public Result<String> deleteFile(@PathVariable Long id) {
        KbFile kbFile = kbFileService.getById(id);
        if (kbFile == null) {
            return Result.error("文件不存在");
        }

        // 删除磁盘上的文件
        String filePath = "." + kbFile.getFileUrl();  // 转换为本地路径
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        // 删除数据库记录
        kbFileService.removeById(id);
        return Result.success("文件删除成功");
    }
}
