package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.event.ActionEvent;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Controller.EmployeeController.globalId;

public class ViewEmployeeController implements Initializable{
    // Open Connection for database
    private Connection con = null;
    private Statement st;
    private ResultSet rs;
    private Stage stage;
    public ViewEmployeeController()
    {
        con = DbHelperPSQL.ConnectDB();
    }

    @FXML
    private Pane pnlViewEmp;

    @FXML
    private JFXTextField txtEmpId,txtFirst,txtLast,txtEmail,txtPassword,txtPhone;

    @FXML
    private JFXComboBox<String> cbxType,cbxDept;

    @FXML
    private JFXButton btnEdit,btnBack, btnSave;

    @FXML
    private StackPane stackButton;

    @FXML
    private JFXToggleButton toggleBtnStatus;

    @FXML
    private ImageView imgViewPassword;

    @FXML
    private ImageView imgViewPassword2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        FillData();
        ToggleStatus();
        comboBoxType();
        comboBoxDep();

    }

    @FXML
    public void handleActionButton(ActionEvent event) {
        if (event.getSource() == btnEdit) {
            txtEmpId.setEditable(false);
            txtFirst.setEditable(false);
            txtLast.setEditable(false);
            txtEmail.setEditable(true);
            txtPassword.setEditable(true);
            txtPhone.setEditable(true);
            cbxType.setDisable(false);
            cbxDept.setDisable(false);
            btnSave.toFront();
        }
        if (event.getSource() == btnSave) {
            try {
                st = con.createStatement();
                st.executeUpdate("UPDATE tbl_employee SET email = '" + txtEmail.getText() + "', password = '" + txtPassword.getText() + "'" +
                        ", contact = '" + txtPhone.getText() + "', department = '" + cbxDept.getValue() + "', typeofemp = '" + cbxType.getValue() + "'"+
                        " WHERE employee_id = '"+txtEmpId.getText()+"';");


            } catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
            txtEmpId.setEditable(false);
            txtFirst.setEditable(false);
            txtLast.setEditable(false);
            txtEmail.setEditable(false);
            txtPassword.setEditable(false);
            txtPhone.setEditable(false);
            cbxType.setDisable(true);
            cbxDept.setDisable(true);
//            EmployeeController obj = new EmployeeController();
//            obj.oblist.removeAll(obj.oblist);
//            obj.tableEmployee();
            btnEdit.toFront();
        }
        if (event.getSource() == btnBack) {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close();
        }
        //Toggle Button (Status: Active/Inactive)
        toggleBtnStatus.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            }
        });
        if(toggleBtnStatus.isSelected() == true) {
            toggleBtnStatus.setText("Active");
            System.out.println("Active");
            System.out.println(txtEmpId.getText());
            try {
                st = con.createStatement();
                st.executeUpdate("UPDATE tbl_employee SET Status = 'Active' WHERE employee_id = '" + txtEmpId.getText() + "';");

            } catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
        } else {
            toggleBtnStatus.setText("Inactive");
            System.out.println("Inactive");
            try {
                st = con.createStatement();
                st.executeUpdate("UPDATE tbl_employee SET Status = 'Inactive' WHERE employee_id = '" + txtEmpId.getText() + "';");
            } catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
//    @FXML
//    void handleActionClicked(MouseEvent event) {
//        this.imgViewPassword2.setVisible(false);
//        if (event.getSource() == imgViewPassword2) {
//            imgViewPassword2.setVisible(true);
//            imgViewPassword.setVisible(false);
//            txtPassword.setText("");
//        }
//        if (event.getSource() == imgViewPassword) {
//            imgViewPassword.setVisible(true);
//            imgViewPassword2.setVisible(false);
//            txtPassword.setText("*");
//        }
//    }
    private void FillData()
    {
        try{
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM tbl_employee WHERE employee_id = '"+globalId+"';");
            rs.next();
            txtEmpId.setText(rs.getString("employee_id"));
            txtFirst.setText(rs.getString("firstname"));
            txtLast.setText(rs.getString("lastname"));
            txtEmail.setText(rs.getString("email"));
            txtPassword .setText(rs.getString("password"));
            cbxDept.setPromptText(rs.getString("department"));
            cbxType.setPromptText(rs.getString("typeofemp"));
            txtPhone.setText(rs.getString("contact"));
            if (rs.getString("status").equals("Active")) {
                toggleBtnStatus.setText("Active");
                toggleBtnStatus.setSelected(true);
                toggleBtnStatus.setToggleColor(Color.WHITE);
                toggleBtnStatus.setToggleLineColor(Color.LIME);
            } else {
                toggleBtnStatus.setText("Inactive");
                toggleBtnStatus.setSelected(false);
                toggleBtnStatus.setToggleColor(Color.WHITE);
                toggleBtnStatus.setToggleLineColor(Color.GRAY);
            }
        }catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    @FXML
    void handleActionClicked(MouseEvent event) {

    }
    private void ToggleStatus(){
        try {
            st = con.createStatement();
            rs  = st.executeQuery("SELECT Status FROM tbl_employee WHERE employee_id = '"+globalId+"';");

            if(rs.next()){
                if(rs.getString("status").equalsIgnoreCase("Inactive")){
                    toggleBtnStatus.isPressed();
                    System.out.println("Inactive");
                }
                else{
                    toggleBtnStatus.isDisable();
                }
            }

        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void comboBoxDep()
    {
        ObservableList<String> listacombo= FXCollections.observableArrayList();
        try{
            st = con.createStatement();
            String query = "SELECT * FROM tbl_department";
            rs = st.executeQuery(query);

            while (rs.next())
            {
                String name = rs.getString("department_name");
                listacombo.add(name);
            }//end while
            cbxDept.setItems(listacombo);
            // con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void comboBoxType()
    {
        ObservableList<String> listacombo= FXCollections.observableArrayList();
        try{
            st = con.createStatement();
            String query = "SELECT * FROM tbl_typeofemp";
            rs = st.executeQuery(query);

            while (rs.next())
            {
                String name = rs.getString("type_name");
                listacombo.add(name);
                //    cbxType.getItems().add(new Label(name));

            }//end while
            cbxType.setItems(listacombo);
            //    con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
