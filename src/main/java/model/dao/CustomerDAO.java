package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Customer;

import java.util.ArrayList;

public interface CustomerDAO {


    Customer updateCustomer(String idCustomer, String businessName, String spokeperson, String product, String customerType, String contract, Integer services);

    public Customer deleteCustomer(Customer customer);


    public Customer findByCustomerId(Integer idcustomer);

    //ArrayList<Customer> findCustomers( ArrayList<Customer> CustomerList, Long id);

    public ArrayList<Customer>  findByCustomerName(String businessName, Long id);

    ArrayList<Customer> findByProductName(String productName, Long id);

    ArrayList<Customer> findByAnyString(String productName, Long id);

    Customer findDeleted(Integer idcustomer);

    public ArrayList<Customer> findCustomers(Long id);

    Customer create(
            String businessName,
            String spokeperson,
            Integer services,
            String product,
            String customerType,
            String contract,
            Long id) throws DuplicatedObjectException;


}
