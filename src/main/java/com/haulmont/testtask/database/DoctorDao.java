package com.haulmont.testtask.database;

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.GenericDao;
import com.haulmont.testtask.entity.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Класс для работы с таблицей врачей из БД по шаблону DAO
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class DoctorDao implements GenericDao<Doctor> {

    private Connection connection = null;

    protected DoctorDao(Connection connection) {
        this.connection = connection;
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public Doctor persist(Doctor object) throws DaoException {
        Doctor doctor = new Doctor();
        String sql = "insert into T_DOCTOR (NAME, SURNAME, PATRONYMIC, SPECIALIZATION) values (?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, object.getName());
            st.setString(2, object.getSurname());
            st.setString(3, object.getPatronymic());
            st.setString(4, object.getSpecialization());
            if (st.executeUpdate() == 1) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    rs.next();
                    doctor.setId(rs.getLong(1));
                    doctor.setName(object.getName());
                    doctor.setSurname(object.getSurname());
                    doctor.setPatronymic(object.getPatronymic());
                    doctor.setSpecialization(object.getSpecialization());
                } catch (SQLException e) {
                    throw e;
                }
            } else {
                throw new SQLException("Creating Doctor failed, no row inserted.");
            }
        } catch (SQLException e) {
            doctor = null;
            throw new DaoException(e);
        }
        return doctor;
    }

    @Override
    public void update(Doctor object) throws DaoException {
        String sql = "update T_DOCTOR set NAME = ?, SURNAME = ?, PATRONYMIC = ?, SPECIALIZATION = ? where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, capitalize(object.getName()));
            st.setString(2, capitalize(object.getSurname()));
            st.setString(3, capitalize(object.getPatronymic()));
            st.setString(4, capitalize(object.getSpecialization()));
            st.setLong(5, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Doctor object) throws DaoException {
        String sql = "delete from T_DOCTOR where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Doctor getByPrimaryKey(Long key) throws DaoException {
        Doctor doctor = null;
        String sql = "select NAME, SURNAME, PATRONYMIC, SPECIALIZATION from T_DOCTOR where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, key);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                doctor = new Doctor();
                doctor.setId(key);
                doctor.setName(rs.getString("NAME"));
                doctor.setSurname(rs.getString("SURNAME"));
                doctor.setPatronymic(rs.getString("PATRONYMIC"));
                doctor.setSpecialization(rs.getString("SPECIALIZATION"));
            }
        } catch (SQLException e) {
            doctor = null;
            throw new DaoException(e);
        }
        return doctor;
    }

    @Override
    public List<Doctor> getAll() throws DaoException {
        List<Doctor> list = new ArrayList<Doctor>();
        String sql = "select ID, NAME, SURNAME, PATRONYMIC, SPECIALIZATION from T_DOCTOR;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("ID"));
                doctor.setName(rs.getString("NAME"));
                doctor.setSurname(rs.getString("SURNAME"));
                doctor.setPatronymic(rs.getString("PATRONYMIC"));
                doctor.setSpecialization(rs.getString("SPECIALIZATION"));
                list.add(doctor);
            }
        } catch (SQLException e) {
            list = null;
            throw new DaoException(e);
        }
        return list;
    }
}
