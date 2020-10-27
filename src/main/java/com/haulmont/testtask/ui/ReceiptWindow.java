package com.haulmont.testtask.ui;

import com.haulmont.testtask.AppTheme;
import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.database.DaoFactory;
import com.haulmont.testtask.database.ReceiptDao;
import com.haulmont.testtask.entity.Doctor;
import com.haulmont.testtask.entity.Patient;
import com.haulmont.testtask.entity.Receipt;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

//Класс реализует окно для добавления и изменения рецептов
//Шеюхин Данил Борисович
//danya.bob@gmail.com

class ReceiptWindow extends Window {

    private Grid<Receipt> grid;
    private boolean edit;
    private Button okButton;
    private Button cancelButton;
    private TextArea descriptionText;
    private ComboBox<Patient> patientComboBox;
    private ComboBox<Doctor> doctorComboBox;
    private DateField creationDateField;
    private DateField expireDateField;
    private ComboBox<String> priorityComboBox;

    private Binder<Receipt> binder = new Binder<>(Receipt.class);

    private Receipt receipt;

    private static Logger logger = Logger.getLogger(PatientWindow.class.getName());

    /**
     * Конструктор окна изменения/добавления рецептов
     */
    ReceiptWindow(Grid<Receipt> grid, boolean edit) {
        this.grid = grid;
        this.edit = edit;
        buildReceiptWindow();
        fillPatientsComboBox();
        fillDoctorsComboBox();
        fillPriorityComboBox();
        setupListeners();
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void buildReceiptWindow() {
        setStyleName(AppTheme.MODAL_WINDOW);
        setWidth("450px");
        setHeight("500px");
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

        descriptionText = new TextArea("Препарат");
        descriptionText.setMaxLength(140);
        descriptionText.setWidth("100%");
        binder.forField(descriptionText)
                .withValidator(string -> string != null && !string.isEmpty(), "Пожалуйста, введите препарат.")
                .asRequired()
                .bind(Receipt::getDescription, Receipt::setDescription);

        patientComboBox = new ComboBox<Patient>("ФИО пациента");
        patientComboBox.setTextInputAllowed(false);
        patientComboBox.setPlaceholder("Выберите пациента");
        patientComboBox.setWidth("100%");
        binder.forField(patientComboBox)
                .withValidator(Objects::nonNull, "Пожалуйста, выберите пациента.")
                .asRequired()
                .bind(Receipt::getPatient, Receipt::setPatient);

        doctorComboBox = new ComboBox<Doctor>("ФИО врача");
        doctorComboBox.setTextInputAllowed(false);
        doctorComboBox.setPlaceholder("Выберите врача");
        doctorComboBox.setWidth("100%");
        binder.forField(doctorComboBox)
                .withValidator(Objects::nonNull, "Пожалуйста, выберите врача")
                .asRequired()
                .bind(Receipt::getDoctor, Receipt::setDoctor);

        creationDateField = new DateField("Дата создания");
        creationDateField.setDateFormat("dd.MM.yyyy");
        creationDateField.setPlaceholder("дд.мм.гггг");
        creationDateField.setTextFieldEnabled(false);
        creationDateField.setWidth("100%");
        //Дата конвертируется из LocalDate в Date, для корректной записи в БД
        binder.forField(creationDateField)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .withValidator(Objects::nonNull, "Пожалуйста, выберите дату создания")
                .asRequired()
                .bind(Receipt::getCreationDate, Receipt::setCreationDate);

        expireDateField = new DateField("Дата окончания срока");
        expireDateField.setDateFormat("dd.MM.yyyy");
        expireDateField.setPlaceholder("дд.мм.гггг");
        expireDateField.setTextFieldEnabled(false);
        expireDateField.setWidth("100%");
        //Дата конвертируется из LocalDate в Date, для корректной записи в БД
        binder.forField(expireDateField)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .withValidator(Objects::nonNull, "Пожалуйста, выберите дату окончания срока.")
                .asRequired()
                .bind(Receipt::getExpireDate, Receipt::setExpireDate);

        priorityComboBox = new ComboBox<String>("Приоритет");
        priorityComboBox.setTextInputAllowed(false);
        priorityComboBox.setPageLength(5);
        priorityComboBox.setPlaceholder("Выберите приоритет");
        priorityComboBox.setWidth("100%");
        binder.forField(priorityComboBox)
                .withValidator(Objects::nonNull, "Пожалуйста, выберите приоритет.")
                .asRequired()
                .bind(Receipt::getPriority, Receipt::setPriority);

        formLayout.addComponents(patientComboBox, doctorComboBox, descriptionText, creationDateField, expireDateField, priorityComboBox);

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

    private void fillPatientsComboBox() {
        try {
            List<Patient> patients = DaoFactory.getInstance().getPatientDao().getAll();
            patientComboBox.setItems(patients);
            patientComboBox.setItemCaptionGenerator(patient -> patient.getSurname() + " " + patient.getName().substring(0, 1) + ". " + patient.getPatronymic().substring(0, 1) + ".");
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void fillDoctorsComboBox() {
        try {
            List<Doctor> doctors = DaoFactory.getInstance().getDoctorDao().getAll();
            doctorComboBox.setItems(doctors);
            doctorComboBox.setItemCaptionGenerator(doctor -> doctor.getSurname() + " " + doctor.getName().substring(0, 1) + ". " + doctor.getPatronymic().substring(0, 1) + ".");
        } catch (Exception e) {
            logger.severe(e.getMessage());

        }
    }

    private void fillPriorityComboBox() {
        try {
            List<String> priority = new ArrayList<>();
            priority.add("Нормальный");
            priority.add("Срочный");
            priority.add("Немедленный");
            priorityComboBox.setItems(priority);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        if (edit) {
            setCaption("Редактировать рецепт");
            if (!grid.asSingleSelect().isEmpty()) {
                try {
                    receipt = grid.asSingleSelect().getValue();
                    binder.setBean(receipt);
                } catch (Exception e) {
                    logger.severe(e.getMessage());
                }
            }
        } else {
            setCaption("Добавить рецепт");
            descriptionText.focus();
        }

        okButton.addClickListener(clickEvent -> {
            if (binder.validate().isOk()) {
                if ((Date.from(creationDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .compareTo(Date.from(expireDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant())) > 0) {
                    Notification notification = new Notification("Дата создания не может быть " +
                            "позже даты окончания срока годности.");
                    notification.setDelayMsec(2000);
                    notification.show(Page.getCurrent());
                } else {
                    try {
                        Receipt receiptToWrite = new Receipt();
                        receiptToWrite.setDescription(capitalize(descriptionText.getValue()));
                        receiptToWrite.setPatient(patientComboBox.getValue());
                        receiptToWrite.setDoctor(doctorComboBox.getValue());
                        receiptToWrite.setCreationDate(Date.from(creationDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        receiptToWrite.setExpireDate(Date.from(expireDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        receiptToWrite.setPriority(priorityComboBox.getValue());
                        ReceiptDao receiptDao = DaoFactory.getInstance().getReceiptDao();
                        if (edit) {
                            try {
                                receiptDao.update(receipt);
                            } catch (Exception e) {
                                logger.severe(e.getMessage());
                            }
                        } else {
                            receiptDao.persist(receiptToWrite);
                        }
                        List<Receipt> receipts = DaoFactory.getInstance().getReceiptDao().getAll();
                        grid.setItems(receipts);
                    } catch (DaoException e) {
                        logger.severe(e.getMessage());
                    }
                    close();
                }
            }
        });
        cancelButton.addClickListener(clickEvent -> close());
    }
}

