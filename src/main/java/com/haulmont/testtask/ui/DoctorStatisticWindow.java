package com.haulmont.testtask.ui;

import com.haulmont.testtask.AppTheme;
import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.database.DaoFactory;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Receipt;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Window;

import java.util.List;
import java.util.logging.Logger;

//Класс реализует окно для отображения статистики врачей
//Шеюхин Данил Борисович
//danya.bob@gmail.com

class DoctorStatisticWindow extends Window {

    private static Logger logger = Logger.getLogger(DoctorWindow.class.getName());

    DoctorStatisticWindow() {
        buildDoctorStatisticWindow();
    }

    private void buildDoctorStatisticWindow() {
        setCaption("Статистика");

        Grid<Doctor> statGrid = new Grid<Doctor>();
        statGrid.setSizeFull();

        statGrid.removeAllColumns();
        statGrid.addColumn(doctor -> doctor.getSurname() + " " + doctor.getName().substring(0, 1) + ". " + doctor.getPatronymic().substring(0, 1) + ".").setCaption("ФИО врача");
        statGrid.addColumn(doctor -> {
            int count = 0;
            try {
                List<Receipt> receipts = DaoFactory.getInstance().getReceiptDao().getAll();
                count = 0;
                for (Receipt receipt : receipts) {
                    if (receipt.getDoctor().equals(doctor)) {
                        count++;
                    }
                }
            } catch (DaoException e) {
                logger.severe(e.getMessage());
            }
            return count;
        }).setCaption("Количество рецептов");
        try {
            statGrid.setItems(DaoFactory.getInstance().getDoctorDao().getAll());
        } catch (DaoException e) {
            logger.severe(e.getMessage());
        }
        setStyleName(AppTheme.MODAL_WINDOW);
        setWidth("550px");
        setHeight("500px");
        setModal(true);
        setResizable(false);
        setContent(statGrid);
        center();

    }
}
