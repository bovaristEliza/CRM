package model.dao.CookieImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


import model.dao.*;
import model.dao.CustomerDAO;
import model.dao.exception.DuplicatedObjectException;
import model.dao.mySQLJDBCImpl.CustomerJDBC;
import model.mo.Customer;

public class CookieDAOFactory extends DAOFactory{

    private Map factoryParameters;



    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    public CookieDAOFactory(Map factoryParameters) {

        this.factoryParameters=factoryParameters;
        this.request=(HttpServletRequest) factoryParameters.get("request");
        this.response=(HttpServletResponse) factoryParameters.get("response");;
    }

    @Override
    public void beginTransaction() {

        try {
            //this.request=(HttpServletRequest) factoryParameters.get("request");
           // this.response=(HttpServletResponse) factoryParameters.get("response");;
            this.session= request.getSession(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void commitTransaction() {}

    @Override
    public void rollbackTransaction() {}

    @Override
    public void closeTransaction() {}

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOCookieImpl(request,response);
    } //passa request e response

    @Override
    public CustomerDAO getCustomerDAO() {
        return null;
    }

    @Override
    public AppServicesDAO getAppServicesDAO() {
        return null;
    }

    @Override
    public NotesDAO getNotesDAO() {
        return null;
    }

    @Override
    public ScheduleDAO getScheduleDAO() {
        return null;
    }

    @Override
    public OpportunityDAO getOpportunityDAO() {
        return null;
    }

    @Override
    public LoggerTabDAO getLoggerTabDAO() {
        return null;
    }

}
