package com.mobile.group.tlu_contact_be.service;

import com.mobile.group.tlu_contact_be.dto.response.ImageUploadResponse;
import com.mobile.group.tlu_contact_be.exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageService {

    private static final int IMAGE_WIDTH = 120;
    private static final int IMAGE_HEIGHT = 120;
    private static final String PNG_FORMAT = "png";
    private static final String BASE64_PREFIX = "data:image/png;base64,";

    public ImageUploadResponse processAndConvertImage(MultipartFile file) {
        try {
            // Check if file is an image
            if (!isImageFile(file)) {
                throw new CustomException("Uploaded file is not an image", HttpStatus.BAD_REQUEST);
            }

            // Resize the image to 120x120 pixels
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(IMAGE_WIDTH, IMAGE_HEIGHT)
                    .outputFormat(PNG_FORMAT)
                    .toOutputStream(outputStream);

            // Convert to Base64
            String base64Image = BASE64_PREFIX + 
                    Base64.getEncoder().encodeToString(outputStream.toByteArray());

            return ImageUploadResponse.builder()
                    .base64Image(base64Image)
                    .originalFilename(file.getOriginalFilename())
                    .size(outputStream.size())
                    .build();

        } catch (IOException e) {
            throw new CustomException("Error processing image: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
} 