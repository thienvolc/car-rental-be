package fun.dashspace.carrentalsystem.service.impl;

import fun.dashspace.carrentalsystem.dto.upload.FileUpLoadResponse;
import fun.dashspace.carrentalsystem.exception.custom.resource.FileUploadFailedException;
import fun.dashspace.carrentalsystem.external.CloudinaryAdapter;
import fun.dashspace.carrentalsystem.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {
    private final CloudinaryAdapter cloudinaryAdapter;

    @Override
    public FileUpLoadResponse uploadFile(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinaryAdapter.uploadImage(file);
            return buildFileUploadResponse(result);
        } catch (IOException ex) {
            throw new FileUploadFailedException(ex.getMessage());
        }
    }

    private FileUpLoadResponse buildFileUploadResponse(Map<?, ?> imgUploadResult) {
        return FileUpLoadResponse.builder()
                .publicId((String) imgUploadResult.get("public_id"))
                .secureUrl((String) imgUploadResult.get("secure_url"))
                .build();
    }

    @Override
    public List<FileUpLoadResponse> uploadFiles(List<MultipartFile> files) {
        var result = files.stream().map(this::uploadFile);
        return result.toList();
    }

    @Override
    public void deleteFile(String imageUrl) {
        cloudinaryAdapter.deleteImage(imageUrl);
    }
}
