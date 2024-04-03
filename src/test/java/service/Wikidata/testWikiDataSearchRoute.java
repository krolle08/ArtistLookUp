package service.Wikidata;

import Application.YourApplication;
import Application.api.WikidataSearchRoute;
import Application.service.ArtistContainer.WikiInfoObj;
import Application.utils.RestTempUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testWikiDataSearchRoute {

    @Autowired
    private WikidataSearchRoute wikidataSearchRoute;

    @Test
    public void testWikiDataEndpoint_Nirvana() {
        // Given
        String nirvanaTerm = "Q11649";
        String succescriteria = "Nirvana (band)";

        WikiInfoObj wikiInfoObj = new WikiInfoObj(nirvanaTerm, null);

        // When
        try {
            wikidataSearchRoute.getWikidataForArtist(wikiInfoObj);
        } catch (URISyntaxException e) {
            e.getStackTrace();
        } catch (JsonProcessingException e) {
            e.getStackTrace();
        }

        // Then
        assertThat(RestTempUtil.decodeString(wikiInfoObj.getWikipedia())).isEqualTo(succescriteria);
        assertThat(wikiInfoObj.getWikiDataStatuccode()).isEqualTo("200");
    }

    @Test
    public void testRateLimitsResponse(){
        // Given
        String nirvanaTerm = "Q11649";
        String bandName = "Nirvana (band)";
        String responseBody = responseWithRateLimits();
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        String apiUrl = "https://wikidata.org/w/api.php?action=wbgetentities&format=json&ids=Q11649&props=sitelinks";


        // When
        ResponseEntity<String> result = null;
        try {
            result = WikidataSearchRoute.handleResponse(responseEntity, apiUrl);
        } catch (URISyntaxException e) {
            e.getStackTrace();
        }
        JsonObject jsonObject = JsonParser.parseString(result.getBody()).getAsJsonObject();

        // Then
        Assertions.assertTrue(jsonObject.get("entities").getAsJsonObject().get(nirvanaTerm).getAsJsonObject().get("id")
                .getAsJsonPrimitive().getAsString().equals(nirvanaTerm));
        Assertions.assertTrue(Objects.equals(jsonObject.get("entities").getAsJsonObject().get(nirvanaTerm).getAsJsonObject()
                .get("sitelinks").getAsJsonObject().get("enwiki").getAsJsonObject().get("title").getAsJsonPrimitive()
                .getAsString(), bandName));
    }

    public String responseWithRateLimits() {
        return "{\n" +
                "  \"batchcomplete\": \"\",\n" +
                "  \"query\": {\n" +
                "    \"userinfo\": {\n" +
                "      \"id\": 0,\n" +
                "      \"name\": \"194.62.169.80\",\n" +
                "      \"anon\": \"\",\n" +
                "      \"ratelimits\": {\n" +
                "        \"edit\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 8,\n" +
                "            \"seconds\": 60\n" +
                "          }\n" +
                "        },\n" +
                "        \"badcaptcha\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 15,\n" +
                "            \"seconds\": 60\n" +
                "          }\n" +
                "        },\n" +
                "        \"mailpassword\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 5,\n" +
                "            \"seconds\": 3600\n" +
                "          }\n" +
                "        },\n" +
                "        \"sendemail\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 5,\n" +
                "            \"seconds\": 86400\n" +
                "          }\n" +
                "        },\n" +
                "        \"purge\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 30,\n" +
                "            \"seconds\": 60\n" +
                "          }\n" +
                "        },\n" +
                "        \"linkpurge\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 30,\n" +
                "            \"seconds\": 60\n" +
                "          }\n" +
                "        },\n" +
                "        \"renderfile\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 700,\n" +
                "            \"seconds\": 30\n" +
                "          }\n" +
                "        },\n" +
                "        \"renderfile-nonstandard\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 70,\n" +
                "            \"seconds\": 30\n" +
                "          }\n" +
                "        },\n" +
                "        \"cxsave\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 10,\n" +
                "            \"seconds\": 30\n" +
                "          }\n" +
                "        },\n" +
                "        \"urlshortcode\": {\n" +
                "          \"ip\": {\n" +
                "            \"hits\": 10,\n" +
                "            \"seconds\": 120\n" +
                "          }\n" +
                "        },\n" +
                "        \"pagetriage-mark-action\": {\n" +
                "          \"anon\": {\n" +
                "            \"hits\": 1,\n" +
                "            \"seconds\": 3\n" +
                "          }\n" +
                "        },\n" +
                "        \"pagetriage-tagging-action\": {\n" +
                "          \"anon\": {\n" +
                "            \"hits\": 1,\n" +
                "            \"seconds\": 10\n" +
                "          }\n" +
                "        },\n" +
                "        \"growthexperimentsuserimpacthandler\": {\n" +
                "          \"anon\": {\n" +
                "            \"hits\": 5,\n" +
                "            \"seconds\": 86400\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
