package com.haulmont.testtask.entity;

//Класс реализует отношение между сущностями по первичному ключу (ID)
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public interface Entity {
    Long getId();

    void setId(Long id);
}