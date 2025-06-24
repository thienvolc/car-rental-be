package fun.dashspace.carrentalsystem.external;

import com.cloudinary.utils.ObjectUtils;
import fun.dashspace.carrentalsystem.util.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryAdapter {
    private final Cloudinary cloudinary;

    public Map<?, ?> uploadImage(MultipartFile file, String folder) throws IOException {
        Map<?, ?> options = createUploadOptions(folder);
        return cloudinary.uploader().upload(file.getBytes(), options);
    }

    private Map<?, ?> createUploadOptions(String folder) {
        return ObjectUtils.asMap("folder", folder, "resource_type", "auto", "unique_filename", true);
    }

    public void deleteImage(String imageurl) {
        try {
            var publicId = CloudinaryUtils.extractPublicIdFromImageUrl(imageurl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException | IllegalArgumentException _) {
        }
    }
}
