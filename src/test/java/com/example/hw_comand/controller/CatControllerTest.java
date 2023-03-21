package com.example.hw_comand.controller;

import com.example.hw_comand.model.Cat;
import com.example.hw_comand.service.CatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing CatController
 * @see CatService
 * @author Dmitriy Kuzeev
 */

@WebMvcTest(CatController.class)
class CatControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CatService catService;

    /**
     * Test getById method in CatController
     * <br>
     * Mockito: when <b>CatService::getById()</b> method called, returns <b>cat</b> object
     * @throws Exception
     */

    @Test
    void getByid() throws Exception {
        Cat cat = new Cat();
        cat.setId(1L);
        when(catService.getById(anyLong())).thenReturn(cat);

        mockMvc.perform(
                        get("/cat/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(catService).getById(1L);
    }

}
