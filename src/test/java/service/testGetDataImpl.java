package service;
import Application.service.GetDataImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class testGetDataImpl {
    @Test
    public void testGetTypeOfSearch() {
        // Arrange
        String userInput = "2";
        InputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(inputStream); // Redirect System.in to use inputStream

        GetDataImpl getDataImpl = new GetDataImpl();

        // Act
        try {
            Map<String, String> searchParams = getDataImpl.getTypeOfSearch();

            // Assert
            Assertions.assertNotNull(searchParams);
            Assertions.assertEquals("Artist", searchParams.keySet().iterator().next());
            Assertions.assertEquals("SearchValue", searchParams.values().iterator().next());
        } catch (Exception e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        } finally {
            System.setIn(System.in); // Reset System.in
        }
    }
}
