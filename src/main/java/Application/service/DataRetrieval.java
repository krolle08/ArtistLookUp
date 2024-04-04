package Application.service;
/**
 * Interface responsible for retrieving data based on user input.
 */
public interface DataRetrieval {

    /**
     * Runs the process of retrieving data based on user input.
     */
    void run();
    /**
     * Runs the process of printing the response
     */
    void printJsonResponse(String jsonResponse);
}

