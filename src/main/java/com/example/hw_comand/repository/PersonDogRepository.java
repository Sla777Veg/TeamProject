package com.example.hw_comand.repository;

import com.example.hw_comand.model.PersonDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface PersonDogRepository.
 * @author Tatiana Alekseeva
 * @version 1.0.0
 */
@Repository
public interface PersonDogRepository extends JpaRepository<PersonDog, Long> {

    Optional<PersonDog> findByChatId(Long chatId);

}