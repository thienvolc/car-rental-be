package fun.dashspace.carrentalsystem.upload;

import fun.dashspace.carrentalsystem.dto.response.upload.FileUpLoadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final CloudinaryService cloudinaryService;

    @Override
    public FileUpLoadResponse uploadFile(MultipartFile file, String folder) {
        return cloudinaryService.upload(file, folder);
    }

    @Override
    public List<FileUpLoadResponse> uploadFiles(List<MultipartFile> files, String folder) {
        var result = files.stream().map(file -> uploadFile(file, folder));
        return result.toList();
    }

    @Override
    public void deleteFile(String publicId) {
        cloudinaryService.delete(publicId);
    }

    @Override
    public String extractPublicIdFromUrl(String url) {
        return cloudinaryService.extractPublicIdFromUrl(url);
    }
}
