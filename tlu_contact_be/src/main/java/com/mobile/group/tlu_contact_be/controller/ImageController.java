package com.mobile.group.tlu_contact_be.controller;

import com.mobile.group.tlu_contact_be.dto.response.BaseResponse;
import com.mobile.group.tlu_contact_be.dto.response.ImageUploadResponse;
import com.mobile.group.tlu_contact_be.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<ImageUploadResponse>> uploadImage(
            @RequestParam("image") MultipartFile file) {
        
        ImageUploadResponse response = imageService.processAndConvertImage(file);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
} 