package Application.service;
/**
 * Interface responsible for controlling the type and flow of data based on user input.
 */
public interface DataController {

    /**
     * Runs the process of retrieving data based on user input.
     */
    void run();
    /**
     * Runs the process of printing the response
     */
    void printJsonResponse(String jsonResponse);
}

