package service.wikipedia;

import Application.Application;
import Application.service.Artist.WikiInfoObj;
import Application.service.Wiki.WikiPediaService;
import Application.utils.RestTempUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testWikiPediaService {

    @Autowired
    private WikiPediaService wikiPediaService;
    @Test
    public void testGetWikiepediaData_Succes(){
        //Given
        String nirvanatitle = "Nirvana (band)";
        String nirvanaURLQuery = RestTempUtil.encodeIfNeeded(nirvanatitle);
        WikiInfoObj wikiInfoObj = new WikiInfoObj(null, nirvanaURLQuery);

        //When
        wikiPediaService.getWikiPediadata(wikiInfoObj);

        //Then
        assertEquals((int) wikiInfoObj.getWikiPediaStatuccode(), HttpStatus.OK.value());
        assertFalse(wikiInfoObj.getDescription().isEmpty());
    }

    @Test
    public void testGetWikiepediaData_NoResponse(){
        //Given
        String nirvanatitle = "NO Response Given";
        String nirvanaURLQuery = RestTempUtil.encodeIfNeeded(nirvanatitle);
        WikiInfoObj wikiInfoObj = new WikiInfoObj(null, nirvanaURLQuery);

        //When
        wikiPediaService.getWikiPediadata(wikiInfoObj);

        //Then
        assertEquals((int) wikiInfoObj.getWikiPediaStatuccode(), HttpStatus.OK.value());
        assertTrue(wikiInfoObj.getDescription().equals("No description found"));
    }
    @Test
    public void testExtractDescription_Succes() {
        //Given
        WikiInfoObj wikiInfoObj = new WikiInfoObj("", "");
        ResponseEntity<String> response = ResponseEntity.ok().body(getResponseBody());
        String expectedSubstring = "<b>Nirvana</b>"; // Specify the substring expected to be present in the extract

        //When

        wikiPediaService.getDescription(response, wikiInfoObj);

        //Then
        assertTrue(wikiInfoObj.getDescription().contains(expectedSubstring));
    }

    private String getResponseBody(){
        return "{\"batchcomplete\":\"\",\"warnings\":{\"extracts\":{\"*\":\"HTML may be malformed and/or unbalanced and may omit inline images. Use at your own risk. Known problems are listed at https://www.mediawiki.org/wiki/Special:MyLanguage/Extension:TextExtracts#Caveats.\"}},\"query\":{\"pages\":{\"21231\":{\"pageid\":21231,\"ns\":0,\"title\":\"Nirvana (band)\",\"extract\":\"<p class=\\\"mw-empty-elt\\\">\\n\\n\\n\\n</p>\\n<p><b>Nirvana</b> was an American rock band formed in Aberdeen, Washington, in 1987. Founded by lead singer and guitarist Kurt Cobain and bassist Krist Novoselic, the band went through a succession of drummers, most notably Chad Channing, before recruiting Dave Grohl in 1990. Nirvana's success popularized alternative rock, and they were often referenced as the figurehead band of Generation X. Despite a short mainstream career spanning only three years, their music maintains a popular following and continues to influence modern rock culture.\\n</p><p>In the late 1980s, Nirvana established itself as part of the Seattle grunge scene, releasing its first album, <i>Bleach</i>, for the independent record label Sub Pop in 1989. They developed a sound that relied on dynamic contrasts, often between quiet verses and loud, heavy choruses. After signing to the major label DGC Records in 1991, Nirvana found unexpected mainstream success with \\\"Smells Like Teen Spirit\\\", the first single from their landmark second album <i>Nevermind</i> (1991). A cultural phenomenon of the 1990s, <i>Nevermind</i> was certified Diamond by the Recording Industry Association of America (RIAA) and is credited for ending the dominance of hair metal.</p><p>Characterized by their punk aesthetic, Nirvana's fusion of pop melodies with noise, combined with their themes of abjection and social alienation, brought them global popularity. Following extensive tours and the 1992 compilation album <i>Incesticide</i> and EP <i>Hormoaning</i>, the band released their highly anticipated third studio album, <i>In Utero</i> (1993). The album topped both the US and UK album charts, and was acclaimed by critics. Nirvana disbanded following Cobain's suicide in April 1994. Further releases have been overseen by Novoselic, Grohl, and Cobain's widow, Courtney Love. The live album <i>MTV Unplugged in New York</i> (1994) won Best Alternative Music Performance at the 1996 Grammy Awards.\\n</p><p>Nirvana is one of the best-selling bands of all time, having sold more than 75\\u00a0million records worldwide. During their three years as a mainstream act, Nirvana received an American Music Award, Brit Award, and Grammy Award, as well as seven MTV Video Music Awards and two NME Awards. They achieved five number-one hits on the <i>Billboard</i> Alternative Songs chart and four number-one albums on the <i>Billboard</i> 200. In 2004, <i>Rolling Stone</i> named Nirvana among the 100 greatest artists of all time. They were inducted into the Rock and Roll Hall of Fame in their first year of eligibility in 2014.\\n</p>\\n\\n\\n\"}}}}";
    }


}
