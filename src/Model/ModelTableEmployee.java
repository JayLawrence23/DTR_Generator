package Model;

public class ModelTableEmployee {

    // Use ALT + INS and click Constructor and getter and setter
    String EmpId;
    String FName;
    String LName;
    String Type;
    String Dept;
    String Status;
    String LastOut;

    public ModelTableEmployee(String empId, String FName, String LName, String type, String dept, String status, String lastOut) {
        this.EmpId = empId;
        this.FName = FName;
        this.LName = LName;
        this.Type = type;
        this.Dept = dept;
        this.Status = status;
        this.LastOut = lastOut;
    }

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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDept() {
        return Dept;
    }

    public void setDept(String dept) {
        Dept = dept;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLastOut() {
        return LastOut;
    }

    public void setLastOut(String lastOut) {
        LastOut = lastOut;
    }
}
