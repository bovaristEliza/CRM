package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.Customer;
import model.mo.Opportunity;

import java.util.ArrayList;

public interface OpportunityDAO {


    Opportunity create(

            String oppName
    )  throws DuplicatedObjectException;

    ArrayList<Opportunity> retrieveOpp(Customer customer);

    void addOppToCustomer(Customer viewableCustomer, Integer opportunity) throws DuplicatedObjectException;

    ArrayList<Opportunity> show(Integer idcustomer);

    ArrayList<Opportunity> showOpportunities();

    void removeOppToCustomer(Customer viewableCustomer, Integer idopportunity);
}
