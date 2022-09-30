/*
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
import model.mo.AppServices;
import model.mo.Notes;
import services.config.Configuration;
import services.logservice.LogService;

import model.mo.User;
import model.mo.Customer;


public class CustomerManagement {

    public static void create(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException{ //metodo statico

        //  Helper.validateSession(request, response, "CustomerManagement/create");

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

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();



            AppServicesDAO appServicesDAO = daoFactory.getAppServicesDAO();

            ArrayList<AppServices> appServicesList = appServicesDAO.getAppServices();

            if(null==appServicesList || appServicesList.isEmpty()){

                request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                request.setAttribute("viewUrl", "CustomerManagement/message");

            }else {

                request.setAttribute("appServicesList", appServicesList);
                request.setAttribute("viewUrl", "CustomerManagement/create");

            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


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

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();

            Long id = loggedUser.getIduser();

            Customer createdCustomer = customerDAO.create(

                    request.getParameter("businessName"),
                    request.getParameter("spokesperson"),
                    Integer.valueOf(request.getParameter("services")),
                    request.getParameter("product"),
                    request.getParameter("customerType"),
                    request.getParameter("contract"),
                    id

            );

            AppServicesDAO appServicesDAO = daoFactory.getAppServicesDAO();

            ArrayList<AppServices> appServicesList = appServicesDAO.getAppServices();

            if(null==appServicesList || appServicesList.isEmpty()){

                request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                request.setAttribute("viewUrl", "CustomerManagement/message");

            }else {

                request.setAttribute("appServicesList", appServicesList);
                request.setAttribute("viewUrl", "CustomerManagement/create");

            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("createdCustomerBusinessName", createdCustomer.getBusinessName());
            request.setAttribute("createdCustomerSpokesperson", createdCustomer.getSpokeperson());

        } catch (DuplicatedObjectException e) {
            //applicationMessage = "Contatto già esistente";
            //logger.log(Level.INFO, "Tentativo di inserimento di contatto già esistente");
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

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

            Long id = loggedUser.getIduser();
            String businessName = request.getParameter("stringName");
            String product = request.getParameter("stringProduct");
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            if(businessName==null && product==null) {

                CustomerDAO customerDAO = daoFactory.getCustomerDAO();

                ArrayList<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDAO.findCustomers(id);

                request.setAttribute("CustomerList", customerList);


            }else if(businessName!=null){

                CustomerDAO customerDAO = daoFactory.getCustomerDAO();

                ArrayList<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDAO.findByCustomerName(businessName);

                request.setAttribute("CustomerList", customerList);

            } else if(product!=null) {

                CustomerDAO customerDAO = daoFactory.getCustomerDAO();

                ArrayList<Customer> customerList = new ArrayList<Customer>();
                customerList = customerDAO.findByProductName(product);

                request.setAttribute("CustomerList", customerList);


            }

            AppServicesDAO appServicesDAO = daoFactory.getAppServicesDAO();

            ArrayList<AppServices> appServicesList = appServicesDAO.getAppServices();

            if(null==appServicesList || appServicesList.isEmpty()){

                request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                request.setAttribute("viewUrl", "CustomerManagement/message");

            }else {

                request.setAttribute("appServicesList", appServicesList);
                request.setAttribute("viewUrl", "CustomerManagement/create");

            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();


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

    public static void customerView(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException{ //metodo statico


        //Integer customerid = Integer.valueOf(request.getParameter("idcustomer"));

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

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);

            String idCustomer = request.getParameter("idCustomer");
            request.setAttribute("yourid", idCustomer);

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            NotesDAO notesDAO = daoFactory.getNotesDAO();
            ArrayList<Notes> viewableNotes = new ArrayList<Notes>();
            viewableNotes = notesDAO.show(Integer.valueOf(idCustomer));

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

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            request.setAttribute("yourCustomer", viewableCustomer);
            request.setAttribute("customerNotes", viewableNotes);

            request.setAttribute("viewUrl", "CustomerManagement/customerProfile");


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

    public static void customerEdit(HttpServletRequest request, HttpServletResponse response) throws DuplicatedObjectException{ //metodo statico

        DAOFactory sessionDAOFactory= null;
        DAOFactory daoFactory = null;
        User loggedUser;

        String idCustomer = request.getParameter("yourCustomer");
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


            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction(); //apre la transazione verso il database

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();
            Customer updatedCustomer = customerDAO.updateCustomer(idCustomer ,businessName, spokeperson, product,customerType, contract, services );

            NotesDAO notesDAO = daoFactory.getNotesDAO();
            ArrayList<Notes> viewableNotes = new ArrayList<Notes>();
            viewableNotes = notesDAO.show(Integer.valueOf(idCustomer));

            AppServicesDAO appServicesDAO = daoFactory.getAppServicesDAO();

            ArrayList<AppServices> appServicesList = appServicesDAO.getAppServices();

            if(null==appServicesList || appServicesList.isEmpty()){

                request.setAttribute("applicationText", "Services list is empty; Please check with your administrator.");
                request.setAttribute("viewUrl", "CustomerManagement/message");

            }else {

                request.setAttribute("appServicesList", appServicesList);
                request.setAttribute("viewUrl", "CustomerManagement/customerProfile");

            }

            daoFactory.commitTransaction(); //commit sul databse
            sessionDAOFactory.commitTransaction(); //committo su cookie -> non ha vera importanza ma tratto cookie come db

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("yourCustomer", updatedCustomer);
            Integer idc =  updatedCustomer.getIdcustomer();
            request.setAttribute("yourid", idc);
            request.setAttribute("customerNotes", viewableNotes);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Controller Error", e);
            try {
                //if (daoFactory != null) daoFactory.rollbackTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.rollbackTransaction();
            } catch (Throwable t) {
            }
            throw new RuntimeException(e);

        } finally {
            try {
                // if (daoFactory != null) daoFactory.closeTransaction();
                if (sessionDAOFactory != null) sessionDAOFactory.closeTransaction();
            } catch (Throwable t) {
            }
        }


        //Integer customerid = Integer.valueOf(request.getParameter("idcustomer"));


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

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL,null);
            daoFactory.beginTransaction();

            CustomerDAO customer = daoFactory.getCustomerDAO();
            Customer deletedCustomer = customer.deleteCustomer(customerToDel);
            String deletedMessage="Customer " + deletedCustomer.getBusinessName() + "was deleted successfully!";
            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn",loggedUser!=null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("yourCustomer", deletedCustomer);
            request.setAttribute("viewUrl", "CustomerManagement/message");
            request.setAttribute("applicationText", deletedMessage);

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



    //TODO: verificare a database che l'utente loggato sia autorizzato a compiere questa funzione

    //TODO: processo i dati del form. Se giusti salvo a database e rimando ad una view di successo,
    // se errati elenco gli errori e rimando al form di invio

    // Per rimandare ad una view diversa da quell'helper, popolo: request.setAttribute("viewUrl", "manager/view");
    // Posso anche passare nella request delle variabili/messaggi per l'utente da stampare nella jsp


}
*/
