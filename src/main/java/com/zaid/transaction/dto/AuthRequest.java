package com.zaid.transaction.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(

        @NotBlank(message = "usernamecannot be blank")
        @Size(min = 8, max = 8, message = "Username Terdiri Atas 8 Karakter")
        String username,

        @NotBlank(message = "PIN Number cannot be blank")
        @Size(min = 6, max = 6, message = "PIN Number must be 6 digits")
        String pinNumber
) {
}
