package lk.ijse.mobileshop.dto;

public class Employee {
    String id;
    String name;
    double sallary;

    public Employee(String id, String name, double sallary) {
        this.id = id;
        this.name = name;
        this.sallary = sallary;
    }

    public Employee() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSallary() {
        return sallary;
    }

    public void setSallary(double sallary) {
        this.sallary = sallary;
    }
}
