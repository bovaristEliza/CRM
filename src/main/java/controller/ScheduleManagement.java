package controller;

import helpers.Helper;
import model.dao.*;
import model.dao.exception.DuplicatedObjectException;
import model.mo.Customer;
import model.mo.Notes;
import model.mo.Schedule;
import model.mo.User;
import services.config.Configuration;
import services.logservice.LogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleManagement {

    public static void scheduleView(HttpServletRequest request, HttpServletResponse response) { //metodo statico

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

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


            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUserId(loggedUser.getIduser());

            ScheduleDAO scheduleDAO = daoFactory.getScheduleDAO();
            ArrayList<Schedule> schedList = scheduleDAO.showSchedules(user);

            if(null==schedList || schedList.isEmpty()){

                request.setAttribute("applicationText", "You have no appointments yet.");

            }else {
                request.setAttribute("scheduleList", schedList);
            }


            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("profileUser", user);
            request.setAttribute("viewUrl", "scheduleManagement/check");


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

    public static void setAppointment(DAOFactory daoFactory, DAOFactory sessionDAOFactory, HttpServletRequest request, String idNote) {

        UserDAO sessionUserDAO = sessionDAOFactory.getUserDAO();
        User loggedUser = sessionUserDAO.findLoggedUser();

            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);


            String idCustomer = request.getParameter("idCustomer");
            String idManager = request.getParameter("idmanager");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String timesec = time + ":00";

            String datetime = date + " " + timesec;

            String prova = idNote;

            ScheduleDAO scheduleDAO = daoFactory.getScheduleDAO();
            Schedule schedule = scheduleDAO.create(idNote, idManager, idCustomer, datetime);

        String actionType = "add";
        String actionTable = "schedule";
        Helper.logAction(daoFactory, sessionDAOFactory, request, Math.toIntExact(loggedUser.getIduser()), actionType, actionTable, schedule.getIdschedule());

    }


    public static void scheduleDateView(HttpServletRequest request, HttpServletResponse response) { //metodo statico

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();
        String date = request.getParameter("date");

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

            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUserId(loggedUser.getIduser());

            ScheduleDAO scheduleDAO = daoFactory.getScheduleDAO();
            ArrayList<Schedule> schedList = scheduleDAO.showSchedulesByDate(date, user);

            if(null==schedList || schedList.isEmpty()){

                ArrayList<Schedule> allSchedList = scheduleDAO.showSchedules(user);
                request.setAttribute("scheduleList", allSchedList);
                request.setAttribute("appText", "You have no appointments on the requested date: " + date);
                request.setAttribute("viewUrl", "CustomerManagement/message");

            }else {
                request.setAttribute("scheduleList", schedList);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("profileUser", user);
            request.setAttribute("viewUrl", "scheduleManagement/check");

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


    public static void scheduleFutureView(HttpServletRequest request, HttpServletResponse response) { //metodo statico

        DAOFactory sessionDAOFactory = null;
        DAOFactory daoFactory = null;
        User loggedUser;
        String applicationMessage = null;

        Logger logger = LogService.getApplicationLogger();

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


            UserDAO userDAO = daoFactory.getUserDAO();
            User user = userDAO.findByUserId(loggedUser.getIduser());

            ScheduleDAO scheduleDAO = daoFactory.getScheduleDAO();
            ArrayList<Schedule> schedList = scheduleDAO.showFutureSchedules(user);

            if(null==schedList || schedList.isEmpty()){

                request.setAttribute("applicationText", "You have no appointments yet.");

            }else {
                request.setAttribute("scheduleList", schedList);
            }

            daoFactory.commitTransaction();
            sessionDAOFactory.commitTransaction();

            request.setAttribute("loggedOn", loggedUser != null);
            request.setAttribute("loggedUser", loggedUser);
            request.setAttribute("profileUser", user);
            request.setAttribute("viewUrl", "scheduleManagement/check");

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
