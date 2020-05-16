package Model;

public class ModelTableDepartment {
    String Dept;
    String Status;
    String Date;

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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public ModelTableDepartment(String dept, String status, String date) {
        this.Dept = dept;
        this.Status = status;
        this.Date = date;
    }
}
