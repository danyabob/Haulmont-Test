package com.haulmont.testtask.database;

import com.haulmont.testtask.entity.Patient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

//Юнит тест для проверки функциональных возможностей класса PatientDao
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class PatientDaoTest {

    private PatientDao patientDao;

    @Before
    public void setUp() throws Exception {
        patientDao = DaoFactory.getInstance().getPatientDao();
    }

    @After
    public void tearDown() throws Exception {
        patientDao = null;
    }

    @Test
    public void testPersist() throws Exception {
        // create test data
        Patient patient = new Patient();
        patient.setName("Рената");
        patient.setSurname("Ахметшина");
        patient.setPatronymic("Александровна");
        patient.setPhoneNumber("89276873215");

        // test
        patient = patientDao.persist(patient);
        assertNotNull("Patient must be not null", patient);
        assertNotNull("Id must be not null", patient.getId());

        // delete test data
        patientDao.delete(patient);
    }

    @Test
    public void testUpdate() throws Exception {
        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);

        try {
            patient.setPhoneNumber("89271234567");
            patientDao.update(patient);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        patientDao.delete(patient);
    }

    @Test
    public void testDelete() throws Exception {
        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);

        try {
            patientDao.delete(patient);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetByPrimaryKey() throws Exception {
        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);

        Long id = patient.getId();
        patient = patientDao.getByPrimaryKey(id);
        assertNotNull("Patient must be not null", patient);

        patientDao.delete(patient);
    }

    @Test
    public void testGetAll() throws Exception {
        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);

        List<Patient> patients = patientDao.getAll();
        assertNotNull("List must be not null", patients);
        assertTrue("List size must be greater than 0", patients.size() > 0);

        patientDao.delete(patient);
    }
}
