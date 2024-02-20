package com.example.secumix.security.userprofile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileDetailRepository extends JpaRepository<ProfileDetail,Integer> {
}
