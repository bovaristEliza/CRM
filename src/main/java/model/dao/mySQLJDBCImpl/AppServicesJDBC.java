package model.dao.mySQLJDBCImpl;

import model.dao.AppServicesDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.AppServices;
import model.mo.Customer;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppServicesJDBC implements AppServicesDAO {

    Connection conn;
    public AppServicesJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public AppServices create(

            String serviceName,
            BigDecimal serviceCost,
            String description
    )  throws DuplicatedObjectException  {
        PreparedStatement ps;

        AppServices services = new AppServices();
        services.setServiceName(serviceName);
        services.setServiceCost(serviceCost);
        services.setDescription(description);


        try {

            String sql
                    = " SELECT serviceName "
                    + " FROM services "
                    + " WHERE "
                    + " deleted ='N' AND "
                    + " serviceName = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, services.getServiceName());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (exist) {
                throw new DuplicatedObjectException("Tentativo di inserimento di un customer già esistente.");
            }

            sql   = " INSERT INTO services ("
                    + "     serviceName,"
                    + "     serviceCost,"
                    + "     description"
                    + "   ) "
                    + " VALUES (?,?,?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 1;
            ps.setString(i++, services.getServiceName());
            ps.setString(i++, String.valueOf(services.getServiceCost()));
            ps.setString(i++, services.getDescription());

            int result = ps.executeUpdate();

            if (result > 0) {
                try {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        services.setIdservice(rs.getInt(1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DuplicatedObjectException e) {
            e.printStackTrace();
        }

        return services;

    }

    @Override
    public ArrayList<AppServices> getAppServices() {

        PreparedStatement ps;
        AppServices appServices;
        ArrayList<AppServices> appServicesList = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM services "
                    + " WHERE "
                    + "   deleted  = 'N' ";


            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                appServices = read(resultSet);
                appServicesList.add(appServices);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appServicesList;
    }

    static AppServices read(ResultSet rs) {
        AppServices appServices = new AppServices();
        try
        {
            appServices.setIdservice(rs.getInt("idservice"));
            appServices.setServiceName(rs.getString("serviceName"));
            appServices.setServiceCost(rs.getBigDecimal("serviceCost"));
            appServices.setDescription(rs.getString("description"));
            appServices.setDeleted(String.valueOf(rs.getString("deleted")));

        } catch (SQLException sqle) {
            System.out.println("Error reading resultset from database");
        }
        return appServices;
    }

    @Override
    public void linkService(Integer idservice, Integer idopportunity) throws DuplicatedObjectException {
        PreparedStatement ps;

        try {
            String sql
                    = " SELECT * "
                    + " FROM  link_opportunity"
                    + " WHERE "
                    + " idOpportunity = ? AND "
                    + " idService = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, idopportunity);
            ps.setInt(i++, idservice);

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (exist) {
                throw new DuplicatedObjectException("Tentativo di inserimento link service to opportunity già esistente.");
            }


            sql   = " INSERT INTO link_opportunity ("
                    + "     idOpportunity,"
                    + "     idService"
                    + "   ) "
                    + " VALUES (?,?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 1;
            ps.setInt(i++, idopportunity);
            ps.setInt(i++, idservice);

            int result = ps.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    
}
