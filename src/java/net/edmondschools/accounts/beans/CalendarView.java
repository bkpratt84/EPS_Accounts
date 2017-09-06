package net.edmondschools.accounts.beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "calendarView")
@ViewScoped
public class CalendarView implements Serializable {
    private static final long serialVersionUID = 5820665800880362469L;
    
    private ArrayList<Calendar> cals;
    
    @Inject
    private CalendarService service;
    
    @PostConstruct
    public void init() {
        try {
            cals = service.getCalendars();
        } catch (SQLException e) {
            //todo
        }
    }
    
    public ArrayList<Calendar> getCalendars() {
        return cals;
    }
    
    public void setService(CalendarService service) {
        this.service = service;
    }
}