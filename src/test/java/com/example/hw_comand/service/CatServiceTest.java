package com.example.hw_comand.service;

import com.example.hw_comand.exceptions.CatNotFoundException;
import com.example.hw_comand.model.Cat;
import com.example.hw_comand.repository.CatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

/**
 * Class for testing CatService
 *
 * @author Dmitriy Kuzeev
 * @see CatService
 * @see CatRepository
 */
@ExtendWith(MockitoExtension.class)
public class CatServiceTest {

    @Mock
    private CatRepository catRepositoryMock;

    @InjectMocks
    private CatService catService;

    /**
     * Test for method <b>getById()</b> in CatService
     * <br>
     * Mockito: when <b>CatRepository::findById()</b> method called, returns <b>expected</b> object
     */

    @Test
    public void getByidTest() {
        Cat expected = new Cat();
        expected.setName("testName");
        expected.setDescription("testDesc");
        expected.setBreed("testBreed");
        expected.setYearOfBirth(2021);

        Mockito.when(catRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(expected));

        Cat actual = catService.getById(1L);

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
    }

    /**
     * Test for throwing an exception in method <b>getById()</b> in CatService
     * <br>
     * Mockito: when <b>CatRepository::findById()</b> method called, throws <b>CatNotFoundException</b>
     */

    @Test
    public void getByIdExceptionTest() {
        Mockito.when(catRepositoryMock.findById(any(Long.class))).thenThrow(CatNotFoundException.class);
        org.junit.jupiter.api.Assertions.assertThrows(CatNotFoundException.class, () -> catService.getById(1L));
    }

    /**
     * Test for method <b>create()</b> in CatService
     * <br>
     * Mockito: when <b>CatRepository::save()</b> method called, returns <b>expected</b> object
     */

    @Test
    public void creatTest() {
        Cat expected = new Cat();
        expected.setName("testName");
        expected.setDescription("testDesc");
        expected.setBreed("testBreed");
        expected.setYearOfBirth(2021);

        Mockito.when(catRepositoryMock.save(any(Cat.class))).thenReturn(expected);

        Cat actual = catService.create(expected);

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
    }

    /**
     * Test for method <b>update()</b> in CatService
     * <br>
     * Mockito: when <b>CatRepository::save()</b> method called, returns <b>expected</b> object
     * <br>
     * Mockito: when <b>CatRepository::findById()</b> method called, returns <b>expected</b> object
     */

    @Test
    public void updateTest() {
        Cat expected = new Cat();
        expected.setName("testName");
        expected.setDescription("testDesc");
        expected.setBreed("testBreed");
        expected.setYearOfBirth(2021);
        expected.setId(1L);

        Mockito.when(catRepositoryMock.findById(any(Long.class))).thenReturn(Optional.of(expected));
        Mockito.when(catRepositoryMock.save(any(Cat.class))).thenReturn(expected);

        Cat actual = catService.update(expected);

        Assertions.assertThat(actual.getName()).isEqualTo(expected.getName());
        Assertions.assertThat(actual.getBreed()).isEqualTo(expected.getBreed());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        Assertions.assertThat(actual.getYearOfBirth()).isEqualTo(expected.getYearOfBirth());
    }

    /**
     * Test for throwing an exception in method <b>update()</b> in CatService
     * <br>
     * Creating a <b>Cat</b> object with null id
     */

    @Test
    public void updateExceptionTest() {
        Cat expected = new Cat();
        org.junit.jupiter.api.Assertions.assertThrows(CatNotFoundException.class, () -> catService.update(expected));
    }



}
