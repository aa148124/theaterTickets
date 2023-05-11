package com.dkh.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickName;
    private String phone;
    private String createTime;
    private String icon;
}
