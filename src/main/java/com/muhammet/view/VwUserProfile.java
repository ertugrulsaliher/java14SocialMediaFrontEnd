package com.muhammet.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VwUserProfile {
    private String userName;
    private String avatar;
    private Long followerCount;
    private Long followingCount;
    private String name;
    private String about;
    private Integer bornDate;
    private String phone;
    private String address;
}
