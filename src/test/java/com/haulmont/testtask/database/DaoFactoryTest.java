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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Юнит тест для проверки функциональных возможностей класса DaoFactory
 *
 * @author alex
 * @version 1.0
 * @since 2019-11-16
 */
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
