package model.dao.mySQLJDBCImpl;

import model.dao.ScheduleDAO;
import model.mo.*;
import model.mo.Schedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleJDBC implements ScheduleDAO {

    Connection conn;
    public ScheduleJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Schedule create(String idnote, String idmanager, String idcustomer, String dateApp) {


        PreparedStatement ps;

        Schedule sched = new Schedule();
        sched.setCustomerid(Integer.valueOf(idcustomer));
        sched.setUserid(Integer.valueOf(idmanager));
        sched.setDateApp(Timestamp.valueOf(dateApp));
        sched.setNoteid(Integer.valueOf(idnote));
        try {

            String sql   = " INSERT INTO schedule ("
                    + "     noteid,"
                    + "     customerid,"
                    + "     userid,"
                    + "     dateApp"
                    + "   ) "
                    + " VALUES (?,?,?,?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setString(i++, String.valueOf(sched.getNoteid()));
            ps.setString(i++, String.valueOf(sched.getCustomerid())) ;
            ps.setString(i++, String.valueOf(sched.getUserid()));
            ps.setString(i++, String.valueOf(sched.getDateApp()));

            int result = ps.executeUpdate();

            if (result > 0) {
                try {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        sched.setIdschedule(rs.getInt(1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sched;

    }
    

    @Override
    public ArrayList<Schedule> showSchedules(User user) {

        PreparedStatement ps;
        Schedule schedule = new Schedule();
        schedule.setUser(user);
        ArrayList<Schedule> schedules = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM schedule JOIN notes ON schedule.noteid=notes.noteid JOIN customer ON notes.customer=customer.idcustomer JOIN user ON customer.manager=user.iduser"
                    + " WHERE userid=? AND"
                    + "   schedule.deleted  = 'N' ";


            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, Math.toIntExact(schedule.getUser().getIduser()));
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                schedule = read(resultSet);
                //letture di oggetti ****************************
                schedule.setCustomer(CustomerJDBC.read(resultSet));
                schedule.setNote(NotesJDBC.read(resultSet));
                schedule.setUser(UserDAOMySQLJDBCImpl.read(resultSet));
                schedules.add(schedule);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    @Override
    public ArrayList<Schedule> showSchedulesByDate(String dateToSearch, User user) {

        PreparedStatement ps;
        Schedule schedule;
        ArrayList<Schedule> schedules = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM schedule JOIN notes ON schedule.noteid=notes.noteid JOIN customer ON notes.customer=customer.idcustomer JOIN user ON customer.manager=user.iduser"
                    + " WHERE userid=? AND"
                    + "  date(schedule.dateApp)=? AND"
                    + "   schedule.deleted  = 'N' ";


            ps = conn.prepareStatement(sql);
            ps.setInt(1, Math.toIntExact(user.getIduser()));
            ps.setString(2, dateToSearch);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                schedule = read(resultSet);
                schedule.setCustomer(CustomerJDBC.read(resultSet));
                schedule.setNote(NotesJDBC.read(resultSet));
                schedule.setUser(UserDAOMySQLJDBCImpl.read(resultSet));
                schedules.add(schedule);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    @Override
    public ArrayList<Schedule> showFutureSchedules(User user) {

        PreparedStatement ps;
        Schedule schedule;
        ArrayList<Schedule> schedules = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM schedule JOIN notes ON schedule.noteid=notes.noteid JOIN customer ON notes.customer=customer.idcustomer JOIN user ON customer.manager=user.iduser"
                    + " WHERE userid=? AND"
                    + "  TIMESTAMPDIFF(DAY, date(schedule.dateApp), CURDATE())<=0 AND"
                    + "   schedule.deleted  = 'N' ";


            ps = conn.prepareStatement(sql);
            ps.setInt(1, Math.toIntExact(user.getIduser()));


            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                schedule = read(resultSet);
                schedule.setCustomer(CustomerJDBC.read(resultSet));
                schedule.setNote(NotesJDBC.read(resultSet));
                schedule.setUser(UserDAOMySQLJDBCImpl.read(resultSet));
                schedules.add(schedule);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }



    Schedule read(ResultSet rs) {
        Schedule schedule = new Schedule();
        try
        {
            schedule.setIdschedule(rs.getInt("idschedule"));
            schedule.setCustomerid(rs.getInt("customerid"));
            schedule.setNoteid(rs.getInt("noteid"));
            schedule.setUserid(rs.getInt("userid"));
            schedule.setDateApp(rs.getTimestamp("dateApp"));
            schedule.setDeleted(String.valueOf(rs.getString("deleted")));

        } catch (SQLException sqle) {
            System.out.println("Error reading resultset from database");
        }
        return schedule;
    }

}
