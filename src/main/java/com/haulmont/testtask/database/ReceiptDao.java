package com.haulmont.testtask.database;

import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.dao.GenericDao;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Receipt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Класс для работы с таблицей рецептов из БД по шаблону DAO
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class ReceiptDao implements GenericDao<Receipt> {

    private Connection connection = null;

    protected ReceiptDao(Connection connection) {
        this.connection = connection;
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public Receipt persist(Receipt object) throws DaoException {
        Receipt receipt = new Receipt();
        String sql = "insert into T_RECEIPT(DESCRIPTION, PATIENT_ID, DOCTOR_ID, CREATION_DATE, EXPIRE_DATE, PRIORITY) " +
                "values (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, object.getDescription());
            Long patientId = null;
            if (object.getPatient() != null) {
                patientId = object.getPatient().getId();
            }
            st.setLong(2, patientId);
            Long doctorId = null;
            if (object.getDoctor() != null) {
                doctorId = object.getDoctor().getId();
            }
            st.setLong(3, doctorId);
            Date creationDate = null;
            if (object.getCreationDate() != null) {
                creationDate = new Date(object.getCreationDate().getTime());
            }
            st.setDate(4, creationDate);
            Date expireDate = null;
            if (object.getCreationDate() != null) {
                expireDate = new Date(object.getExpireDate().getTime());
            }
            st.setDate(5, expireDate);
            st.setString(6, object.getPriority());
            if (st.executeUpdate() == 1) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    rs.next();
                    receipt.setId(rs.getLong(1));
                    receipt.setDescription(object.getDescription());
                    receipt.setPatient(object.getPatient());
                    receipt.setDoctor(object.getDoctor());
                    receipt.setCreationDate(object.getCreationDate());
                    receipt.setExpireDate(object.getExpireDate());
                    receipt.setPriority(object.getPriority());
                } catch (SQLException e) {
                    throw e;
                }
            } else {
                throw new SQLException("Creating Receipt failed, no row inserted.");
            }
        } catch (SQLException e) {
            receipt = null;
            throw new DaoException(e);
        }
        return receipt;
    }

    @Override
    public void update(Receipt object) throws DaoException {
        String sql = "update T_RECEIPT set DESCRIPTION = ?, PATIENT_ID = ?, DOCTOR_ID = ?, CREATION_DATE = ?, EXPIRE_DATE = ?, PRIORITY = ?" +
                "where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, capitalize(object.getDescription()));
            Long patientId = null;
            if (object.getPatient() != null) {
                patientId = object.getPatient().getId();
            }
            st.setLong(2, patientId);
            Long doctorId = null;
            if (object.getDoctor() != null) {
                doctorId = object.getDoctor().getId();
            }
            st.setLong(3, doctorId);
            Date creationDate = null;
            if (object.getCreationDate() != null) {
                creationDate = new Date(object.getCreationDate().getTime());
            }
            st.setDate(4, creationDate);
            Date expireDate = null;
            if (object.getCreationDate() != null) {
                expireDate = new Date(object.getExpireDate().getTime());
            }
            st.setDate(5, expireDate);
            st.setString(6, object.getPriority());
            st.setLong(7, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Receipt object) throws DaoException {
        String sql = "delete from T_RECEIPT where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, object.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Receipt getByPrimaryKey(Long key) throws DaoException {
        Receipt receipt = null;
        String sql = "select DESCRIPTION, PATIENT_ID, DOCTOR_ID, CREATION_DATE, EXPIRE_DATE, PRIORITY from T_RECEIPT where ID = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setLong(1, key);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                receipt = new Receipt();
                receipt.setId(key);
                receipt.setDescription(rs.getString("DESCRIPTION"));
                PatientDao patientDao = DaoFactory.getInstance().getPatientDao();
                Patient patient = patientDao.getByPrimaryKey(rs.getLong("PATIENT_ID"));
                receipt.setPatient(patient);
                DoctorDao doctorDao = DaoFactory.getInstance().getDoctorDao();
                Doctor doctor = doctorDao.getByPrimaryKey(rs.getLong("DOCTOR_ID"));
                receipt.setDoctor(doctor);
                receipt.setCreationDate(rs.getDate("CREATION_DATE"));
                receipt.setExpireDate(rs.getDate("EXPIRE_DATE"));
                receipt.setPriority(rs.getString("PRIORITY"));
            }
        } catch (SQLException e) {
            receipt = null;
            throw new DaoException(e);
        }
        return receipt;
    }

    @Override
    public List<Receipt> getAll() throws DaoException {
        List<Receipt> list = new ArrayList<Receipt>();
        String sql = "select ID, DESCRIPTION, PATIENT_ID, DOCTOR_ID, CREATION_DATE, EXPIRE_DATE, PRIORITY from T_RECEIPT;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Receipt receipt = new Receipt();
                receipt.setId(rs.getLong("ID"));
                receipt.setDescription(rs.getString("DESCRIPTION"));
                PatientDao patientDao = DaoFactory.getInstance().getPatientDao();
                Patient patient = patientDao.getByPrimaryKey(rs.getLong("PATIENT_ID"));
                receipt.setPatient(patient);
                DoctorDao doctorDao = DaoFactory.getInstance().getDoctorDao();
                Doctor doctor = doctorDao.getByPrimaryKey(rs.getLong("DOCTOR_ID"));
                receipt.setDoctor(doctor);
                receipt.setCreationDate(rs.getDate("CREATION_DATE"));
                receipt.setExpireDate(rs.getDate("EXPIRE_DATE"));
                receipt.setPriority(rs.getString("PRIORITY"));
                list.add(receipt);
            }
        } catch (SQLException e) {
            list = null;
            throw new DaoException(e);
        }
        return list;
    }

}
