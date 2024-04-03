import Application.SearchEngineManager;
import Application.YourApplication;
import Application.service.GetDataImpl;
import Application.utils.ScannerWrapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testApplication {

    @Autowired
    private SearchEngineManager searchEngineManager;

    @Test
    public void testStartOfApplication() {
        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        doNothing().when(searchEngineManager).runSearchEngine();
        // When / Then
        try{
            YourApplication.main(new String[0]);
        }catch (Exception e){
         System.out.println("No exceptions should have been thrown");
        }
    }
    @Test
    public void testMainMethod() {
        // Mock the Spring context
        ConfigurableApplicationContext context = Mockito.mock(ConfigurableApplicationContext.class);

        // Mock the search engine manager
        SearchEngineManager searchEngineManager = Mockito.mock(SearchEngineManager.class);

        // Mock the context.getBean() method to return the mock search engine manager
        when(context.getBean(SearchEngineManager.class)).thenReturn(searchEngineManager);

        // Mock the runSearchEngine() method
        doNothing().when(searchEngineManager).runSearchEngine();

        // Call the main method
        YourApplication.main(new String[]{});

        // Verify that the context is closed
        verify(context).close();

        // Verify that no threads are running
        // (Note: This part may be challenging to test thoroughly and may require more complex mocking)
    }

    @Test
    public void testtMainMethod() {
        // Mocking the ConfigurableApplicationContext
        ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);

        // Mocking the SearchEngineManager
        SearchEngineManager searchEngineManager = mock(SearchEngineManager.class);

        // Mocking the SpringApplication
        SpringApplication springApplication = mock(SpringApplication.class);
        Mockito.doReturn(context).when(springApplication).run(ArgumentMatchers.eq(YourApplication.class), ArgumentMatchers.any());

        // Mocking the bean retrieval from the Spring context
        Mockito.doReturn(searchEngineManager).when(context).getBean(SearchEngineManager.class);

        // Mocking the runSearchEngine method to return false to exit the loop
        Mockito.doReturn(false).when(searchEngineManager).getRestartSearchEngine();

        // Call the main method indirectly by invoking the static method
        YourApplication.main(new String[]{});

        // Verifying that SpringApplication.run is called once
        verify(springApplication, times(1)).run(YourApplication.class, new String[]{});

        // Verifying that context.close is called once
        verify(context, times(1)).close();

        // Verifying that searchEngineManager.runSearchEngine is called at least once
        verify(searchEngineManager, atLeastOnce()).runSearchEngine();
    }

}
