package com.muhammet.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VwUserForFollow {
    private Long id;
    private String name;
    private String userName;
    private String avatar;

}
