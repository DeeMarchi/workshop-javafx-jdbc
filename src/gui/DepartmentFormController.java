package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private void initializeNodes() {
        Constraints.setTextFieldInteger(textId);
        Constraints.setTextFieldMaxLength(textName, 30);
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
    public void onButtonSaveAction() {
        System.out.println("onButtonSaveAction");
    }

    @FXML
    public void onButtonCancelAction() {
        System.out.println("onButtonCancelAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        initializeNodes();
    }
}
