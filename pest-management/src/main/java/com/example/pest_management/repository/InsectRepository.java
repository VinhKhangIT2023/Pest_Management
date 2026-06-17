package com.example.pest_management.repository;

import com.example.pest_management.entity.Insect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InsectRepository extends JpaRepository<Insect, Long> {
    Optional<Insect> findByName(String name);
}