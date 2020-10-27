package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Entity;

import java.util.List;

//Интерфейс с описанием общих методов, которые используются для взаимодействия с базой данных
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public interface GenericDao<T extends Entity> {

    T persist(T object) throws DaoException;

    void update(T object) throws DaoException;

    void delete(T object) throws DaoException;

    T getByPrimaryKey(Long key) throws DaoException;

    List<T> getAll() throws DaoException;
}
