package net.edmondschools.accounts.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;

@Named(value = "userSearchView")
@ViewScoped
public class UserSearchView implements Serializable {
    static final Logger logger = Logger.getLogger("test");
    
    private static final long serialVersionUID = -4642034411614855427L;
    
    private User user, selUser;
    private ArrayList<User> searchUsers;
    private ArrayList<UserHistory> hist;
    private String search;
    private String type;
    private SearchResult result;
    
    //@Size(min = 8, max = 20, message = "Enter a new password (8-20 characters)")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})", message = "At least 1 upper, 1 lower, 1 number, and 8-20 characters")
    private String pw;
    
    @Inject
    UserService service;
    
    @Inject
    UserHistoryService service2;
    
    @Inject
    UserResetService service3;
    
    @PostConstruct
    public void init(){
        searchUsers(true);
    }

    public User getSelUser() {
        return selUser;
    }

    public void setSelUser(User selUser) {
        this.selUser = selUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getSearch() {
        return search;
    }
    
    public void setSearch(String search) {    
        this.search = search;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<User> getSearchUsers() {
        return searchUsers;
    }
    
    public int getResultCount() {
        if (searchUsers == null) {
            return 0;
        }
        
        return searchUsers.size();
    }
    
    public ArrayList<UserHistory> getHist() {
        return hist;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
    
    public void resetPW() {
        if (user != null && pw != null && !"".equals(pw)) {
            try {
                service3.resetPW(user.getUserID(), pw);
                searchUsers(false);
            } catch (SQLException e) {
                Messages.setErrorMessage("Error resetting password: " + e.getMessage());
            }
        } 
    }
    
    private void searchUsers(boolean clear) {
        if (clear)
            user = null;
        
        try {
            result = new SearchResult();
            result.process(search);
            
            if (type != null && type.equals("user") & !result.isEmpty()) {
                searchUsers = service.getUsersByUser(result);
            } else if (type != null && type.equals("person") & !result.isEmpty()) {
                searchUsers = service.getUsersByPerson(result);
            } else {
                searchUsers = null;
            }
        } catch (SQLException e) {
            searchUsers = null;
        } 
    }
    
    public void searchUser() {
        try {
            user = service.getUser(selUser.getPersonID());
            
            if (user != null) {
                hist = service2.getUserHistory(user.getUserID());
            }
        } catch (SQLException e) {
            user = null;
        } 
    }
}