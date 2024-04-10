package api;

import Application.Application;
import Application.api.WikipediaSearchRoute;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testWikiPediaSearchRoute {
    @Autowired
    private WikipediaSearchRoute wikipediaSearchRoute;

    @Test
    public void testGetUri_Succes() {
        // Given
        String wikiPediaSearchTerm = RestTempUtil.encodeIfNeeded("Nirvana (band)");
        URI result;

        // When
        result = wikipediaSearchRoute.getUri(wikiPediaSearchTerm);

        // Then
        Assertions.assertNotNull(result); // Assert that URI is not null
        Assertions.assertEquals("https", result.getScheme()); // Assert scheme
        Assertions.assertEquals("en.wikipedia.org", result.getHost()); // Assert host
        Assertions.assertEquals("/w/api.php", result.getPath()); // Assert path
    }

    @Test
    public void testDoGetResponse_Success() {
        //Given
        String searchTerm = "Nirvana (band)";

        //When

        ResponseEntity<String> result = wikipediaSearchRoute.doGetResponse(searchTerm);

        //Then
        assertNotNull(result.toString(), "Response entity should not be null");
        assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should have OK status");
        assertFalse(result.getBody().isEmpty(), "Response body should not be empty");


        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(result.getBody());
            assertTrue(rootNode.has("query"), "Response should contain 'query' field");
            JsonNode pagesNode = rootNode.path("query").path("pages");
            assertTrue(pagesNode.isObject(), "Pages node should be an object");
            boolean extractPresent = false;
            for (JsonNode pageNode : pagesNode) {
                if (pageNode.has("extract")) {
                    extractPresent = true;
                    break;
                }
            }
            assertTrue(extractPresent, "At least one page should contain 'extract' field");
        } catch (Exception e) {
            fail("Error reading JSON response: " + e.getMessage());
        }
    }

    @Test
    public void testNegativeResponse_Error() {
        //Given
        String uri = "Nirvana (test)";

        // When

        ResponseEntity<String> result = wikipediaSearchRoute.doGetResponse(uri);

        //Then
        assertNotNull(result.getBody(), "Response entity should not be null");
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should have OK status");

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(result.getBody());
            assertTrue(rootNode.has("query"), "Response should contain 'query' field");

            JsonNode pagesNode = rootNode.path("query").path("pages");
            assertTrue(pagesNode.isObject(), "Pages node should be an object");

            // Check if the "missing" field is present
            if (pagesNode.has("-1")) {
                JsonNode missingNode = pagesNode.path("-1");
                assertTrue(missingNode.has("missing"), "Page should be missing");
            } else {
                fail("Expected page not found");
            }
        } catch (Exception e) {
            fail("Error reading JSON response: " + e.getMessage());
        }
    }
}
