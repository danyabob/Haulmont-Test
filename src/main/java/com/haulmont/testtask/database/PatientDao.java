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

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.GenericDao;
import com.haulmont.testtask.entity.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Класс для работы с таблицей пациентов из БД по шаблону DAO
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class PatientDao implements GenericDao<Patient> {

    private Connection connection = null;

    protected PatientDao(Connection connection) {
        this.connection = connection;
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public Patient persist(Patient object) throws DaoException {
        Patient patient = new Patient();
        String sql = "insert into T_PATIENT(NAME, SURNAME, PATRONYMIC, PHONE_NUMBER) " +
                "values (?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, object.getName());
            st.setString(2, object.getSurname());
            st.setString(3, object.getPatronymic());
            st.setString(4, object.getPhoneNumber());
            if (st.executeUpdate() == 1) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    rs.next();
                    patient.setId(rs.getLong(1));
                    patient.setName(object.getName());
                    patient.setSurname(object.getSurname());
                    patient.setPatronymic(object.getPatronymic());
                    patient.setPhoneNumber(object.getPhoneNumber());
                } catch (SQLException e) {
                    throw e;
                }
            } else {
                throw new SQLException("Creating Patient failed, no row inserted.");
            }
        } catch (SQLException e) {
            patient = null;
            throw new DaoException(e);
        }
        return patient;
    }

    @Override
    public void update(Patient object) throws DaoException {
        String sql = "update T_PATIENT set NAME = ?, SURNAME = ?, PATRONYMIC = ?, PHONE_NUMBER = ? " +
                "where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, capitalize(object.getName()));
            st.setString(2, capitalize(object.getSurname()));
            st.setString(3, capitalize(object.getPatronymic()));
            st.setString(4, object.getPhoneNumber());
            st.setLong(5, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Patient object) throws DaoException {
        String sql = "delete from T_PATIENT where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Patient getByPrimaryKey(Long key) throws DaoException {
        Patient patient = null;
        String sql = "select NAME, SURNAME, PATRONYMIC, PHONE_NUMBER from T_PATIENT where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, key);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                patient = new Patient();
                patient.setId(key);
                patient.setName(rs.getString("NAME"));
                patient.setSurname(rs.getString("SURNAME"));
                patient.setPatronymic(rs.getString("PATRONYMIC"));
                patient.setPhoneNumber(rs.getString("PHONE_NUMBER"));
            }
        } catch (SQLException e) {
            patient = null;
            throw new DaoException(e);
        }
        return patient;
    }

    @Override
    public List<Patient> getAll() throws DaoException {
        List<Patient> list = new ArrayList<Patient>();
        String sql = "select ID, NAME, SURNAME, PATRONYMIC, PHONE_NUMBER from T_PATIENT;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getLong("ID"));
                patient.setName(rs.getString("NAME"));
                patient.setSurname(rs.getString("SURNAME"));
                patient.setPatronymic(rs.getString("PATRONYMIC"));
                patient.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                list.add(patient);
            }
        } catch (SQLException e) {
            list = null;
            throw new DaoException(e);
        }
        return list;
    }
}
