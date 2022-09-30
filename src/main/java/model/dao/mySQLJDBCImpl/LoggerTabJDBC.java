package model.dao.mySQLJDBCImpl;

import model.dao.LoggerTabDAO;
import model.mo.AppServices;
import model.mo.LoggerTab;
import model.mo.Notes;

import java.sql.*;
import java.util.ArrayList;

public class LoggerTabJDBC implements LoggerTabDAO {

    Connection conn;
    public LoggerTabJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public LoggerTab insert(Integer idUser, Timestamp logTime, String actionType, String actionTable, Integer actionRecordId) {
        PreparedStatement ps;

        LoggerTab log = new LoggerTab();
        log.setIdUser(idUser);
        log.setLogTime(logTime);
        log.setActionType(actionType);
        log.setActionTable(actionTable);
        log.setActionRecordId(actionRecordId);

        try {

            String sql   = " INSERT INTO loggertab ("
                    + "     idUser,"
                    + "     logTime,"
                    + "     actionType,"
                    + "     actionTable,"
                    + "     actionRecordId"
                    + "   ) "
                    + " VALUES (?,?,?,?,?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setInt(i++, log.getIdUser());
            ps.setString(i++, String.valueOf(log.getLogTime())) ;
            ps.setString(i++, log.getActionType());
            ps.setString(i++, log.getActionTable());
            ps.setInt(i++, log.getActionRecordId());

            int result = ps.executeUpdate();

            if (result > 0) {
                try {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        log.setIdLogger(rs.getInt(1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return log;
    }

    @Override
    public ArrayList<LoggerTab> getLogs() {

        PreparedStatement ps;

        LoggerTab loggerTab;
        ArrayList<LoggerTab> logList = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM loggertab JOIN user ON loggertab.idUser=user.iduser";

            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                loggerTab = read(resultSet);
                loggerTab.setUser(UserDAOMySQLJDBCImpl.read(resultSet));
                logList.add(loggerTab);
            }
            resultSet.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return logList;
    }




    static LoggerTab read(ResultSet rs) {
        LoggerTab loggerTab = new LoggerTab();
        try
        {
            loggerTab.setIdLogger(rs.getInt("idLogger"));
            loggerTab.setIdUser(rs.getInt(  "idUser"));
            loggerTab.setLogTime(rs.getTimestamp("logTime"));
            loggerTab.setActionType(rs.getString("actionType"));
            loggerTab.setActionTable(rs.getString("actionTable"));
            loggerTab.setActionRecordId(rs.getInt("actionRecordId"));

        } catch (SQLException sqle) {
            System.out.println("Error reading resultset from database");
        }
        return loggerTab;
    }
}
