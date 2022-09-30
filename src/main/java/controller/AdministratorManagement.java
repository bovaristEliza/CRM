package controller;

import helpers.Helper;
import model.dao.*;
import model.dao.exception.DuplicatedObjectException;
import model.mo.*;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministratorManagement {


    public static void adminView(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String notAdmin = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUserId(loggedUser.getIduser());
            Integer admin = loggedUser.getIsAdmin();

            daoFactory.commitTransaction(); //commit sul databse
            sessionDAOFactory.commitTransaction(); //committo su cookie -> non ha vera importanza ma tratto cookie come db

            if(loggedUser.getIsAdmin()==1) {
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("profileUser", user);
                request.setAttribute("viewUrl", "AdminManagement/adminPage");

            }else {
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "AdminManagement/adminPage");
                notAdmin = "Please, log in with your Administrator profile <br />";
                request.setAttribute("applicationText", notAdmin);
            }


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void addAdminView(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String notAdmin = null;


        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            if(loggedUser.getIsAdmin()==1) {

                UserDAO userDAO = daoFactory.getUserDAO();

                ArrayList<User> userList = new ArrayList<User>();

                userList = userDAO.show();

                if(null==userList || userList.isEmpty()){
                    request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                    request.setAttribute("viewUrl", "CustomerManagement/message");
                }else {

                    request.setAttribute("userList", userList);
                    request.setAttribute("loggedOn", loggedUser != null);
                    request.setAttribute("loggedUser", loggedUser);
                    request.setAttribute("viewUrl", "AdminManagement/registerAdmin");
                }

            } else {

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "AdminManagement/adminPage");
                notAdmin = "Please, log in with your Administrator profile <br />";
                request.setAttribute("applicationText", notAdmin);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void addAdmin(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String notAdmin = null;

        Long iduser = Long.valueOf(request.getParameter("iduser"));


        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            if(loggedUser.getIsAdmin()==1) {



                UserDAO userDAO = daoFactory.getUserDAO();
                User user = userDAO.findByUserId(iduser);
                userDAO.makeAdmin(user);

                ArrayList<User> userList = new ArrayList<User>();

                userList = userDAO.show();

                if(null==userList || userList.isEmpty()){
                    request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                    request.setAttribute("viewUrl", "CustomerManagement/message");
                }else {

                    request.setAttribute("userList", userList);
                    request.setAttribute("loggedOn", loggedUser != null);
                    request.setAttribute("loggedUser", loggedUser);
                    request.setAttribute("viewUrl", "AdminManagement/registerAdmin");
                }

            } else {

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "AdminManagement/adminPage");
                notAdmin = "Please, log in with your Administrator profile <br />";
                request.setAttribute("applicationText", notAdmin);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void revokeAdmin(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String notAdmin = null;

        Long iduser = Long.valueOf(request.getParameter("iduser"));


        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            if(loggedUser.getIsAdmin()==1) {

                UserDAO userDAO = daoFactory.getUserDAO();
                User user = userDAO.findByUserId(iduser);
                userDAO.revokeAdmin(user);

                ArrayList<User> userList = new ArrayList<User>();

                userList = userDAO.show();

                if(null==userList || userList.isEmpty()){
                    request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                    request.setAttribute("viewUrl", "CustomerManagement/message");
                }else {

                    request.setAttribute("userList", userList);
                    request.setAttribute("loggedOn", loggedUser != null);
                    request.setAttribute("loggedUser", loggedUser);
                    request.setAttribute("viewUrl", "AdminManagement/registerAdmin");
                }

            } else {

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "AdminManagement/adminPage");
                notAdmin = "Please, log in with your Administrator profile <br />";
                request.setAttribute("applicationText", notAdmin);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void deleteUser(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String notAdmin = null;

        Long iduser = Long.valueOf(request.getParameter("iduser"));


        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            if(loggedUser.getIsAdmin()==1) {

                UserDAO userDAO = daoFactory.getUserDAO();
                User user = userDAO.findByUserId(iduser);
                userDAO.deleteUser(user);

                String actionType = "del";
                String actionTable = "user";
                Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, Math.toIntExact(user.getIduser()));

                ArrayList<User> userList = new ArrayList<User>();

                userList = userDAO.show();

                if(null==userList || userList.isEmpty()){
                    request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                    request.setAttribute("viewUrl", "CustomerManagement/message");
                }else {

                    request.setAttribute("userList", userList);
                    request.setAttribute("loggedOn", loggedUser != null);
                    request.setAttribute("loggedUser", loggedUser);
                    request.setAttribute("viewUrl", "AdminManagement/registerAdmin");
                }

            } else {

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "AdminManagement/adminPage");
                notAdmin = "Please, log in with your Administrator profile <br />";
                request.setAttribute("applicationText", notAdmin);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }



    public static void viewService(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String notAdmin = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUserId(loggedUser.getIduser());
            Integer admin = loggedUser.getIsAdmin();


            if(loggedUser.getIsAdmin()==1) {

                Helper.getServices(daoFactory, sessionDAOFactory, request);

                OpportunityDAO opportunityDAO = daoFactory.getOpportunityDAO();
                ArrayList<Opportunity> opportunities = opportunityDAO.showOpportunities();

                request.setAttribute("opportunities", opportunities);

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("profileUser", user);
                request.setAttribute("viewUrl", "AdminManagement/serviceCreate");

            }else {
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "AdminManagement/serviceCreate");
                notAdmin = "Please, log in with your Administrator profile <br />";
                request.setAttribute("applicationText", notAdmin);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

    public static void addService(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String notAdmin = null;

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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            if(loggedUser.getIsAdmin()==1) {

                String amount = request.getParameter("serviceCost");

                if (!amount.matches("[-+]?\\d*\\.?\\d+")) {

                    request.setAttribute("applicationText1", "Service cost must be a number!");
                    viewService(request, response);
                    return;
                }

                AppServicesDAO appServicesDAO = daoFactory.getAppServicesDAO();

                AppServices createdService = appServicesDAO.create(

                        request.getParameter("serviceName"),
                        BigDecimal.valueOf(Long.parseLong(request.getParameter("serviceCost"))),
                        request.getParameter("description")
                );

                String actionType = "add";
                String actionTable = "service";
                Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, createdService.getIdservice());

                request.setAttribute("applicationText1", "Service " + request.getParameter("serviceName")+ " added succesfully.");
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("service", createdService);
                request.setAttribute("serviceName", createdService.getServiceName());

            } else {
            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "AdminManagement/adminPage");
            notAdmin = "Please, log in with your Administrator profile <br />";
            request.setAttribute("applicationText", notAdmin);
        }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


        } catch (DuplicatedObjectException e) {
            String applicationText = "The service you added already exists.";
            logger.log(Level.INFO, "Tentativo di inserimento di servizio già esistente");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

        viewService(request, response);
    }

    public static void linkServiceToOpp(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationText = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String notAdmin = null;

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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();


                AppServicesDAO appServicesDAO = daoFactory.getAppServicesDAO();
                Integer idservice = Integer.valueOf(request.getParameter("services"));
                Integer idopportunity = Integer.valueOf(request.getParameter("opportunity"));

                try {
                    appServicesDAO.linkService(idservice, idopportunity);
                }catch (DuplicatedObjectException e) {
                    applicationText ="You already added this service to the same opportunity";
                    logger.log(Level.INFO, "Tentativo di inserimento di link servizio-opportunità già esistente");
                    request.setAttribute("applicationText", applicationText);

                }
            request.setAttribute("applicationText3", "Linking completed succesfully.");
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

        viewService(request, response);
    }

    public static void registerView(HttpServletRequest request, HttpServletResponse response) { //metodo statico

        Helper.validateSession(request, response, "homeManagement/registerView");

    }

    public static void registerUser(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String notAdmin = null;
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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            //validazione dati input
            String validationErrors="";
            if(!Helper.isValidEmailAddress(request.getParameter("email")))
            {
                validationErrors += "Invalid email format:" + request.getParameter("email") + "<br />";
            }
            if(validationErrors!=""){
                request.setAttribute("emailError", validationErrors);
                request.setAttribute("viewUrl", "homeManagement/registerView");
                return ;
            }

            try {

                UserDAO userDAO = daoFactory.getUserDAO();

                User registeredUser = userDAO.register(
                        request.getParameter("password"),
                        request.getParameter("email"),
                        request.getParameter("firstname"),
                        request.getParameter("surname"),
                        request.getParameter("gender"),
                        request.getParameter("tel"),
                        request.getParameter("employment"));

                request.setAttribute("registeredUserFirstname", registeredUser.getFirstname());
                request.setAttribute("registeredUserSurname", registeredUser.getSurname());

                String actionType = "add";
                String actionTable = "user";
                Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, Math.toIntExact(registeredUser.getIduser()));
            } catch (DuplicatedObjectException e) {
                applicationMessage = "This user already exists.";
                logger.log(Level.INFO, "Tentativo di inserimento di user già esistente");

            }

            daoFactory.commitTransaction();
            daoFactory.closeTransaction();

            //TODO: stampare a monitor l'utente appena creato
            request.setAttribute("appText", applicationMessage);
            request.setAttribute("viewUrl", "homeManagement/registerView");


        }  catch (Exception e) {

            try {
                if (daoFactory != null) {
                    daoFactory.rollbackTransaction();
                }
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }

    }

    public static void viewLogTab(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String notAdmin = null;

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUserId(loggedUser.getIduser());
            Integer admin = loggedUser.getIsAdmin();


            if(loggedUser.getIsAdmin()==1) {

                LoggerTabDAO loggerTabDAO = daoFactory.getLoggerTabDAO();

                ArrayList<LoggerTab> logList = loggerTabDAO.getLogs();

                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("logList", logList);
                request.setAttribute("profileUser", user);
                request.setAttribute("viewUrl", "AdminManagement/logTabView");

            }else {
                request.setAttribute("loggedOn",loggedUser!=null);
                request.setAttribute("loggedUser", loggedUser);
                request.setAttribute("viewUrl", "homeManagement/view");
                notAdmin = "Please, log in with your Administrator profile <br />";
                request.setAttribute("applicationText", notAdmin);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }
    }

}
