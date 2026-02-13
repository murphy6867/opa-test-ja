package com.ops.authservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRequest {

    @NonNull
    private String username;

    @NonNull
    private String password;
}
