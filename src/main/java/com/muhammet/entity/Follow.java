package com.muhammet.entity;

import com.muhammet.utility.FollowState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tblfollow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId; //Takip etmek isteyen kişi
    private Long followId; //Takip edilmek istenen kişi
    @Enumerated(EnumType.STRING)
    private FollowState state;
}
