package com.example.DataDisplay.repository;

import com.example.DataDisplay.model.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebUserRepository extends JpaRepository<WebUser,Long> {

    Optional<WebUser> findByUsername(String username);

}
