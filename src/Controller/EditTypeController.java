package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Controller.SettingsController.globalDep;
import static Controller.SettingsController.globalType;

public class EditTypeController implements Initializable {

    // Open Connection for database
    private Connection con = null;
    private Statement st;
    private ResultSet rs;
    private Stage stage;
    public EditTypeController()
    {
        con = DbHelperPSQL.ConnectDB();
    }

    @FXML
    private JFXToggleButton tglStatus;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXTextField txtType;

    @FXML
    private StackPane stackButton;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnEdit;

    @FXML
    void handleActionButton(ActionEvent event) {
        if(event.getSource() == btnEdit)
        {
            txtType.setEditable(true);
            tglStatus.setDisable(false);
            btnSave.toFront();
        }
        if(event.getSource() == btnSave)
        {
            try {
                st = con.createStatement();
                st.executeQuery("UPDATE tbl_employee SET type_name = '" + txtType.getText() + "', status = '" + txtType.getText()+ "' WHERE type_name ='"+globalType+"';");
                tglStatus.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {

                        if(tglStatus.isSelected() == true)
                        {
                            tglStatus.setText("Active");
                            tglStatus.setSelected(true);
                            tglStatus.setToggleColor(Color.GREEN);
                        }
                        else {
                            tglStatus.setText("Inactive");
                            tglStatus.setSelected(false);
                            tglStatus.setToggleColor(Color.GRAY);
                        }

                    }
                });

            } catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
            txtType.setEditable(false);
            tglStatus.setDisable(true);
            btnEdit.toFront();
        }
        if(event.getSource() == btnBack)
        {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        }
    }
    private void toggle(){
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT status FROM tbl_typeofemp WHERE type_name = '"+globalType+"';");
            if(rs.next())
            {
                tglStatus.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        try {
                            if(rs.getString("status").equals("Active"))
                            {
                                tglStatus.setText("Active");
                                tglStatus.setSelected(true);
                                tglStatus.setToggleColor(Color.GREEN);
                            }
                            else {
                                tglStatus.setText("Inactive");
                                tglStatus.setSelected(true);
                                tglStatus.setToggleColor(Color.GRAY);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggle();
    }
}
