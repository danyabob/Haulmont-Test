package com.haulmont.testtask.database;

import com.haulmont.testtask.entity.Doctor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

//Юнит тест для проверки функциональных возможностей класса DoctorDao
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class DoctorDaoTest {

    private DoctorDao doctorDao;

    @Before
    public void setUp() throws Exception {
        doctorDao = DaoFactory.getInstance().getDoctorDao();
    }

    @After
    public void tearDown() throws Exception {
        doctorDao = null;
    }

    @Test
    public void testPersist() throws Exception {
        // create test data
        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");

        // test
        doctor = doctorDao.persist(doctor);
        assertNotNull("Doctor must be not null", doctor);
        assertNotNull("Id must be not null", doctor.getId());

        // delete test data
        doctorDao.delete(doctor);
    }

    @Test
    public void testUpdate() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setName("Ксения");
        doctor.setSurname("Калеганова");
        doctor.setPatronymic("Владимировна");
        doctor.setSpecialization("Стоматолог");
        doctor = doctorDao.persist(doctor);

        try {
            //She is afraid of dentists...
            doctor.setSpecialization("Психолог");
            doctorDao.update(doctor);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        doctorDao.delete(doctor);
    }

    @Test
    public void testDelete() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");
        doctor = doctorDao.persist(doctor);

        try {
            doctorDao.delete(doctor);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetByPrimaryKey() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");
        doctor = doctorDao.persist(doctor);

        Long id = doctor.getId();
        doctor = doctorDao.getByPrimaryKey(id);
        assertNotNull("Doctor must be not null", doctor);

        doctorDao.delete(doctor);
    }

    @Test
    public void testGetAll() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");
        doctor = doctorDao.persist(doctor);

        List<Doctor> doctors = doctorDao.getAll();
        assertNotNull("List must be not null", doctors);
        assertTrue("List size must be greater than 0", doctors.size() > 0);

        doctorDao.delete(doctor);
    }
}
