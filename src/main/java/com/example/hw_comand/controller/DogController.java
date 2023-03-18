package com.example.hw_comand.controller;

import com.example.hw_comand.model.Dog;
import com.example.hw_comand.service.DogService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
/**
 * Class of DogController
 * @author Kravchuk Vyacheslav
 */
@RestController
@RequestMapping("dog")
public class DogController {
    private final DogService service;

    public DogController(DogService service) {
        this.service = service;
    }

    @Operation(summary = "Получение собаки по id")
    @GetMapping("/{id}")
    public Dog getById(@PathVariable Long id) {
        return this.service.getById(id);
    }

    @Operation(summary = "Создание собаки")
    @PostMapping()
    public Dog save(@RequestBody Dog dog) {
        return this.service.create(dog);
    }

    @Operation(summary = "Изменение данных у собаки")
    @PutMapping()
    public Dog update(@RequestBody Dog dog) {
        return this.service.update(dog);
    }

    @Operation(summary = "Удаленеи собаки по id")
    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        this.service.removeById(id);
    }

    @Operation(summary = "Просмотр всех собак")
    @GetMapping("/all")
    public Collection<Dog> getAll() {
        return this.service.getAll();
    }
}
