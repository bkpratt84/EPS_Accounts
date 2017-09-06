package net.edmondschools.accounts.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

@Named(value = "userService")
@RequestScoped
public class UserService {
    static final Logger logger = Logger.getLogger("test");

    @Inject
    private LoginBean login;

    @Inject
    ResultsMessageBean message;
        
    @Resource(name = "jdbc/ICDB")
    private DataSource ds;
    
    
    public ArrayList<User> getUsersByPerson(SearchResult result) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String sql;
        
        if (ds == null) {
            throw new SQLException("DS is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("Conn is null; Can't get db connection");
        }
        
        try {
            sql = "SELECT DISTINCT userID,personID,username,ExpireDate,forceChangePassword,disable,PWResetPref,SecEmail,studentNumber,firstName,middleName,lastName,homePhone,workPhone,cellPhone,email " +
                    "FROM VW_EPS_Project_User ";
            
            if (!login.isAllCals()) {
                sql += "WHERE Cal IN(" + login.getCalSQL() +")";
            } else {
                sql += "WHERE 1=1";
            }
            
            PreparedStatement ps;
            
            if (result.getFirst() != null && result.getLast() != null) {
                sql += " AND firstName LIKE ? AND lastName LIKE ?";
                sql += " ORDER BY lastName, firstName";
                
                ps = conn.prepareStatement(sql);
                ps.setString(1, result.getFirst() + "%");
                ps.setString(2, result.getLast() + "%");
            } else if (result.getFirst() != null) {
                sql += " AND firstName LIKE ?";
                sql += " ORDER BY lastName, firstName";
                
                ps = conn.prepareStatement(sql);
                ps.setString(1, result.getFirst() + "%");
            } else if (result.getLast() != null) {
                sql += " AND lastName LIKE ?";
                sql += " ORDER BY lastName, firstName";  
                
                ps = conn.prepareStatement(sql);
                ps.setString(1, result.getLast() + "%");
            } else {
                return null;
            }
            
            //System.out.println(sql);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                
                user.setUserID(rs.getInt("userID"));
                user.setPersonID(rs.getInt("personID"));
                user.setUsername(rs.getString("username"));
                user.setStdNumber(rs.getString("studentNumber"));
                user.setFirstName(rs.getString("firstName"));
                user.setMiddleName(rs.getString("middleName"));
                user.setLastName(rs.getString("lastName"));
                user.setHome(rs.getString("homePhone"));
                user.setWork(rs.getString("workPhone"));
                user.setCell(rs.getString("cellPhone"));
                user.setEmail(rs.getString("email"));
                user.setExpiresDate(rs.getString("ExpireDate"));
                user.setForcePasswordChange(rs.getBoolean("forceChangePassword"));
                user.setDisable(rs.getBoolean("disable"));
                user.setPwResetPref(rs.getBoolean("PWResetPref"));
                user.setSecEmail(rs.getString("SecEmail"));
                
                users.add(user);
            }  
        } catch (Exception e) {
            message.setMessage("Error querying database: " + e.getMessage());
            return null;
        } finally {
            conn.close();
        }
        
        return users;
    }
    
    public ArrayList<User> getUsersByUser(SearchResult result) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String sql;
        
        if (ds == null) {
            throw new SQLException("DS is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("Conn is null; Can't get db connection");
        }
        
        try {
            sql = "SELECT DISTINCT userID,personID,username,ExpireDate,forceChangePassword,disable,PWResetPref,SecEmail,studentNumber,firstName,middleName,lastName,homePhone,workPhone,cellPhone,email " +
                    "From VW_EPS_Project_User " +
                    "WHERE username LIKE ? ";
            
            if (!login.isAllCals()) {
                sql = sql + " AND Cal IN(" + login.getCalSQL() +")";
            }
            
            sql += "ORDER BY username";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setString(1, "%" + result.getLast() + "%");
            
            //System.out.println(sql);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                
                user.setUserID(rs.getInt("userID"));
                user.setPersonID(rs.getInt("personID"));
                user.setUsername(rs.getString("username"));
                user.setStdNumber(rs.getString("studentNumber"));
                user.setFirstName(rs.getString("firstName"));
                user.setMiddleName(rs.getString("middleName"));
                user.setLastName(rs.getString("lastName"));
                user.setHome(rs.getString("homePhone"));
                user.setWork(rs.getString("workPhone"));
                user.setCell(rs.getString("cellPhone"));
                user.setEmail(rs.getString("email"));
                user.setExpiresDate(rs.getString("ExpireDate"));
                user.setForcePasswordChange(rs.getBoolean("forceChangePassword"));
                user.setDisable(rs.getBoolean("disable"));
                user.setPwResetPref(rs.getBoolean("PWResetPref"));
                user.setSecEmail(rs.getString("SecEmail"));
                
                users.add(user);
            }  
        } catch (Exception e) {
            message.setMessage("Error querying database: " + e.getMessage());
            return null;
        } finally {
            conn.close();
        }
        
        return users;
    }
    
    public User getUser(int id) throws SQLException {
        User user = new User();
        String sql;
        
        if (ds == null) {
            throw new SQLException("DS is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("Conn is null; Can't get db connection");
        }
        
        try {
            sql = "SELECT DISTINCT userID,personID,username,ExpireDate,forceChangePassword,disable,PWResetPref,SecEmail,studentNumber,firstName,middleName,lastName,homePhone,workPhone,cellPhone,email " +
                    "From VW_EPS_Project_User " +
                    "WHERE personID = ?";
            
            if (!login.isAllCals()) {
                sql = sql + " AND Cal IN(" + login.getCalSQL() +")";
            }
            
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.isBeforeFirst()) {
                rs.next();
                
                user.setUserID(rs.getInt("userID"));
                user.setPersonID(rs.getInt("personID"));
                user.setUsername(rs.getString("username"));
                user.setStdNumber(rs.getString("studentNumber"));
                user.setFirstName(rs.getString("firstName"));
                user.setMiddleName(rs.getString("middleName"));
                user.setLastName(rs.getString("lastName"));
                user.setHome(rs.getString("homePhone"));
                user.setWork(rs.getString("workPhone"));
                user.setCell(rs.getString("cellPhone"));
                user.setEmail(rs.getString("email"));
                user.setExpiresDate(rs.getString("ExpireDate"));
                user.setForcePasswordChange(rs.getBoolean("forceChangePassword"));
                user.setDisable(rs.getBoolean("disable"));
                user.setPwResetPref(rs.getBoolean("PWResetPref"));
                user.setSecEmail(rs.getString("SecEmail"));
            }    
        } catch (Exception e) {
            message.setMessage("Error querying database: " + e.getMessage());
            return null;
        } finally {
            conn.close();
        }
//        if (user.getUserID() == 0) {
//            if (s == null || s.isEmpty()) {
//                message.setMessage("Search a user!");
//            } else {
//                message.setMessage("User does not exist or you don't have access to view this user.");
//            }
//            
//            return null;
//        }
        return user;
    }
}