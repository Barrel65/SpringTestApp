package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select * from orders where (person_id = ?1)", nativeQuery = true)
    List<Order> findByPersonId(int id);


    Optional<Order> findById(int id);

    @Query(value = "select * from orders", nativeQuery = true)
    List<Order> findAll();

    @Query(value = "SELECT * FROM orders WHERE LOWER(number) LIKE %:str%", nativeQuery = true)
    List<Order> findByLastFourSymbols(@Param("str") String str);

}
