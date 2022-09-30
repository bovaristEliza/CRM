package controller;

import helpers.Helper;
import model.dao.*;
import model.dao.exception.DuplicatedObjectException;
import model.mo.*;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

public class NotesManagement {

    public static void displayNotesForm(HttpServletRequest request, HttpServletResponse response) { //metodo statico

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

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);

            String idCustomer = request.getParameter("idCustomer");
            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();

            Customer viewableCustomer = customerDAO.findByCustomerId(Integer.valueOf(idCustomer));

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();
            request.setAttribute("yourCustomer", viewableCustomer);


            request.setAttribute("viewUrl", "notesManagement/notesForm");


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

    public static void tagNote(HttpServletRequest request, HttpServletResponse response) { //metodo statico

        DAOFactory daoFactory = null;
        User loggedUser = null;
        DAOFactory sessionDAOFactory = null;
        Logger logger = LogService.getApplicationLogger();
        SimpleDateFormat formatter;

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

            String requestedAppointment = request.getParameter("appointment");
            if(requestedAppointment.equals("true")) {
                String requestedDate = request.getParameter("date");
                String requestedTime = request.getParameter("time");
                if ( requestedDate.equals("") || requestedTime.equals("") ) {
                    request.setAttribute("applicationText", "No date/time selected for the appointment");
                    NotesManagement.displayNotesForm(request, response);
                    //request.setAttribute("viewUrl", "CustomerManagement/message");
                    return;
                }
            }

            String idCustomer = request.getParameter("idCustomer");
            String idManager = request.getParameter("idmanager");

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            CustomerDAO customerDAO = daoFactory.getCustomerDAO();
            Customer viewableCustomer = customerDAO.findByCustomerId(Integer.valueOf(idCustomer));
            Integer idc = viewableCustomer.getIdcustomer();
            NotesDAO notesDAO = daoFactory.getNotesDAO();

            Notes newNote = notesDAO.create(
                    request.getParameter("content"),
                    String.valueOf(idCustomer),
                    String.valueOf(idManager)

            );

            String actionType = "add";
            String actionTable = "notes";
            Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, newNote.getNoteid());

            String idNote = String.valueOf(newNote.getNoteid());
            String appointment = request.getParameter("appointment");

            if(appointment.equals("true"))
            {

               ScheduleManagement.setAppointment(daoFactory, sessionDAOFactory, request, idNote);

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

            } else {

                daoFactory.commitTransaction();
                sessionDAOFactory.commitTransaction();

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

        CustomerManagement.customerView(request, response);

    }


    public static void deleteNote(HttpServletRequest request, HttpServletResponse response) { //metodo statico

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

        String idCustomer = request.getParameter("idCustomer");
        Notes noteToDel = new Notes();
        Integer idNote = Integer.valueOf(request.getParameter("idnote"));
        noteToDel.setNoteid(idNote);

        try {

            Map sessionFactoryParameters = new HashMap<String, Object>();
            sessionFactoryParameters.put("request", request);
            sessionFactoryParameters.put("response", response);
            sessionDAOFactory = DAOFactory.getDAOFactory(Configuration.COOKIE_IMPL, sessionFactoryParameters);
            sessionDAOFactory.beginTransaction();

            UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
            loggedUser = sessionUserDAO.findLoggedUser();

            if(loggedUser==null){
                HomeManagement.view(request,response);
                sessionDAOFactory.commitTransaction();
                sessionDAOFactory.closeTransaction();
                return;
            }

            daoFactory = DAOFactory.getDAOFactory(Configuration.DAO_IMPL, null);
            daoFactory.beginTransaction();

            NotesDAO notesDAO = daoFactory.getNotesDAO();
            notesDAO.deleteNote(idNote);

            String actionType = "del";
            String actionTable = "notes";
            Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, idNote);

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
}
