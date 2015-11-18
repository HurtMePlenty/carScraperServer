package carScraperServer.schedulerEntities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class ScheduledTask {

    @Id
    @GeneratedValue
    private Long id;
    private Date startDate;
    private String message;
    private Integer textEmailBoth;
    private String cell;
    private String emailTo;
    private String emailFrom;
    private Integer frequency;
    private Date expirationDate;
    private String dealerId;
    private String locationID;
    private String source;
    private String sourceID;
    private Date lastExecutionDate;
    private int totalExecutedTimes;

    public void executed(Date executionDate){
        this.lastExecutionDate = executionDate;
        this.totalExecutedTimes++;
    }

    public Long getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTextEmailBoth() {
        return textEmailBoth;
    }

    public void setTextEmailBoth(Integer textEmailBoth) {
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

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public Date getLastExecutionDate() {
        return lastExecutionDate;
    }

    @Override
    public String toString() {
        return "ScheduledTask{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", message='" + message + '\'' +
                ", textEmailBoth=" + textEmailBoth +
                ", cell='" + cell + '\'' +
                ", emailTo='" + emailTo + '\'' +
                ", emailFrom='" + emailFrom + '\'' +
                ", frequency=" + frequency +
                ", expirationDate=" + expirationDate +
                ", dealerId='" + dealerId + '\'' +
                ", locationID='" + locationID + '\'' +
                ", source='" + source + '\'' +
                ", sourceID='" + sourceID + '\'' +
                ", lastExecutionDate=" + lastExecutionDate +
                ", totalExecutedTimes=" + totalExecutedTimes +
                '}';
    }
}


/*


ID
StartDate (date)
Subject (string)
Message (memo or string)
Text/Email/Both ( Integer )
Cell
EmailTo
EmailFrom
Frequency (Number of days in Integer)
ExpirationDate
DealerID
LocationID
Source (DC, FN, PS, DNL, DNC)
SourceID (Record number at Source)

 */
