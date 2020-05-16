package Model;

public class ModelTableAllRecords {
    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAmArr() {
        return AmArr;
    }

    public void setAmArr(String amArr) {
        AmArr = amArr;
    }

    public String getAmDep() {
        return AmDep;
    }

    public void setAmDep(String amDep) {
        AmDep = amDep;
    }

    public String getPmArr() {
        return PmArr;
    }

    public void setPmArr(String pmArr) {
        PmArr = pmArr;
    }

    public String getPmDep() {
        return PmDep;
    }

    public void setPmDep(String pmDep) {
        PmDep = pmDep;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    String EmpId;
    String FName;
    String LName;
    String Day;
    String Type;
    String AmArr;
    String AmDep;
    String PmArr;
    String PmDep;
    String Date;

    public ModelTableAllRecords(String empId, String FName, String LName, String day, String type, String amArr, String amDep, String pmArr, String pmDep, String date) {
        this.EmpId = empId;
        this.FName = FName;
        this.LName = LName;
        this.Day = day;
        this.Type = type;
        this.AmArr = amArr;
        this.AmDep = amDep;
        this.PmArr = pmArr;
        this.PmDep = pmDep;
        this.Date = date;
    }
}
