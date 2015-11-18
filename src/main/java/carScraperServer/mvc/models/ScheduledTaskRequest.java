package carScraperServer.mvc.models;

import org.apache.commons.lang3.StringUtils;

public class ScheduledTaskRequest {
    private String startDate;
    private String message;
    private String textEmailBoth;
    private String cell;
    private String emailTo;
    private String emailFrom;
    private String frequency;
    private String expirationDate;
    private String dealerId;
    private String locationId;
    private String source;
    private String sourceId;

    public String validate() {
        if (StringUtils.isEmpty(startDate)) {
            return "startDate param is empty";
        }

        if (StringUtils.isEmpty(message)) {
            return "message param is empty";
        }

        if (StringUtils.isEmpty(textEmailBoth)) {
            return "textEmailBoth param is empty";
        }

        if (StringUtils.isEmpty(cell)) {
            return "cell param is empty";
        }

        if (StringUtils.isEmpty(emailTo)) {
            return "emailTo param is empty";
        }

        if (StringUtils.isEmpty(emailFrom)) {
            return "emailFrom param is empty";
        }

        if (StringUtils.isEmpty(frequency)) {
            return "frequency param is empty";
        }

        if (StringUtils.isEmpty(expirationDate)) {
            return "expirationDate param is empty";
        }

        if (StringUtils.isEmpty(dealerId)) {
            return "dealerId param is empty";
        }

        if (StringUtils.isEmpty(locationId)) {
            return "locationId param is empty";
        }

        if (StringUtils.isEmpty(source)) {
            return "source param is empty";
        }

        if (StringUtils.isEmpty(sourceId)) {
            return "sourceId param is empty";
        }
        return null;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTextEmailBoth() {
        return textEmailBoth;
    }

    public void setTextEmailBoth(String textEmailBoth) {
        this.textEmailBoth = textEmailBoth;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
