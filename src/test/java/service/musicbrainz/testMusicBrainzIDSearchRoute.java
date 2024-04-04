package service.musicbrainz;

import Application.Application;
import Application.service.Artist.ArtistInfoObj;
import Application.service.MusicBrainz.MusicBrainzIdService;
import Application.utils.TypeOfSearchEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzIDSearchRoute {

    @Autowired
    private MusicBrainzIdService musicBrainzIdService;

    @Test
    public void testMBidEndpoint() {
        //Given
        String nirvana = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        String succescriteria = "Q11649";
        ArtistInfoObj artistInfoObj;

        //When
        artistInfoObj = musicBrainzIdService.getDataBymbid(nirvana);

        //Then
        assertThat(artistInfoObj.getName()).isEqualTo("Nirvana");
        assertThat(artistInfoObj.getmBStatusCode()).isEqualTo(200);
        assertThat(artistInfoObj.getWikiInfo().getWikidataSearchTerm()).isEqualTo(succescriteria);
        assertThat(!artistInfoObj.getAlbums().isEmpty());
        assertThat(artistInfoObj.getAlbums().size()).isEqualTo(17); // Check if it has a size of 16, prepare for updates if they release albums
    }

    @Test
    public void testMusicBrainzNameSearchExtractData() {
        // Given
        Map<String, String> fillterparams = new HashMap<>();
        ResponseEntity<String> mockedResponseEntity = ResponseEntity.ok(responseBody());

        fillterparams.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Nirvana");

        // When
        ArtistInfoObj artistInfoObj = new ArtistInfoObj();
        artistInfoObj = musicBrainzIdService.extractData(mockedResponseEntity, fillterparams.get(TypeOfSearchEnum.ARTIST.getSearchType()));


        // Then
        assertFalse(artistInfoObj.getName().isEmpty());
        assertFalse(artistInfoObj.getAlbums() == null);
    }

    private String responseBody() {
        return "{\"id\":\"5b11f4ce-a62d-471e-81fc-a69a8278c7da\",\"area\":{\"sort-name\":\"United States\",\"type\":null,\"id\":\"489ce91b-6658-3307-9877-795b68554c98\",\"type-id\":null,\"disambiguation\":\"\",\"iso-3166-1-codes\":[\"US\"],\"name\":\"United States\"},\"sort-name\":\"Nirvana\",\"name\":\"Nirvana\",\"ipis\":[],\"release-groups\":[{\"title\":\"Bleach\",\"id\":\"f1afec0b-26dd-3db5-9aa1-c91229a74a24\",\"secondary-types\":[],\"first-release-date\":\"1989-06-01\",\"secondary-type-ids\":[],\"primary-type\":\"Album\",\"disambiguation\":\"\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\"},{\"primary-type\":\"Album\",\"secondary-type-ids\":[],\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\",\"id\":\"1b022e01-4da6-387b-8658-8678046e4cef\",\"title\":\"Nevermind\",\"first-release-date\":\"1991-09-24\",\"secondary-types\":[]},{\"disambiguation\":\"\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"secondary-type-ids\":[],\"primary-type\":\"Album\",\"secondary-types\":[],\"first-release-date\":\"1993-09-21\",\"title\":\"In Utero\",\"id\":\"2a0981fb-9593-3019-864b-ce934d97a16e\"},{\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\"],\"primary-type\":\"Album\",\"disambiguation\":\"\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"title\":\"Incesticide\",\"id\":\"01cf1391-141b-3c87-8650-45ade6e59070\",\"secondary-types\":[\"Compilation\"],\"first-release-date\":\"1992-01-01\"},{\"first-release-date\":\"2002-10-18\",\"secondary-types\":[\"Compilation\"],\"id\":\"5ab32af4-c62e-3cbf-aa8c-c761581d3b94\",\"title\":\"Nirvana\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\",\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\"],\"primary-type\":\"Album\"},{\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\",\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\"],\"primary-type\":\"Album\",\"first-release-date\":\"2002-10-23\",\"secondary-types\":[\"Compilation\"],\"id\":\"0b8fef8c-1cc5-4ce7-b007-05cbd6d5aa0b\",\"title\":\"Greatest Hits\"},{\"primary-type\":\"Album\",\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\"],\"disambiguation\":\"\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"title\":\"With the Lights Out\",\"id\":\"d4d28ec1-220a-327c-93c5-ae006be43598\",\"secondary-types\":[\"Compilation\"],\"first-release-date\":\"2004-11-23\"},{\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\",\"primary-type\":\"Album\",\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\"],\"first-release-date\":\"2005-10-31\",\"secondary-types\":[\"Compilation\"],\"id\":\"e9674d41-d94b-344a-89f5-734736853d5f\",\"title\":\"Sliver: The Best of the Box\"},{\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\"],\"primary-type\":\"Album\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\",\"id\":\"5cc5dc44-8860-462e-8aa5-2cf9b71af237\",\"title\":\"ICON\",\"first-release-date\":\"2010-08-31\",\"secondary-types\":[\"Compilation\"]},{\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\",\"primary-type\":\"Album\",\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\"],\"first-release-date\":\"2011\",\"secondary-types\":[\"Compilation\"],\"id\":\"95563c6a-92e5-456c-9f86-6fe2ff1d148e\",\"title\":\"2 for 1: Incesticide / In Utero\"},{\"id\":\"55fca0ec-17ed-4860-b700-ef366574aa42\",\"title\":\"Live! Tonight! Sold Out!!\",\"first-release-date\":\"1994-11-15\",\"secondary-types\":[\"Compilation\",\"Live\"],\"primary-type\":\"Album\",\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\",\"6fd474e2-6b58-3102-9d17-d6f7eb7da0a0\"],\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\"},{\"first-release-date\":\"1996-09-30\",\"secondary-types\":[\"Compilation\",\"Live\"],\"id\":\"249e7835-5c39-3a10-b15b-e2d3470fb40c\",\"title\":\"From the Muddy Banks of the Wishkah\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\",\"secondary-type-ids\":[\"dd2a21e1-0c00-3729-a7a0-de60b84eb5d1\",\"6fd474e2-6b58-3102-9d17-d6f7eb7da0a0\"],\"primary-type\":\"Album\"},{\"primary-type\":\"Album\",\"secondary-type-ids\":[\"6fd474e2-6b58-3102-9d17-d6f7eb7da0a0\"],\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"disambiguation\":\"\",\"id\":\"fb3770f6-83fb-32b7-85c4-1f522a92287e\",\"title\":\"MTV Unplugged in New York\",\"first-release-date\":\"1994-10-31\",\"secondary-types\":[\"Live\"]},{\"secondary-type-ids\":[\"6fd474e2-6b58-3102-9d17-d6f7eb7da0a0\"],\"primary-type\":\"Album\",\"disambiguation\":\"\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\",\"title\":\"Live at Reading\",\"id\":\"48f5d526-0fa6-4ca6-ac59-9b2cf9ef464f\",\"secondary-types\":[\"Live\"],\"first-release-date\":\"2009-10-30\"},{\"title\":\"Live at the Paramount\",\"id\":\"e0372c5a-1750-46ec-8f1b-4b76df1fe8e7\",\"secondary-types\":[\"Live\"],\"first-release-date\":\"2011-09-26\",\"secondary-type-ids\":[\"6fd474e2-6b58-3102-9d17-d6f7eb7da0a0\"],\"primary-type\":\"Album\",\"disambiguation\":\"\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\"},{\"title\":\"Live and Loud\",\"id\":\"c171986d-83d9-4659-9644-ce9dc2b30836\",\"secondary-types\":[\"Live\"],\"first-release-date\":\"2013-09-23\",\"primary-type\":\"Album\",\"secondary-type-ids\":[\"6fd474e2-6b58-3102-9d17-d6f7eb7da0a0\"],\"disambiguation\":\"\",\"primary-type-id\":\"f529b476-6e62-324f-b0aa-1f3e33d313fc\"},{\"secondary-types\":[],\"first-release-date\":\"1988-11\",\"title\":\"Love Buzz\",\"id\":\"04f53329-d0d0-3b0d-856a-1e2cbcde0e69\",\"disambiguation\":\"\",\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\",\"primary-type\":\"Single\",\"secondary-type-ids\":[]},{\"id\":\"c01d417b-0e34-3723-9ebe-87de4620080c\",\"title\":\"Sliver\",\"first-release-date\":\"1990-09\",\"secondary-types\":[],\"primary-type\":\"Single\",\"secondary-type-ids\":[],\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\",\"disambiguation\":\"\"},{\"id\":\"8c22577f-aaea-3973-9d27-20731751e088\",\"title\":\"Candy / Molly’s Lips\",\"first-release-date\":\"1991-01\",\"secondary-types\":[],\"secondary-type-ids\":[],\"primary-type\":\"Single\",\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\",\"disambiguation\":\"\"},{\"first-release-date\":\"1991-06\",\"secondary-types\":[],\"id\":\"40f18565-ab15-3a93-8a9a-4ed6be9a112e\",\"title\":\"Here She Comes Now / Venus in Furs\",\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\",\"disambiguation\":\"\",\"primary-type\":\"Single\",\"secondary-type-ids\":[]},{\"secondary-type-ids\":[],\"primary-type\":\"Single\",\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\",\"disambiguation\":\"\",\"id\":\"03345972-d2f8-36bb-b49a-03a9ceccb7a7\",\"title\":\"Smells Like Teen Spirit\",\"first-release-date\":\"1991-09-10\",\"secondary-types\":[]},{\"title\":\"Come as You Are\",\"id\":\"6970c348-86ce-3902-bee3-d2ebabc2643d\",\"secondary-types\":[],\"first-release-date\":\"1992-01\",\"secondary-type-ids\":[],\"primary-type\":\"Single\",\"disambiguation\":\"\",\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\"},{\"first-release-date\":\"1992-07-20\",\"secondary-types\":[],\"id\":\"be95ba52-8a66-3769-82ad-b5024e993ad7\",\"title\":\"Lithium\",\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\",\"disambiguation\":\"\",\"primary-type\":\"Single\",\"secondary-type-ids\":[]},{\"id\":\"5d8050da-e5c9-3a1b-9e39-0a73620218c0\",\"title\":\"In Bloom\",\"first-release-date\":\"1992-11-30\",\"secondary-types\":[],\"primary-type\":\"Single\",\"secondary-type-ids\":[],\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\",\"disambiguation\":\"\"},{\"disambiguation\":\"\",\"primary-type-id\":\"d6038452-8ee0-3f68-affc-2de9a1ede0b9\",\"secondary-type-ids\":[],\"primary-type\":\"Single\",\"secondary-types\":[],\"first-release-date\":\"1993-02-22\",\"title\":\"Puss / Oh, the Guilt\",\"id\":\"8cf53089-743c-3c3f-9a80-5cd5f396dfeb\"}],\"relations\":[{\"source-credit\":\"\",\"type-id\":\"6b3e3c85-0002-4f34-aca6-80ace0d7e846\",\"attributes\":[],\"ended\":false,\"direction\":\"forward\",\"type\":\"allmusic\",\"attribute-ids\":{},\"end\":null,\"attribute-values\":{},\"url\":{\"resource\":\"https://www.allmusic.com/artist/mn0000357406\",\"id\":\"4a425cd3-641d-409c-a282-2334935bf1bd\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null},{\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"resource\":\"https://www.bbc.co.uk/music/artists/5b11f4ce-a62d-471e-81fc-a69a8278c7da\",\"id\":\"627ce98c-0eef-41c7-b28f-cc3387b98aab\"},\"type\":\"BBC Music page\",\"attribute-ids\":{},\"attribute-values\":{},\"end\":\"2020-11-19\",\"attributes\":[],\"source-credit\":\"\",\"type-id\":\"d028a975-000c-4525-9333-d3c8425e4b54\",\"ended\":true,\"direction\":\"forward\"},{\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"resource\":\"http://www.livenirvana.com/bootography/listing85a9.html?listingquery=all\",\"id\":\"73a6779b-8aaa-42ff-9833-e550ad974be4\"},\"attribute-ids\":{},\"type\":\"discography page\",\"attribute-values\":{},\"end\":null,\"attributes\":[],\"type-id\":\"4fb0eeec-a6eb-4ae3-ad52-b55765b94e8f\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false},{\"attributes\":[],\"type-id\":\"4fb0eeec-a6eb-4ae3-ad52-b55765b94e8f\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"discography page\",\"end\":null,\"attribute-values\":{},\"target-credit\":\"\",\"url\":{\"id\":\"aa2f9928-f2d0-4ce3-9714-af7566b9df94\",\"resource\":\"http://www.livenirvana.com/digitalnirvana/discography/index.html\"},\"target-type\":\"url\",\"begin\":null},{\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"resource\":\"http://www.nirvanaarchive.com/\",\"id\":\"d0498330-0679-4174-a145-273dd974e09e\"},\"attribute-ids\":{},\"type\":\"discography page\",\"attribute-values\":{},\"end\":null,\"attributes\":[],\"type-id\":\"4fb0eeec-a6eb-4ae3-ad52-b55765b94e8f\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false},{\"url\":{\"id\":\"81846eca-af41-43d0-bcae-b62dbf5cfa2f\",\"resource\":\"https://www.discogs.com/artist/125246\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null,\"source-credit\":\"\",\"type-id\":\"04a5b104-a4c2-4bac-99a1-7b837c37d9e4\",\"attributes\":[],\"ended\":false,\"direction\":\"forward\",\"type\":\"discogs\",\"attribute-ids\":{},\"end\":null,\"attribute-values\":{}},{\"url\":{\"resource\":\"http://www.livenirvana.com/\",\"id\":\"74c7fc4f-cb3d-45ef-9c83-f7a1061f0272\"},\"target-credit\":\"\",\"begin\":null,\"target-type\":\"url\",\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"f484f897-81cc-406e-96f9-cd799a04ee24\",\"attributes\":[],\"end\":null,\"attribute-values\":{},\"type\":\"fanpage\",\"attribute-ids\":{}},{\"attribute-ids\":{},\"type\":\"fanpage\",\"end\":null,\"attribute-values\":{},\"attributes\":[],\"type-id\":\"f484f897-81cc-406e-96f9-cd799a04ee24\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"id\":\"e42476ce-e923-498e-8e98-d11ae200aebb\",\"resource\":\"http://www.nirvanaclub.com/\"}},{\"url\":{\"id\":\"f6a499eb-5959-4861-95f1-13caec960006\",\"resource\":\"https://open.spotify.com/artist/6olE6TJLqED3rqDCT0FyPh\"},\"target-credit\":\"\",\"begin\":null,\"target-type\":\"url\",\"direction\":\"forward\",\"ended\":false,\"type-id\":\"769085a1-c2f7-4c24-a532-2375a77693bd\",\"source-credit\":\"\",\"attributes\":[],\"end\":null,\"attribute-values\":{},\"attribute-ids\":{},\"type\":\"free streaming\"},{\"target-credit\":\"\",\"url\":{\"id\":\"05fd4bd7-b7c8-44f3-afdd-0f6b6b0b6092\",\"resource\":\"https://www.deezer.com/artist/415\"},\"target-type\":\"url\",\"begin\":null,\"attributes\":[],\"source-credit\":\"\",\"type-id\":\"769085a1-c2f7-4c24-a532-2375a77693bd\",\"ended\":false,\"direction\":\"forward\",\"type\":\"free streaming\",\"attribute-ids\":{},\"attribute-values\":{},\"end\":null},{\"begin\":null,\"target-type\":\"url\",\"url\":{\"resource\":\"http://www.pandora.com/nirvana\",\"id\":\"c29fa499-8e33-455a-aaa0-d93ea088c55f\"},\"target-credit\":\"\",\"attribute-values\":{},\"end\":null,\"attribute-ids\":{},\"type\":\"free streaming\",\"direction\":\"forward\",\"ended\":false,\"type-id\":\"769085a1-c2f7-4c24-a532-2375a77693bd\",\"source-credit\":\"\",\"attributes\":[]},{\"url\":{\"id\":\"88867281-e540-4b37-9fce-870dda1bfd8b\",\"resource\":\"https://commons.wikimedia.org/wiki/File:Nirvana_around_1992.jpg\"},\"target-credit\":\"\",\"begin\":null,\"target-type\":\"url\",\"direction\":\"forward\",\"ended\":false,\"type-id\":\"221132e9-e30e-43f2-a741-15afc4c5fa7c\",\"source-credit\":\"\",\"attributes\":[],\"attribute-values\":{},\"end\":null,\"attribute-ids\":{},\"type\":\"image\"},{\"attribute-values\":{},\"end\":null,\"attribute-ids\":{},\"type\":\"IMDb\",\"direction\":\"forward\",\"ended\":false,\"attributes\":[],\"type-id\":\"94c8b0cc-4477-4106-932c-da60e63de61c\",\"source-credit\":\"\",\"begin\":null,\"target-type\":\"url\",\"target-credit\":\"\",\"url\":{\"resource\":\"https://www.imdb.com/name/nm1110321/\",\"id\":\"85229dcd-cc79-4ce8-a3be-4b0539e9148a\"}},{\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"08db8098-c0df-4b78-82c3-c8697b4bba7f\",\"attributes\":[],\"attribute-values\":{},\"end\":null,\"type\":\"last.fm\",\"attribute-ids\":{},\"url\":{\"id\":\"36dc918b-2a58-4d31-9ccd-10af003e7386\",\"resource\":\"https://www.last.fm/music/Nirvana\"},\"target-credit\":\"\",\"begin\":null,\"target-type\":\"url\"},{\"attributes\":[],\"type-id\":\"e4d73442-3762-45a8-905c-401da65544ed\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"lyrics\",\"attribute-values\":{},\"end\":null,\"target-credit\":\"\",\"url\":{\"id\":\"740505c7-3a30-4482-8ca6-10183b37d308\",\"resource\":\"http://muzikum.eu/en/122-4216/nirvana/lyrics.html\"},\"target-type\":\"url\",\"begin\":null},{\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"id\":\"eb2efdf0-05cf-4ea8-bc00-b1b0d2bcdcfb\",\"resource\":\"https://genius.com/artists/Nirvana\"},\"type\":\"lyrics\",\"attribute-ids\":{},\"attribute-values\":{},\"end\":null,\"attributes\":[],\"source-credit\":\"\",\"type-id\":\"e4d73442-3762-45a8-905c-401da65544ed\",\"ended\":false,\"direction\":\"forward\"},{\"target-credit\":\"\",\"url\":{\"id\":\"b7863533-0c1f-405a-9ca0-cd5c09f94efd\",\"resource\":\"https://www.musixmatch.com/artist/Nirvana\"},\"target-type\":\"url\",\"begin\":null,\"attributes\":[],\"type-id\":\"e4d73442-3762-45a8-905c-401da65544ed\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"lyrics\",\"attribute-values\":{},\"end\":null},{\"target-credit\":\"\",\"url\":{\"resource\":\"https://myspace.com/nirvana\",\"id\":\"706cb178-5d5c-49e0-a07f-149751b94043\"},\"target-type\":\"url\",\"begin\":null,\"attributes\":[],\"type-id\":\"bac47923-ecde-4b59-822e-d08f0cd10156\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"myspace\",\"attribute-values\":{},\"end\":null},{\"type-id\":\"fe33d22f-c3b0-4d68-bd53-a856badf2b15\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"official homepage\",\"attribute-values\":{},\"end\":null,\"url\":{\"id\":\"4347ffe2-82ec-4059-9520-6a1a3f73a304\",\"resource\":\"http://www.nirvana.com/\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null},{\"ended\":false,\"direction\":\"forward\",\"attributes\":[],\"source-credit\":\"\",\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"end\":null,\"attribute-values\":{},\"type\":\"other databases\",\"attribute-ids\":{},\"target-credit\":\"\",\"url\":{\"resource\":\"http://id.loc.gov/authorities/names/n92011111\",\"id\":\"222144c5-b46d-4d67-9f56-e9fb654e2e86\"},\"begin\":null,\"target-type\":\"url\"},{\"begin\":null,\"target-type\":\"url\",\"url\":{\"resource\":\"http://musicmoz.org/Bands_and_Artists/N/Nirvana/\",\"id\":\"21daaa31-4c41-4fc9-b07d-e77b3f01d3e6\"},\"target-credit\":\"\",\"end\":null,\"attribute-values\":{},\"attribute-ids\":{},\"type\":\"other databases\",\"direction\":\"forward\",\"ended\":false,\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"source-credit\":\"\",\"attributes\":[]},{\"url\":{\"resource\":\"https://catalogue.bnf.fr/ark:/12148/cb13944446b\",\"id\":\"96d41441-df74-4dda-8a0f-c99462b22a7c\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null,\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"other databases\",\"end\":null,\"attribute-values\":{}},{\"url\":{\"resource\":\"https://d-nb.info/gnd/10295339-9\",\"id\":\"88a3f586-d383-4cd8-9458-f48512533799\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null,\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"other databases\",\"end\":null,\"attribute-values\":{}},{\"target-type\":\"url\",\"begin\":null,\"url\":{\"id\":\"984aef79-06a7-471f-96ef-0f2dd1fd20de\",\"resource\":\"https://imvdb.com/n/nirvana\"},\"target-credit\":\"\",\"type\":\"other databases\",\"attribute-ids\":{},\"end\":null,\"attribute-values\":{},\"source-credit\":\"\",\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"attributes\":[],\"ended\":false,\"direction\":\"forward\"},{\"target-credit\":\"\",\"url\":{\"resource\":\"https://nla.gov.au/nla.party-1179730\",\"id\":\"1631d12d-9c1f-4899-a2d4-f54ffe615ca1\"},\"target-type\":\"url\",\"begin\":null,\"attributes\":[],\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"other databases\",\"attribute-values\":{},\"end\":null},{\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"attributes\":[],\"end\":null,\"attribute-values\":{},\"type\":\"other databases\",\"attribute-ids\":{},\"url\":{\"resource\":\"https://rateyourmusic.com/artist/nirvana\",\"id\":\"07a468c6-3ceb-4e44-b0b9-f46cca1151dc\"},\"target-credit\":\"\",\"begin\":null,\"target-type\":\"url\"},{\"attributes\":[],\"source-credit\":\"\",\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"ended\":false,\"direction\":\"forward\",\"type\":\"other databases\",\"attribute-ids\":{},\"attribute-values\":{},\"end\":null,\"target-credit\":\"\",\"url\":{\"id\":\"856db026-ed62-4d4b-b91e-301ab15bc89f\",\"resource\":\"https://www.45cat.com/artist/nirvana-us\"},\"target-type\":\"url\",\"begin\":null},{\"target-credit\":\"\",\"url\":{\"id\":\"059071fd-b921-4ee4-a031-de11e2c8d4d3\",\"resource\":\"https://www.livefans.jp/artists/21724\"},\"target-type\":\"url\",\"begin\":null,\"attributes\":[],\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"other databases\",\"attribute-values\":{},\"end\":null},{\"ended\":false,\"direction\":\"forward\",\"attributes\":[],\"source-credit\":\"\",\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"end\":null,\"attribute-values\":{},\"type\":\"other databases\",\"attribute-ids\":{},\"target-credit\":\"\",\"url\":{\"resource\":\"https://www.musik-sammler.de/artist/nirvana/\",\"id\":\"902a6503-ed52-4207-ae6e-f1a64dc53888\"},\"begin\":null,\"target-type\":\"url\"},{\"begin\":null,\"target-type\":\"url\",\"url\":{\"resource\":\"https://www.spirit-of-metal.com/en/band/Nirvana\",\"id\":\"dc807420-0cd6-4c4f-8c2e-0c70cbe37f64\"},\"target-credit\":\"\",\"attribute-values\":{},\"end\":null,\"type\":\"other databases\",\"attribute-ids\":{},\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"attributes\":[]},{\"end\":null,\"attribute-values\":{},\"type\":\"other databases\",\"attribute-ids\":{},\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"attributes\":[],\"begin\":null,\"target-type\":\"url\",\"url\":{\"id\":\"805e0346-cdfb-4eae-a8c4-f27937288cda\",\"resource\":\"https://www.whosampled.com/Nirvana/\"},\"target-credit\":\"\"},{\"url\":{\"resource\":\"http://www.worldcat.org/wcidentities/lccn-n92-11111\",\"id\":\"2127a8c5-befb-401c-a6f0-057b4f4a4581\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null,\"type-id\":\"d94fb61c-fa20-4e3c-a19a-71a949fb2c55\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"other databases\",\"attribute-values\":{},\"end\":null},{\"ended\":true,\"direction\":\"forward\",\"attributes\":[],\"source-credit\":\"\",\"type-id\":\"f8319a2f-f824-4617-81c8-be6560b3b203\",\"end\":\"2020-10-12\",\"attribute-values\":{},\"type\":\"purchase for download\",\"attribute-ids\":{},\"target-credit\":\"\",\"url\":{\"resource\":\"https://play.google.com/store/music/artist?id=Apyli2ev5del3s42qsjpnmqwuue\",\"id\":\"adb16d0b-c38d-471c-b3da-4f715267f620\"},\"begin\":null,\"target-type\":\"url\"},{\"begin\":null,\"target-type\":\"url\",\"target-credit\":\"\",\"url\":{\"id\":\"9fc44b9f-8ca9-4079-952f-fcd8a2976dbf\",\"resource\":\"https://itunes.apple.com/us/artist/id112018\"},\"end\":null,\"attribute-values\":{},\"type\":\"purchase for download\",\"attribute-ids\":{},\"ended\":false,\"direction\":\"forward\",\"attributes\":[],\"source-credit\":\"\",\"type-id\":\"f8319a2f-f824-4617-81c8-be6560b3b203\"},{\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"f8319a2f-f824-4617-81c8-be6560b3b203\",\"attributes\":[],\"attribute-values\":{},\"end\":null,\"type\":\"purchase for download\",\"attribute-ids\":{},\"url\":{\"resource\":\"https://us.7digital.com/artist/nirvana\",\"id\":\"d1f77e8a-8483-4add-aaf0-ad362795872c\"},\"target-credit\":\"\",\"begin\":null,\"target-type\":\"url\"},{\"type-id\":\"f8319a2f-f824-4617-81c8-be6560b3b203\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"purchase for download\",\"attribute-values\":{},\"end\":null,\"url\":{\"resource\":\"https://www.junodownload.com/artists/Nirvana/releases/\",\"id\":\"5b9a913e-3024-4754-bd0b-abafcfa098d8\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null},{\"type-id\":\"f8319a2f-f824-4617-81c8-be6560b3b203\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"purchase for download\",\"end\":null,\"attribute-values\":{},\"url\":{\"resource\":\"https://www.qobuz.com/gb-en/interpreter/nirvana/download-streaming-albums\",\"id\":\"1a3728c4-3e23-49c7-916e-02b02af4d6b0\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null},{\"attribute-ids\":{},\"type\":\"purchase for mail-order\",\"attribute-values\":{},\"end\":null,\"attributes\":[],\"type-id\":\"611b1862-67af-4253-a64f-34adba305d1d\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"id\":\"7be83207-f706-435b-81f2-5f57f109e652\",\"resource\":\"https://www.cdjapan.co.jp/person/700298036\"}},{\"url\":{\"id\":\"61139a11-0318-481d-b305-bf569e400e50\",\"resource\":\"http://www.purevolume.com/Nirvana109A\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null,\"type-id\":\"b6f02157-a9d3-4f24-9057-0675b2dbc581\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":true,\"attribute-ids\":{},\"type\":\"purevolume\",\"attribute-values\":{},\"end\":\"2018-06-30\"},{\"attribute-values\":{},\"end\":null,\"type\":\"secondhandsongs\",\"attribute-ids\":{},\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"79c5b84d-a206-4f4c-9832-78c028c312c3\",\"attributes\":[],\"begin\":null,\"target-type\":\"url\",\"url\":{\"id\":\"5f33ae58-aa56-40bd-ad14-ab7db9b3d3fd\",\"resource\":\"https://secondhandsongs.com/artist/169\"},\"target-credit\":\"\"},{\"url\":{\"id\":\"47616077-74cb-47c6-8bd4-cf34753a9af0\",\"resource\":\"https://www.setlist.fm/setlists/nirvana-7bd69ee8.html\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null,\"source-credit\":\"\",\"type-id\":\"bf5d0d5e-27a1-4e94-9df7-3cdc67b3b207\",\"attributes\":[],\"ended\":false,\"direction\":\"forward\",\"type\":\"setlistfm\",\"attribute-ids\":{},\"end\":null,\"attribute-values\":{}},{\"type-id\":\"99429741-f3f6-484b-84f8-23af51991770\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"social network\",\"end\":null,\"attribute-values\":{},\"url\":{\"id\":\"f5cfd704-b99a-45fc-9aed-8747257cad03\",\"resource\":\"https://twitter.com/Nirvana\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null},{\"attribute-ids\":{},\"type\":\"social network\",\"attribute-values\":{},\"end\":null,\"type-id\":\"99429741-f3f6-484b-84f8-23af51991770\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"target-type\":\"url\",\"begin\":null,\"url\":{\"id\":\"a9cec2d1-0544-4dc9-a4b2-640751654573\",\"resource\":\"https://www.facebook.com/Nirvana\"},\"target-credit\":\"\"},{\"begin\":null,\"target-type\":\"url\",\"target-credit\":\"\",\"url\":{\"id\":\"14c6ec03-4bd8-4f12-bfef-f2450746adab\",\"resource\":\"https://soundcloud.com/nirvana\"},\"end\":null,\"attribute-values\":{},\"attribute-ids\":{},\"type\":\"soundcloud\",\"direction\":\"forward\",\"ended\":false,\"attributes\":[],\"type-id\":\"89e4a949-0976-440d-bda1-5f772c1e5710\",\"source-credit\":\"\"},{\"end\":null,\"attribute-values\":{},\"type\":\"streaming\",\"attribute-ids\":{},\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"63cc5d1f-f096-4c94-a43f-ecb32ea94161\",\"attributes\":[],\"begin\":null,\"target-type\":\"url\",\"url\":{\"resource\":\"https://music.amazon.com/artists/B001DTD5NQ\",\"id\":\"0d12ebdb-3408-4ef7-ae8f-ef1d88c378af\"},\"target-credit\":\"\"},{\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"resource\":\"https://music.apple.com/us/artist/112018\",\"id\":\"930d6ea3-5ce7-4588-8edf-7ef465acca0c\"},\"attribute-ids\":{},\"type\":\"streaming\",\"attribute-values\":{},\"end\":null,\"attributes\":[],\"type-id\":\"63cc5d1f-f096-4c94-a43f-ecb32ea94161\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false},{\"url\":{\"resource\":\"https://tidal.com/artist/19368\",\"id\":\"a39c4986-e579-4c23-85a4-4137a5d0e63e\"},\"target-credit\":\"\",\"target-type\":\"url\",\"begin\":null,\"type-id\":\"63cc5d1f-f096-4c94-a43f-ecb32ea94161\",\"source-credit\":\"\",\"attributes\":[],\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"streaming\",\"attribute-values\":{},\"end\":null},{\"attribute-ids\":{},\"type\":\"streaming\",\"end\":null,\"attribute-values\":{},\"attributes\":[],\"type-id\":\"63cc5d1f-f096-4c94-a43f-ecb32ea94161\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"id\":\"821d7ca7-1b18-4df9-8cdd-db892caf2cc7\",\"resource\":\"https://us.napster.com/artist/nirvana\"}},{\"target-credit\":\"\",\"url\":{\"id\":\"421a959a-c50f-4a52-99e4-3c603dd37145\",\"resource\":\"http://viaf.org/viaf/138573893\"},\"target-type\":\"url\",\"begin\":null,\"attributes\":[],\"type-id\":\"e8571dcc-35d4-4e91-a577-a3382fd84460\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false,\"attribute-ids\":{},\"type\":\"VIAF\",\"attribute-values\":{},\"end\":null},{\"attribute-values\":{},\"end\":null,\"type\":\"wikidata\",\"attribute-ids\":{},\"ended\":false,\"direction\":\"forward\",\"source-credit\":\"\",\"type-id\":\"689870a4-a1e4-4912-b17f-7b2664215698\",\"attributes\":[],\"begin\":null,\"target-type\":\"url\",\"url\":{\"id\":\"1221730c-3a48-49fa-8001-beaa6e93c892\",\"resource\":\"https://www.wikidata.org/wiki/Q11649\"},\"target-credit\":\"\"},{\"target-type\":\"url\",\"begin\":null,\"target-credit\":\"\",\"url\":{\"id\":\"c8d415be-b993-4ade-bd28-d3ab4806fcbd\",\"resource\":\"https://www.youtube.com/user/NirvanaVEVO\"},\"attribute-ids\":{},\"type\":\"youtube\",\"attribute-values\":{},\"end\":null,\"attributes\":[],\"type-id\":\"6a540e5b-58c6-4192-b6ba-dbc71ec8fcf0\",\"source-credit\":\"\",\"direction\":\"forward\",\"ended\":false}],\"isnis\":[\"0000000123486830\",\"0000000123487390\"],\"disambiguation\":\"1980s~1990s US grunge band\",\"begin-area\":{\"sort-name\":\"Aberdeen\",\"type\":null,\"id\":\"a640b45c-c173-49b1-8030-973603e895b5\",\"type-id\":null,\"disambiguation\":\"\",\"name\":\"Aberdeen\"},\"life-span\":{\"end\":\"1994-04-05\",\"begin\":\"1987\",\"ended\":true},\"end-area\":null,\"type\":\"Group\",\"end_area\":null,\"gender\":null,\"country\":\"US\",\"gender-id\":null,\"type-id\":\"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\"begin_area\":{\"sort-name\":\"Aberdeen\",\"type\":null,\"id\":\"a640b45c-c173-49b1-8030-973603e895b5\",\"type-id\":null,\"disambiguation\":\"\",\"name\":\"Aberdeen\"}}";
    }
}