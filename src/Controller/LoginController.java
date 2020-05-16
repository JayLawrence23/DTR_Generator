package Controller;

import animatefx.animation.FlipInY;
import animatefx.animation.ZoomIn;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Calendar.PM;

public class LoginController implements Initializable {

    static String globalName, globalId;

    @FXML
    private Circle btnClose;

    @FXML
    private Text lblTime, lblDay;

    @FXML
    private Text lblDate;

    @FXML
    private StackPane pnlStack;

    @FXML
    private Button btnTimeIn, btnTimeOut;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnSignIn;

    @FXML
    private ImageView btnBack;

    @FXML
    private TextField txtUsername, txtUsername1;

    @FXML
    private Text btnForgot;

    @FXML
    private Pane pnlLogin;

    @FXML
    private Pane pnlPassword;

    @FXML
    private Label lblError, lblHi;
    @FXML
    private Button btnNext;
    @FXML
    private Label lblName,lblFirst,lblLast,lblEmpId;

    @FXML
    private ImageView btnShow;
    @FXML
    private Label lblError1;


    private Connection con = null;
    private Statement st;
    private ResultSet rs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initClock();

    }
    public LoginController()
    {
        con = DbHelperPSQL.ConnectDB();
    }
    private int x = 1;
    @FXML
    void handleButtonAction(ActionEvent event) {
        if(event.getSource().equals(btnBack))
        {

        }
        //BUTTON FOR SIGN IN
        if(event.getSource().equals(btnNext))
        {
            new ZoomIn(pnlPassword).play();
            pnlPassword.toFront();
            pnlLogin.setVisible(false);
            pnlPassword.setVisible(true);
            txtUsername1.requestFocus();
        }
        // BUTTON IN SIGN IN
        if(event.getSource().equals(btnSignIn))
        {
            String user = txtUsername1.getText();
            String pass = txtPassword.getText();
            if(user.equals("") && pass.equals(""))
            {
                lblError1.setTextFill(Color.RED);
                lblError1.setText("Both fields are required!");
                lblError1.setVisible(true);
            }
            else if(user.equals(""))
            {
                lblError1.setTextFill(Color.RED);
                lblError1.setText("Enter Username*");
                lblError1.setVisible(true);
            }
            else if(pass.equals(""))
            {
                lblError1.setTextFill(Color.RED);
                lblError1.setText("Enter Password*");
                lblError1.setVisible(true);
            }
            else {

                try {

                    st = con.createStatement();
                    rs = st.executeQuery("SELECT * FROM tbl_employee WHERE (username = '" + user + "' OR employee_id = '" + user + "') AND password = '" + pass + "';");
                    if (!rs.next()) {
                        lblError1.setTextFill(Color.TOMATO);
                        lblError1.setText("Invalid Username or Password");
                        lblError1.setVisible(true);
                        txtUsername1.setText("");
                        txtPassword.setText("");
                    } else if (rs.getString("status").equals("Inactive")) {
                        lblError1.setTextFill(Color.RED);
                        lblError1.setText("The employee is not active");
                        lblError1.setVisible(true);
                        txtPassword.setText("");
                    } else if (!(rs.getString("typeofemp").equals("Administrator")) && rs.getString("status").equals("Active")) {
                        //to navigate to the next page
                        globalId = rs.getString("employee_id");
                        globalName = rs.getString("firstname");
                        Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenuEmp.fxml"));
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        //to set screen on Center
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    } else {
                        globalId = rs.getString("employee_id");
                        globalName = rs.getString("firstname");
                        Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenu.fxml"));
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        //to set screen on Center
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    }
                } catch (Exception e) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }
    //KEY EVENTS IN TXTUSERNAME
    @FXML
    void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if(txtUsername.getText().equals(""))
            {
                lblError.setTextFill(Color.TOMATO);
                lblError.setText("Enter Username first");
            }
        }
    }
    //KEY EVENTS IN TXTPASSWORD
    @FXML
    void KeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String user = txtUsername1.getText();
            String pass = txtPassword.getText();
            if(txtPassword.equals(""))
            {
                lblError1.setTextFill(Color.RED);
                lblError1.setText("Enter Password first");
                txtPassword.setText("");

            }else{
                try {
                    st = con.createStatement();
                    rs = st.executeQuery("SELECT * FROM tbl_employee WHERE (username = '" + user + "' OR employee_id = '" + user + "') AND password = '" + pass + "';");
                    if (!rs.next()) {
                        lblError1.setTextFill(Color.TOMATO);
                        lblError1.setText("Invalid Username or Password");
                        lblError1.setVisible(true);
                        txtUsername.setText("");
                        txtPassword.setText("");
                    }
                    else if (rs.getString("status").equals("Inactive")) {
                        lblError1.setTextFill(Color.RED);
                        lblError1.setText("The employee is not active");
                        txtPassword.setText("");
                        lblError1.setVisible(true);
                    }
                    else if (!(rs.getString("typeofemp").equals("Administrator")) && rs.getString("status").equals("Active")) {
                        //to navigate to the next page
                        globalId = rs.getString("employee_id");
                        globalName = rs.getString("firstname");
                        Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenuEmp.fxml"));
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        //to set screen on Center
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    }
                    else {
                        globalId = rs.getString("employee_id");
                        globalName = rs.getString("firstname");
                        Parent root = FXMLLoader.load(getClass().getResource("../View/MainMenu.fxml"));
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        //to set screen on Center
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    }
                } catch (Exception e) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    @FXML
    void handleKeyTyped(KeyEvent event) {
        if (event.getSource() == txtUsername) {
            try {
                String user = txtUsername.getText();
                st = con.createStatement();
                rs = st.executeQuery("SELECT  COUNT(amdeparture) as amdep, COUNT(amarrival) as amarr, COUNT(pmarrival) as pmarr from tbl_monthlytime where (employee_id= (select employee_id from tbl_employee WHERE username = '" + user + "') OR employee_id ='" + user+ "') AND logindate = CURRENT_DATE;");
                if(rs.next())
                {
                    LocalTime time = LocalTime.parse("12:00:00");
                        if(rs.getString("amarr").equals(String.valueOf(0))){
                            btnTimeIn.toFront();
                        }
                        if(rs.getString("amarr").equals(String.valueOf(1)) && java.time.LocalTime.now().isBefore(time)){
                            btnTimeOut.toFront();
                        }
                        if(java.time.LocalTime.now().isAfter(time) && rs.getString("amarr").equals(String.valueOf(1))) {
                            btnTimeIn.toFront();
                        }
                        if(java.time.LocalTime.now().isAfter(time)  && rs.getString("amarr").equals(String.valueOf(1)) && rs.getString("pmarr").equals(String.valueOf(1))) {
                            btnTimeOut.toFront();
                        }
                        if(java.time.LocalTime.now().isAfter(time) && rs.getString("pmarr").equals(String.valueOf(1))) {
                            btnTimeOut.toFront();
                        }
                        if(java.time.LocalTime.now().isAfter(time) && rs.getString("amarr").equals(String.valueOf(1))) {
                            btnTimeOut.toFront();
                        }

                }
            }
            catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        if(event.getSource() == txtUsername1)
        {

        }
    }
    @FXML
    void handleMouseAction(MouseDragEvent event) {
        //Scene scene = new Scene(200, 200);
        if (event.getSource() == btnSignIn) {

        }

    }

    @FXML
    void handleKeyReleased(KeyEvent event) throws SQLException {
        if (event.getSource() == txtUsername) {
        }

    }

    @FXML
    void handleMouseClicked(MouseEvent event) {
        if (event.getSource() == btnClose) {
            System.exit(0);
        }
        if (event.getSource() == btnBack) {
            BackToLogin();
        }
        if (event.getSource() == btnForgot) {

        }
        if (event.getSource() == btnTimeIn) {
            try {
                String user = txtUsername.getText();
                st = con.createStatement();
                rs = st.executeQuery("SELECT employee_id, username, firstname,lastname,typeofemp, password FROM tbl_employee WHERE username = '" + user + "' OR employee_id = '" + user + "';");
//                String user, id;
//                user = rs.getString("username");
//                id = rs.getString("employee_id");
                if(!rs.next()){
                    lblError.setTextFill(Color.RED);
                    lblError.setText("Invalid Username/Employee ID");
                    txtUsername.setText("");
                }
                else{
                    LocalDate currentDate = LocalDate.now();
                    Month m = currentDate.getMonth();
                    LocalTime time = LocalTime.parse("12:00:00");
                    if(java.time.LocalTime.now().isBefore(time))
                    {
                        Statement st1;
                        ResultSet rs1;
                        st1 = con.createStatement();
                        rs1 = st1.executeQuery("SELECT  COUNT(amdeparture) as amdep, COUNT(amarrival) as amarr, COUNT(pmarrival) as pmarr from tbl_monthlytime where (employee_id= (select employee_id from tbl_employee WHERE username = '" + user + "') OR employee_id ='" + user+ "') AND logindate = CURRENT_DATE;");
                        if(rs1.next()) {
                            if (rs1.getString("amarr").equals(String.valueOf(0))) {
                                st = con.createStatement();
                                st.executeUpdate("INSERT INTO tbl_monthlytime(employee_id, daymonth, dayofweek, amarrival,amdeparture,pmarrival,pmdeparture,logindate,logoutdate,created_on)VALUES(" +
                                        "'" + rs.getString("employee_id") + "','" + m + "','" + lblDay.getText() + "',CURRENT_TIME,NULL" +
                                        ",NULL, NULL, CURRENT_DATE, NULL, CURRENT_TIMESTAMP);");
                                BackToLogin();
                                lblError.setVisible(true);
                                lblError.setTextFill(Color.GREEN);
                                lblError.setText("Time In successfully complete!");
                                txtUsername.setText("");
                            } else {
                                st = con.createStatement();
                                st.executeUpdate("UPDATE tbl_monthlytime SET amarrival = CURRENT_TIME, logindate = CURRENT_DATE WHERE (employee_id= (select employee_id from tbl_employee WHERE username = '" + user + "') OR employee_id ='" + user + "') AND logindate = CURRENT_DATE;");
                                lblError.setVisible(true);
                                lblError.setTextFill(Color.GREEN);
                                lblError.setText("Time In successfully complete!");
                                txtUsername.setText("");
                            }
                        }
                    }
                    else if(java.time.LocalTime.now().isAfter(time))
                    {
                        Statement st1;
                        ResultSet rs1;
                        st1 = con.createStatement();
                        rs1 = st1.executeQuery("SELECT COUNT(amarrival) as amarr, COUNT(pmarrival) as pmarr from tbl_monthlytime where (employee_id= (select employee_id from tbl_employee WHERE username = '" + user + "') OR employee_id ='" + user+ "') AND logindate = CURRENT_DATE;");
                        if(rs1.next()) {
                            if (rs1.getString("pmarr").equals(String.valueOf(0)) && rs1.getString("amarr").equals(String.valueOf(0))) {
                                st = con.createStatement();
                                st.executeUpdate("INSERT INTO tbl_monthlytime(employee_id, daymonth, dayofweek, amarrival,amdeparture,pmarrival,pmdeparture,logindate,logoutdate,created_on)VALUES(" +
                                        "'" + rs.getString("employee_id") + "','" + m + "','" + lblDay.getText() + "',null,null" +
                                        ",CURRENT_TIME, NULL, CURRENT_DATE, NULL, CURRENT_TIMESTAMP);");
                                BackToLogin();
                                lblError.setVisible(true);
                                lblError.setTextFill(Color.GREEN);
                                lblError.setText("Time In successfully complete!");
                                txtUsername.setText("");
                            } else {
                                st = con.createStatement();
                                st.executeUpdate("UPDATE tbl_monthlytime SET pmarrival = CURRENT_TIME, logindate = CURRENT_DATE WHERE (employee_id= (select employee_id from tbl_employee WHERE username = '" + user + "') OR employee_id ='" + user + "') AND logindate = CURRENT_DATE;");
                                BackToLogin();
                                lblError.setVisible(true);
                                lblError.setTextFill(Color.GREEN);
                                lblError.setText("Time In successfully complete!");
                                txtUsername.setText("");
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);

            }
        }
        if (event.getSource() == btnTimeOut) {
            try {
                String user = txtUsername.getText();
                st = con.createStatement();
                rs = st.executeQuery("SELECT employee_id, username, password FROM tbl_employee WHERE (username = '" + user + "' OR employee_id = '" + user + "');");

                if(!rs.next()){
                    lblError.setTextFill(Color.RED);
                    lblError.setText("Invalid Username/Employee ID");
                    txtUsername.setText("");
                }
                else{
                    LocalTime time = LocalTime.parse("12:00:00");
                    if(java.time.LocalTime.now().isBefore(time)) {
                        st = con.createStatement();
                        st.executeUpdate("UPDATE tbl_monthlytime SET amdeparture = CURRENT_TIME, logoutdate = CURRENT_DATE WHERE (employee_id= (select employee_id from tbl_employee WHERE username = '" + user + "') OR employee_id ='" + user + "') AND logindate = CURRENT_DATE;");
                        BackToLogin();
                        lblError.setVisible(true);
                        lblError.setTextFill(Color.GREEN);
                        lblError.setText("Time Out successfully complete!");
                        txtUsername.setText("");
                    }
                    else if(java.time.LocalTime.now().isAfter(time))
                    {
                        st = con.createStatement();
                        st.executeUpdate("UPDATE tbl_monthlytime SET pmdeparture = CURRENT_TIME, logoutdate = CURRENT_DATE WHERE  (employee_id= (select employee_id from tbl_employee WHERE username = '" + user + "') OR employee_id ='" + user + "') AND logindate = CURRENT_DATE;");
                        BackToLogin();
                        lblError.setVisible(true);
                        lblError.setTextFill(Color.GREEN);
                        lblError.setText("Time Out successfully complete!");
                        txtUsername.setText("");
                    }

                }
            }
            catch (Exception e) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    private void BackToLogin(){
        new FlipInY(pnlLogin).play();
        pnlLogin.toFront();
        pnlPassword.setVisible(false);
        pnlLogin.setVisible(true);
        txtUsername.setText("");
        txtUsername1.setText("");
        lblError.setText("");
        lblError1.setVisible(false);
        txtPassword.setText("");
        lblHi.setVisible(false);
        lblName.setVisible(false);
        txtUsername.requestFocus();
    }
    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            lblTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        //to display date
        DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
        String dateString2 = dateFormat2.format(new Date()).toString();
        lblDate.setText(dateString2);

        //to display day
        LocalDate day = LocalDate.now(); // or myDatePicker.getValue()
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        System.out.println(day.format(format));
        lblDay.setText(day.format(format));
    }

}
