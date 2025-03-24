package org.example.common.model.user;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String uname;
    private String sex;
    private String phone;
    private String pwd;
    private String image;
}
