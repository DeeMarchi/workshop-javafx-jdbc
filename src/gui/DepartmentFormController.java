package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

import java.net.URL;
import java.util.*;

public class DepartmentFormController implements Initializable {

    private Department entity;

    private DepartmentService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    public void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(textId);
        Constraints.setTextFieldMaxLength(textName, 30);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        textId.setText(String.valueOf(entity.getId()));
        textName.setText(entity.getName());
    }

    private Department getFormData() {
        Department department = new Department();

        ValidationException exception = new ValidationException("Validation error");

        department.setId(Utils.tryParseToInt(textId.getText()));

        if (textName.getText() == null || textName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty");
        }
        department.setName(textName.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return department;
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
    }

    @FXML
    private TextField textId;

    @FXML
    private TextField textName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonCancel;

    @FXML
    public void onButtonSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onButtonCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        initializeNodes();
    }
}
