package Controller;

import Model.ModelTableEmployee; // Constructor of the table
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections; // Utility class that consists of static methods
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;//Package url parses URLs and implements query escaping.
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle; //Resource bundles contain locale-specific objects.
import java.util.logging.Level;
import java.util.logging.Logger; // log in concole to know the error

public class EmployeeController implements Initializable {
    // Open Connection for database
    private Connection con = null;
    private Statement st;
    private ResultSet rs;
    private Stage stage;
    public EmployeeController()
    {
        con = DbHelperPSQL.ConnectDB();
    }

    static String globalId;

    @FXML
    private TextField txtSearch;
    @FXML
    private JFXButton btnCancel,btnSave ,btnAddEmp;
    @FXML
    private JFXComboBox<String> cbxDept,cbxType;
    @FXML
    private JFXTextField txtLast,txtFirst,txtEmail,txtAddUsername,txtContact,txtAddress,txtEmpID;
    @FXML
    private Label lblErContact,lblErLast,lblErFirst,lblErEmail,lblErUser,lblErAddress,lblErEmpID, lblErDept, lblErType,lblEmpSuc;
    @FXML
    private ImageView imgUser, imgErEmp;
    @FXML
    private ImageView btnBacktoEmp;
    @FXML
    private Pane pnlAddEmp, pnEmp = new Pane();
// TABLE MODEL FOR RETRIEVING DATA FROM DATABASE
    @FXML
    private TableView<ModelTableEmployee> tbl_employee;

    @FXML
    private TableColumn<ModelTableEmployee, String> col_EmpId,col_FName,col_LName,col_Type,col_Dept,col_Status,col_LastOut;

    @FXML
    private TableColumn col_Action;
    ObservableList<ModelTableEmployee> oblist = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxDep();
        comboBoxType();
        tableEmployee();

    }
//    public void setStage(Stage stage) {
//        this.stage =stage;
//    }
    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource().equals(btnSave)){
            String Last = txtLast.getText(),First = txtFirst.getText(),Email =txtEmail.getText(),AddUsername = txtAddUsername.getText(),Contact = txtContact.getText();
            String Address = txtAddress.getText(),EmpID = txtEmpID.getText();
            String Dept = cbxDept.getValue(),Type = cbxType.getValue();
            if(Last.equals(""))
                lblErLast.setVisible(true);
            else
                lblErLast.setVisible(false);
            if(First.equals(""))
                lblErFirst.setVisible(true);
            else
                lblErFirst.setVisible(false);
            if(Email.equals(""))
                lblErEmail.setVisible(true);
            else
                lblErEmail.setVisible(false);
            if(AddUsername.equals(""))
                lblErUser.setVisible(true);
            else
                lblErUser.setVisible(false);
            if(Contact.equals(""))
                lblErContact.setVisible(true);
            else
                lblErContact.setVisible(false);
            if(Address.equals(""))
                lblErAddress.setVisible(true);
            else
                lblErAddress.setVisible(false);
            if(EmpID.equals(""))
                lblErEmpID.setVisible(true);
            else
                lblErEmpID.setVisible(false);
            if(cbxDept.getValue() == null)
                lblErDept.setVisible(true);
            else
                lblErDept.setVisible(false);
            if(cbxType.getValue() == null)
                lblErType.setVisible(true);
            else
                lblErType.setVisible(false);
            if(!(Last.equals("") && First.equals("") && Email.equals("") && AddUsername.equals("") && Contact.equals("") && Address.equals("") && EmpID.equals("") && cbxDept.getValue() == null && cbxType.getValue() == null) && imgUser.isVisible() && imgErEmp.isVisible())
            {
                try{
                    st = con.createStatement();
                    st.executeUpdate("INSERT INTO tbl_employee (employee_id, firstname, lastname, username, password," +
                            "email, address,contact, department, status, typeofemp, created_on) VALUES ('"+EmpID+"',"+
                            "'"+First+"','"+Last+"','"+AddUsername+"','123','"+Email+"','"+Address+"','"+Contact+"', " +
                            "'"+Dept+"','Active','"+Type+"', current_timestamp);");

                    lblEmpSuc.setVisible(true);
                    oblist.removeAll(oblist);
                    tableEmployee();
                    ClearAllText();
                }catch (Exception e) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
                }
                //   lblEmpSuc.setVisible(true);
                //    ClearAllText();
            }
        }
        //===to proceed to Employee panel ==
        if(event.getSource().equals(btnCancel)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cancel Adding Employee");
            alert.setHeaderText("Are you sure do you want to cancel?");
            alert.setContentText("Are you ok with this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                pnEmp.toFront();
                ClearAllText();
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
        if(event.getSource().equals(btnAddEmp)){
            new FadeIn(pnlAddEmp).play();
            pnlAddEmp.toFront();
        }
    }
    // ======= NOT TO DUPLICATE USERNAME
    @FXML
    void UsernameKey(KeyEvent event) {
        if (event.getSource() == txtAddUsername) {
            String AddUsername = txtAddUsername.getText();
            try {
                st = con.createStatement();
                rs = st.executeQuery("SELECT username FROM tbl_employee WHERE username = \'" + AddUsername + "';");
                if (rs.next()) {
                    if (rs.getString("username") != AddUsername) {
                        lblErUser.setTextFill(Color.TOMATO);
                        lblErUser.setText("The Username is already exist!");
                        lblErUser.setVisible(true);
                        imgUser.setVisible(false);
                    } else {
                    }
                    //    imgErEmp.setVisible(true);
                } else {
                    imgUser.setVisible(true);
                    lblErUser.setVisible(false);
                    lblErUser.setText("The Username is Required*");
                }
            } catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        // ======= NOT TO DUPLICATE EMPLOYEE ID
        if (event.getSource() == txtEmpID) {
            String EmpID = txtEmpID.getText();
            try {
                st = con.createStatement();
                rs = st.executeQuery("SELECT employee_id FROM tbl_employee WHERE employee_id = \'" + EmpID + "';");
                if (rs.next()) {
                    if (rs.getString("employee_id") != EmpID) {
                        lblErEmpID.setTextFill(Color.RED);
                        lblErEmpID.setText("The Username is already exist!");
                        lblErEmpID.setVisible(true);
                        imgErEmp.setVisible(false);
                    } else {
                    }
                } else {
                    imgErEmp.setVisible(true);
                    lblErEmpID.setVisible(false);
                    lblErEmpID.setText("The Username is Required*");
                }
            } catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        if (event.getSource() == txtContact) {
            if(txtContact.getText().length() >= 12)
            {
                lblErContact.setText("Invalid phone number");
                lblErContact.setVisible(true);
            }
            else
                lblErContact.setVisible(false);
        }
    }
    //key press events
    @FXML
    void KeyPressed(KeyEvent event) {
        if (event.getSource() == txtFirst) {
            new FadeOut(lblEmpSuc).play();
            lblEmpSuc.setVisible(false);
        }
    }

    // to back from add employee to employee
    @FXML
    void MouseClicked(MouseEvent event) {
        if (event.getSource() == btnBacktoEmp) {
            new FadeIn(pnEmp).play();
            pnEmp.toFront();
            ClearAllText();
//            txtUsername.setText("");
//            lblError.setText("");
        }

    }
    @FXML
    void SearchTyped(KeyEvent event) {
        if (event.getSource() == txtSearch) {
            String search = txtSearch.getText();
            oblist.removeAll(oblist);
            try{
                st = con.createStatement();
                rs = st.executeQuery("SELECT employee_id, firstname, lastname, typeofemp, department, status, created_on FROM tbl_employee WHERE employee_id LIKE '"+search+"%' OR firstname LIKE '"+search+"%' OR lastname LIKE '"+search+"%'");
                while (rs.next()) {
                    oblist.add(new ModelTableEmployee(rs.getString("employee_id"), rs.getString("firstname"), rs.getString("lastname"),
                            rs.getString("typeofemp"), rs.getString("department"), rs.getString("status"), rs.getString("created_on")));
                }
            }catch(Exception e){
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
            col_EmpId.setCellValueFactory(new PropertyValueFactory<>("EmpId"));
            col_FName.setCellValueFactory(new PropertyValueFactory<>("FName"));
            col_LName.setCellValueFactory(new PropertyValueFactory<>("LName"));
            col_Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
            col_Dept.setCellValueFactory(new PropertyValueFactory<>("Dept"));
            col_Status.setCellValueFactory(new PropertyValueFactory<>("Status"));
            col_LastOut.setCellValueFactory(new PropertyValueFactory<>("LastOut"));

            //ACTION BUTTON ON EVERY ROW
            Callback<TableColumn<ModelTableEmployee, String>, TableCell<ModelTableEmployee, String>> cellFactory = (param) -> {
                final TableCell<ModelTableEmployee, String> cell = new TableCell<ModelTableEmployee, String>() {
                    // override updateItem method
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        //ensure that cell is create only on non-empty row
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            //Now we can create the action button
                            final Button editButton = new Button("View");
                            //Attach listener on button, what to do when clicked
                            editButton.setOnAction(event -> {
                                //Extract the clicked Person Objec


                                ModelTableEmployee m = getTableView().getItems().get(getIndex());
                                globalId = m.getEmpId();
                                // let's show which item has been selected
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("../View/ViewEmployee.fxml"));
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
                    }

                    ;
                };
                return cell; // to change body of generated lambdas, choose Tools or Templates.
            };
            col_Action.setCellFactory(cellFactory);

        tbl_employee.setItems(oblist);

        }


    }
    // to print data in combobox from database
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
    // ===== TO CLEAR ALL FIELDS
    private void ClearAllText()
    {
        txtLast.setText("");
        txtFirst.setText("");
        txtEmail.setText("");
        txtAddUsername.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        txtEmpID.setText("");
        cbxType.getSelectionModel().clearSelection();
        cbxDept.getSelectionModel().clearSelection();
        imgUser.setVisible(false);
        lblErContact.setVisible(false);
        lblErLast.setVisible(false);
        lblErFirst.setVisible(false);
        lblErEmail.setVisible(false);
        lblErUser.setVisible(false);
        lblErAddress.setVisible(false);
        lblErEmpID.setVisible(false);
        lblErDept.setVisible(false);
        lblErType.setVisible(false);
        imgErEmp.setVisible(false);
    }
    // COMBO BOX FROM DATABASE
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

    // =================== TABLE LIST IN EMPLOYEE =======================
    public void tableEmployee()
    {
        try{
            st = con.createStatement();
            rs = st.executeQuery("SELECT employee_id, firstname, lastname, typeofemp, department, status, created_on FROM tbl_employee");
            while (rs.next())
            {
                oblist.add(new ModelTableEmployee(rs.getString("employee_id"),rs.getString("firstname"),rs.getString("lastname"),
                        rs.getString("typeofemp"),rs.getString("department"),rs.getString("status"),rs.getString("created_on")));
            }
        }catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
        col_EmpId.setCellValueFactory(new PropertyValueFactory<>("EmpId"));
        col_FName.setCellValueFactory(new PropertyValueFactory<>("FName"));
        col_LName.setCellValueFactory(new PropertyValueFactory<>("LName"));
        col_Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        col_Dept.setCellValueFactory(new PropertyValueFactory<>("Dept"));
        col_Status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        col_LastOut.setCellValueFactory(new PropertyValueFactory<>("LastOut"));

        //ACTION BUTTON ON EVERY ROW
        Callback<TableColumn<ModelTableEmployee, String>, TableCell<ModelTableEmployee, String>> cellFactory = (param) -> {
            final TableCell<ModelTableEmployee, String> cell = new TableCell<ModelTableEmployee, String>(){
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
                        final Button editButton = new Button("View");
                        //Attach listener on button, what to do when clicked
                        editButton.setOnAction(event ->{
                            //Extract the clicked Person Objec


                            ModelTableEmployee m = getTableView().getItems().get(getIndex());
                            globalId = m.getEmpId();
                            // let's show which item has been selected
                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("../View/ViewEmployee.fxml"));
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

        tbl_employee.setItems(oblist);
    }
}
