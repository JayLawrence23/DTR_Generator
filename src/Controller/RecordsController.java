package Controller;

import Model.ModelTableAllRecords;
import Model.ModelTableDaily;
import Model.ModelTableEmployee;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.time.Year;
import java.util.*;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.print.PageFormat;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Controller.LoginController.globalId;
import static Controller.LoginController.globalName;

public class RecordsController implements Initializable {
    // Open Connection for database
    private Connection con = null;
    private Statement st;
    private ResultSet rs;
    public RecordsController()
    {
        con = DbHelperPSQL.ConnectDB();
    }


    @FXML
    private JFXButton btnGenerate;

    //======= FOR Monthly =====

    @FXML
    private TableView<ModelTableDaily> tbl_daily;

    @FXML
    private TableColumn<ModelTableDaily, String> col_EmpID,col_FName,col_LName,col_Day,col_Type;

    @FXML
    private TableColumn<ModelTableDaily, Time> col_amarr,col_amdep,col_pmarr,col_pmdep, col_Date;

    @FXML
    private TableColumn col_Action;
    ObservableList<ModelTableDaily> oblist = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> dtpkGenerate;

    @FXML
    private Spinner<Integer> spnYear;



    // ======== FOR ALL REC ============
    @FXML
    private TableView<ModelTableAllRecords> tbl_All;

    @FXML
    private TableColumn<ModelTableAllRecords, String> col_EmpID1,col_FName1,col_LName1,col_Day1,col_amarr1,col_amdep1,col_pmarr1,col_pmdep1,col_Type1,col_Date1;

    @FXML
    private JFXButton btnApply;

    @FXML
    private DatePicker dtpkFrom;

    @FXML
    private DatePicker dtpkTo;

    //======= Observer List ======
    ObservableList<ModelTableAllRecords> oblist1 = FXCollections.observableArrayList();
    Alert a = new Alert(Alert.AlertType.NONE);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String months[] =
                { "JANUARY", "FEBUARY", "MARCH",
                        "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" };
        ObservableList<String> data = FXCollections.observableArrayList(months);
        dtpkGenerate.setItems(data);

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);

        final int initialValue = year;
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, 2050, initialValue);
        spnYear.setValueFactory(valueFactory);
        tableAll();
        tableDaily();
    }

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource() == btnApply)
        {
            LocalDate ld1 = dtpkFrom.getValue();
            LocalDate ld2 = dtpkTo.getValue();
            String st1 = (dtpkFrom.getValue() != null ? dtpkFrom.getValue().toString() : "") ;
            String st2 = (dtpkTo.getValue() != null ? dtpkTo.getValue().toString() : "") ;
            if(ld1.equals("") || ld2.equals(""))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Date Error");
                alert.setHeaderText("All date must be fill");
                alert.setContentText("Please select date first");
                alert.showAndWait();
            }
            if(ld1.isAfter(ld2))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Date Error");
                alert.setHeaderText("End date must be in present or in future");
                alert.setContentText("Please enter date properly ");
                alert.showAndWait();
            }
            else {
                oblist1.removeAll(oblist1);
                try {
                    st = con.createStatement();
                    rs = st.executeQuery("select m.employee_id, e.firstname, e.lastname, m.dayofweek,TO_CHAR(m.amarrival, 'HH12:MI') AS amarrival,TO_CHAR(m.amdeparture, 'HH12:MI') AS amdeparture,TO_CHAR(m.pmarrival, 'HH12:MI') AS pmarrival,TO_CHAR(m.pmdeparture, 'HH12:MI') AS pmdeparture, e.typeofemp" +
                            ", TO_CHAR(logindate, 'mm/dd/yyyy')as logindate from tbl_monthlytime m, tbl_employee e WHERE (m.employee_id = e.employee_id AND m.logindate >='" + ld1 + "' AND m.logindate <= '" + ld2 + "') AND e.employee_id ='"+globalId+"';");
                    while (rs.next()) {
                        oblist1.add(new ModelTableAllRecords(rs.getString("employee_id"), rs.getString("firstname"), rs.getString("lastname"),
                                rs.getString("dayofweek"), rs.getString("typeofemp"), rs.getString("amarrival"), rs.getString("amdeparture"), rs.getString("pmarrival"), rs.getString("pmdeparture"), rs.getString("logindate")));
                    }
                } catch (Exception e) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
                }
                col_EmpID1.setCellValueFactory(new PropertyValueFactory<>("EmpId"));
                col_FName1.setCellValueFactory(new PropertyValueFactory<>("FName"));
                col_LName1.setCellValueFactory(new PropertyValueFactory<>("LName"));
                col_Day1.setCellValueFactory(new PropertyValueFactory<>("Day"));
                col_amarr1.setCellValueFactory(new PropertyValueFactory<>("AmArr"));
                col_amdep1.setCellValueFactory(new PropertyValueFactory<>("AmDep"));
                col_pmarr1.setCellValueFactory(new PropertyValueFactory<>("PmArr"));
                col_pmdep1.setCellValueFactory(new PropertyValueFactory<>("PmDep"));
                col_Type1.setCellValueFactory(new PropertyValueFactory<>("Type"));
                col_Date1.setCellValueFactory(new PropertyValueFactory<>("Date"));

                tbl_All.setItems(oblist1);
            }
        }
            if(event.getSource() == btnGenerate)
            {
                if(dtpkGenerate.getValue() == null)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Empty Month");
                    alert.setHeaderText("No month selected");
                    alert.show();
                }
                else {
                    try {
                        String month = dtpkGenerate.getValue();
                        int yearInt = spnYear.getValue();
                        String year = String.valueOf(yearInt);
                        String file_name = "C:\\Users\\Public\\" + globalName +" "+ month +" "+ year + ".pdf";
                        Document document = new Document();
                        Rectangle one = new Rectangle(90, 260);
                        document.setPageSize(one);
                        document.setMargins(2, 2, 2, 2);
                        PdfWriter.getInstance(document, new FileOutputStream(file_name));
                        document.open();
                        Font regular = new Font(Font.FontFamily.TIMES_ROMAN, 2);
                        Font regular2 = new Font(Font.FontFamily.TIMES_ROMAN, 1);
                        Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 2, Font.BOLD);
                        Font boldhead = new Font(Font.FontFamily.TIMES_ROMAN, 3, Font.BOLD);
                        Font boldname = new Font(Font.FontFamily.TIMES_ROMAN, 4, Font.BOLD);


                        st = con.createStatement();
                        rs = st.executeQuery("select e.firstname, e.lastname, m.daymonth, date_part('year', logindate) as years " +
                                "from tbl_monthlytime m, tbl_employee e WHERE m.employee_id = '" + globalId + "' AND m.employee_id = e.employee_id AND m.daymonth = '" + month + "' AND date_part('year', m.logindate) = '" + year + "'");

                        rs.next();
                        // simple paragraph

                        Paragraph p1 = new Paragraph("DAILY TIME RECORD", boldhead);
                        p1.setAlignment(Element.ALIGN_CENTER);
                        document.add(p1);

                        Paragraph p2 = new Paragraph("----------", bold);
                        p2.setAlignment(Element.ALIGN_CENTER);
                        document.add(p2);

                        Paragraph p4 = new Paragraph(rs.getString("firstname") + " " + rs.getString("lastname"), boldname);
                        p4.setAlignment(Element.ALIGN_CENTER);
                        document.add(p4);
                        Paragraph p5 = new Paragraph("----------------------------------------------------------------", bold);
                        p5.setAlignment(Element.ALIGN_CENTER);
                        document.add(p5);

                        Chunk month1 = new Chunk(rs.getString("daymonth"));
                        Chunk year1 = new Chunk(rs.getString("years"));
                        month1.setUnderline(0.2f, -2f); //0.1 thick, -2 y-location
                        Paragraph p7 = new Paragraph("For the month of ", regular);
                        p7.add(new Chunk(month1 + ", " + year1, bold));
                        p7.setAlignment(Element.ALIGN_CENTER);
                        document.add(p7);
                        Statement st3;
                        ResultSet rs3;
                        st3 = con.createStatement();
                        rs3 = st3.executeQuery("select COUNT(*) AS count from tbl_monthlytime WHERE date_part('dow', logindate) != '06' AND employee_id = '" + globalId + "'AND daymonth = '" + month + "' AND date_part('year',logindate) = '" + year + "'");
                        rs3.next();
                        Chunk countreg = new Chunk(rs3.getString("count"));
                        countreg.setUnderline(0.1f, -2f); //0.1 thick, -2 y-location
                        Paragraph p8 = new Paragraph("Official hours of arrival          Regular days: ", regular);
                        p8.add(new Chunk(String.valueOf(countreg), bold));
                        p8.setAlignment(Element.ALIGN_CENTER);
                        document.add(p8);

                        Statement st2;
                        ResultSet rs2;
                        st2 = con.createStatement();
                        rs2 = st2.executeQuery("select COUNT(*) AS count from tbl_monthlytime WHERE date_part('dow', logindate) = '06' AND employee_id = '" + globalId + "'AND daymonth = '" + month + "' AND date_part('year',logindate) = '" + year + "'");
                        rs2.next();
                        Chunk countsat = new Chunk(rs2.getString("count"));
                        countsat.setUnderline(0.1f, -2f); //0.1 thick, -2 y-location
                        Paragraph p9 = new Paragraph("              and departure          Saturdays: ", regular);
                        p9.add(new Chunk(String.valueOf(countsat), bold));
                        p9.setAlignment(Element.ALIGN_CENTER);
                        document.add(p9);

                        Paragraph p10 = new Paragraph(" ", regular);
                        document.add(p10);

                        PdfPTable table = new PdfPTable(7);
                        table.setWidths(new int[]{7, 8, 10, 8, 10, 7, 9});
                        PdfPCell cell;
                        cell = new PdfPCell(new Phrase("Days", regular) );
                        cell.setRowspan(2);
                        table.addCell(cell);
                        cell = new PdfPCell(new Phrase("A.M.", regular));
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell.setRowspan(2);
                        cell = new PdfPCell(new Phrase("P.M.", regular));
                        cell.setColspan(2);
                        table.addCell(cell);
                        cell.setRowspan(2);
                        cell = new PdfPCell(new Phrase("UNDER TIME", regular));
                        cell.setColspan(2);
                        table.addCell(cell);

                        table.addCell(new Phrase("ARRIVAL", regular2));
                        table.addCell(new Phrase("DEPARTURE", regular2));
                        table.addCell(new Phrase("ARRIVAL", regular2));
                        table.addCell(new Phrase("DEPARTURE", regular2));
                        table.addCell(new Phrase("HOURS", regular2));
                        table.addCell(new Phrase("MINUTES", regular2));

                        Statement st1;
                        ResultSet rs1;
                        String days = "";
                        int x = 0, totHr = 0, totMin = 0;
                        st1 = con.createStatement();
                        rs1 = st1.executeQuery("select e.firstname, e.lastname, m.daymonth,EXTRACT(DAY FROM logindate) AS day, TO_CHAR(m.amarrival,'HH12:MI') AS amarr,TO_CHAR(m.amdeparture,'HH12:MI') AS amdep,TO_CHAR(m.pmarrival,'HH12:MI') AS pmarr,TO_CHAR(m.pmdeparture,'HH12:MI') AS pmdep, " +
                                "COALESCE(TRUNC(extract(epoch from (m.amdeparture - m.amarrival))/3600),0) as amhours, COALESCE(TRUNC(extract(epoch from (m.pmdeparture - m.pmarrival))/3600),0) as pmhours, COALESCE(TRUNC(extract(epoch from (m.amdeparture - m.amarrival))/3600)*60,0) as amminutes," +
                                "COALESCE(TRUNC(extract(epoch from (m.pmdeparture - m.pmarrival))/3600)*60 ,0) as pmminutes " +
                                "from tbl_monthlytime m, tbl_employee e WHERE (m.employee_id = '" + globalId + "' AND m.employee_id = e.employee_id) AND daymonth = '" + month + "'");

                        String rec[][] = new String[31][31];
                        int recHr[][] = new int[31][31];
                        while (rs1.next()) {
                            rec[x][0] = "" + rs1.getString("day");
                            rec[x][1] = rs1.getString("amarr");
                            rec[x][2] = rs1.getString("amdep");
                            rec[x][3] = rs1.getString("pmarr");
                            rec[x][4] = rs1.getString("pmdep");
                            recHr[x][0] = Integer.parseInt(rs1.getString("amhours"));
                            recHr[x][1] = Integer.parseInt(rs1.getString("pmhours"));
                            recHr[x][2] = Integer.parseInt(rs1.getString("amminutes"));
                            recHr[x][3] = Integer.parseInt(rs1.getString("pmminutes"));
                            recHr[x][4] = recHr[x][0] + recHr[x][1];
                            recHr[x][5] = recHr[x][2] + recHr[x][3];
                            rec[x][5] = ""+recHr[x][4];
                            rec[x][6] = ""+recHr[x][5];
                            totHr += recHr[x][4];
                            totMin += recHr[x][5];
                            x++;
                        }
                        int index = 1, y = 0, num = 0;
                        boolean desicion = true;
                        for (int limit = 1; limit <= 31; limit++) {
                            days = "" + limit;
                            desicion = true;
                            while (rec[y][0] != null) {
                                boolean bool = rec[y][0].equalsIgnoreCase(days);
                                if (bool) {
                                    PdfPTable table2 = new PdfPTable(7);
                                    table.setWidths(new int[]{7, 8, 10, 8, 10, 7, 9});
                                    PdfPCell cell2;
                                    cell2 = new PdfPCell(new Phrase(days, bold));
                                    cell2.setRowspan(1);
                                    table.addCell(cell2);
                                    table.addCell(new Phrase(rec[y][1], bold));
                                    table.addCell(new Phrase(rec[y][2], bold));
                                    table.addCell(new Phrase(rec[y][3], bold));
                                    table.addCell(new Phrase(rec[y][4], bold));
                                    table.addCell(new Phrase(rec[y][5], bold));
                                    table.addCell(new Phrase(rec[y][6], bold));
                                    desicion = false;
                                }
                                y++;
                            }
                            if (desicion) {
                                PdfPTable table2 = new PdfPTable(7);
                                table.setWidths(new int[]{7, 8, 10, 8, 10, 7, 9});
                                PdfPCell cell2;
                                cell2 = new PdfPCell(new Phrase(days, bold));
                                cell2.setRowspan(1);
                                table.addCell(cell2);
                                table.addCell(new Phrase("", bold));
                                table.addCell(new Phrase("", bold));
                                table.addCell(new Phrase("", bold));
                                table.addCell(new Phrase("", bold));
                                table.addCell(new Phrase("", bold));
                                table.addCell(new Phrase("", bold));
                            }
                            y = 0;
                        }
                        document.add(table);

                        Paragraph p11 = new Paragraph("Total ___________________________________________" + totHr + "________" + totMin, bold);
                        p11.setAlignment(Element.ALIGN_CENTER);
                        document.add(p11);

                        Paragraph p12 = new Paragraph(" ", regular);
                        document.add(p12);

                        Paragraph p13 = new Paragraph("I CERTIFY on my honor that the above is a true and correct\n" +
                                "report of the hours of work performed, record of which was made\n" +
                                "daily at the time of arrival and departure from office.", regular);
                        p13.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
                        document.add(p13);

                        Paragraph p18 = new Paragraph(" ", regular);
                        document.add(p18);
//
                        Paragraph p19 = new Paragraph(" ", regular);
                        document.add(p19);

                        Paragraph p20 = new Paragraph("______________________________________________________\n" +
                                "                               In-Charge", regular);
                        p20.setAlignment(Element.ALIGN_RIGHT);
                        document.add(p20);
                        document.close();

                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Your DTR Generated successfully");
                    alert.setContentText("Done");
                    alert.show();
                }
            }
    }
    public void Cell(String amarr,String amdep, String pmarr, String pmdep, int hours, double min)
    {
    }
    private void tableDaily()
    {
        try{
            LocalDate currentDate = LocalDate.now();
            Month m = currentDate.getMonth();
            st = con.createStatement();
            rs = st.executeQuery("select m.employee_id, e.firstname, e.lastname, m.dayofweek,TO_CHAR(m.amarrival, 'HH12:MI') AS amarrival,TO_CHAR(m.amdeparture, 'HH12:MI') AS amdeparture,TO_CHAR(m.pmarrival, 'HH12:MI') AS pmarrival,TO_CHAR(m.pmdeparture, 'HH12:MI') AS pmdeparture, e.typeofemp" +
                    ", TO_CHAR(logindate, 'mm/dd/yyyy')as logindate from tbl_monthlytime m, tbl_employee e WHERE (daymonth = '"+m+"' AND m.employee_id = e.employee_id) AND e.employee_id ='"+globalId+"';");
            while (rs.next())
            {
                oblist.add(new ModelTableDaily(rs.getString("employee_id"),rs.getString("firstname"),rs.getString("lastname"),
                        rs.getString("dayofweek"),rs.getString("typeofemp"),rs.getString("amarrival"),rs.getString("amdeparture"),rs.getString("pmarrival"),rs.getString("pmdeparture"), rs.getString("logindate")));
            }
        }catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
        col_EmpID.setCellValueFactory(new PropertyValueFactory<>("EmpId"));
        col_FName.setCellValueFactory(new PropertyValueFactory<>("FName"));
        col_LName.setCellValueFactory(new PropertyValueFactory<>("LName"));
        col_Day.setCellValueFactory(new PropertyValueFactory<>("Day"));
        col_amarr.setCellValueFactory(new PropertyValueFactory<>("AmArr"));
        col_amdep.setCellValueFactory(new PropertyValueFactory<>("AmDep"));
        col_pmarr.setCellValueFactory(new PropertyValueFactory<>("PmArr"));
        col_pmdep.setCellValueFactory(new PropertyValueFactory<>("PmDep"));
        col_Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        col_Date.setCellValueFactory(new PropertyValueFactory<>("Date"));

        tbl_daily.setItems(oblist);
    }
    private void tableAll()
    {
        try{
            st = con.createStatement();
            rs = st.executeQuery("select m.employee_id, e.firstname, e.lastname, m.dayofweek,TO_CHAR(m.amarrival, 'HH12:MI') AS amarrival,TO_CHAR(m.amdeparture, 'HH12:MI') AS amdeparture,TO_CHAR(m.pmarrival, 'HH12:MI') AS pmarrival,TO_CHAR(m.pmdeparture, 'HH12:MI') AS pmdeparture, e.typeofemp" +
                    ", TO_CHAR(logindate, 'mm/dd/yyyy')as logindate from tbl_monthlytime m, tbl_employee e WHERE m.employee_id = e.employee_id AND e.employee_id='"+globalId+"';");
            while (rs.next())
            {
                oblist1.add(new ModelTableAllRecords(rs.getString("employee_id"),rs.getString("firstname"),rs.getString("lastname"),
                        rs.getString("dayofweek"),rs.getString("typeofemp"),rs.getString("amarrival"),rs.getString("amdeparture"),rs.getString("pmarrival"),rs.getString("pmdeparture"), rs.getString("logindate")));
            }
        }catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
        col_EmpID1.setCellValueFactory(new PropertyValueFactory<>("EmpId"));
        col_FName1.setCellValueFactory(new PropertyValueFactory<>("FName"));
        col_LName1.setCellValueFactory(new PropertyValueFactory<>("LName"));
        col_Day1.setCellValueFactory(new PropertyValueFactory<>("Day"));
        col_amarr1.setCellValueFactory(new PropertyValueFactory<>("AmArr"));
        col_amdep1.setCellValueFactory(new PropertyValueFactory<>("AmDep"));
        col_pmarr1.setCellValueFactory(new PropertyValueFactory<>("PmArr"));
        col_pmdep1.setCellValueFactory(new PropertyValueFactory<>("PmDep"));
        col_Type1.setCellValueFactory(new PropertyValueFactory<>("Type"));
        col_Date1.setCellValueFactory(new PropertyValueFactory<>("Date"));

        tbl_All.setItems(oblist1);

    }

}
