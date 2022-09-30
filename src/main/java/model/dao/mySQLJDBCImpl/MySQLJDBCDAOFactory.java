package model.dao.mySQLJDBCImpl;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import model.dao.*;
import services.config.Configuration;


public  class MySQLJDBCDAOFactory extends DAOFactory {

    private Map factoryParameters;

    private Connection connection;

    public MySQLJDBCDAOFactory(Map factoryParameters) {
        this.factoryParameters=factoryParameters;
    }



    @Override
    public void beginTransaction() {
        try {

            Class.forName(Configuration.DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(Configuration.DATABASE_URL);
            this.connection.setAutoCommit(false);

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }


     // metodi wrapper



    @Override
    public void commitTransaction() {
        try {
            this.connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rollbackTransaction() {

        try {
            this.connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void closeTransaction() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOMySQLJDBCImpl(connection);
    }

    @Override
    public CustomerJDBC getCustomerDAO() {
        return new CustomerJDBC(connection);
    }

    @Override
    public AppServicesDAO getAppServicesDAO() {
        return new AppServicesJDBC(connection);
    }

    @Override
    public NotesDAO getNotesDAO() {
        return new NotesJDBC(connection);
    }

    @Override
    public ScheduleDAO getScheduleDAO() {
        return new ScheduleJDBC(connection);
    }

    @Override
    public OpportunityDAO getOpportunityDAO() { return new OpportunityJDBC(connection); }

    @Override
    public LoggerTabDAO getLoggerTabDAO() {
        return new LoggerTabJDBC(connection) {};
    }


}
