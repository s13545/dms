package com.ssp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    private String uid;

    private String username;

    private String password;

    private String realName;

    private int phone;

    private String department;

    private String perm; //权限
}
