package com.haulmont.testtask.ui;

import com.haulmont.testtask.AppTheme;
import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.database.DaoFactory;
import com.haulmont.testtask.entity.Doctor;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.util.List;
import java.util.logging.Logger;

//Класс реализует отображение информации о врачах в таблице
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class DoctorView extends VerticalLayout implements View {

    public static final String NAME = "doctors";

    private Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);

    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Button statisticButton;

    private static Logger logger = Logger.getLogger(DoctorView.class.getName());

    public DoctorView() {
        buildDoctorView();
        setupListeners();
    }

    private void buildDoctorView() {
        try {
            doctorGrid.removeAllColumns();
            doctorGrid.addColumn(doctor -> doctor.getSurname() + " " + doctor.getName() + " " + doctor.getPatronymic()).setCaption("ФИО врача");
            doctorGrid.addColumn(Doctor::getSpecialization).setCaption("Специальность");
            //doctorGrid.setSizeFull();
            doctorGrid.getEditor().setEnabled(false);

            HorizontalLayout buttonsLayout = new HorizontalLayout();

            buttonsLayout.setHeight("48px");
            buttonsLayout.setWidth("100%");
            buttonsLayout.setMargin(false);
            buttonsLayout.setSpacing(true);

            addButton = new Button("Добавить врача", new ThemeResource(AppTheme.BUTTON_ADD));
            editButton = new Button("Изменить врача", new ThemeResource(AppTheme.BUTTON_EDIT));
            deleteButton = new Button("Удалить врача", new ThemeResource(AppTheme.BUTTON_DELETE));
            statisticButton = new Button("Статистика врачей", new ThemeResource(AppTheme.BUTTON_STATISTIC));
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            buttonsLayout.addComponents(addButton, editButton, deleteButton, statisticButton);
            //buttonsLayout.setComponentAlignment(statisticButton, Alignment.MIDDLE_RIGHT);
            buttonsLayout.setExpandRatio(statisticButton, 1f);

            setMargin(true);
            setSpacing(true);
            setSizeFull();
            addComponents(doctorGrid, buttonsLayout);
            setExpandRatio(doctorGrid, 1f);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        try {
            doctorGrid.addSelectionListener(valueChangeEvent -> {
                if (!doctorGrid.asSingleSelect().isEmpty()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new DoctorWindow(doctorGrid, false)));

            editButton.addClickListener(clickEvent -> {
                getUI().addWindow(new DoctorWindow(doctorGrid, true));
            });

            statisticButton.addClickListener(clickEvent -> {
                getUI().addWindow(new DoctorStatisticWindow());
            });

            deleteButton.addClickListener(clickEvent -> {
                if (!doctorGrid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getDoctorDao().delete(doctorGrid.asSingleSelect().getValue());
                        updateGrid();
                    } catch (DaoException e) {
                        if (e.getCause().getClass().equals(java.sql.SQLIntegrityConstraintViolationException.class)) {
                            Notification notification = new Notification("Нельзя удалить врача с активными рецептами");
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
            List<Doctor> doctors = DaoFactory.getInstance().getDoctorDao().getAll();
            doctorGrid.setItems(doctors);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }
}