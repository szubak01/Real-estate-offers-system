package com.example.application.data.repository;

import com.example.application.data.entity.Rate;
import com.example.application.data.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {

  @Query("select r from Rate r " +
      "where lower(r.comment) like lower(concat('%', :searchTerm, '%')) " +
      "or lower(r.rateNumber) like lower(concat('%', :searchTerm, '%'))"
  )
  List<Rate> search(@Param("searchTerm") String searchTerm);
}
