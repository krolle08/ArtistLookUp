package service.musicbrainz;

import Application.Application;
import Application.service.Artist.ArtistInfoObj;
import Application.service.MusicBrainz.MusicBrainzNameService;
import Application.utils.TypeOfSearchEnum;
import Application.utils.URIException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzNameSearchRoute {
    @Autowired
    private MusicBrainzNameService musicBrainzNameService;

    @Test
    public void testMusicBrainzWithNameSearch_Nirvana() {
        //Given
        Map<String, String> nirvana = new HashMap<>();
        nirvana.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Nirvana");
        String mBid = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        //When
        ArtistInfoObj artistInfoObj = null;
        try {
            artistInfoObj = musicBrainzNameService.getDataByName(nirvana);
        } catch (URIException e) {
            System.out.println("Should not have thrown an exception");
            fail();
        }

        //Then
        assertThat(artistInfoObj.getName()).isEqualTo("Nirvana");
        assertThat(artistInfoObj.getmBStatusCode()).isEqualTo(200);
        assertThat(artistInfoObj.getmBID()).isEqualTo(mBid);
    }

    @Test
    public void testMusicBrainzWithNameSearch_Slipknot() {
        //Given
        Map<String, String> slipknot = new HashMap<>();
        slipknot.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Slipknot");
        String MBID = "a466c2a2-6517-42fb-a160-1087c3bafd9f";
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        //When
        try {
            artistInfoObj = musicBrainzNameService.getDataByName(slipknot);
        } catch (URIException e) {
            System.out.println("Should not have thrown an exception");
            fail();
        }

        //Then
        assertThat(artistInfoObj.getName()).isEqualTo("Slipknot");
        assertThat(artistInfoObj.getmBStatusCode()).isEqualTo(200);
        assertThat(artistInfoObj.getmBID()).isEqualTo(MBID);
    }

    @Test
    public void testMusicBrainzNameSearchWithErrorInName() {
        // Given
        Map<String, String> ErrorInSearch = new HashMap<>();
        ErrorInSearch.put(TypeOfSearchEnum.ARTIST.getSearchType(), "ErrorInName");

        String uri = "http://musicbrainz.org:80/ws/2/artist/?query=artist:Nirvana&fmt=json";

        // When
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        try {
            artistInfoObj = musicBrainzNameService.getDataByName(ErrorInSearch);
        } catch (URIException e) {
            System.out.println("Should not have thrown an exception");
            fail();
        }

        // Then
        assertThat(artistInfoObj.getName()).isNull();
        assertThat(artistInfoObj.getmBID()).isNull();
        assertThat(artistInfoObj.getAlbums()).isEmpty();
    }

    @Test
    public void testMusicBrainzNameSearchExtractData() {
        // Given
        Map<String, String> fillterparams = new HashMap<>();
        ResponseEntity<String> mockedResponseEntity = ResponseEntity.ok(responseBody());

        fillterparams.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Nirvana");

        // When
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
            artistInfoObj = musicBrainzNameService.extractDataAndPopulateObj(mockedResponseEntity, fillterparams.get(TypeOfSearchEnum.ARTIST.getSearchType()));



        // Then
        assertFalse(artistInfoObj.getName().isEmpty());
        assertFalse(artistInfoObj.getmBID().isEmpty());
        assertFalse(artistInfoObj.getAlbums() == null);
    }

    private String responseBody() {
        return "{\"created\":\"2024-04-03T13:23:33.571Z\",\"count\":34,\"offset\":0,\"artists\":[{\"id\":\"5b11f4ce-a62d-471e-81fc-a69a8278c7da\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":100,\"name\":\"Nirvana\",\"sort-name\":\"Nirvana\",\"country\":\"US\",\"area\":{\"id\":\"489ce91b-6658-3307-9877-795b68554c98\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"United States\",\"sort-name\":\"United States\",\"life-span\":{\"ended\":null}},\"begin-area\":{\"id\":\"a640b45c-c173-49b1-8030-973603e895b5\",\"type\":\"City\",\"type-id\":\"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\"name\":\"Aberdeen\",\"sort-name\":\"Aberdeen\",\"life-span\":{\"ended\":null}},\"disambiguation\":\"1980s~1990s US grunge band\",\"isnis\":[\"0000000123486830\",\"0000000123487390\"],\"life-span\":{\"begin\":\"1987\",\"end\":\"1994-04-05\",\"ended\":true},\"aliases\":[{\"sort-name\":\"ニルヴァーナ\",\"type-id\":\"894afba6-2816-3c24-8072-eadb66bd04bc\",\"name\":\"ニルヴァーナ\",\"locale\":\"ja\",\"type\":\"Artist name\",\"primary\":true,\"begin-date\":null,\"end-date\":null},{\"sort-name\":\"Nirvana\",\"type-id\":\"894afba6-2816-3c24-8072-eadb66bd04bc\",\"name\":\"Nirvana\",\"locale\":\"en\",\"type\":\"Artist name\",\"primary\":true,\"begin-date\":null,\"end-date\":null},{\"sort-name\":\"Nirvana US\",\"name\":\"Nirvana US\",\"locale\":null,\"type\":null,\"primary\":null,\"begin-date\":null,\"end-date\":null}],\"tags\":[{\"count\":16,\"name\":\"rock\"},{\"count\":19,\"name\":\"alternative rock\"},{\"count\":3,\"name\":\"90s\"},{\"count\":1,\"name\":\"punk\"},{\"count\":9,\"name\":\"american\"},{\"count\":0,\"name\":\"punk rock\"},{\"count\":0,\"name\":\"experimental\"},{\"count\":5,\"name\":\"seattle\"},{\"count\":46,\"name\":\"grunge\"},{\"count\":0,\"name\":\"band\"},{\"count\":2,\"name\":\"usa\"},{\"count\":0,\"name\":\"indie rock\"},{\"count\":0,\"name\":\"alternative\"},{\"count\":0,\"name\":\"américain\"},{\"count\":0,\"name\":\"legendary\"},{\"count\":3,\"name\":\"acoustic rock\"},{\"count\":6,\"name\":\"noise rock\"},{\"count\":0,\"name\":\"90\"},{\"count\":0,\"name\":\"northwest\"},{\"count\":0,\"name\":\"rock and indie\"},{\"count\":0,\"name\":\"united states\"},{\"count\":0,\"name\":\"nirvana\"},{\"count\":0,\"name\":\"kurt cobain\"},{\"count\":0,\"name\":\"2008 universal fire victim\"}]},{\"id\":\"9282c8b4-ca0b-4c6b-b7e3-4f7762dfc4d6\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":79,\"name\":\"Nirvana\",\"sort-name\":\"Nirvana\",\"country\":\"GB\",\"area\":{\"id\":\"8a754a16-0027-3a29-b6d7-2b40ea0481ed\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"United Kingdom\",\"sort-name\":\"United Kingdom\",\"life-span\":{\"ended\":null}},\"begin-area\":{\"id\":\"f03d09b3-39dc-4083-afd6-159e3f0d462f\",\"type\":\"City\",\"type-id\":\"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\"name\":\"London\",\"sort-name\":\"London\",\"life-span\":{\"ended\":null}},\"disambiguation\":\"60s band from the UK\",\"life-span\":{\"begin\":\"1967\",\"ended\":null},\"tags\":[{\"count\":2,\"name\":\"rock\"},{\"count\":1,\"name\":\"pop\"},{\"count\":3,\"name\":\"progressive rock\"},{\"count\":0,\"name\":\"orchestral\"},{\"count\":1,\"name\":\"british\"},{\"count\":1,\"name\":\"power pop\"},{\"count\":1,\"name\":\"psychedelic rock\"},{\"count\":1,\"name\":\"baroque pop\"},{\"count\":1,\"name\":\"soft rock\"},{\"count\":1,\"name\":\"psychedelic pop\"},{\"count\":1,\"name\":\"pop rock\"},{\"count\":1,\"name\":\"symphonic rock\"},{\"count\":1,\"name\":\"english\"}]},{\"id\":\"c3a64a25-251b-4d03-afba-1471440245b8\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":73,\"name\":\"Approaching Nirvana\",\"sort-name\":\"Approaching Nirvana\",\"country\":\"US\",\"area\":{\"id\":\"489ce91b-6658-3307-9877-795b68554c98\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"United States\",\"sort-name\":\"United States\",\"life-span\":{\"ended\":null}},\"life-span\":{\"begin\":\"2009\",\"ended\":null}},{\"id\":\"c49d69dc-e008-47cf-b5ff-160fafb1fe1f\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":70,\"name\":\"Nirvana\",\"sort-name\":\"Nirvana\",\"country\":\"FR\",\"area\":{\"id\":\"08310658-51eb-3801-80de-5a0739207115\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"France\",\"sort-name\":\"France\",\"life-span\":{\"ended\":null}},\"disambiguation\":\"’70s French band from Martigues\",\"life-span\":{\"ended\":null}},{\"id\":\"85af0709-95db-4fbc-801a-120e9f4766d0\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":70,\"name\":\"Nirvana\",\"sort-name\":\"Nirvana\",\"country\":\"FI\",\"area\":{\"id\":\"6a264f94-6ff1-30b1-9a81-41f7bfabd616\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"Finland\",\"sort-name\":\"Finland\",\"life-span\":{\"ended\":null}},\"disambiguation\":\"Early 1980's Finnish punk band\",\"life-span\":{\"ended\":null},\"tags\":[{\"count\":1,\"name\":\"punk\"},{\"count\":1,\"name\":\"finland\"}]},{\"id\":\"28a4618c-38e4-4027-ad2c-db66a14a2d85\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":70,\"name\":\"Nirvana\",\"sort-name\":\"Nirvana\",\"country\":\"YU\",\"area\":{\"id\":\"885dce63-c211-3033-8cf7-46cb82d440c7\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"Yugoslavia\",\"sort-name\":\"Yugoslavia\",\"life-span\":{\"begin\":\"1918\",\"end\":\"2003\",\"ended\":true}},\"disambiguation\":\"Croatian prog-rock band active in first half of 70s in former Yugoslavia.\",\"life-span\":{\"ended\":null}},{\"id\":\"3aa878c0-224b-41e5-abd1-63be359d2bca\",\"score\":70,\"name\":\"Nirvana\",\"sort-name\":\"Nirvana\",\"disambiguation\":\"founded in 1987 by a Michael Jackson double/imitator\",\"life-span\":{\"begin\":\"1987\",\"ended\":null}},{\"id\":\"f2dfdff9-3862-4be0-bf85-9c833fa3059e\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":64,\"name\":\"Nirvana 2002\",\"sort-name\":\"Nirvana 2002\",\"country\":\"SE\",\"area\":{\"id\":\"23d10872-f5ae-3f0c-bf55-332788a16ecb\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"Sweden\",\"sort-name\":\"Sweden\",\"life-span\":{\"ended\":null}},\"begin-area\":{\"id\":\"43bf66f7-e06e-4455-a878-1139b4687fc4\",\"type\":\"Subdivision\",\"type-id\":\"fd3d44c5-80a1-3842-9745-2c4972d35afa\",\"name\":\"Gävleborg\",\"sort-name\":\"Gävleborg\",\"life-span\":{\"begin\":\"1070\",\"ended\":true}},\"disambiguation\":\"Swedish death metal band\",\"life-span\":{\"begin\":\"1988\",\"end\":\"2012\",\"ended\":true},\"aliases\":[{\"sort-name\":\"Nirvana\",\"name\":\"Nirvana\",\"locale\":null,\"type\":null,\"primary\":null,\"begin-date\":\"1988\",\"end-date\":\"1988\"},{\"sort-name\":\"N2K2\",\"name\":\"N2K2\",\"locale\":null,\"type\":null,\"primary\":null,\"begin-date\":null,\"end-date\":null},{\"sort-name\":\"Prophet 2002\",\"name\":\"Prophet 2002\",\"locale\":null,\"type\":null,\"primary\":null,\"begin-date\":\"1988\",\"end-date\":\"1988\"}],\"tags\":[{\"count\":1,\"name\":\"death metal\"}]},{\"id\":\"329c04ae-3b73-4ca3-996f-75608ab1befb\",\"type\":\"Person\",\"type-id\":\"b6e035f4-3ce9-331c-97df-83397230b0df\",\"score\":62,\"name\":\"Nirvana Singh\",\"sort-name\":\"Singh, Nirvana\",\"life-span\":{\"ended\":null}},{\"id\":\"89e9ed9d-9b45-42ad-bce2-54ef36053a1a\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":62,\"name\":\"Nirvana Seekerz\",\"sort-name\":\"Nirvana Seekerz\",\"life-span\":{\"ended\":null}},{\"id\":\"86f9ae24-ba2a-4d55-9275-0b89b85f6e3a\",\"score\":61,\"name\":\"Weed Nirvana\",\"sort-name\":\"Weed Nirvana\",\"life-span\":{\"ended\":null}},{\"id\":\"206419e0-3a7a-49ce-8437-4e757767d02b\",\"type\":\"Person\",\"type-id\":\"b6e035f4-3ce9-331c-97df-83397230b0df\",\"score\":61,\"gender-id\":\"93452b5a-a947-30c8-934f-6a4056b151c2\",\"name\":\"Nirvana Savoury\",\"sort-name\":\"Savoury, Nirvana\",\"gender\":\"female\",\"country\":\"US\",\"area\":{\"id\":\"489ce91b-6658-3307-9877-795b68554c98\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"United States\",\"sort-name\":\"United States\",\"life-span\":{\"ended\":null}},\"life-span\":{\"ended\":null}},{\"id\":\"b305320e-c158-43f4-b5be-4450e2f99a32\",\"score\":61,\"name\":\"El Nirvana\",\"sort-name\":\"Nirvana, El\",\"life-span\":{\"ended\":null}},{\"id\":\"7c525bf4-abf0-42e3-a1bd-81cbcb55c1f4\",\"score\":61,\"name\":\"Genta Nirvana\",\"sort-name\":\"Genta Nirvana\",\"country\":\"ID\",\"area\":{\"id\":\"d3a68bd0-7419-3f99-a5bd-204d6e057089\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"Indonesia\",\"sort-name\":\"Indonesia\",\"life-span\":{\"ended\":null}},\"disambiguation\":\"experimental\",\"life-span\":{\"ended\":null}},{\"id\":\"74ba0d66-e029-4b2e-9bd2-627b0febdc1d\",\"score\":61,\"name\":\"Cheick Nirvana\",\"sort-name\":\"Cheick Nirvana\",\"life-span\":{\"ended\":null}},{\"id\":\"322210a0-efe6-484f-9a5a-90a54f3aba22\",\"score\":61,\"name\":\"Project Nirvana\",\"sort-name\":\"Nirvana, Project\",\"disambiguation\":\"Doujin Artist\",\"life-span\":{\"ended\":null}},{\"id\":\"3cad39a4-68c9-431e-ad86-80a9a5890a3a\",\"score\":61,\"name\":\"Nirvana Star\",\"sort-name\":\"Nirvana Star\",\"life-span\":{\"ended\":null},\"tags\":[{\"count\":1,\"name\":\"downtempo\"},{\"count\":1,\"name\":\"instrumental\"},{\"count\":1,\"name\":\"hip hop\"},{\"count\":1,\"name\":\"lo-fi\"},{\"count\":1,\"name\":\"boom bap\"},{\"count\":1,\"name\":\"chillwave\"},{\"count\":1,\"name\":\"instrumental hip hop\"},{\"count\":1,\"name\":\"lo-fi hip hop\"}]},{\"id\":\"da77e424-c473-481e-a2ae-5d966b2ee0b6\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":61,\"name\":\"Nirvana Undercover\",\"sort-name\":\"Nirvana Undercover\",\"begin-area\":{\"id\":\"ef1b7cc0-cd26-36f4-8ea0-04d9623786c7\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"Netherlands\",\"sort-name\":\"Netherlands\",\"life-span\":{\"ended\":null}},\"life-span\":{\"ended\":null}},{\"id\":\"f58febd3-18de-4371-8d95-4a68d4f79456\",\"type\":\"Person\",\"type-id\":\"b6e035f4-3ce9-331c-97df-83397230b0df\",\"score\":61,\"name\":\"Nirvana Kelly\",\"sort-name\":\"Kelly, Nirvana\",\"life-span\":{\"ended\":null}},{\"id\":\"8f32371e-4bac-4090-ba13-2c1cd1aeab0d\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":61,\"name\":\"Nirvana UK\",\"sort-name\":\"Nirvana UK\",\"area\":{\"id\":\"07607044-8140-47ba-bb24-7129babe586b\",\"type\":\"Subdivision\",\"type-id\":\"fd3d44c5-80a1-3842-9745-2c4972d35afa\",\"name\":\"West Midlands\",\"sort-name\":\"West Midlands\",\"life-span\":{\"ended\":null}},\"life-span\":{\"ended\":null}},{\"id\":\"1d7c7a00-a650-47a3-8073-9b33cafbed5c\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":61,\"name\":\"Nirvana Tribute\",\"sort-name\":\"Nirvana Tribute\",\"disambiguation\":\"Finnish Nirvana tribute band\",\"life-span\":{\"ended\":null}},{\"id\":\"c2a67502-675c-4c1a-8d8e-2bde447557fd\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":61,\"name\":\"El Nirvana\",\"sort-name\":\"Nirvana, El\",\"begin-area\":{\"id\":\"0df04709-c7d8-3b55-a6ea-f3e5069a947b\",\"type\":\"Country\",\"type-id\":\"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\"name\":\"Argentina\",\"sort-name\":\"Argentina\",\"life-span\":{\"ended\":null}},\"disambiguation\":\"Prog Group from Argentina\",\"life-span\":{\"ended\":null}},{\"id\":\"69544776-5086-4480-a654-7bf2b5362c16\",\"type\":\"Person\",\"type-id\":\"b6e035f4-3ce9-331c-97df-83397230b0df\",\"score\":61,\"gender-id\":\"93452b5a-a947-30c8-934f-6a4056b151c2\",\"name\":\"Lexly Nirvana\",\"sort-name\":\"Nirvana, Lexly\",\"gender\":\"female\",\"area\":{\"id\":\"f934c8da-e40e-4056-8f8c-212e68fdcaec\",\"type\":\"Subdivision\",\"type-id\":\"fd3d44c5-80a1-3842-9745-2c4972d35afa\",\"name\":\"Texas\",\"sort-name\":\"Texas\",\"life-span\":{\"ended\":null}},\"disambiguation\":\"christian rapper\",\"life-span\":{\"ended\":null}},{\"id\":\"25128726-6b84-4569-a351-20fec1b1a773\",\"score\":61,\"name\":\"Shiva Nirvana\",\"sort-name\":\"Shiva Nirvana\",\"life-span\":{\"ended\":null}},{\"id\":\"45eacd92-6857-4faa-9283-023e72a1d4b1\",\"type\":\"Group\",\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"score\":55,\"name\":\"The Nirvana Experience\",\"sort-name\":\"The Nirvana Experience\",\"area\":{\"id\":\"c920948b-83e3-40b7-8fe9-9ab5abaac55b\",\"type\":\"City\",\"type-id\":\"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\"name\":\"Houston\",\"sort-name\":\"Houston\",\"life-span\":{\"ended\":null}},\"begin-area\":{\"id\":\"c920948b-83e3-40b7-8fe9-9ab5abaac55b\",\"type\":\"City\",\"type-id\":\"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\"name\":\"Houston\",\"sort-name\":\"Houston\",\"life-span\":{\"ended\":null}},\"disambiguation\":\"Nirvana Cover Band\",\"life-span\":{\"begin\":\"2012\",\"ended\":null}}]}";
    }

}
