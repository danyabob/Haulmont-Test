package com.haulmont.testtask.ui;

import com.haulmont.testtask.AppTheme;
import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.database.DaoFactory;
import com.haulmont.testtask.entity.Patient;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

 //Класс реализует отображение информации о пациентах в таблице
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class PatientView extends VerticalLayout implements View {

    public static final String NAME = "";

    private Grid<Patient> patientGrid = new Grid<>(Patient.class);

    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    private static Logger logger = Logger.getLogger(PatientView.class.getName());

    public PatientView() {
        buildPatientView();
        setupListeners();
    }

    private void buildPatientView() {
        try {
            patientGrid.removeAllColumns();
            patientGrid.addColumn(patient -> patient.getSurname() + " " + patient.getName() + " " + patient.getPatronymic()).setCaption("ФИО пациента");
            patientGrid.addColumn(Patient::getPhoneNumber).setCaption("Телефон");
            //patientGrid.setSizeFull();

            HorizontalLayout buttonsLayout = new HorizontalLayout();

            buttonsLayout.setSpacing(true);

            addButton = new Button("Добавить пациента", new ThemeResource(AppTheme.BUTTON_ADD));
            editButton = new Button("Изменить пациента", new ThemeResource(AppTheme.BUTTON_EDIT));
            deleteButton = new Button("Удалить пациента", new ThemeResource(AppTheme.BUTTON_DELETE));
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            buttonsLayout.addComponents(addButton, editButton, deleteButton);

            setMargin(true);
            setSpacing(true);
            setSizeFull();
            addComponents(patientGrid, buttonsLayout);
            setExpandRatio(patientGrid, 1f);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        try {
            patientGrid.addSelectionListener(valueChangeEvent -> {
                if (!patientGrid.asSingleSelect().isEmpty()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new PatientWindow(patientGrid, false)));

            editButton.addClickListener(clickEvent ->
                    getUI().addWindow(new PatientWindow(patientGrid, true)));

            deleteButton.addClickListener(clickEvent -> {
                if (!patientGrid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getPatientDao().delete(patientGrid.asSingleSelect().getValue());
                        updateGrid();
                    } catch (DaoException e) {
                        if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                            Notification notification = new Notification("Удаление пациента невозможно, " +
                                    "так как у него есть активные рецепты");
                            notification.setDelayMsec(2000);
                            notification.show(Page.getCurrent());
                        } else {
                            logger.severe(e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void updateGrid() {
        try {
            List<Patient> patients = DaoFactory.getInstance().getPatientDao().getAll();
            patientGrid.setItems(patients);
        } catch (DaoException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }

}
