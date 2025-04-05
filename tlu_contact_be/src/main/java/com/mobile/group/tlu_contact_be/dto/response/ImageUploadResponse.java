package com.mobile.group.tlu_contact_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadResponse {
    private String base64Image;
    private String originalFilename;
    private long size;
} 