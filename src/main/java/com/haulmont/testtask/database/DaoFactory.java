package com.haulmont.testtask.database;

import com.haulmont.testtask.dao.DaoException;

import java.sql.SQLException;

//Класс, реализующий шаблон DAO factory
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class DaoFactory {

    private static DaoFactory instance = null;

    private DaoFactory() {
    }

    public static synchronized DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public DoctorDao getDoctorDao() throws DaoException {
        try {
            return new DoctorDao(DatabaseHelper.getConnection());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public PatientDao getPatientDao() throws DaoException {
        try {
            return new PatientDao(DatabaseHelper.getConnection());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public ReceiptDao getReceiptDao() throws DaoException {
        try {
            return new ReceiptDao(DatabaseHelper.getConnection());
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public void releaseResources() throws DaoException {
        try {
            DatabaseHelper.closeConnection();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
