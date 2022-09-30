package controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jnlp.IntegrationService;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import helpers.Helper;
import model.dao.*;
import model.dao.exception.DuplicatedObjectException;
import model.mo.*;
import services.config.Configuration;
import services.logservice.LogService;


public class CustomerManagement {

    public static void create(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException{ //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMText = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            Helper.getServices(daoFactory, sessionDAOFactory, request);

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "CustomerManagement/create");

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
    
    public static void newCustomer(HttpServletRequest request, HttpServletResponse response) { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();

        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            String validationErrors="";
            if(!Helper.isValidEmailAddress( request.getParameter("spokesperson")))
            {
                validationErrors += "Invalid email format:" +  request.getParameter("spokesperson") + "<br />";
            }
            if(validationErrors!=""){
                request.setAttribute("emailError", validationErrors);
                create(request,response);
                return ;
            }

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();

            Long id = loggedUser.getIduser();

            try {

                Customer createdCustomer = customerDAO.create(

                        request.getParameter("businessName"),
                        request.getParameter("spokesperson"),
                        Integer.valueOf(request.getParameter("services")),
                        request.getParameter("product"),
                        request.getParameter("customerType"),
                        request.getParameter("contract"),
                        id

                );

                request.setAttribute("createdCustomerBusinessName", createdCustomer.getBusinessName());
                request.setAttribute("createdCustomerSpokesperson", createdCustomer.getSpokeperson());

                String actionType = "add";
                String actionTable = "customer";
                Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, createdCustomer.getIdcustomer());

            } catch (DuplicatedObjectException e) {
                applicationMessage = request.getParameter("businessName") + " already exists!";
                logger.log(Level.INFO, "Tentativo di inserimento di customer già esistente");
             }

            Helper.getServices(daoFactory, sessionDAOFactory, request);

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("appText", applicationMessage);
            request.setAttribute("viewUrl", "CustomerManagement/create");

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

    public static void showCustomers(HttpServletRequest request, HttpServletResponse response) {


        DAOFactory daoFactory = null;
        User loggedUser = null;
        String applicationMessage = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();

        String test = request.getParameter("businessName");

        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            Long id = loggedUser.getIduser();
            String businessName = request.getParameter("stringName");
            String product = request.getParameter("stringProduct");
            String freeSearch = request.getParameter("search");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();

            if(businessName==null && product==null && freeSearch==null) {

                ArrayList<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDAO.findCustomers(id);

                request.setAttribute("CustomerList", customerList);

            }else if(businessName!=null){

                ArrayList<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDAO.findByCustomerName(businessName, id);

                request.setAttribute("CustomerList", customerList);

            } else if(product!=null) {

                ArrayList<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDAO.findByProductName(product, id);

                request.setAttribute("CustomerList", customerList);

            }  else if(freeSearch!=null) {

                ArrayList<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDAO.findByAnyString(freeSearch, id);

                request.setAttribute("CustomerList", customerList);

            }

            Helper.getServices(daoFactory, sessionDAOFactory, request);

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("viewUrl", "CustomerManagement/create");

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

    public static void customerView(HttpServletRequest request, HttpServletResponse response){ //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();


        try {
            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO(); //
            loggedUser = sessionUserDAO.findLoggedUser();

            String idCustomer = request.getParameter("idCustomer");
            request.setAttribute("yourid", idCustomer);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            NotesDAO notesDAO = daoFactory.getNotesDAO();
            ArrayList<Notes> viewableNotes = new ArrayList<Notes>();
            viewableNotes = notesDAO.show(Integer.valueOf(idCustomer));

            OpportunityDAO opportunityDAO = daoFactory.getOpportunityDAO();
            ArrayList<Opportunity> viewableOpps = new ArrayList<Opportunity>();
            viewableOpps = opportunityDAO.show(Integer.valueOf(idCustomer));

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();
            Customer viewableCustomer = customerDAO.findByCustomerId(Integer.valueOf(idCustomer));

            AppServicesDAO appServicesDAO = daoFactory.getAppServicesDAO();

            ArrayList<AppServices> appServicesList = appServicesDAO.getAppServices();

            if(null==appServicesList || appServicesList.isEmpty()){

                request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                request.setAttribute("viewUrl", "CustomerManagement/message");

            }else {

                request.setAttribute("appServicesList", appServicesList);
                request.setAttribute("viewUrl", "CustomerManagement/customerProfile");

            }

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("yourCustomer", viewableCustomer);
            request.setAttribute("customerNotes", viewableNotes);
            request.setAttribute("opportunityList", viewableOpps);
            request.setAttribute("viewUrl", "CustomerManagement/customerProfile");

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

    public static void customerEdit(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException{ //metodo statico

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationText = null;

        String idCustomer = request.getParameter("idCustomer");
        String businessName= request.getParameter("businessName");
        String spokeperson = request.getParameter("spokeperson");
        String product = request.getParameter("product");
        String customerType = request.getParameter("customerType");
        String contract = request.getParameter("contract");
        Integer services = Integer.parseInt(request.getParameter("services"));

        Logger logger = LogService.getApplicationLogger();

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            String validationErrors="";
            if(!Helper.isValidEmailAddress( request.getParameter("spokeperson")))
            {
                validationErrors += "Invalid email format:" +  request.getParameter("spokeperson") + "<br />";
            }
            if(validationErrors!=""){
                request.setAttribute("emailError", validationErrors);
                customerView(request,response);
                return ;
            }

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();
            Customer updatedCustomer = customerDAO.updateCustomer(idCustomer ,businessName, spokeperson, product,customerType, contract, services );

            String actionType = "upd";
            String actionTable = "customer";
            Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, updatedCustomer.getIdcustomer());

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

        customerView(request,response);

    }

    public static void deleteCustomer(HttpServletRequest request, HttpServletResponse response) { //metodo statico

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        Customer customerToDel = new Customer();
        Integer id = Integer.valueOf(request.getParameter("custid"));
        customerToDel.setIdcustomer(id);

        try {

            Map sessionFactoryParameters=new HashMap<String,Object>();
            sessionFactoryParameters.put("request",request);
            sessionFactoryParameters.put("response",response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL,sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            CustomerDAO customer = daoFactory.getCustomerDAO();
            Customer deletedCustomer = customer.deleteCustomer(customerToDel);
            String deletedMessage="Customer " + deletedCustomer.getBusinessName() + "was deleted successfully!";

            String actionType = "del";
            String actionTable = "customer";
            Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, deletedCustomer.getIdcustomer());

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("yourCustomer", deletedCustomer);
            request.setAttribute("viewUrl", "CustomerManagement/message");
            request.setAttribute("applicationText", deletedMessage);

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
