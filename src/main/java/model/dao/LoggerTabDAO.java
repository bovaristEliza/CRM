package model.dao;

import model.dao.exception.DuplicatedObjectException;
import model.mo.LoggerTab;
import model.mo.User;

import java.util.ArrayList;

public interface LoggerTabDAO{

    public LoggerTab insert(
            Integer idUser,
            java.sql.Timestamp logTime,
            String actionType,
            String actionTable,
            Integer actionRecordId
    );

    ArrayList<LoggerTab> getLogs();
}
