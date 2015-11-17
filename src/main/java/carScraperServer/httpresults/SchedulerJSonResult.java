package carScraperServer.httpresults;


public class SchedulerJSonResult {
    private String message;
    private boolean isError;

    public SchedulerJSonResult(String message) {
         this(message, false);
    }

    public SchedulerJSonResult(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return isError;
    }
}
