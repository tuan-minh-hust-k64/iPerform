package com.platform.iperform.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private List<String> roles = new ArrayList<>();
}
