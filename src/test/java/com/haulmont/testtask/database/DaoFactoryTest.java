package com.haulmont.testtask.database;

import org.junit.Test;

import static org.junit.Assert.*;

//Юнит тест для проверки функциональных возможностей класса DaoFactory
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class DaoFactoryTest {

    @Test
    public void testGetDoctorDao() throws Exception {
        DoctorDao doctorDao = DaoFactory.getInstance().getDoctorDao();
        assertNotNull("DoctorDao must be not null", doctorDao);
    }

    @Test
    public void testGetPatientDao() throws Exception {
        PatientDao patientDao = DaoFactory.getInstance().getPatientDao();
        assertNotNull("PatientDao must be not null", patientDao);
    }

    @Test
    public void testGetReceiptDao() throws Exception {
        ReceiptDao receiptDao = DaoFactory.getInstance().getReceiptDao();
        assertNotNull("Receipt must be not null", receiptDao);
    }
}
