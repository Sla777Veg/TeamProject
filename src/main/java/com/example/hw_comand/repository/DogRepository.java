package com.example.hw_comand.repository;

import com.example.hw_comand.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Interface DogRepository.
 * @author Kravchuk Vyacheslav
 */
@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

}
