package net.edmondschools.accounts.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named(value = "resultsMessageBean")
@SessionScoped
public class ResultsMessageBean implements Serializable {
    private static final long serialVersionUID = 3389373141619391803L;
    
    private String message;
    private String message2;

    @PostConstruct
    public void init(){
        if (message == null) {
            message = "Search a user!";
        }
        if (message2 == null) {
            message2 = "No detail";
        }
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }
}