package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static Controller.LoginController.globalId;


public class ProfileController implements Initializable {


    private Connection con = null;
    private Statement st;
    private ResultSet rs;
    public ProfileController() { con = DbHelperPSQL.ConnectDB(); }


    @FXML

    private Label txtFirst, txtLast, txtId, txtUname, txtNum, txtMail, txtAdd;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //txtUsername =

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM tbl_employee WHERE employee_id = \'"+globalId+"';");

            if(rs.next()){
                txtFirst.setText(rs.getString("firstname"));
                txtLast.setText(rs.getString("lastname"));
                txtId.setText(rs.getString("employee_id"));
                txtUname.setText(rs.getString("username"));
                txtNum.setText(rs.getString("contact"));
                txtMail.setText(rs.getString("email"));
                txtAdd.setText(rs.getString("address"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }




    }






}
