package com.example.hw_comand.repository;

import com.example.hw_comand.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface CatRepository.
 * @author Dmitiy Kuzeev
 * @version 1.0.0
 */

@Repository
public interface CatRepository extends JpaRepository <Cat, Long> {
}
