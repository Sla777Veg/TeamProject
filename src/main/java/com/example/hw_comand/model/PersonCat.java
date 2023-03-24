package com.example.hw_comand.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Class of owner for the cat.
 * @author Andrei Klimov
 * @version 1.0.0
 */
@Entity
@Table(name = "person_cat")
public class PersonCat {

    /** "ID" field */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** "Name" field */
    @Column(name = "name")
    private String name;


    /** "Phone" field */
    @Column(name = "phone")
    private String phone;

    /** "Mail" field */
    @Column(name = "mail")
    private String mail;

    /** "Address" field */
    @Column(name = "address")
    private String address;

    /** "id Chat" field */
    @Column(name = "chat_id")
    private Long chatId;

    /** "Pet type" field */
    @Column(name = "pet_type")
    private PetType petType;

    /** "Status" field */
    @Column(name = "status")
    private Status status;

    /** "Cat" field */
    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    /** "Report data" field */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "person_cat_report_data",
            joinColumns = @JoinColumn(name = "person_cat_null"),
            inverseJoinColumns = @JoinColumn(name = "report_data_id"))
    private Collection<ReportData> reportData;

    /**
     * Constructor - creating a new object.
     * @see PersonCat
     */
    public PersonCat() {
    }

    /**
     * Constructor - creating a new object with certain values.
     * @param name
     * @param phone
     * @param chatId
     */
    public PersonCat(String name, String phone, Long chatId) {
        this.name = name;
        this.phone = phone;
        this.chatId = chatId;
    }

    /**
     * Constructor - creating a new object with certain values.
     * @param name
     * @param chatId
     * @param petType
     */
    public PersonCat(String name, Long chatId, PetType petType) {
        this.name = name;
        this.chatId = chatId;
        this.petType = petType;
    }

    /**
     * Constructor - creating a new object with certain values.
     * @param id
     * @param name
     * @param phone
     * @param mail
     * @param address
     * @param chatId
     */
    public PersonCat(Long id, String name, String phone, String mail, String address, Long chatId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.chatId = chatId;
    }

    /**
     * Constructor - creating a new object with certain values.
     * @param name
     * @param phone
     * @param mail
     * @param address
     * @param chatId
     * @param petType
     * @param status
     * @param cat
     * @param reportData
     */
    public PersonCat(String name, String phone, String mail, String address, Long chatId, PetType petType, Status status, Cat cat, Collection<ReportData> reportData) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
        this.chatId = chatId;
        this.petType = petType;
        this.status = status;
        this.cat = cat;
        this.reportData = reportData;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Collection<ReportData> getReportData() {
        return reportData;
    }

    public void setReportData(Collection<ReportData> reportData) {
        this.reportData = reportData;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonCat personCat = (PersonCat) o;
        return Objects.equals(id, personCat.id) && Objects.equals(name, personCat.name) && Objects.equals(phone, personCat.phone) && Objects.equals(mail, personCat.mail) && Objects.equals(address, personCat.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, mail, address);
    }

    @Override
    public String toString() {
        return "PersonCat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", address='" + address + '\'' +
                ", chatId=" + chatId +
                ", petType=" + petType +
                ", status=" + status +
                ", cat=" + cat +
                ", reportData=" + reportData +
                '}';
    }
}
