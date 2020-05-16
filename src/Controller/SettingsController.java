package Controller;


//import Controller.Maintenance;
import Model.ModelTableDaily;
import Model.ModelTableDepartment;
import Model.ModelTableType;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javafx.stage.*;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;

import static Controller.LoginController.globalId;

public class SettingsController implements Initializable {
    // Open Connection for database
    private Connection con = null;
    private Statement st;
    private ResultSet rs;

    static String globalDep, globalType;

    //==== FOR ALL ===
    @FXML
    private ImageView imgDep, imgEmp;

    @FXML
    private Label lblGroups, lblPlease;

    //=======For Department =========
    @FXML
    private TableView<ModelTableDepartment> tbl_Dept;

    @FXML
    private TableColumn<ModelTableDepartment, String> col_Dept,col_Status;
    @FXML
    private TableColumn col_Action;

    @FXML
    private TableColumn<ModelTableDepartment, String> col_Date;

    @FXML
    private Label lblDept;

    @FXML
    private JFXButton btn_AddDept;
    @FXML
    private TextField txtAddDept;

    //======== FOR TYPE OF EMPLOYEE =========
    @FXML
    private TableView<ModelTableType> tbl_Type;

    @FXML
    private TableColumn<ModelTableType, String> col_Type,col_Status1;

    @FXML
    private TableColumn col_Action1;

    @FXML
    private Label lblType;

    @FXML
    private TableColumn<ModelTableType, String> col_Date1;

    @FXML
    private JFXButton btn_AddType;
    @FXML
    private TextField txtAddType;

    //================= ObserverList ==========================
    ObservableList<ModelTableDepartment> oblistdep = FXCollections.observableArrayList();
    ObservableList<ModelTableType> oblisttype = FXCollections.observableArrayList();

    //========= FOR CHANGE PASS ======

    @FXML
    private JFXButton btnType_Employee, btnDepartment, btnChangepass, btnSavePass;

    @FXML
    private Pane pnChangepass, pnDept, pnType, pnSettings;

    @FXML
    private PasswordField txtCurrentPass, txtNewPass, txtRenewPass;

    @FXML
    private Label lblMatch, lblNew;

    @FXML
    private ImageView imgCurrentWrong, imgRetypeCheck, imgRetypeWrong, imgChar, imgNum,imgChar1, imgNum1;



    public SettingsController() {
        con = DbHelperPSQL.ConnectDB();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            PleaseChange();
            Access();
            tableDepartment();
            tableType();
            tbl_Dept.refresh();
            tbl_Type.refresh();
    }

    Alert a = new Alert(Alert.AlertType.NONE);
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {

        if(event.getSource() == btnChangepass)
        {
            pnDept.setVisible(false);
            pnType.setVisible(false);
            pnChangepass.setVisible(true);
            pnChangepass.toFront();

        }
        if(event.getSource() == btnDepartment)
        {
            lblType.setVisible(false);
            pnChangepass.setVisible(false);
            pnType.setVisible(false);
            pnDept.setVisible(true);
            pnDept.toFront();

        }
        if(event.getSource() == btnType_Employee)
        {
            lblDept.setVisible(false);
            pnChangepass.setVisible(false);
            pnDept.setVisible(false);
            pnType.setVisible(true);
            pnType.toFront();

        }
        if(event.getSource() == btnSavePass)
        {
            if(imgRetypeCheck.isVisible() && imgChar.isVisible() && imgNum.isVisible())
            {
                try {
                    String pass = txtRenewPass.getText();
                    st = con.createStatement();
                    st.executeUpdate("UPDATE tbl_employee SET password = '"+pass+"' WHERE employee_id = '"+globalId+"';");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Password Changed");
                    alert.setHeaderText("Password Changed Successfully");
                    alert.setContentText("Remember your password.");

                    alert.showAndWait();
                    PleaseChange();
                    txtRenewPass.setText("");
                    txtNewPass.setText("");
                    txtCurrentPass.setText("");
                    txtRenewPass.setDisable(true);
                    txtNewPass.setDisable(true);
                    Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenuEmp.fxml"));
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    //to set screen on Center
                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                } catch (Exception e) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);

                }

            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Password Change Unsuccessful");
                alert.setHeaderText("Invalid Password");
                alert.setContentText("Follow the password requirements");

                alert.showAndWait();
            }

        }
        if(event.getSource() == btn_AddDept)
        {
            if(txtAddDept.getText().equals(""))
            {
                lblDept.setTextFill(Color.RED);
                lblDept.setVisible(true);
                lblDept.setText("Enter Department");

            }else {
                try {
                    String add = txtAddDept.getText();
                    st = con.createStatement();
                    st.executeUpdate("INSERT INTO tbl_Department(department_name, status, created_on)VALUES ('"+add+"','Active',CURRENT_DATE);");
                    tbl_Dept.refresh();
                }catch (Exception e) {
                    Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, e);

                }
                lblType.setTextFill(Color.GREEN);
                lblType.setVisible(true);
                txtAddDept.setText("");
                oblistdep.removeAll(oblistdep);
                tableDepartment();
                lblType.setText("Department Added Successfuly");

            }

        }
        if(event.getSource() == btn_AddType)
        {

            if(txtAddType.getText().equals(""))
            {
                lblType.setTextFill(Color.RED);
                lblType.setVisible(true);
                lblType.setText("Enter Type of Employee");

            }else {
                try {
                    String add = txtAddType.getText();
                    st = con.createStatement();
                    st.executeUpdate("INSERT INTO tbl_Typeofemp(type_name, status, created_on)VALUES ('"+add+"','Active',CURRENT_DATE);");

                }catch (Exception e) {
                    Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, e);

                }
                lblType.setTextFill(Color.GREEN);
                lblType.setVisible(true);
                txtAddType.setText("");
                oblisttype.removeAll(oblisttype);
                tableType();
                tbl_Type.refresh();
                lblType.setText("Type of Employee Added Successfuly");

            }

        }

    }

    @FXML
    void handleKeyTyped(KeyEvent event) {

        if(event.getSource() == txtCurrentPass) {
            try {
                String pass = txtCurrentPass.getText();
                st = con.createStatement();
                rs = st.executeQuery("SELECT  password FROM tbl_employee WHERE employee_id = '"+globalId+"' AND password = '"+pass+"';");

                if (!rs.next()) {
                    lblMatch.setVisible(true);
                    lblMatch.setTextFill(Color.TOMATO);
                    imgCurrentWrong.setVisible(true);
                    lblMatch.setText("Password Not Match");
                } else {
                    lblMatch.setVisible(true);
                    lblMatch.setTextFill(Color.GREEN);
                    imgCurrentWrong.setVisible(false);
                    lblMatch.setText("Password Match");
                    txtNewPass.setDisable(false);
                    txtRenewPass.setDisable(false);
                }
            } catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);

            }
        }
        if(event.getSource() == txtNewPass)
        {

            boolean NewPass = Pattern.compile( "[0-9]" ).matcher(txtNewPass.getText()).find();
            boolean aplha =  StringUtils.isAlpha(txtNewPass.getText());
            boolean num =  StringUtils.isNumeric(txtNewPass.getText());
            boolean empty =  StringUtils.isBlank(txtNewPass.getText());
            if(empty){
            imgChar.setVisible(false);
            imgNum.setVisible(false);
            imgChar1.setVisible(false);
            imgNum1.setVisible(false);
            }
            if(aplha  && txtNewPass.getText().length() >= 6){
                imgChar.setVisible(true);
                imgNum.setVisible(false);
                imgNum1.setVisible(true);
                imgChar1.setVisible(false);
            }
            if(num){
                imgNum.setVisible(true);
                imgNum1.setVisible(false);
                imgChar1.setVisible(true);
                imgChar.setVisible(false);
            }

            if(NewPass && txtNewPass.getText().length() >= 6){
                imgChar.setVisible(true);
                imgNum.setVisible(true);
                imgChar1.setVisible(false);
                imgNum1.setVisible(false);
            }
            if(NewPass && txtNewPass.getText().length() <= 6){
                imgChar.setVisible(false);
                imgNum.setVisible(true);
                imgChar1.setVisible(true);
                imgNum1.setVisible(false);
            }
            if(txtNewPass.getText().equals(""))
            {
                imgChar1.setVisible(true);
                imgNum1.setVisible(true);
                imgChar.setVisible(false);
                imgNum.setVisible(false);
            }
        }
        if(event.getSource() == txtRenewPass)
        {
            String renew = txtRenewPass.getText();
            if(renew.equals(txtNewPass.getText())){
                imgRetypeCheck.setVisible(true);
                imgRetypeWrong.setVisible(false);
            }
            else
            {
                imgRetypeWrong.setVisible(true);
                imgRetypeCheck.setVisible(false);
            }

        }

    }
    @FXML
    void handleKeyPressed(KeyEvent event) {

    }

    //======================= TABLE LIST IN DEPARTMENT ==========================
    private void tableDepartment(){
        try{
            st = con.createStatement();
            rs = st.executeQuery("SELECT department_name, status, TO_CHAR(created_on, 'mm/dd/yyyy')as date_created FROM tbl_Department;");
            while (rs.next())
            {
                oblistdep.add(new ModelTableDepartment(rs.getString("department_name"),rs.getString("status"),rs.getString("date_created")));
            }
        }catch (Exception e) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, e);
        }
        col_Dept.setCellValueFactory(new PropertyValueFactory<>("Dept"));
        col_Status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        col_Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        //ACTION BUTTON ON EVERY ROW
        Callback<TableColumn<ModelTableDepartment, String>, TableCell<ModelTableDepartment, String>> cellFactory = (param) -> {
            final TableCell<ModelTableDepartment, String> cell = new TableCell<ModelTableDepartment, String>(){
                // override updateItem method
                @Override
                public void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);

                    //ensure that cell is create only on non-empty row
                    if(empty){
                        setGraphic(null);
                        setText(null);
                    }
                    else
                    {
                        //Now we can create the action button
                        final Button editButton = new Button("Edit");
                        //Attach listener on button, what to do when clicked
                        editButton.setOnAction(event ->{
                            //Extract the clicked Person Object


                            ModelTableDepartment m = getTableView().getItems().get(getIndex());
                            globalDep = m.getDept();
                            // let's show which item has been selected
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../View/EditDept.fxml"));
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));

                                stage.initModality(Modality.WINDOW_MODAL);
                                stage.initStyle(StageStyle.UNDECORATED);
                                stage.initOwner(editButton.getScene().getWindow());
                                stage.show();
//                                stage.showAndWait();

                                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        setGraphic(editButton);
                        setText(null);
                    }
                };
            };
            return cell; // to change body of generated lambdas, choose Tools or Templates.
        };
        col_Action.setCellFactory(cellFactory);

        tbl_Dept.setItems(oblistdep);
    }
    private void tableType(){
        try{
            st = con.createStatement();
            rs = st.executeQuery("SELECT type_name, status, TO_CHAR(created_on, 'mm/dd/yyyy')as date_created FROM tbl_Typeofemp;");
            while (rs.next())
            {
                oblisttype.add(new ModelTableType(rs.getString("type_name"),rs.getString("status"),rs.getString("date_created")));
            }
        }catch (Exception e) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, e);
        }
        col_Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        col_Status1.setCellValueFactory(new PropertyValueFactory<>("Status"));
        col_Date1.setCellValueFactory(new PropertyValueFactory<>("Date"));

        //ACTION BUTTON ON EVERY ROW
        Callback<TableColumn<ModelTableType, String>, TableCell<ModelTableType, String>> cellFactorys=(param) ->{
            //Make table cell containing button
            final TableCell<ModelTableType, String> cells = new TableCell<ModelTableType, String>()
            {
                //override updateItem method
                @Override
                public void updateItem(String item, boolean empty){
                    super.updateItem(item, empty);

                    if(empty){
                        setGraphic(null);
                        setText(null);
                    }else{
                        //How we can create the action button
                        final Button edit = new Button("Edit");
                        //attach listener
                        edit.setOnAction(actionEvent -> {
                            ModelTableType m = getTableView().getItems().get(getIndex());
                            globalType = m.getType();
                            // let's show which item has been selected
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../View/EditType.fxml"));
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));

                                stage.initModality(Modality.WINDOW_MODAL);
                                stage.initStyle(StageStyle.UNDECORATED);
                                stage.initOwner(edit.getScene().getWindow());
                                stage.show();
//                                stage.showAndWait();

                                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            };
            return cells;
        };
        col_Action1.setCellFactory(cellFactorys);
        tbl_Type.setItems(oblisttype);
    }
    private void Access(){
        try{
            st = con.createStatement();
            rs = st.executeQuery("SELECT typeofemp FROM tbl_employee where employee_id='"+globalId+"';");
            if(rs.next())
            {
                if(!(rs.getString("typeofemp").equals("Administrator"))){
                    lblGroups.setVisible(false);
                    imgDep.setVisible(false);
                    imgEmp.setVisible(false);
                    btnType_Employee.setVisible(false);
                    btnDepartment.setVisible(false);
                }
                else
                {
                    lblGroups.setVisible(true);
                    imgDep.setVisible(true);
                    imgEmp.setVisible(true);
                    btnType_Employee.setVisible(true);
                    btnDepartment.setVisible(true);
                }
            }


        }catch (Exception e) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void PleaseChange()
    {
        try{
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM tbl_employee where employee_id='"+globalId+"' AND password = '123';");
            if(rs.next())
            {
                lblPlease.setVisible(true);
            }
            else
                lblPlease.setVisible(false);


        }catch (Exception e) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
