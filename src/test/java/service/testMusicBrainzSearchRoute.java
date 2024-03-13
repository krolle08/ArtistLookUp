package service;

import Application.YourApplication;
import Application.api.MusicBrainzSearchRoute;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzSearchRoute {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private MusicBrainzSearchRoute musicBrainzSearchRoute;

    private String response ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<metadata xmlns=\"http://musicbrainz.org/ns/mmd-2.0#\">\n" +
            "    <artist id=\"5b11f4ce-a62d-471e-81fc-a69a8278c7da\" type=\"Group\" type-id=\"e431f5f6-b5d2-343d-8b36-72607fffb74b\">\n" +
            "        <name>Nirvana</name>\n" +
            "        <sort-name>Nirvana</sort-name>\n" +
            "        <disambiguation>1980s~1990s US grunge band</disambiguation>\n" +
            "        <isni-list>\n" +
            "            <isni>0000000123486830</isni>\n" +
            "            <isni>0000000123487390</isni>\n" +
            "        </isni-list>\n" +
            "        <country>US</country>\n" +
            "        <area id=\"489ce91b-6658-3307-9877-795b68554c98\">\n" +
            "            <name>United States</name>\n" +
            "            <sort-name>United States</sort-name>\n" +
            "            <iso-3166-1-code-list>\n" +
            "                <iso-3166-1-code>US</iso-3166-1-code>\n" +
            "            </iso-3166-1-code-list>\n" +
            "        </area>\n" +
            "        <begin-area id=\"a640b45c-c173-49b1-8030-973603e895b5\">\n" +
            "            <name>Aberdeen</name>\n" +
            "            <sort-name>Aberdeen</sort-name>\n" +
            "        </begin-area>\n" +
            "        <life-span>\n" +
            "            <begin>1987</begin>\n" +
            "            <end>1994-04-05</end>\n" +
            "            <ended>true</ended>\n" +
            "        </life-span>\n" +
            "        <alias-list count=\"3\">\n" +
            "            <alias locale=\"en\" sort-name=\"Nirvana\" type=\"Artist name\" type-id=\"894afba6-2816-3c24-8072-eadb66bd04bc\" primary=\"primary\">Nirvana</alias>\n" +
            "            <alias sort-name=\"Nirvana US\">Nirvana US</alias>\n" +
            "            <alias locale=\"ja\" sort-name=\"ニルヴァーナ\" type=\"Artist name\" type-id=\"894afba6-2816-3c24-8072-eadb66bd04bc\" primary=\"primary\">ニルヴァーナ</alias>\n" +
            "        </alias-list>\n" +
            "    </artist>\n" +
            "</metadata>";
    @Test
    public void testHelloWorldEndpoint() throws URISyntaxException {
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        String rest = musicBrainzSearchRoute.searchArtist(nirvana);
        assertThat(rest).contains("Nirvana");
    }
}
