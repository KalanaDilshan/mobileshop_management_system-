package lk.ijse.mobileshop.Util;
import lk.ijse.mobileshop.db.dbconnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cruddutil {
    public static <T>T execute(String sql, Object... args) throws SQLException {
        PreparedStatement pstm = dbconnection.getInstance().getConnection().prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            pstm.setObject((i+1), args[i]);
        }

        if(sql.startsWith("SELECT") || sql.startsWith("select")) {
            return (T) pstm.executeQuery(); // ResultSet
        }
        return (T) (Boolean)(pstm.executeUpdate() > 0); //Boolean
    }
}
