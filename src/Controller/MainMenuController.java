package Controller;

import animatefx.animation.FlipInY;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // Controller initialization interface
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL; //Package url parses URLs and implements query escaping.
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle; //Resource bundles contain locale-specific objects.
import java.util.logging.Level;
import java.util.logging.Logger; // log in concole to know the error

import static Controller.LoginController.globalName;

public class MainMenuController implements Initializable {
    private Connection con = null;
    private Statement st;
    private ResultSet rs;
    private Object LoginController;
    private Object Scene;

    public MainMenuController()
    {
        con = DbHelperPSQL.ConnectDB();
    }
    @FXML
    private Button btnDash,btnEmployee,btnRecord,btnLogout, btnSettings, btnProfile, btnTimeIn2;
    @FXML
    private BorderPane bdPane;
    @FXML
    private Pane pnDash;
    @FXML
    private Label lblTimeIn,lblTotalGen,lblTotalEmp,lblTotalAbs;

    @FXML
    private ImageView imgViewTime,imgViewGen,imgViewEmp,imgViewAbs;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        totalEmployee();
        totalInactive();
        totalTimeIn();
    }


    private void loadpage(String page)
    {
        try{
            Pane root = FXMLLoader.load(getClass().getResource("../View/"+page+".fxml"));
            bdPane.setCenter(root);

        }catch (IOException e)
        {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void loadpage1(String page)
    {
        try{
            StackPane root = FXMLLoader.load(getClass().getResource("../View/"+page+".fxml"));
            bdPane.setCenter(root);

        }catch (IOException e)
        {
            Logger.getLogger(MainMenuController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    void handleActionClicked(MouseEvent event){
        if(event.getSource() == imgViewTime){
            loadpage("Records");
        }
        if(event.getSource() == imgViewEmp){
            loadpage1("Employee");
        }
        if(event.getSource() == imgViewGen){

        }
        if(event.getSource() == imgViewAbs){
            loadpage1("Employee");
        }
    }

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        //To select Employee
        if (event.getSource() == btnEmployee) {
            loadpage1("Employee");
        }
        //To select dashboard
        if (event.getSource().equals(btnDash)) {
            bdPane.setCenter(pnDash);
            totalEmployee();
        }

        if (event.getSource().equals(btnProfile)) {
            loadpage("Profile");
            totalEmployee();
        }
        //To select records
        if (event.getSource().equals(btnRecord)) {
            loadpage("Records");
        }
        // to select settings
        if (event.getSource().equals(btnSettings)) {
            loadpage("Settings");

        }
        //To logout
        if (event.getSource().equals(btnLogout)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Log out");
            alert.setHeaderText("Are you sure do you want to log out?");
            alert.setContentText("Are you ok with this?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("../View/Login.fxml"));
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(new Scene(root));
                globalName = "";
                //to set screen on Center
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
        //When you click the Time-In Button, it will go to Login Scene/Page
        if (event.getSource().equals(btnTimeIn2)) {
            //to navigate to the next page
            Parent root = FXMLLoader.load(getClass().getResource("../View/TimeIn.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initOwner(btnTimeIn2.getScene().getWindow());

                stage.showAndWait();

                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

        }
        }
    private void totalEmployee() // to count the number of employee
    {
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM tbl_employee WHERE Status = 'Active'");
            rs.next();
            int rowCount = rs.getInt(1);
            lblTotalEmp.setText(String.valueOf(rowCount)); // to convert rowcount to String
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void totalInactive() // to count the number of employee
    {
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM tbl_employee WHERE Status = 'Inactive'");
            rs.next();
            int rowCount = rs.getInt(1);
            lblTotalAbs.setText(String.valueOf(rowCount)); // to convert rowcount to String
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void totalTimeIn() // to count the number of employee
    {
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT COUNT(*) FROM tbl_monthlytime WHERE logindate = CURRENT_DATE");
            rs.next();
            int rowCount = rs.getInt(1);
            lblTimeIn.setText(String.valueOf(rowCount)); // to convert rowcount to String
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    };