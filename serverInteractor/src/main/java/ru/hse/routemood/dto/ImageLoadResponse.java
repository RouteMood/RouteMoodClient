package ru.hse.routemood.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageLoadResponse {

    private byte[] fileData;
    private String mimeType;
}
