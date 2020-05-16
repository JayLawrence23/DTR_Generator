package Controller;

import java.sql.*;

public class DbHelperPSQL {
    public static Connection con = null;

    public static Connection ConnectDB()
    {
            try {
                Class.forName("org.postgresql.Driver");
                //Database information
                String dbType = "jdbc:postgresql:";
                String dbUrl = "//localhost:";
                String dbPort = "5432/";
                String dbName = "db_inventory";
                String dbUser = "postgres";
                String dbPass = "12345678";
                //Establishing Connection
                con = DriverManager.getConnection(dbType + dbUrl + dbPort + dbName, dbUser, dbPass);
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_dtr", "postgres", "12345678");
                System.out.println("Connected");
                return con;

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
                return null;
            }


    }
    public void CloseSt(Statement st)
    {
        try {
            if (st != null){
                st.close();
            }
        }
        catch  (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void CloseRs(ResultSet rs)
    {
        try{
            if (rs != null)
            {
                rs.close();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void CloseCon(Connection con)
    {
        try
        {
            if (con != null)
            {
                con.close();
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}




//===== yung una
//==
//        primaryStage.setScene(scene);
//                primaryStage.initStyle(StageStyle.TRANSPARENT);
//
//                //grab your root here
//                root.setOnMousePressed((MouseEvent event) -> {
//                xOffset = event.getXOnScreen();
//                yOffset = event.getYOnScreen();
//                });
//                //move around here
//                root.setOnMousePressed((MouseEvent event) -> {
//                primaryStage.setX(event.getXOnScreen() - xOffset);
//                });
//                primaryStage.setScene(scene);
//                primaryStage.show();