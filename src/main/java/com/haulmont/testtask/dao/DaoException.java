package com.haulmont.testtask.dao;

//Класс описывающий ошибки DAO
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class DaoException extends Exception {

    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
