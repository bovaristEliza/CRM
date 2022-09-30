package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.AppServices;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface AppServicesDAO {


    AppServices create(

            String serviceName,
            BigDecimal serviceCost,
            String description
    ) throws DuplicatedObjectException;

    ArrayList<AppServices> getAppServices();

    void linkService(Integer idservice, Integer idopportunity) throws DuplicatedObjectException;
}
