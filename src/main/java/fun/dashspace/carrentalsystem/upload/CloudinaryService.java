package fun.dashspace.carrentalsystem.upload;

import com.cloudinary.utils.ObjectUtils;
import fun.dashspace.carrentalsystem.dto.response.upload.FileUpLoadResponse;
import fun.dashspace.carrentalsystem.exception.custom.resource.FileUploadFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public FileUpLoadResponse upload(MultipartFile file, String folder) {
        Map<?, ?> options = createUploadOptions(folder);

        try {
            var result = cloudinary.uploader().upload(file.getBytes(), options);
            return buildFileUploadResponse(result);
        } catch (IOException ex) {
            throw new FileUploadFailedException("Upload file failed", ex);
        }
    }

    private Map<?, ?> createUploadOptions(String folder) {
        return ObjectUtils.asMap("folder", folder, "resource_type", "auto", "unique_filename", true);
    }

    private FileUpLoadResponse buildFileUploadResponse(Map<?, ?> result) {
        return FileUpLoadResponse.builder()
                .publicId((String) result.get("public_id"))
                .secureUrl((String) result.get("secure_url"))
                .build();
    }

    public void delete(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException ex) {
            throw new FileUploadFailedException("Delete file failed", ex);
        }
    }

    // Example: https://res.cloudinary.com/cld-docs/image/upload/v1719309138/do8wnccnlzrfvwv1mqkq.jpg
    public String extractPublicIdFromUrl(String url) {
        int uploadIndex = url.indexOf("/upload/");
        String path = url.substring(uploadIndex + "/upload/".length());
        if (path.startsWith("v")) {
            int slashIndex = path.indexOf('/');
            if (slashIndex != -1) {
                path = path.substring(slashIndex + 1);
            }
        }

        int dotIndex = path.lastIndexOf(".");
        if (dotIndex != -1) {
            path = path.substring(0, dotIndex);
        }

        return path;
    }
}
