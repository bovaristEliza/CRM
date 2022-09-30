package model.dao.mySQLJDBCImpl;

import model.dao.NotesDAO;
import model.mo.Customer;
import model.mo.Notes;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class NotesJDBC implements NotesDAO {
    Connection conn;
    public NotesJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public Notes create(String content, String idmanager, String idcustomer) {

        java.util.Date date = new Date();
        Object noteDate = new java.sql.Timestamp(date.getTime());

        PreparedStatement ps;

        Notes note = new Notes();
        note.setContent(content);
        note.setNoteDate((Timestamp) noteDate);
        note.setCustomer(Integer.valueOf(idcustomer));
        note.setManager(Integer.valueOf(idmanager));



        try {

            String sql   = " INSERT INTO notes ("
                    + "     content,"
                    + "     noteDate,"
                    + "     manager,"
                    + "     customer"
                    + "   ) "
                    + " VALUES (?,?,?,?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setString(i++, note.getContent());
            ps.setString(i++, String.valueOf(note.getNoteDate())) ;
            ps.setString(i++, String.valueOf(note.getCustomer()));
            ps.setString(i++, String.valueOf(note.getManager()));

            int result = ps.executeUpdate();

            if (result > 0) {
                try {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        note.setNoteid(rs.getInt(1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return note;

    }

    @Override
    public ArrayList<Notes> show(Integer id) {

        PreparedStatement ps;
        Notes note;
        ArrayList<Notes> notes = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM notes "
                    + " WHERE "
                    + "   customer = ? AND "
                    + "   deleted  = 'N' ";


            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                note = read(resultSet);
                notes.add(note);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notes;
    }

    @Override
    public  void deleteNote(Integer idNote) {
        PreparedStatement ps;

        Notes note = new Notes();
        note.setNoteid(idNote);

        try {

            String sql
                    = " UPDATE notes "
                    + " SET deleted='Y' "
                    + " WHERE "
                    + " noteid=?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, note.getNoteid());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    static Notes read(ResultSet rs) {
        Notes note = new Notes();
        try
        {
            note.setNoteid(rs.getInt("noteid"));
            note.setContent(rs.getString("content"));
            note.setNoteDate(Timestamp.valueOf(rs.getString("noteDate")));
            note.setManager(rs.getInt("manager"));
            note.setCustomer(rs.getInt("customer"));
            note.setDeleted(String.valueOf(rs.getString("deleted")));

        } catch (SQLException sqle) {
            System.out.println("Error reading resultset from database");
        }
        return note;
    }



}
