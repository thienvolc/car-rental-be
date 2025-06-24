package fun.dashspace.carrentalsystem.service;

import fun.dashspace.carrentalsystem.dto.upload.FileUpLoadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {
    FileUpLoadResponse uploadFile(MultipartFile file);
    List<FileUpLoadResponse> uploadFiles(List<MultipartFile> files);
    void deleteFile(String imageUrl);
}
