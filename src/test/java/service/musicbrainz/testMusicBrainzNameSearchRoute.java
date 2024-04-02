package service.musicbrainz;

import Application.YourApplication;
import Application.api.MusicBrainzNameSearchRoute;
import Application.service.ArtistInfo;
import Application.service.TypeOfSearchEnum;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzNameSearchRoute {
    @Autowired
    private MusicBrainzNameSearchRoute musicBrainzNameSearchRoute;
    private static Logger log;
    private static ListAppender<ILoggingEvent> listAppender;


    @BeforeAll
    public static void setup() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        log = LoggerFactory.getLogger(testMusicBrainzNameSearchRoute.class);

        // Configure Logback list appender
        listAppender = new ListAppender<>();
        listAppender.start();
        context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(listAppender);
    }
    @Test
    public void testMusicBrainzWithNameSearch_Nirvana() {
        //Given
        Map<String, String> nirvana = new HashMap<>();
        nirvana.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Nirvana");
        String mBid = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        //When
        ArtistInfo artistInfo = musicBrainzNameSearchRoute.getArtistMBID(nirvana);

        //Then
        assertThat(artistInfo.getName()).isEqualTo("Nirvana");
        assertThat(artistInfo.getmBStatusCode()).isEqualTo("200");
        assertThat(artistInfo.getmBID()).isEqualTo(mBid);
    }

    @Test
    public void testMusicBrainzWithNameSearch_Slipknot() {
        //Given
        Map<String, String> slipknot = new HashMap<>();
        slipknot.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Slipknot");
        String MBID = "a466c2a2-6517-42fb-a160-1087c3bafd9f";

        //When
        ArtistInfo artistInfo = musicBrainzNameSearchRoute.getArtistMBID(slipknot);

        //Then
        assertThat(artistInfo.getName()).isEqualTo("Slipknot");
        assertThat(artistInfo.getmBStatusCode()).isEqualTo("200");
        assertThat(artistInfo.getmBID()).isEqualTo(MBID);
    }

    @Test
    public void testMusicBrainzWithNameSearch_ErrorInSearch() {
        //Given
        Map<String, String> ErrorInSearch = new HashMap<>();
        ErrorInSearch.put(TypeOfSearchEnum.ARTIST.getSearchType(), "ErrorInSearch");

        // Create a list appender to capture log messages
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        Logger logger = (Logger) LoggerFactory.getLogger(MusicBrainzNameSearchRoute.class);
        ch.qos.logback.classic.Logger classicLogger = (ch.qos.logback.classic.Logger) logger;
        classicLogger.addAppender(listAppender);

        //When
        ArtistInfo artistInfo = musicBrainzNameSearchRoute.getArtistMBID(ErrorInSearch);

        //Then
        assertEquals(1, listAppender.list.size()); // Assuming only one log message is expected

        // Get the logged event
        ILoggingEvent logEvent = listAppender.list.get(0);
        assertEquals("No response was given", logEvent.getMessage()); // Example assertion for log message
    }
}
