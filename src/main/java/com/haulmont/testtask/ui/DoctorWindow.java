package com.haulmont.testtask.ui;

import com.haulmont.testtask.AppTheme;
import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.database.DaoFactory;
import com.haulmont.testtask.database.DoctorDao;
import com.haulmont.testtask.entity.Doctor;
import com.vaadin.data.Binder;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

 //Класс реализует окно для добавления и редактирования врачей.
//Шеюхин Данил Борисович
//danya.bob@gmail.com

class DoctorWindow extends Window {

    private Grid<Doctor> grid;
    private boolean edit;
    private Button okButton;
    private Button cancelButton;
    private TextField nameText;
    private TextField surnameText;
    private TextField patronymicText;
    private TextField specializationText;

    private Doctor doctor;

    private static Logger logger = Logger.getLogger(DoctorWindow.class.getName());

    private Binder<Doctor> binder = new Binder<>(Doctor.class);

    DoctorWindow(Grid<Doctor> grid, boolean edit) {
        this.grid = grid;
        this.edit = edit;
        buildDoctorWindow();
        setupListeners();
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void buildDoctorWindow() {
        setStyleName(AppTheme.MODAL_WINDOW);
        setWidth("450px");
        setHeight("360px");
        setModal(true);
        setResizable(false);
        center();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        FormLayout formLayout = new FormLayout();
        formLayout.setSizeFull();
        formLayout.setMargin(false);
        formLayout.setSpacing(true);

        surnameText = new TextField("Фамилия");
        surnameText.setMaxLength(32);
        surnameText.setWidth("100%");
        surnameText.setRequiredIndicatorVisible(true);
        binder.forField(surnameText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите фамилию.")
                .asRequired()
                .bind(Doctor::getSurname, Doctor::setSurname);

        nameText = new TextField("Имя");
        nameText.setMaxLength(32);
        nameText.setWidth("100%");
        nameText.setRequiredIndicatorVisible(true);
        binder.forField(nameText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите имя.")
                .asRequired()
                .bind(Doctor::getName, Doctor::setName);

        patronymicText = new TextField("Отчество");
        patronymicText.setMaxLength(32);
        patronymicText.setWidth("100%");
        patronymicText.setRequiredIndicatorVisible(true);
        binder.forField(patronymicText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите отчество.")
                .asRequired()
                .bind(Doctor::getPatronymic, Doctor::setPatronymic);

        specializationText = new TextField("Специальность");
        specializationText.setMaxLength(32);
        specializationText.setWidth("100%");
        specializationText.setRequiredIndicatorVisible(true);
        binder.forField(specializationText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите специальность врача.")
                .asRequired()
                .bind(Doctor::getSpecialization, Doctor::setSpecialization);

        formLayout.addComponents(surnameText, nameText, patronymicText, specializationText);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);

        okButton = new Button("ОК");
        okButton.setWidth("125px");
        cancelButton = new Button("Отменить");
        cancelButton.setWidth("125px");

        buttonsLayout.addComponents(okButton, cancelButton);

        mainLayout.addComponents(formLayout, buttonsLayout);
        mainLayout.setExpandRatio(formLayout, 1f);
        mainLayout.setComponentAlignment(buttonsLayout, Alignment.BOTTOM_CENTER);
        setContent(mainLayout);
    }

    private void setupListeners() {
        if (edit) {
            setCaption("Изменить врача");
            if (!grid.asSingleSelect().isEmpty()) {
                try {
                    doctor = grid.asSingleSelect().getValue();
                    binder.setBean(doctor);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавить врача");
            surnameText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                try {
                    Doctor doctorToWrite = new Doctor();
                    doctorToWrite.setName(capitalize(nameText.getValue()));
                    doctorToWrite.setSurname(capitalize(surnameText.getValue()));
                    doctorToWrite.setPatronymic(capitalize(patronymicText.getValue()));
                    doctorToWrite.setSpecialization(capitalize(specializationText.getValue()));
                    DoctorDao doctorDao = DaoFactory.getInstance().getDoctorDao();
                    if (edit) {
                        doctorDao.update(doctor);
                    } else {
                        doctorDao.persist(doctorToWrite);
                    }
                    List<Doctor> doctors = DaoFactory.getInstance().getDoctorDao().getAll();
                    grid.setItems(doctors);
                } catch (DaoException e) {
                    logger.severe(e.getMessage());
                }
                close();
            }
        });

        cancelButton.addClickListener(clickEvent ->
                close());
    }
}