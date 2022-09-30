package model.mo;

import java.sql.Timestamp;

public class LoggerTab {

    private Integer idUser;
    private java.sql.Timestamp logTime;
    private String actionType;
    private String actionTable;
    private Integer actionRecordId;

    // 1-N
    private User user;

    private Integer idLogger;

    public Integer getIdLogger() {
        return idLogger;
    }

    public void setIdLogger(Integer idLogger) {
        this.idLogger = idLogger;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Timestamp getLogTime() {
        return logTime;
    }

    public void setLogTime(Timestamp logTime) {
        this.logTime = logTime;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionTable() {
        return actionTable;
    }

    public void setActionTable(String actionTable) {
        this.actionTable = actionTable;
    }

    public Integer getActionRecordId() {
        return actionRecordId;
    }

    public void setActionRecordId(Integer actionRecordId) {
        this.actionRecordId = actionRecordId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
