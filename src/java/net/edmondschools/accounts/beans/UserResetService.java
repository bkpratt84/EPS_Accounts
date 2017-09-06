package net.edmondschools.accounts.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

@Named(value = "userResetService")
@RequestScoped
public class UserResetService {
    static final Logger logger = Logger.getLogger("test");
    
    @Resource(name = "jdbc/ICDB")
    private DataSource ds;
    
    @Inject
    private UserSearchView view;
    
    public void resetPW(int uID, String pw) throws SQLException {
        String sql;
        boolean success = false;
        
        if (ds == null) {
            throw new SQLException("DS is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("Conn is null; Can't get db connection");
        }
        
        try {
            sql = "UPDATE UserAccount " +
                "SET password = ?, failCount = 0, forceChangePassword = 1, hashVersion = 0 " +
                "FROM UserAccount " +
                "WHERE userID = ?";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pw);
            ps.setInt(2, uID);
            ps.execute();
            
            success = true;
        } catch (Exception e) {
            Messages.setErrorMessage("Error resetting password: " + e.getMessage());
        } finally {
            conn.close();
        }
        
        if (success) {
            view.setPw(null);
            Messages.setSuccessMessage("Password reset!");
        }
    }
}