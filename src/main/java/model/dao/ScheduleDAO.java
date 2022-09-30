package model.dao;

import model.mo.Schedule;
import model.mo.User;

import java.util.ArrayList;

public interface ScheduleDAO {

    Schedule create(String idnote, String idmanager, String idcustomer, String date);


    ArrayList<Schedule> showSchedules(User user);


    ArrayList<Schedule> showSchedulesByDate(String dateToSearch, User user);

    ArrayList<Schedule> showFutureSchedules(User user);
}
