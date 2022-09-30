package model.dao;

import model.dao.mySQLJDBCImpl.MySQLJDBCDAOFactory;
import model.dao.CookieImpl.CookieDAOFactory;

import java.util.Map;

public abstract class DAOFactory {

    // List of DAO types supported by the factory
    public static final String MYSQLJDBCIMPL = "MySQLJDBCImpl";
    public static final String COOKIEIMPL= "CookieImpl"; // cookie come basi dati
    // gestione delle transazioni
    public abstract void beginTransaction();
    public abstract void commitTransaction();
    public abstract void rollbackTransaction();
    public abstract void closeTransaction();

    //*************************** DAO **********************************//

    public abstract UserDAO getUserDAO();
    public abstract CustomerDAO getCustomerDAO();
    public abstract AppServicesDAO getAppServicesDAO();
    public abstract NotesDAO getNotesDAO();
    public abstract ScheduleDAO getScheduleDAO();
    public abstract OpportunityDAO getOpportunityDAO();
    public abstract LoggerTabDAO getLoggerTabDAO();

    //******************************************************************//

    public static DAOFactory getDAOFactory(String whichFactory,Map factoryParameters) {

        if (whichFactory.equals(MYSQLJDBCIMPL)) {

            return new MySQLJDBCDAOFactory(factoryParameters);

        } else if (whichFactory.equals(COOKIEIMPL)) {

            return new CookieDAOFactory(factoryParameters);

        } else {

            return null;
        }
    }



}
