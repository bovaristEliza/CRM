package controller;

import helpers.Helper;
import model.dao.CustomerDAO;
import model.dao.DAOFactory;
import model.dao.OpportunityDAO;
import model.dao.UserDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Customer;
import model.mo.Opportunity;
import model.mo.User;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpportunityManagement {

    public static void displayOpp(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();


        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);

            String idCustomer = request.getParameter("customerid");
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();

            Customer viewableCustomer = customerDAO.findByCustomerId(Integer.valueOf(idCustomer));

            OpportunityDAO opportunityDAO = daoFactory.getOpportunityDAO();
            ArrayList<Opportunity> viewableOpps = opportunityDAO.retrieveOpp(viewableCustomer);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            request.setAttribute("yourCustomer", viewableCustomer);
            request.setAttribute("opportunityList", viewableOpps);
            request.setAttribute("viewUrl", "opportunityManagement/addOpportunity");


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

    public static void addOpportunity(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String applicationText = null;


        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);

            String idCustomer = request.getParameter("customerid");
            String idOpportunity = request.getParameter("opportunity");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();
            Customer viewableCustomer = customerDAO.findByCustomerId(Integer.valueOf(idCustomer));

            OpportunityDAO opportunityDAO = daoFactory.getOpportunityDAO();
            request.setAttribute("applicationText", "Opportunity added successfully");

            try {

                opportunityDAO.addOppToCustomer(viewableCustomer, Integer.valueOf(idOpportunity));

                String actionType = "add";
                String actionTable = "link_opportunity";
                Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, Integer.valueOf(idOpportunity));


            }catch (DuplicatedObjectException e) {
                applicationText ="You already added this opportunity to " + viewableCustomer.getBusinessName();
                logger.log(Level.INFO, "Tentativo di inserimento di opportunità già esistente");
                request.setAttribute("applicationText", applicationText);
            }

            ArrayList<Opportunity> viewableOpps = opportunityDAO.retrieveOpp(viewableCustomer);

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            request.setAttribute("yourCustomer", viewableCustomer);
            request.setAttribute("opportunityList", viewableOpps);

            request.setAttribute("viewUrl", "opportunityManagement/addOpportunity");


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

    public static void deleteOpp(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        String applicationText = null;


        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);

            String idCustomer = request.getParameter("idCustomer");
            String idOpportunity = request.getParameter("idopp");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();
            Customer viewableCustomer = customerDAO.findByCustomerId(Integer.valueOf(idCustomer));

            OpportunityDAO opportunityDAO = daoFactory.getOpportunityDAO();
            opportunityDAO.removeOppToCustomer(viewableCustomer, Integer.valueOf(idOpportunity));

            String actionType = "del";
            String actionTable = "link_opportunity";
            Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, Integer.valueOf(idOpportunity));

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

        CustomerManagement.customerView(request, response);
    }


    public static void createOpportunity(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();


        try {
            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "AdminManagement/adminPage");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            String opportunityName = request.getParameter("opportunityName");

            OpportunityDAO opportunityDAO = daoFactory.getOpportunityDAO();

            Opportunity opp = opportunityDAO.create(opportunityName);

            String actionType = "add";
            String actionTable = "opportunity";
            Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, opp.getIdopp());

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("opportunity", opp);
            request.setAttribute("applicationText2", "Opportunity " + opportunityName + " added succesfully.");

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

        AdministratorManagement.viewService(request, response);
    }
}
