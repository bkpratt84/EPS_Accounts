package net.edmondschools.accounts.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "navigationBean")
@SessionScoped
public class NavigationBean implements Serializable {
    private static final long serialVersionUID = 5812865292253653206L;

    public String toLogin() {
        return "/login.xhtml";
    }
    
    public String redirectToMain() {
        return "/secure/main.xhtml?faces-redirect=true";
    }
    
    public String toMain() {
        return "/secure/main.xhtml";
    }
    
    public String toTest() {
        return "/test.xhtml?faces-redirect=true";
    }
}