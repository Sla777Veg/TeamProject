package com.example.hw_comand.service;

import com.example.hw_comand.exceptions.PersonCatNotFoundException;
import com.example.hw_comand.model.PersonCat;
import com.example.hw_comand.repository.PersonCatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PersonCatService {

    private final PersonCatRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(PersonCatService.class);

    public PersonCatService(PersonCatRepository repository) {
        this.repository = repository;
    }

    /**
     * Method to get a personCat by id.
     *
     * @param id
     * @return {@link PersonCatRepository#findById(Object)}
     * @throws PersonCatNotFoundException
     * @see PersonCatService
     */
    public PersonCat getById(Long id) {
        logger.info("Was invoked method to get a personCat by id={}", id);

        return this.repository.findById(id)
                .orElseThrow(PersonCatNotFoundException::new);
    }

    /**
     * Method to create a personCat.
     *
     * @param personCat
     * @return {@link PersonCatRepository#save(Object)}
     * @see PersonCatService
     */
    public PersonCat create(PersonCat personCat) {
        logger.info("Was invoked method to create a personCat");

        return this.repository.save(personCat);
    }

    /**
     * Method to update a personCat.
     *
     * @param personCat
     * @return {@link PersonCatRepository#save(Object)}
     * @throws PersonCatNotFoundException
     * @see PersonCatService
     */
    public PersonCat update(PersonCat personCat) {
        logger.info("Was invoked method to update a personCat");

        if (personCat.getId() != null) {
            if (getById(personCat.getId()) != null) {
                return repository.save(personCat);
            }
        }
        throw new PersonCatNotFoundException();
    }

    /**
     * Method to remove a personCat by id.
     *
     * @param id
     */
    public void removeById(Long id) {
        logger.info("Was invoked method to remove a personCat by id={}", id);

        this.repository.deleteById(id);
    }

    /**
     * Method to get all personsCat.
     *
     * @return {@link PersonCatRepository#findAll()}
     * @see PersonCatService
     */
    public Collection<PersonCat> getAll() {
        logger.info("Was invoked method to get all personsCat");

        return this.repository.findAll();
    }

    /**
     * Method to get a personsCat by chatId.
     *
     * @param chatId
     * @return {@link PersonCatRepository#findByChatId(Long)}
     * @see PersonCatService
     */
    public Optional<PersonCat> getByChatId(Long chatId) {
        logger.info("Was invoked method to get a personsCat by chatId={}", chatId);

        return this.repository.findByChatId(chatId);
    }
}
