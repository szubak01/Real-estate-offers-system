package com.example.application.data.repository;

import com.example.application.data.entity.Offer;
import com.example.application.data.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

  @Query("select u from User u " +
      "where lower(u.username) like lower(concat('%', :searchTerm, '%')) " +
      "or lower(u.firstName) like lower(concat('%', :searchTerm, '%'))" +
      "or lower(u.lastName) like lower(concat('%', :searchTerm, '%'))" +
      "or lower(u.email) like lower(concat('%', :searchTerm, '%'))"
  )
  List<User> search(@Param("searchTerm") String searchTerm);
}