package com.globalco.payload;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    private String phoneNumber;
    private String profileImage;
}
