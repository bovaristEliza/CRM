package model.dao.mySQLJDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.dao.exception.DuplicatedObjectException;
import model.mo.AppServices;
import model.mo.Customer;
import model.dao.CustomerDAO;


public class CustomerJDBC implements CustomerDAO {

    Connection conn;
    public CustomerJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Customer create(
            String businessName,
            String spokeperson,
            Integer services,
            String product,
            String customerType,
            String contract,
            Long id) throws DuplicatedObjectException {

        PreparedStatement ps;
        Integer iduser = Math.toIntExact(id);
        Customer customer = new Customer();
        customer.setBusinessName(businessName);
        customer.setSpokeperson(spokeperson);
        customer.setServices(services);
        customer.setManager(iduser);
        customer.setProduct(product);
        customer.setCustomerType(customerType);
        customer.setContract(contract);

        try {

            String sql
                    = " SELECT businessName "
                    + " FROM customer "
                    + " WHERE "
                    + " deleted ='N' AND "
                    + " businessName = ?";

            ps = conn.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, customer.getBusinessName());

            ResultSet resultSet = ps.executeQuery();

            boolean exist;
            exist = resultSet.next();
            resultSet.close();

            if (exist) {
                throw new DuplicatedObjectException("Tentativo di inserimento di un customer già esistente.");
            }

            sql   = " INSERT INTO customer ("
                    + "     businessName,"
                    + "     spokeperson,"
                    + "     services,"
                    + "     manager,"
                    + "     product,"
                    + "     customerType,"
                    + "     contract"
                    + "   ) "
                    + " VALUES (?,?,?,?,?,?,?)";

            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 1;
            ps.setString(i++, customer.getBusinessName());
            ps.setString(i++, customer.getSpokeperson());
            ps.setString(i++, String.valueOf(customer.getServices()));
            ps.setString(i++, String.valueOf(customer.getManager()));
            ps.setString(i++, customer.getProduct());
            ps.setString(i++, customer.getCustomerType());
            ps.setString(i++, customer.getContract());

            int result = ps.executeUpdate();

            if (result > 0) {
                try {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        customer.setIdcustomer(rs.getInt(1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customer;

    }

    @Override
    public Customer updateCustomer(String idCustomer, String businessName, String spokeperson, String product, String customerType, String contract, Integer services)  {
        PreparedStatement ps;
        Customer customer = new Customer();
        customer.setIdcustomer(Integer.valueOf(idCustomer));
        customer.setBusinessName(businessName);
        customer.setSpokeperson(spokeperson);
        customer.setProduct(product);
        customer.setCustomerType(customerType);
        customer.setServices(Integer.valueOf(services));
        customer.setContract(contract);

        try {
            String sql = " UPDATE customer SET businessName=?, spokeperson=?, product=?, services=?, customerType=?, contract=? WHERE idcustomer=?";

            ps = conn.prepareStatement(sql);
            int i = 1;

            ps.setString(i++, customer.getBusinessName());
            ps.setString(i++, customer.getSpokeperson());
            ps.setString(i++, customer.getProduct());
            ps.setString(i++, String.valueOf(customer.getServices()));
            ps.setString(i++, customer.getCustomerType());
            ps.setString(i++, customer.getContract());
            ps.setString(i++, String.valueOf(customer.getIdcustomer()));


            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Customer tempCustomer = findByCustomerId(customer.getIdcustomer());
        return tempCustomer;
    }

    @Override
    public Customer deleteCustomer(Customer customer) {
        PreparedStatement ps;
        try {

            String sql
                    = " UPDATE customer "
                    + " SET deleted='Y' "
                    + " WHERE "
                    + " idcustomer=?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, customer.getIdcustomer());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Customer tempCustomer = findDeleted(customer.getIdcustomer());
        return tempCustomer;
    }

    @Override
    public Customer findByCustomerId(Integer idcustomer) {

        PreparedStatement ps;
        Customer customer = null;

        try {

            String sql
                    = " SELECT *"
                    + " FROM customer "
                    + " JOIN services ON customer.services = services.idservice "
                    + " WHERE "
                    + "   customer.idcustomer = ? AND "
                    + "   customer.deleted  = 'N' ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idcustomer);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                customer = read(resultSet);
                customer.setAppServices(AppServicesJDBC.read(resultSet));
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customer;
    }

    @Override
    public ArrayList<Customer> findByCustomerName(String name, Long id) {
        PreparedStatement ps;
        Customer customer;
        ArrayList<Customer> customers = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM customer "
                    + " JOIN services ON customer.services = services.idservice "
                    + " WHERE "
                    + " customer.manager = ? AND "
                    + " customer.deleted  = 'N'  AND"
                    + " INSTR(BusinessName, ?)!=0 " ;


            ps = conn.prepareStatement(sql);
            ps.setInt(1, Math.toIntExact(id));
            ps.setString(2, name);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                customer = read(resultSet);
                customer.setAppServices(AppServicesJDBC.read(resultSet));
                customers.add(customer);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }
    @Override
    public ArrayList<Customer> findByProductName(String productName, Long id) {
        PreparedStatement ps;
        Customer customer;
        ArrayList<Customer> customers = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM customer "
                    + " JOIN services ON customer.services = services.idservice "
                    + " WHERE "
                    + "   customer.manager = ? AND "
                    + "  INSTR(product, ?)!=0 AND"
                    + "  customer.deleted  = 'N' ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, Math.toIntExact(id));
            ps.setString(2, productName);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                customer = read(resultSet);
                customer.setAppServices(AppServicesJDBC.read(resultSet));
                customers.add(customer);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public ArrayList<Customer> findByAnyString(String search, Long id) {
        PreparedStatement ps;
        Customer customer;
        ArrayList<Customer> customers = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM customer "
                    + " JOIN services ON customer.services = services.idservice "
                    + " WHERE "
                    + "   customer.manager = ? AND "
                    + "   customer.deleted  = 'N' AND"
                    + " ( INSTR(businessName, ?)!=0 OR"
                    + "  INSTR(spokeperson, ?)!=0 OR"
                    + "  INSTR(customerType, ?)!=0 OR"
                    + "  INSTR(contract, ?)!=0 OR"
                    + "  INSTR(serviceName, ?)!=0 OR"
                    + "  INSTR(product, ?)!=0)";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, Math.toIntExact(id));
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);
            ps.setString(5, search);
            ps.setString(6, search);
            ps.setString(7, search);


            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                customer = read(resultSet);
                customer.setAppServices(AppServicesJDBC.read(resultSet));
                customers.add(customer);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    @Override
    public Customer findDeleted(Integer idcustomer) {

        PreparedStatement ps;
        Customer customer = null;

        try {

            String sql
                    = " SELECT *"
                    + " FROM customer "
                    + " WHERE "
                    + "   idcustomer = ? AND"
                    + "   deleted  = 'Y' ";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idcustomer);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                customer = read(resultSet);
            }
            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customer;
    }

    @Override
    public ArrayList<Customer> findCustomers( Long id) {

        PreparedStatement ps;
        Customer customer;
        ArrayList<Customer> customers = new ArrayList<>();

        try {

            String sql =  " SELECT *"
                    + " FROM customer "
                    + " JOIN services ON customer.services = services.idservice "
                    + " WHERE "
                    + "   customer.manager = ? AND "
                    + "   customer.deleted  = 'N' ";


            ps = conn.prepareStatement(sql);
            ps.setInt(1, Math.toIntExact(id));

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                customer = read(resultSet);
                customer.setAppServices(AppServicesJDBC.read(resultSet));
                customers.add(customer);
            }

            resultSet.close();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }



    static Customer read(ResultSet rs) {
        Customer customer = new Customer();
        try
        {
            customer.setIdcustomer(rs.getInt("idcustomer"));
            customer.setBusinessName(rs.getString("businessName"));
            customer.setSpokeperson(rs.getString("spokeperson"));
            customer.setServices((rs.getInt("customer.services")));
            customer.setManager(rs.getInt("manager"));
            customer.setProduct(rs.getString("product"));
            customer.setCustomerType(rs.getString("customerType"));
            customer.setContract(rs.getString("contract"));
            customer.setDeleted(String.valueOf(rs.getString("deleted")));

        } catch (SQLException sqle) {
            System.out.println("Error reading resultset from database");
        }
        return customer;
    }
}