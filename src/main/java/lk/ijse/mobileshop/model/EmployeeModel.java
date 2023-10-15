package lk.ijse.mobileshop.model;

import lk.ijse.mobileshop.Util.Cruddutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.Employee;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class EmployeeModel {
    private final static String URL = "jdbc:mysql://localhost:3306/mobileshop";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "12345");
    }

    public static boolean saveEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employee(Emp_Id,Emp_Name,Sallary) VALUES(?, ?, ?)";

        return Cruddutil.execute(sql, employee.getId(), employee.getName(), employee.getSallary());
    }

    public static   String generateNextId() throws SQLException {
        Connection con = dbconnection.getInstance().getConnection();

        String sql = "SELECT Emp_Id FROM Employee ORDER BY Emp_Id DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return splitEmp_Id(resultSet.getString(1));
        }
        return splitEmp_Id(null);
    }

    public static String splitEmp_Id(String splitEmp_Id) {
        if (splitEmp_Id != null) {
            String[] strings = splitEmp_Id.split("Em-0");
            int id = Integer.parseInt(strings[1]);
            id++;

            return "Em-00" + id;
        }
        return "Em-001";
    }

    public static ResultSet getAll(){
        ResultSet resultSet = null;
        try {
            resultSet=Cruddutil.execute("SELECT * FROM employee");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }
    public static Employee search(String Id) throws SQLException {
        ResultSet rs = Cruddutil.execute("SELECT * FROM employee WHERE Emp_Id = ?",Id);
        if(rs.next())return new Employee(rs.getString(1)
                ,rs.getString(2),rs.getDouble(3));
        return null;
    }
    public static boolean delete(String id) throws SQLException {
        return Cruddutil.execute("delete from employee where Emp_Id = ?",id);
    }
    public static boolean update(Employee correctData) throws SQLException {

        System.out.println(correctData);
        return Cruddutil.execute("update employee set Emp_Name = ? ,Sallary = ?  where Emp_Id = ?"
                ,correctData.getName(),correctData.getSallary(),correctData.getId());
    }

}
