package com.muhammet.repository;

import com.muhammet.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f.followId FROM Follow f WHERE f.userId = :userId")
    List<Long> findFollowedUserIdsByUserId(@Param("userId") Long userId);
}
