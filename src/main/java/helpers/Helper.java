package helpers;

import model.dao.*;
import model.mo.AppServices;
import model.mo.Schedule;
import model.mo.User;
import services.config.Configuration;
import services.logservice.LogService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Helper {

    public static void validateSession(HttpServletRequest request, HttpServletResponse response, String managementAction) {
        DAOFactory sessionDAOFactory= null;
        User loggedUser;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", managementAction); // *****************************************************


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static void getServices(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request) {

        AppServicesDAO appServicesDAO = daoFactory.getAppServicesDAO();

        ArrayList<AppServices> appServicesList = appServicesDAO.getAppServices();

        if(null==appServicesList || appServicesList.isEmpty()){

            request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
            request.setAttribute("viewUrl", "CustomerManagement/message");
            return;

        }else {

            request.setAttribute("appServicesList", appServicesList);
        }


    }

    public static void logAction(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request, Integer idUser, String actionType, String actionTable, Integer actionRecordId ) {

        java.util.Date date = new Date();
        Object currentDate = new java.sql.Timestamp(date.getTime());

        LoggerTabDAO LoggerTabDAO = daoFactory.getLoggerTabDAO();
        LoggerTabDAO.insert(idUser, (Timestamp) currentDate, actionType, actionTable, actionRecordId);




    }

}




