package com.haulmont.testtask.entity;

import java.util.Objects;

//Класс описывает сущность Врача
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class Doctor implements Entity {

    private Long id = null;
    private String name;
    private String surname;
    private String patronymic;
    private String specialization;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(getId(), doctor.getId()) &&
                Objects.equals(getName(), doctor.getName()) &&
                Objects.equals(getSurname(), doctor.getSurname()) &&
                Objects.equals(getPatronymic(), doctor.getPatronymic()) &&
                Objects.equals(getSpecialization(), doctor.getSpecialization());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getPatronymic(), getSpecialization());
    }
}