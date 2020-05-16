package Model;

public class ModelTableType {
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
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

    String Type;
    String Status;
    String Date;

    public ModelTableType(String type, String status, String date) {
        this.Type = type;
        this.Status = status;
        this.Date = date;
    }
}
