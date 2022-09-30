package model.dao.mySQLJDBCImpl;

import model.dao.OpportunityDAO;
import model.dao.exception.DuplicatedObjectException;
import model.mo.AppServices;
import model.mo.Customer;
import model.mo.Notes;
import model.mo.Opportunity;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class OpportunityJDBC implements OpportunityDAO {
    Connection conn;
    public OpportunityJDBC(Connection conn) {
        this.conn = conn;
    }


    @Override
    public Opportunity create(

            String oppName
    )  throws DuplicatedObjectException {
        PreparedStatement ps;

        Opportunity opportunity = new Opportunity();
        opportunity.setOppName(oppName);

        try {

            String sql
                    = " SELECT oppName "
                    + " FROM opportunity "
                    + " WHERE "
                    + " deleted ='N' AND "
                    + " oppName = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, opportunity.getOppName());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (exist) {
                throw new DuplicatedObjectException("Tentativo di inserimento di un customer già esistente.");
            }

            sql   = " INSERT INTO opportunity ("
                    + "     oppName"

                    + "   ) "
                    + " VALUES (?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 1;
            ps.setString(i++, opportunity.getOppName());

            int result = ps.executeUpdate();

            if (result > 0) {
                try {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        opportunity.setIdopp(rs.getInt(1));
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

        return opportunity;
    }

    @Override
    public ArrayList<Opportunity> retrieveOpp(Customer customer) {
        PreparedStatement ps;
        AppServices appServices;
        Integer idcustomer = customer.getIdcustomer();

        Opportunity opportunity;
        ArrayList<Opportunity> opportunityList = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM customer JOIN services ON customer.services=services.idservice JOIN link_opportunity ON services.idservice=link_opportunity.idService JOIN opportunity ON idOpportunity=idopp "
                    + " WHERE "
                    + " idcustomer  = ? ";


            ps = conn.prepareStatement(sql);
            ps.setInt(1, idcustomer);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                opportunity = read(resultSet);
                opportunityList.add(opportunity);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return opportunityList;
    }

    @Override
    public void addOppToCustomer(Customer viewableCustomer, Integer opportunity)  throws DuplicatedObjectException {

        PreparedStatement ps;

        try {

            String sql
                    = " SELECT * "
                    + " FROM customer_opportunity"
                    + " WHERE "
                    + " idCustomer = ? AND "
                    + " idopportunity = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, viewableCustomer.getIdcustomer());
            ps.setInt(i++, opportunity);

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (exist) {
                throw new DuplicatedObjectException("Tentativo di inserimento opportunità già esistente.");
            }

            sql   = " INSERT INTO customer_opportunity ("
                    + "     idCustomer,"
                    + "     idopportunity"
                    + "   ) "
                    + " VALUES (?,?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 1;
            ps.setInt(i++, viewableCustomer.getIdcustomer());
            ps.setInt(i++, opportunity);

            int result = ps.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Opportunity read(ResultSet rs) {
        Opportunity opp = new Opportunity();
        try
        {
            opp.setIdopp(rs.getInt("idopp"));
            opp.setOppName(rs.getString("oppName"));

        } catch (SQLException sqle) {
            System.out.println("Error reading resultset from database");
        }
        return opp;
    }

    @Override
    public ArrayList<Opportunity> show(Integer idcustomer) {

        PreparedStatement ps;
        Opportunity opportunity;
        ArrayList<Opportunity> opportunities = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM customer_opportunity JOIN opportunity  ON idopportunity=idopp"
                    + " WHERE "
                    + "   idCustomer = ? ";


            ps = conn.prepareStatement(sql);
            ps.setInt(1, idcustomer);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                opportunity = read(resultSet);
                opportunities.add(opportunity);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return opportunities;
    }

    @Override
    public ArrayList<Opportunity> showOpportunities() {
        PreparedStatement ps;
        Opportunity opportunity;
        ArrayList<Opportunity> opportunities = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM opportunity "
                    + " WHERE "
                    + "   deleted  = 'N' ";


            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                opportunity = read(resultSet);
                opportunities.add(opportunity);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return opportunities;
    }

    @Override
    public void removeOppToCustomer(Customer viewableCustomer, Integer opportunity) {
        PreparedStatement ps;

        try {

            String sql   = " DELETE FROM customer_opportunity "
                    + "  WHERE   idCustomer =?"
                    + "  AND idopportunity = ?";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            ps.setInt(i++, viewableCustomer.getIdcustomer());
            ps.setInt(i++, opportunity);

            int result = ps.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
