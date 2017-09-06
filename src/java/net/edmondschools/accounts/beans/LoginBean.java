package net.edmondschools.accounts.beans;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.sql.DataSource;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    static final Logger logger = Logger.getLogger("test");
    
    @Resource(name = "jdbc/ICDB")
    private DataSource ds;
    
    private static final long serialVersionUID = -1062378578757524801L;
    
    private String username;
    private String pw;
    private boolean loggedIn;
    private boolean access;
    private boolean allCals;
    private ArrayList<Calendar> cals;
    private String uID;
    
    @Inject
    private CalendarService service;
    
    @Inject
    private NavigationBean navigationBean;
    
    public LoginBean() {
    }

    public String getUsername() {
        return username;
    }

    public boolean isAllCals() {
        return allCals;
    }

    public void setAllCals(boolean allCals) {
        this.allCals = allCals;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    public String getCalSQL() {
        if (cals == null) {
            return "0";
        } else {
            String sql = "0";
            
            for (Calendar cal : cals) {
                if (sql.equals("0")) {
                    sql = String.valueOf(cal.getCalendarID());
                } else {
                    sql = sql.concat("," + String.valueOf(cal.getCalendarID()));
                }
            }
            
            return sql;
        }
    }
    
    public ArrayList<Integer> getCalSQLArray() {
        ArrayList<Integer> arr = new ArrayList<>();

        if (cals == null) {
            return null;
        } else {
            for (Calendar cal : cals) {
                arr.add(cal.getCalendarID());
            }
            
            return arr;
        }
    }
    
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        loggedIn = false;
        access = false;
        
        return "/login.xhtml?faces-redirect=true";
    }
    
    public String submit() throws IOException {
        String msg = "";
        
        try {
            Hashtable<String, String> props = new Hashtable<>();
            
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            props.put(Context.SECURITY_PRINCIPAL, username + "@" + "eps.local");
            props.put(Context.SECURITY_CREDENTIALS, pw);
            props.put(Context.PROVIDER_URL, "ldap://epsdc1.eps.local:389");
            
            DirContext ctx = new InitialDirContext(props);
            ctx.close();
            
            checkAccess();
        } catch (NamingException | SQLException e) {
            loggedIn = false;
            
            msg = e.getMessage();
        }

        if (access) {
            loggedIn = true;
            return navigationBean.redirectToMain();
        } else {
            if (msg.contains("epsdc")) {
                msg = "Unable to connect to LDAP";
            } else {
                msg = "Invalid login or not authorized";
            }
            
            Messages.setErrorMessage(msg);
            return null;
        }
    }
    
    private void checkAccess() throws SQLException {
        String sql;
        
        access = false;
        
        if (ds == null) {
            throw new SQLException("ds is null; Can't get data source");
        }

        Connection conn = ds.getConnection();

        if (conn == null) {
            throw new SQLException("conn is null; Can't get db connection");
        }
        
        try {
            sql = "SELECT " +
                    "ua.userID, ua.allCalendars " +
                    "FROM " +
                    "UserAccount ua " +
                    "INNER JOIN UserGroupMember ugm ON ua.userID = ugm.userID " +
                    "AND ugm.groupID IN(118, 209) " +
                    "WHERE " +
                    "ua.username = ? " +
                    "AND ua.disable = 0 " +
                    "AND (ua.expiresDate IS NULL OR ua.expiresDate > GETDATE())";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            access = rs.isBeforeFirst();
            
            if (access) {
                rs.next();
                
                uID = rs.getString("userID");
                allCals = rs.getBoolean("allCalendars");
                cals = service.getCalendars();
            }
        } catch (Exception e) {
            access = false;
        } finally {
            conn.close();
        }
    }
}