package net.edmondschools.accounts.beans;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

@Named(value = "calendarService")
@ApplicationScoped
public class CalendarService {
    
    @Inject
    private LoginBean login;

    @Resource(name = "jdbc/ICDB")
    private DataSource ds;
    
    public ArrayList<Calendar> getCalendars() throws SQLException {
        ArrayList<Calendar> cals = new ArrayList<>();
        String sql;
        
        if (ds == null) {
            throw new SQLException("DS is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("Conn is null; Can't get db connection");
        }
        
        try {
            sql = "{call get_userCalendarRights(?)}";
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setString(1, login.getuID());
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Calendar cal = new Calendar();
                
                cal.setCalendarID(rs.getInt("calendarID"));
                cal.setName(rs.getString("name"));
                cal.setSchoolID(rs.getInt("schoolID"));
                
                cals.add(cal);
            }
                
        } catch (Exception e) {
            // to do
        } finally {
            conn.close();
        }
        
        return cals;
    }
}