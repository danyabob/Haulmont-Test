package com.haulmont.testtask.ui;

import com.haulmont.testtask.AppTheme;
import com.haulmont.testtask.dao.DaoException;
import com.haulmont.testtask.database.DaoFactory;
import com.haulmont.testtask.entity.Receipt;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.Renderer;

import java.util.List;
import java.util.logging.Logger;

//Класс реализует отображение информации о рецептах в таблице
//Шеюхин Данил Борисович
//danya.bob@gmail.com

public class ReceiptView extends VerticalLayout implements View {

    public static final String NAME = "receipts";

    private Grid<Receipt> receiptGrid = new Grid<>(Receipt.class);

    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private TextField descriptionText;
    private TextField patientText;
    private TextField priorityText;

    private static Logger logger = Logger.getLogger(ReceiptView.class.getName());

    /**
     * Конструктор отображения информации о рецептах
     */
    public ReceiptView() {
        buildReceiptView();
        setupListeners();
    }

    private void buildReceiptView() {
        try {
            Panel filterPanel = new Panel("Фильтр рецептов");
            HorizontalLayout filterLayout = new HorizontalLayout();
            filterLayout.setMargin(true);
            filterLayout.setSpacing(true);

            descriptionText = new TextField();
            priorityText = new TextField();
            patientText = new TextField();
            descriptionText.setPlaceholder("Препарат");
            priorityText.setPlaceholder("Приоритет");
            patientText.setPlaceholder("ФИО пациента");

            filterLayout.addComponents(patientText, descriptionText, priorityText);
            filterPanel.setContent(filterLayout);

            receiptGrid.removeAllColumns();
            receiptGrid.addColumn(receipt -> receipt.getPatient().getSurname() + " " + receipt.getPatient().getName().substring(0, 1) + ". " + receipt.getPatient().getPatronymic().substring(0, 1) + ". ")
                    .setCaption("ФИО пациента");
            receiptGrid.addColumn(receipt -> receipt.getDoctor().getSurname() + " " + receipt.getDoctor().getName().substring(0, 1) + ". " + receipt.getDoctor().getPatronymic().substring(0, 1) + ". ")
                    .setCaption("ФИО врача");
            receiptGrid.addColumn(Receipt::getDescription).setCaption("Препарат");
            receiptGrid.addColumn(Receipt::getCreationDate)
                    .setRenderer((Renderer) new DateRenderer("%1$td.%1$tm.%1$tY"))
                    .setCaption("Дата создания");
            receiptGrid.addColumn(Receipt::getExpireDate)
                    .setRenderer((Renderer) new DateRenderer("%1$td.%1$tm.%1$tY"))
                    .setCaption("Дата истечения срока");
            receiptGrid.addColumn(Receipt::getPriority).setCaption("Приоритет");
            receiptGrid.setSizeFull();

            HorizontalLayout buttonsLayout = new HorizontalLayout();
            buttonsLayout.setSpacing(true);

            addButton = new Button("Добавить рецепт", new ThemeResource(AppTheme.BUTTON_ADD));
            editButton = new Button("Изменить рецепт", new ThemeResource(AppTheme.BUTTON_EDIT));
            deleteButton = new Button("Удалить рецепт", new ThemeResource(AppTheme.BUTTON_DELETE));
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            buttonsLayout.addComponents(addButton, editButton, deleteButton);

            setMargin(true);
            setSpacing(true);
            setSizeFull();
            addComponents(filterPanel, receiptGrid, buttonsLayout);
            setExpandRatio(receiptGrid, 1f);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void setupListeners() {
        try {
            receiptGrid.addSelectionListener(valueChangeEvent -> {
                if (!receiptGrid.asSingleSelect().isEmpty()) {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                } else {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                }
            });

            addButton.addClickListener(clickEvent ->
                    getUI().addWindow(new ReceiptWindow(receiptGrid, false)));

            editButton.addClickListener(clickEvent -> {
                getUI().addWindow(new ReceiptWindow(receiptGrid, true));
            });

            deleteButton.addClickListener(clickEvent -> {
                if (!receiptGrid.asSingleSelect().isEmpty()) {
                    try {
                        DaoFactory.getInstance().getReceiptDao().delete(receiptGrid.asSingleSelect().getValue());
                        updateGrid();
                    } catch (DaoException e) {
                        logger.severe(e.getMessage());
                    }
                }
            });

            patientText.addValueChangeListener(this::onFilterChange);

            priorityText.addValueChangeListener(this::onFilterChange);

            descriptionText.addValueChangeListener(this::onFilterChange);

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Метод для фильтрации таблицы в зависимости от введенных в поля (пациент, описание, приоритет)
     * информации
     *
     * @param event параметр, проверямый на изменение введенной информации
     */
    private void onFilterChange(HasValue.ValueChangeEvent<String> event) {
        try {
            ListDataProvider<Receipt> dataProvider = (ListDataProvider<Receipt>) receiptGrid.getDataProvider();
            dataProvider.setFilter((item) -> {
                boolean patientFilter = (item.getPatient().getSurname() + " " + item.getPatient()
                        .getName())
                        .toLowerCase()
                        .contains(patientText.getValue().toLowerCase());
                boolean descriptionFilter = item.getDescription()
                        .toLowerCase()
                        .contains(descriptionText.getValue().toLowerCase());
                boolean priorityFilter = item.getPriority()
                        .toLowerCase()
                        .contains(priorityText.getValue().toLowerCase());
                return patientFilter && descriptionFilter && priorityFilter;
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void updateGrid() {
        try {
            List<Receipt> receipts = DaoFactory.getInstance().getReceiptDao().getAll();
            receiptGrid.setItems(receipts);
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateGrid();
    }
}