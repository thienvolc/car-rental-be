package fun.dashspace.carrentalsystem.upload;

import fun.dashspace.carrentalsystem.dto.response.upload.FileUpLoadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {
    FileUpLoadResponse uploadFile(MultipartFile file, String folder);

    List<FileUpLoadResponse> uploadFiles(List<MultipartFile> files, String folder);

    void deleteFile(String publicId);

    String extractPublicIdFromUrl(String url);
}
