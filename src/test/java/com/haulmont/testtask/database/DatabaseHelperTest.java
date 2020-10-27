package com.haulmont.testtask.database;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

//Юнит тест для проверки функциональных возможностей класса DataBaseHelper
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class DatabaseHelperTest {

    @Test
    public void testGetConnection() throws Exception {
        Connection connection = DatabaseHelper.getConnection();
        assertNotNull("Connection must be not null", connection);
    }
}
