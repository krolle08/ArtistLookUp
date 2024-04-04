package service.wikipedia;

import Application.Application;
import Application.api.WikipediaSearchRoute;
import Application.service.Artist.WikiInfoObj;
import Application.service.Wikipedia.WikiPediaService;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testWikiPediaService {
    @Autowired
    private WikipediaSearchRoute wikipediaSearchRoute;
    @Autowired
    private WikiPediaService wikiPediaService;
    @Test
    public void test_WikipediaEndpoint(){
        //Given
        String nirvanatitle = "Nirvana (band)";
        String nirvanaURLQuery = RestTempUtil.encodeQueryString(nirvanatitle);
        WikiInfoObj wikiInfoObj = new WikiInfoObj(null, nirvanaURLQuery);

        //When
        try {
            wikiPediaService.getWikidataForArtist(wikiInfoObj);
        } catch (URISyntaxException | JsonProcessingException e) {
           System.out.println("Exception should not be thrown");
           fail();
        }

        //Then
        assertTrue(wikiInfoObj.getWikiPediaStatuccode().equals(HttpStatus.OK.value()));
        assertFalse(wikiInfoObj.getDescription().isEmpty());
    }
}
