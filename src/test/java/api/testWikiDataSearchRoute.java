package api;

import Application.Application;
import Application.api.WikidataSearchRoute;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testWikiDataSearchRoute {

    @Autowired
    private WikidataSearchRoute wikidataSearchRoute;

    @Test
    public void testRateLimitsResponse() {
        // Given
        String nirvanaTerm = "Q11649";
        String bandName = "Nirvana (band)";
        String responseBody = responseWithRateLimits();
        ResponseEntity<String> responseEntity = ResponseEntity.ok(responseBody);
        String apiUrl = "https://wikidata.org/w/api.php?action=wbgetentities&format=json&ids=Q11649&props=sitelinks";

        // When
        ResponseEntity<String> result = null;
        result = wikidataSearchRoute.handleResponse(responseEntity, apiUrl);
        JsonObject jsonObject = JsonParser.parseString(result.getBody()).getAsJsonObject();

        // Then
        assertFalse(jsonObject.isJsonNull());
        Assertions.assertEquals(jsonObject.get("entities").getAsJsonObject().get(nirvanaTerm).getAsJsonObject().get("id")
                .getAsJsonPrimitive().getAsString(), nirvanaTerm);
        Assertions.assertEquals(jsonObject.get("entities").getAsJsonObject().get(nirvanaTerm).getAsJsonObject()
                .get("sitelinks").getAsJsonObject().get("enwiki").getAsJsonObject().get("title").getAsJsonPrimitive()
                .getAsString(), bandName);
    }

    @Test
    public void testGetUri_Succes() {
        // Given
        String wikidataSearchTerm = "Q11649";
        URI result;

        // When
        result = wikidataSearchRoute.getUri(wikidataSearchTerm);

        // Then
        Assertions.assertNotNull(result); // Assert that URI is not null
        Assertions.assertEquals("https", result.getScheme()); // Assert scheme
        Assertions.assertEquals("wikidata.org", result.getHost()); // Assert host
        Assertions.assertEquals("/w/api.php", result.getPath()); // Assert path


    }

    @Test
    public void testPositiveResponse_Succes() {
        //Given
        String wikidataSearchTerm = "Q11649";

        // When
        ResponseEntity<String> result = null;
        result = wikidataSearchRoute.doGetResponse(wikidataSearchTerm);

        // Then
        assertFalse(result.getBody().isEmpty());
    }

    @Test
    public void testNegativeResponse_Error() throws URISyntaxException  {
        //Given
        String wikidataSearchTerm = "Q00000";

        // When

        ResponseEntity<String> result = wikidataSearchRoute.doGetResponse(wikidataSearchTerm);

        //Then
        assertNotNull(result.getBody(), "Response entity should not be null");
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode(), "Response should have OK status");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(result.getBody());
        } catch (Exception e) {
            fail("Error reading JSON response: " + e.getMessage());
            return; // Return to avoid NullPointerException below
        }

        assertTrue(rootNode.has("error"), "Response should contain 'error' field");
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

    public String getResponseWithResult() {
        return "{\"entities\":{\"Q11649\":{\"type\":\"item\",\"id\":\"Q11649\",\"sitelinks\":{\"afwiki\":{\"site\":\"afwiki\",\"title\":\"Nirvana (rock-groep)\",\"badges\":[]},\"angwiki\":{\"site\":\"angwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"anwiki\":{\"site\":\"anwiki\",\"title\":\"Nirvana (grupo)\",\"badges\":[]},\"arwiki\":{\"site\":\"arwiki\",\"title\":\"\\u0646\\u064a\\u0631\\u0641\\u0627\\u0646\\u0627 (\\u0641\\u0631\\u0642\\u0629 \\u0645\\u0648\\u0633\\u064a\\u0642\\u064a\\u0629)\",\"badges\":[]},\"arzwiki\":{\"site\":\"arzwiki\",\"title\":\"\\u0646\\u064a\\u0631\\u0641\\u0627\\u0646\\u0627 (\\u0641\\u0631\\u0642\\u0629 \\u0645\\u0648\\u0633\\u064a\\u0642\\u0649)\",\"badges\":[]},\"astwiki\":{\"site\":\"astwiki\",\"title\":\"Nirvana (grupu)\",\"badges\":[]},\"azbwiki\":{\"site\":\"azbwiki\",\"title\":\"\\u0646\\u06cc\\u0631\\u0648\\u0627\\u0646\\u0627 (\\u0645\\u0648\\u0632\\u06cc\\u06a9 \\u0642\\u0631\\u0648\\u067e\\u0648)\",\"badges\":[]},\"azwiki\":{\"site\":\"azwiki\",\"title\":\"Nirvana (qrup)\",\"badges\":[]},\"barwiki\":{\"site\":\"barwiki\",\"title\":\"Nirvana (Band)\",\"badges\":[]},\"be_x_oldwiki\":{\"site\":\"be_x_oldwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"bewiki\":{\"site\":\"bewiki\",\"title\":\"Nirvana\",\"badges\":[]},\"bgwiki\":{\"site\":\"bgwiki\",\"title\":\"\\u041d\\u0438\\u0440\\u0432\\u0430\\u043d\\u0430 (\\u0433\\u0440\\u0443\\u043f\\u0430)\",\"badges\":[]},\"bnwiki\":{\"site\":\"bnwiki\",\"title\":\"\\u09a8\\u09bf\\u09b0\\u09ad\\u09be\\u09a8\\u09be\",\"badges\":[]},\"brwiki\":{\"site\":\"brwiki\",\"title\":\"Nirvana (strollad sonerezh)\",\"badges\":[]},\"bswiki\":{\"site\":\"bswiki\",\"title\":\"Nirvana (grupa)\",\"badges\":[\"Q17437798\"]},\"cawiki\":{\"site\":\"cawiki\",\"title\":\"Nirvana (grup de m\\u00fasica)\",\"badges\":[]},\"ckbwiki\":{\"site\":\"ckbwiki\",\"title\":\"\\u0646\\u06cc\\u0631\\u06a4\\u0627\\u0646\\u0627 (\\u06af\\u0631\\u0648\\u0648\\u067e)\",\"badges\":[]},\"cowiki\":{\"site\":\"cowiki\",\"title\":\"Nirvana (gruppu)\",\"badges\":[]},\"csbwiki\":{\"site\":\"csbwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"cswiki\":{\"site\":\"cswiki\",\"title\":\"Nirvana\",\"badges\":[\"Q17437796\"]},\"cswikiquote\":{\"site\":\"cswikiquote\",\"title\":\"Nirvana\",\"badges\":[]},\"cywiki\":{\"site\":\"cywiki\",\"title\":\"Nirvana\",\"badges\":[]},\"dawiki\":{\"site\":\"dawiki\",\"title\":\"Nirvana (band)\",\"badges\":[]},\"dewiki\":{\"site\":\"dewiki\",\"title\":\"Nirvana (US-amerikanische Band)\",\"badges\":[]},\"diqwiki\":{\"site\":\"diqwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"elwiki\":{\"site\":\"elwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"emlwiki\":{\"site\":\"emlwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"enwiki\":{\"site\":\"enwiki\",\"title\":\"Nirvana (band)\",\"badges\":[\"Q17437796\"]},\"enwikiquote\":{\"site\":\"enwikiquote\",\"title\":\"Nirvana (band)\",\"badges\":[]},\"eowiki\":{\"site\":\"eowiki\",\"title\":\"Nirvana\",\"badges\":[]},\"eswiki\":{\"site\":\"eswiki\",\"title\":\"Nirvana (banda)\",\"badges\":[]},\"etwiki\":{\"site\":\"etwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"euwiki\":{\"site\":\"euwiki\",\"title\":\"Nirvana (musika taldea)\",\"badges\":[]},\"extwiki\":{\"site\":\"extwiki\",\"title\":\"Nirvana (banda)\",\"badges\":[]},\"fawiki\":{\"site\":\"fawiki\",\"title\":\"\\u0646\\u06cc\\u0631\\u0648\\u0627\\u0646\\u0627 (\\u06af\\u0631\\u0648\\u0647 \\u0645\\u0648\\u0633\\u06cc\\u0642\\u06cc)\",\"badges\":[]},\"fiwiki\":{\"site\":\"fiwiki\",\"title\":\"Nirvana (yhtye)\",\"badges\":[]},\"frwiki\":{\"site\":\"frwiki\",\"title\":\"Nirvana (groupe)\",\"badges\":[\"Q17437796\"]},\"furwiki\":{\"site\":\"furwiki\",\"title\":\"Nirvana (clape music\\u00e2l)\",\"badges\":[]},\"fywiki\":{\"site\":\"fywiki\",\"title\":\"Nirvana (Amerikaanske band)\",\"badges\":[]},\"gawiki\":{\"site\":\"gawiki\",\"title\":\"Nirvana\",\"badges\":[]},\"glwiki\":{\"site\":\"glwiki\",\"title\":\"Nirvana (grupo musical)\",\"badges\":[]},\"gotwiki\":{\"site\":\"gotwiki\",\"title\":\"\\ud800\\udf3d\\ud800\\udf39\\ud800\\udf42\\ud800\\udf31\\ud800\\udf30\\ud800\\udf3d\\ud800\\udf30\",\"badges\":[]},\"hewiki\":{\"site\":\"hewiki\",\"title\":\"\\u05e0\\u05d9\\u05e8\\u05d5\\u05d5\\u05e0\\u05d4 (\\u05dc\\u05d4\\u05e7\\u05d4)\",\"badges\":[]},\"hewikiquote\":{\"site\":\"hewikiquote\",\"title\":\"\\u05e0\\u05d9\\u05e8\\u05d5\\u05d5\\u05e0\\u05d4 (\\u05dc\\u05d4\\u05e7\\u05d4)\",\"badges\":[]},\"hrwiki\":{\"site\":\"hrwiki\",\"title\":\"Nirvana (sastav)\",\"badges\":[]},\"hsbwiki\":{\"site\":\"hsbwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"huwiki\":{\"site\":\"huwiki\",\"title\":\"Nirvana (egy\\u00fcttes)\",\"badges\":[]},\"hywiki\":{\"site\":\"hywiki\",\"title\":\"Nirvana\",\"badges\":[]},\"idwiki\":{\"site\":\"idwiki\",\"title\":\"Nirvana (grup musik)\",\"badges\":[]},\"iowiki\":{\"site\":\"iowiki\",\"title\":\"Nirvana\",\"badges\":[]},\"iswiki\":{\"site\":\"iswiki\",\"title\":\"Nirvana (hlj\\u00f3msveit)\",\"badges\":[]},\"itwiki\":{\"site\":\"itwiki\",\"title\":\"Nirvana (gruppo musicale)\",\"badges\":[]},\"itwikiquote\":{\"site\":\"itwikiquote\",\"title\":\"Nirvana (gruppo musicale)\",\"badges\":[]},\"jawiki\":{\"site\":\"jawiki\",\"title\":\"\\u30cb\\u30eb\\u30f4\\u30a1\\u30fc\\u30ca (\\u30a2\\u30e1\\u30ea\\u30ab\\u5408\\u8846\\u56fd\\u306e\\u30d0\\u30f3\\u30c9)\",\"badges\":[]},\"jvwiki\":{\"site\":\"jvwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"kawiki\":{\"site\":\"kawiki\",\"title\":\"\\u10dc\\u10d8\\u10e0\\u10d5\\u10d0\\u10dc\\u10d0 (\\u10ef\\u10d2\\u10e3\\u10e4\\u10d8)\",\"badges\":[]},\"kkwiki\":{\"site\":\"kkwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"kmwiki\":{\"site\":\"kmwiki\",\"title\":\"\\u1793\\u17be\\u179c\\u17c9\\u17b6\\u178e\\u17b6\",\"badges\":[]},\"kowiki\":{\"site\":\"kowiki\",\"title\":\"\\ub108\\ubc14\\ub098\",\"badges\":[]},\"kwwiki\":{\"site\":\"kwwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"kywiki\":{\"site\":\"kywiki\",\"title\":\"Nirvana\",\"badges\":[]},\"lawiki\":{\"site\":\"lawiki\",\"title\":\"Nirvana (grex)\",\"badges\":[]},\"ltgwiki\":{\"site\":\"ltgwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"ltwiki\":{\"site\":\"ltwiki\",\"title\":\"Nirvana (amerikie\\u010di\\u0173 grup\\u0117)\",\"badges\":[]},\"lvwiki\":{\"site\":\"lvwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"mgwiki\":{\"site\":\"mgwiki\",\"title\":\"Nirvana (tarika)\",\"badges\":[]},\"mkwiki\":{\"site\":\"mkwiki\",\"title\":\"\\u041d\\u0438\\u0440\\u0432\\u0430\\u043d\\u0430 (\\u043c\\u0443\\u0437\\u0438\\u0447\\u043a\\u0430 \\u0433\\u0440\\u0443\\u043f\\u0430)\",\"badges\":[]},\"mlwiki\":{\"site\":\"mlwiki\",\"title\":\"\\u0d28\\u0d3f\\u0d7c\\u0d35\\u0d3e\\u0d23\",\"badges\":[]},\"mnwiki\":{\"site\":\"mnwiki\",\"title\":\"\\u041d\\u0438\\u0440\\u0432\\u0430\\u043d\\u0430 (\\u0445\\u0430\\u043c\\u0442\\u043b\\u0430\\u0433)\",\"badges\":[]},\"mswiki\":{\"site\":\"mswiki\",\"title\":\"Nirvana (kugiran)\",\"badges\":[]},\"nahwiki\":{\"site\":\"nahwiki\",\"title\":\"Nirvana (tlacu\\u012bcaliztli)\",\"badges\":[]},\"ndswiki\":{\"site\":\"ndswiki\",\"title\":\"Nirvana (Grupp)\",\"badges\":[]},\"newiki\":{\"site\":\"newiki\",\"title\":\"\\u0928\\u093f\\u0930\\u094d\\u092d\\u093e\\u0928\\u093e\",\"badges\":[]},\"nlwiki\":{\"site\":\"nlwiki\",\"title\":\"Nirvana (Amerikaanse band)\",\"badges\":[]},\"nnwiki\":{\"site\":\"nnwiki\",\"title\":\"Musikkgruppa Nirvana\",\"badges\":[]},\"nowiki\":{\"site\":\"nowiki\",\"title\":\"Nirvana (band)\",\"badges\":[]},\"ocwiki\":{\"site\":\"ocwiki\",\"title\":\"Nirvana (grop)\",\"badges\":[]},\"pawiki\":{\"site\":\"pawiki\",\"title\":\"\\u0a28\\u0a3f\\u0a30\\u0a35\\u0a3e\\u0a28\\u0a3e (\\u0a2c\\u0a48\\u0a02\\u0a21)\",\"badges\":[]},\"pcdwiki\":{\"site\":\"pcdwiki\",\"title\":\"Nirvana (grope)\",\"badges\":[]},\"pdcwiki\":{\"site\":\"pdcwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"plwiki\":{\"site\":\"plwiki\",\"title\":\"Nirvana\",\"badges\":[\"Q17437798\"]},\"plwikiquote\":{\"site\":\"plwikiquote\",\"title\":\"Nirvana\",\"badges\":[]},\"pmswiki\":{\"site\":\"pmswiki\",\"title\":\"Nirvana\",\"badges\":[]},\"ptwiki\":{\"site\":\"ptwiki\",\"title\":\"Nirvana (banda)\",\"badges\":[]},\"ptwikiquote\":{\"site\":\"ptwikiquote\",\"title\":\"Nirvana (banda)\",\"badges\":[]},\"rowiki\":{\"site\":\"rowiki\",\"title\":\"Nirvana (forma\\u021bie)\",\"badges\":[]},\"ruwiki\":{\"site\":\"ruwiki\",\"title\":\"Nirvana\",\"badges\":[\"Q17437798\"]},\"ruwikinews\":{\"site\":\"ruwikinews\",\"title\":\"\\u041a\\u0430\\u0442\\u0435\\u0433\\u043e\\u0440\\u0438\\u044f:Nirvana\",\"badges\":[]},\"ruwikiquote\":{\"site\":\"ruwikiquote\",\"title\":\"Nirvana\",\"badges\":[]},\"scnwiki\":{\"site\":\"scnwiki\",\"title\":\"Nirvana (gruppu musicali)\",\"badges\":[]},\"scowiki\":{\"site\":\"scowiki\",\"title\":\"Nirvana (baund)\",\"badges\":[]},\"scwiki\":{\"site\":\"scwiki\",\"title\":\"Nirvana (grupu musicale)\",\"badges\":[]},\"shwiki\":{\"site\":\"shwiki\",\"title\":\"Nirvana (bend)\",\"badges\":[]},\"simplewiki\":{\"site\":\"simplewiki\",\"title\":\"Nirvana (band)\",\"badges\":[]},\"skwiki\":{\"site\":\"skwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"slwiki\":{\"site\":\"slwiki\",\"title\":\"Nirvana (glasbena skupina)\",\"badges\":[]},\"sqwiki\":{\"site\":\"sqwiki\",\"title\":\"Nirvana (grup muzikor)\",\"badges\":[]},\"srwiki\":{\"site\":\"srwiki\",\"title\":\"Nirvana (\\u043c\\u0443\\u0437\\u0438\\u0447\\u043a\\u0430 \\u0433\\u0440\\u0443\\u043f\\u0430)\",\"badges\":[]},\"suwiki\":{\"site\":\"suwiki\",\"title\":\"Nirvana (grup musik)\",\"badges\":[]},\"svwiki\":{\"site\":\"svwiki\",\"title\":\"Nirvana (musikgrupp)\",\"badges\":[\"Q17437796\"]},\"szlwiki\":{\"site\":\"szlwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"thwiki\":{\"site\":\"thwiki\",\"title\":\"\\u0e40\\u0e19\\u0e2d\\u0e23\\u0e4c\\u0e27\\u0e32\\u0e19\\u0e32\",\"badges\":[]},\"tlwiki\":{\"site\":\"tlwiki\",\"title\":\"Nirvana (banda)\",\"badges\":[]},\"trwiki\":{\"site\":\"trwiki\",\"title\":\"Nirvana (m\\u00fczik grubu)\",\"badges\":[]},\"ttwiki\":{\"site\":\"ttwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"ukwiki\":{\"site\":\"ukwiki\",\"title\":\"Nirvana\",\"badges\":[]},\"uzwiki\":{\"site\":\"uzwiki\",\"title\":\"Nirvana (guruh)\",\"badges\":[\"Q17437798\"]},\"viwiki\":{\"site\":\"viwiki\",\"title\":\"Nirvana (ban nh\\u1ea1c)\",\"badges\":[]},\"warwiki\":{\"site\":\"warwiki\",\"title\":\"Nirvana (banda)\",\"badges\":[]},\"wuuwiki\":{\"site\":\"wuuwiki\",\"title\":\"\\u6d85\\u69c3\\u4e50\\u961f\",\"badges\":[]},\"xmfwiki\":{\"site\":\"xmfwiki\",\"title\":\"\\u10dc\\u10d8\\u10e0\\u10d5\\u10d0\\u10dc\\u10d0 (\\u10d1\\u10e3\\u10dc\\u10d0)\",\"badges\":[]},\"zh_yuewiki\":{\"site\":\"zh_yuewiki\",\"title\":\"\\u6d85\\u69c3\\u6a02\\u968a\",\"badges\":[]},\"zhwiki\":{\"site\":\"zhwiki\",\"title\":\"\\u6d85\\u69c3\\u4e50\\u961f\",\"badges\":[]}}}},\"success\":1}";
    }

}
