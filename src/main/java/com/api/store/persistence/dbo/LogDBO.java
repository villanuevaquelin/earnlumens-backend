package com.api.store.persistence.dbo;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "logs")
public class LogDBO {
    @Id
    private String id;

    @NotBlank
    private String alog;

    @NotBlank
    private String errorLog;

    @NotBlank
    private String lastLoginlog;

    @NotBlank
    private String dbFaultlog;

    @NotBlank
    private String exceptionLog;

    @NotBlank
    private String connectionErrorlog;

    @NotBlank
    private String lastIplog;

    @NotBlank
    private String browserLanguageLog;

    @NotBlank
    private String timeConnectionLog;

    public String getAlog() {
        return alog;
    }

    public void setAlog(String alog) {
        this.alog = alog;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getLastLoginlog() {
        return lastLoginlog;
    }

    public void setLastLoginlog(String lastLoginlog) {
        this.lastLoginlog = lastLoginlog;
    }

    public String getDbFaultlog() {
        return dbFaultlog;
    }

    public void setDbFaultlog(String dbFaultlog) {
        this.dbFaultlog = dbFaultlog;
    }

    public String getExceptionLog() {
        return exceptionLog;
    }

    public void setExceptionLog(String exceptionLog) {
        this.exceptionLog = exceptionLog;
    }

    public String getConnectionErrorlog() {
        return connectionErrorlog;
    }

    public void setConnectionErrorlog(String connectionErrorlog) {
        this.connectionErrorlog = connectionErrorlog;
    }

    public String getLastIplog() {
        return lastIplog;
    }

    public void setLastIplog(String lastIplog) {
        this.lastIplog = lastIplog;
    }

    public String getBrowserLanguageLog() {
        return browserLanguageLog;
    }

    public void setBrowserLanguageLog(String browserLanguageLog) {
        this.browserLanguageLog = browserLanguageLog;
    }

    public String getTimeConnectionLog() {
        return timeConnectionLog;
    }

    public void setTimeConnectionLog(String timeConnectionLog) {
        this.timeConnectionLog = timeConnectionLog;
    }
}
