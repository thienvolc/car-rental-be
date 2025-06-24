package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.upload.FileUpLoadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {
    FileUpLoadResponse uploadFile(MultipartFile file, String folder);
    List<FileUpLoadResponse> uploadFiles(List<MultipartFile> files, String folder);
    void deleteFile(String imageUrl);
}
