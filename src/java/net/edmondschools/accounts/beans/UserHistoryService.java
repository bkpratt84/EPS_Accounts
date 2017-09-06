package net.edmondschools.accounts.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

@Named(value = "userHistoryService")
@RequestScoped
public class UserHistoryService {
    @Inject
    ResultsMessageBean message;
        
    @Resource(name = "jdbc/ICDB")
    private DataSource ds;
    
    public ArrayList<UserHistory> getUserHistory(int uID) throws SQLException {
        ArrayList<UserHistory> hist = new ArrayList<>();
        String sql;
        
        if (ds == null) {
            throw new SQLException("DS is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("Conn is null; Can't get db connection");
        }
        
        try {
            sql = "SELECT * FROM VW_EPS_Project_UserHistory WHERE userID = ? ORDER BY SortTime DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, uID);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                UserHistory item = new UserHistory();
                
                item.setFailCount(rs.getInt("failCount"));
                item.setSuccess(rs.getString("Success"));
                item.setBrowserType(rs.getString("BrowserType"));
                item.setAccessTime(rs.getString("AccessTime"));
                item.setThirdParty(rs.getString("ThirdPartyAccess"));

                hist.add(item);
            }
                
        } catch (Exception e) {
            System.out.println(e.toString());
            message.setMessage2("Error querying database: " + e.getMessage());
            return null;
        } finally {
            conn.close();
        }
        
        if (hist.isEmpty()) {
            message.setMessage2("No login history.");
        }
        
        return hist;
    }
}