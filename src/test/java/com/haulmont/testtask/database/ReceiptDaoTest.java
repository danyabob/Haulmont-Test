/*
 * The MIT License
 * Copyright © 2019 Kobzarev Alex
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.haulmont.testtask.database;

import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Receipt;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Юнит тест для проверки функциональных возможностей класса ReceiptDao
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
public class ReceiptDaoTest {

    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private ReceiptDao receiptDao;

    @Before
    public void setUp() throws Exception {
        patientDao = DaoFactory.getInstance().getPatientDao();
        doctorDao = DaoFactory.getInstance().getDoctorDao();
        receiptDao = DaoFactory.getInstance().getReceiptDao();
    }

    @After
    public void tearDown() throws Exception {
        patientDao = null;
        doctorDao = null;
        receiptDao = null;
    }

    @Test
    public void testPersist() throws Exception {
        // create test data
        Receipt receipt = new Receipt();
        receipt.setDescription("Туда сюда");

        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);
        receipt.setPatient(patient);

        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");
        doctor = doctorDao.persist(doctor);
        receipt.setDoctor(doctor);

        Date creationDate = new GregorianCalendar(2019, 4, 10).getTime();
        receipt.setCreationDate(creationDate);
        Date expireDate = new GregorianCalendar(2019, 5, 12).getTime();
        receipt.setExpireDate(expireDate);
        receipt.setPriority("Срочный");

        // test
        receipt = receiptDao.persist(receipt);
        assertNotNull("Receipt must be not null", receipt);
        assertNotNull("Id must be not null", receipt.getId());

        // delete test data
        receiptDao.delete(receipt);
        patientDao.delete(patient);
        doctorDao.delete(doctor);
    }

    @Test
    public void testUpdate() throws Exception {
        //create test data
        Receipt receipt = new Receipt();
        receipt.setDescription("Туда сюда");
        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);
        receipt.setPatient(patient);

        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");
        doctor = doctorDao.persist(doctor);
        receipt.setDoctor(doctor);

        Date creationDate = new GregorianCalendar(2019, 4, 10).getTime();
        receipt.setCreationDate(creationDate);
        Date expireDate = new GregorianCalendar(2019, 5, 12).getTime();
        receipt.setExpireDate(expireDate);
        receipt.setPriority("Срочный");
        receipt = receiptDao.persist(receipt);

        //test
        try {
            receipt.setDescription("Пятое десятое");
            receipt.setPriority("Немедленный");
            Date testCreationDate = new GregorianCalendar(2019, 9, 10).getTime();
            receipt.setCreationDate(testCreationDate);
            receiptDao.update(receipt);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        //delete test data
        receiptDao.delete(receipt);
        patientDao.delete(patient);
        doctorDao.delete(doctor);
    }

    @Test
    public void testDelete() throws Exception {
        Receipt receipt = new Receipt();
        receipt.setDescription("Туда сюда");
        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);
        receipt.setPatient(patient);

        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");
        doctor = doctorDao.persist(doctor);
        receipt.setDoctor(doctor);

        Date creationDate = new GregorianCalendar(2019, 4, 10).getTime();
        receipt.setCreationDate(creationDate);
        Date expireDate = new GregorianCalendar(2019, 5, 12).getTime();
        receipt.setExpireDate(expireDate);
        receipt.setPriority("Срочный");
        receipt = receiptDao.persist(receipt);
        try {
            receiptDao.delete(receipt);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
        patientDao.delete(patient);
        doctorDao.delete(doctor);
    }

    @Test
    public void testGetByPrimaryKey() throws Exception {
        //create test data
        Receipt receipt = new Receipt();
        receipt.setDescription("Туда сюда");
        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);
        receipt.setPatient(patient);

        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");
        doctor = doctorDao.persist(doctor);
        receipt.setDoctor(doctor);

        Date creationDate = new GregorianCalendar(2019, 4, 10).getTime();
        receipt.setCreationDate(creationDate);
        Date expireDate = new GregorianCalendar(2019, 5, 12).getTime();
        receipt.setExpireDate(expireDate);
        receipt.setPriority("Срочный");
        receipt = receiptDao.persist(receipt);

        //test
        Long id = receipt.getId();
        receipt = receiptDao.getByPrimaryKey(id);
        assertNotNull("Receipt must be not null", receipt);

        //delete test data
        receiptDao.delete(receipt);
        patientDao.delete(patient);
        doctorDao.delete(doctor);

    }

    @Test
    public void testGetAll() throws Exception {
        //create test data
        Receipt receipt = new Receipt();
        receipt.setDescription("Туда сюда");
        Patient patient = new Patient();
        patient.setName("Олег");
        patient.setSurname("Чаплашкин");
        patient.setPatronymic("Олегович");
        patient.setPhoneNumber("89279817326");
        patient = patientDao.persist(patient);
        receipt.setPatient(patient);

        Doctor doctor = new Doctor();
        doctor.setName("Виктория");
        doctor.setSurname("Полосухина");
        doctor.setPatronymic("Владиславовна");
        doctor.setSpecialization("Хирург");
        doctor = doctorDao.persist(doctor);
        receipt.setDoctor(doctor);

        Date creationDate = new GregorianCalendar(2019, 4, 10).getTime();
        receipt.setCreationDate(creationDate);
        Date expireDate = new GregorianCalendar(2019, 5, 12).getTime();
        receipt.setExpireDate(expireDate);
        receipt.setPriority("Срочный");
        receipt = receiptDao.persist(receipt);

        //test
        List<Receipt> receipts = receiptDao.getAll();
        assertNotNull("List must be not null", receipts);
        assertTrue("List size must be greater than 0", receipts.size() > 0);

        //delete test data
        receiptDao.delete(receipt);
        patientDao.delete(patient);
        doctorDao.delete(doctor);

    }
}

