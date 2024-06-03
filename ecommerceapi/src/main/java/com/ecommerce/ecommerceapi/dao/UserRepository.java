package com.ecommerce.ecommerceapi.dao;

import com.ecommerce.ecommerceapi.entity.Role;
import com.ecommerce.ecommerceapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {


    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<User> findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(
            @Param("name") String name, @Param("username") String username);

    @Query("SELECT u FROM User u WHERE (LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND u.role = :role")
    List<User> findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseAndRole(
            @Param("name") String name, @Param("username") String username, @Param("role") Role role);


    Optional<User> findByUsername(String username);

}


