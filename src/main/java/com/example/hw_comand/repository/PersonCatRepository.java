package com.example.hw_comand.repository;

import com.example.hw_comand.model.PersonCat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface PersonCatRepository.
 * @author Andrei Klimov
 * @version 1.0.0
 */
public interface PersonCatRepository extends JpaRepository<PersonCat, Long> {

    Optional<PersonCat> findByChatId(Long chatId);

}