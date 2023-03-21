package com.example.hw_comand.service;

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

}
