package com.example.hw_comand.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Class of Cat
 * @author Kravchuk Vyacheslav
 */
@Entity
@Table(name = "dog")
public class Dog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Long id;

    /** "Breed" field */
    @Column(name = "breed")
    private String breed;

    /** "Name" field */
    @Column(name = "name")
    private String name;

    /** "Year Of Birth" field */
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    /** "Description" field */
    @Column(name = "description")
    private String description;

    /**
     * Constructor - creating a new object.
     * @see Dog
     */
    public Dog(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return yearOfBirth == dog.yearOfBirth && Objects.equals(id, dog.id) && Objects.equals(breed, dog.breed) && Objects.equals(name, dog.name) && Objects.equals(description, dog.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, breed, name, yearOfBirth, description);
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", description='" + description + '\'' +
                '}';
    }
}
