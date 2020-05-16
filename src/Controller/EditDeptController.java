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

public class EditDeptController implements Initializable {
    // Open Connection for database
    private Connection con = null;
    private Statement st;
    private ResultSet rs;
    private Stage stage;
    public EditDeptController()
    {
        con = DbHelperPSQL.ConnectDB();
    }

    @FXML
    private JFXToggleButton tglStatus;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXTextField txtDept;

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
            txtDept.setEditable(true);
            tglStatus.setDisable(false);
            btnSave.toFront();
        }
        if(event.getSource() == btnSave)
        {
            try {
                st = con.createStatement();
                st.executeUpdate("UPDATE tbl_department SET department_name = '" + txtDept.getText() + "', status = '" + tglStatus.getText()+ "' WHERE department_name ='"+globalDep+"';");
                tglStatus.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {

                            if(tglStatus.isSelected() == true)
                            {
                                tglStatus.setText("Active");
                                tglStatus.setSelected(true);
                                tglStatus.setToggleColor(Color.WHITE);
                                tglStatus.setToggleLineColor(Color.LIME);
                            }
                            else {
                                tglStatus.setText("Inactive");
                                tglStatus.setSelected(false);
                                tglStatus.setToggleColor(Color.WHITE);
                                tglStatus.setToggleLineColor(Color.GRAY);
                            }

                    }
                });

            } catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
            txtDept.setEditable(false);
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

                tglStatus.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                        if(tglStatus.isSelected() == true)
                            {
                                tglStatus.setText("Active");
                                tglStatus.setToggleColor(Color.WHITE);
                                tglStatus.setToggleLineColor(Color.LIME);
                            }
                            else {
                                tglStatus.setText("Inactive");
                                tglStatus.setToggleColor(Color.WHITE);
                                tglStatus.setToggleLineColor(Color.GRAY);
                            }
                    }
                });


    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FillData();
        toggle();
    }

    private void FillData() {
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM tbl_department WHERE department_name = '" + globalDep + "';");
            rs.next();
            txtDept.setText(rs.getString("department_name"));
//            EditDeptController obj = new EditDeptController();
//            obj.toggle();
            if (rs.getString("status").equals("Active")) {
                tglStatus.setText("Active");
                tglStatus.setSelected(true);
                tglStatus.setToggleColor(Color.WHITE);
                tglStatus.setToggleLineColor(Color.LIME);
            } else {
                tglStatus.setText("Inactive");
                tglStatus.setSelected(false);
                tglStatus.setToggleColor(Color.WHITE);
                tglStatus.setToggleLineColor(Color.GRAY);
            }

        } catch (Exception e) {
            Logger.getLogger(EditDeptController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
