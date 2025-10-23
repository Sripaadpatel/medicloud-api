package com.medicloud.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}