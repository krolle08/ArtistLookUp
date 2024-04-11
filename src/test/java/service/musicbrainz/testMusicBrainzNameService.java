package service.musicbrainz;

import Application.Application;
import Application.service.Artist.ArtistInfoObj;
import Application.service.MusicBrainz.MusicBrainzNameService;
import Application.utils.TypeOfSearchEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testMusicBrainzNameService {
    @Autowired
    private MusicBrainzNameService musicBrainzNameService;

    @Test
    public void testMusicBrainzNameSearchExtractData() {
        // Given
        Map<String, String> fillterparams = new HashMap<>();
        ResponseEntity<String> mockedResponseEntity = ResponseEntity.ok(responseBody());

        fillterparams.put(TypeOfSearchEnum.ARTIST.getSearchType(), "Nirvana");

        // When
        ArtistInfoObj  artistInfoObj = musicBrainzNameService.extractDataAndPopulateObj(mockedResponseEntity, fillterparams.get(TypeOfSearchEnum.ARTIST.getSearchType()));

        // Then
        assertFalse(artistInfoObj.getName().isEmpty());
        assertFalse(artistInfoObj.getmBID().isEmpty());
        assertNotNull(artistInfoObj.getAlbums());
    }

    private String responseBody() {
        return "{\n" +
                "    \"created\": \"2024-04-10T20:38:53.836Z\",\n" +
                "    \"count\": 35,\n" +
                "    \"offset\": 0,\n" +
                "    \"artists\": [\n" +
                "        {\n" +
                "            \"id\": \"5b11f4ce-a62d-471e-81fc-a69a8278c7da\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 100,\n" +
                "            \"name\": \"Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana\",\n" +
                "            \"country\": \"US\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"489ce91b-6658-3307-9877-795b68554c98\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"United States\",\n" +
                "                \"sort-name\": \"United States\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"begin-area\": {\n" +
                "                \"id\": \"a640b45c-c173-49b1-8030-973603e895b5\",\n" +
                "                \"type\": \"City\",\n" +
                "                \"type-id\": \"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\n" +
                "                \"name\": \"Aberdeen\",\n" +
                "                \"sort-name\": \"Aberdeen\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"1980s~1990s US grunge band\",\n" +
                "            \"isnis\": [\n" +
                "                \"0000000123486830\",\n" +
                "                \"0000000123487390\"\n" +
                "            ],\n" +
                "            \"life-span\": {\n" +
                "                \"begin\": \"1987\",\n" +
                "                \"end\": \"1994-04-05\",\n" +
                "                \"ended\": true\n" +
                "            },\n" +
                "            \"aliases\": [\n" +
                "                {\n" +
                "                    \"sort-name\": \"ニルヴァーナ\",\n" +
                "                    \"type-id\": \"894afba6-2816-3c24-8072-eadb66bd04bc\",\n" +
                "                    \"name\": \"ニルヴァーナ\",\n" +
                "                    \"locale\": \"ja\",\n" +
                "                    \"type\": \"Artist name\",\n" +
                "                    \"primary\": true,\n" +
                "                    \"begin-date\": null,\n" +
                "                    \"end-date\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"sort-name\": \"Nirvana\",\n" +
                "                    \"type-id\": \"894afba6-2816-3c24-8072-eadb66bd04bc\",\n" +
                "                    \"name\": \"Nirvana\",\n" +
                "                    \"locale\": \"en\",\n" +
                "                    \"type\": \"Artist name\",\n" +
                "                    \"primary\": true,\n" +
                "                    \"begin-date\": null,\n" +
                "                    \"end-date\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"sort-name\": \"Nirvana US\",\n" +
                "                    \"name\": \"Nirvana US\",\n" +
                "                    \"locale\": null,\n" +
                "                    \"type\": null,\n" +
                "                    \"primary\": null,\n" +
                "                    \"begin-date\": null,\n" +
                "                    \"end-date\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"count\": 16,\n" +
                "                    \"name\": \"rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 19,\n" +
                "                    \"name\": \"alternative rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 3,\n" +
                "                    \"name\": \"90s\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"punk\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 9,\n" +
                "                    \"name\": \"american\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"punk rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"experimental\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 5,\n" +
                "                    \"name\": \"seattle\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 46,\n" +
                "                    \"name\": \"grunge\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"band\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 2,\n" +
                "                    \"name\": \"usa\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"indie rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"alternative\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"américain\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"legendary\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 3,\n" +
                "                    \"name\": \"acoustic rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 6,\n" +
                "                    \"name\": \"noise rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"90\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"northwest\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"rock and indie\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"united states\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"nirvana\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"kurt cobain\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"2008 universal fire victim\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"9282c8b4-ca0b-4c6b-b7e3-4f7762dfc4d6\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 79,\n" +
                "            \"name\": \"Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana\",\n" +
                "            \"country\": \"GB\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"8a754a16-0027-3a29-b6d7-2b40ea0481ed\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"United Kingdom\",\n" +
                "                \"sort-name\": \"United Kingdom\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"begin-area\": {\n" +
                "                \"id\": \"f03d09b3-39dc-4083-afd6-159e3f0d462f\",\n" +
                "                \"type\": \"City\",\n" +
                "                \"type-id\": \"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\n" +
                "                \"name\": \"London\",\n" +
                "                \"sort-name\": \"London\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"60s band from the UK\",\n" +
                "            \"life-span\": {\n" +
                "                \"begin\": \"1967\",\n" +
                "                \"ended\": null\n" +
                "            },\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"count\": 2,\n" +
                "                    \"name\": \"rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"pop\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 3,\n" +
                "                    \"name\": \"progressive rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 0,\n" +
                "                    \"name\": \"orchestral\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"british\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"power pop\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"psychedelic rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"baroque pop\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"soft rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"psychedelic pop\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"pop rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"symphonic rock\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"english\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"c3a64a25-251b-4d03-afba-1471440245b8\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 73,\n" +
                "            \"name\": \"Approaching Nirvana\",\n" +
                "            \"sort-name\": \"Approaching Nirvana\",\n" +
                "            \"country\": \"US\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"489ce91b-6658-3307-9877-795b68554c98\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"United States\",\n" +
                "                \"sort-name\": \"United States\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"life-span\": {\n" +
                "                \"begin\": \"2009\",\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"c49d69dc-e008-47cf-b5ff-160fafb1fe1f\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 70,\n" +
                "            \"name\": \"Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana\",\n" +
                "            \"country\": \"FR\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"08310658-51eb-3801-80de-5a0739207115\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"France\",\n" +
                "                \"sort-name\": \"France\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"’70s French band from Martigues\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"85af0709-95db-4fbc-801a-120e9f4766d0\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 70,\n" +
                "            \"name\": \"Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana\",\n" +
                "            \"country\": \"FI\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"6a264f94-6ff1-30b1-9a81-41f7bfabd616\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"Finland\",\n" +
                "                \"sort-name\": \"Finland\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"Early 1980's Finnish punk band\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            },\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"punk\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"finland\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"28a4618c-38e4-4027-ad2c-db66a14a2d85\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 70,\n" +
                "            \"name\": \"Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana\",\n" +
                "            \"country\": \"YU\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"885dce63-c211-3033-8cf7-46cb82d440c7\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"Yugoslavia\",\n" +
                "                \"sort-name\": \"Yugoslavia\",\n" +
                "                \"life-span\": {\n" +
                "                    \"begin\": \"1918\",\n" +
                "                    \"end\": \"2003\",\n" +
                "                    \"ended\": true\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"Croatian prog-rock band active in first half of 70s in former Yugoslavia.\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"3aa878c0-224b-41e5-abd1-63be359d2bca\",\n" +
                "            \"score\": 70,\n" +
                "            \"name\": \"Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana\",\n" +
                "            \"disambiguation\": \"founded in 1987 by a Michael Jackson double/imitator\",\n" +
                "            \"life-span\": {\n" +
                "                \"begin\": \"1987\",\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"f2dfdff9-3862-4be0-bf85-9c833fa3059e\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 64,\n" +
                "            \"name\": \"Nirvana 2002\",\n" +
                "            \"sort-name\": \"Nirvana 2002\",\n" +
                "            \"country\": \"SE\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"23d10872-f5ae-3f0c-bf55-332788a16ecb\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"Sweden\",\n" +
                "                \"sort-name\": \"Sweden\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"begin-area\": {\n" +
                "                \"id\": \"43bf66f7-e06e-4455-a878-1139b4687fc4\",\n" +
                "                \"type\": \"Subdivision\",\n" +
                "                \"type-id\": \"fd3d44c5-80a1-3842-9745-2c4972d35afa\",\n" +
                "                \"name\": \"Gävleborg\",\n" +
                "                \"sort-name\": \"Gävleborg\",\n" +
                "                \"life-span\": {\n" +
                "                    \"begin\": \"1070\",\n" +
                "                    \"ended\": true\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"Swedish death metal band\",\n" +
                "            \"life-span\": {\n" +
                "                \"begin\": \"1988\",\n" +
                "                \"end\": \"2012\",\n" +
                "                \"ended\": true\n" +
                "            },\n" +
                "            \"aliases\": [\n" +
                "                {\n" +
                "                    \"sort-name\": \"Nirvana\",\n" +
                "                    \"name\": \"Nirvana\",\n" +
                "                    \"locale\": null,\n" +
                "                    \"type\": null,\n" +
                "                    \"primary\": null,\n" +
                "                    \"begin-date\": \"1988\",\n" +
                "                    \"end-date\": \"1988\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"sort-name\": \"N2K2\",\n" +
                "                    \"name\": \"N2K2\",\n" +
                "                    \"locale\": null,\n" +
                "                    \"type\": null,\n" +
                "                    \"primary\": null,\n" +
                "                    \"begin-date\": null,\n" +
                "                    \"end-date\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"sort-name\": \"Prophet 2002\",\n" +
                "                    \"name\": \"Prophet 2002\",\n" +
                "                    \"locale\": null,\n" +
                "                    \"type\": null,\n" +
                "                    \"primary\": null,\n" +
                "                    \"begin-date\": \"1988\",\n" +
                "                    \"end-date\": \"1988\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"death metal\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"329c04ae-3b73-4ca3-996f-75608ab1befb\",\n" +
                "            \"type\": \"Person\",\n" +
                "            \"type-id\": \"b6e035f4-3ce9-331c-97df-83397230b0df\",\n" +
                "            \"score\": 62,\n" +
                "            \"name\": \"Nirvana Singh\",\n" +
                "            \"sort-name\": \"Singh, Nirvana\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"89e9ed9d-9b45-42ad-bce2-54ef36053a1a\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 62,\n" +
                "            \"name\": \"Nirvana Seekerz\",\n" +
                "            \"sort-name\": \"Nirvana Seekerz\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"86f9ae24-ba2a-4d55-9275-0b89b85f6e3a\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Weed Nirvana\",\n" +
                "            \"sort-name\": \"Weed Nirvana\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"206419e0-3a7a-49ce-8437-4e757767d02b\",\n" +
                "            \"type\": \"Person\",\n" +
                "            \"type-id\": \"b6e035f4-3ce9-331c-97df-83397230b0df\",\n" +
                "            \"score\": 61,\n" +
                "            \"gender-id\": \"93452b5a-a947-30c8-934f-6a4056b151c2\",\n" +
                "            \"name\": \"Nirvana Savoury\",\n" +
                "            \"sort-name\": \"Savoury, Nirvana\",\n" +
                "            \"gender\": \"female\",\n" +
                "            \"country\": \"US\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"489ce91b-6658-3307-9877-795b68554c98\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"United States\",\n" +
                "                \"sort-name\": \"United States\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"b305320e-c158-43f4-b5be-4450e2f99a32\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"El Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana, El\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"7c525bf4-abf0-42e3-a1bd-81cbcb55c1f4\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Genta Nirvana\",\n" +
                "            \"sort-name\": \"Genta Nirvana\",\n" +
                "            \"country\": \"ID\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"d3a68bd0-7419-3f99-a5bd-204d6e057089\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"Indonesia\",\n" +
                "                \"sort-name\": \"Indonesia\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"experimental\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"74ba0d66-e029-4b2e-9bd2-627b0febdc1d\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Cheick Nirvana\",\n" +
                "            \"sort-name\": \"Cheick Nirvana\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"322210a0-efe6-484f-9a5a-90a54f3aba22\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Project Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana, Project\",\n" +
                "            \"disambiguation\": \"Doujin Artist\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"3cad39a4-68c9-431e-ad86-80a9a5890a3a\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Nirvana Star\",\n" +
                "            \"sort-name\": \"Nirvana Star\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            },\n" +
                "            \"tags\": [\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"downtempo\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"instrumental\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"hip hop\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"lo-fi\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"boom bap\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"chillwave\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"instrumental hip hop\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"count\": 1,\n" +
                "                    \"name\": \"lo-fi hip hop\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"f58febd3-18de-4371-8d95-4a68d4f79456\",\n" +
                "            \"type\": \"Person\",\n" +
                "            \"type-id\": \"b6e035f4-3ce9-331c-97df-83397230b0df\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Nirvana Kelly\",\n" +
                "            \"sort-name\": \"Kelly, Nirvana\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"8f32371e-4bac-4090-ba13-2c1cd1aeab0d\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Nirvana UK\",\n" +
                "            \"sort-name\": \"Nirvana UK\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"07607044-8140-47ba-bb24-7129babe586b\",\n" +
                "                \"type\": \"Subdivision\",\n" +
                "                \"type-id\": \"fd3d44c5-80a1-3842-9745-2c4972d35afa\",\n" +
                "                \"name\": \"West Midlands\",\n" +
                "                \"sort-name\": \"West Midlands\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"1d7c7a00-a650-47a3-8073-9b33cafbed5c\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Nirvana Tribute\",\n" +
                "            \"sort-name\": \"Nirvana Tribute\",\n" +
                "            \"disambiguation\": \"Finnish Nirvana tribute band\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"c2a67502-675c-4c1a-8d8e-2bde447557fd\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"El Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana, El\",\n" +
                "            \"begin-area\": {\n" +
                "                \"id\": \"0df04709-c7d8-3b55-a6ea-f3e5069a947b\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"Argentina\",\n" +
                "                \"sort-name\": \"Argentina\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"Prog Group from Argentina\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"69544776-5086-4480-a654-7bf2b5362c16\",\n" +
                "            \"type\": \"Person\",\n" +
                "            \"type-id\": \"b6e035f4-3ce9-331c-97df-83397230b0df\",\n" +
                "            \"score\": 61,\n" +
                "            \"gender-id\": \"93452b5a-a947-30c8-934f-6a4056b151c2\",\n" +
                "            \"name\": \"Lexly Nirvana\",\n" +
                "            \"sort-name\": \"Nirvana, Lexly\",\n" +
                "            \"gender\": \"female\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"f934c8da-e40e-4056-8f8c-212e68fdcaec\",\n" +
                "                \"type\": \"Subdivision\",\n" +
                "                \"type-id\": \"fd3d44c5-80a1-3842-9745-2c4972d35afa\",\n" +
                "                \"name\": \"Texas\",\n" +
                "                \"sort-name\": \"Texas\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"christian rapper\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"25128726-6b84-4569-a351-20fec1b1a773\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Shiva Nirvana\",\n" +
                "            \"sort-name\": \"Shiva Nirvana\",\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"da77e424-c473-481e-a2ae-5d966b2ee0b6\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 61,\n" +
                "            \"name\": \"Nirvana Undercover\",\n" +
                "            \"sort-name\": \"Nirvana Undercover\",\n" +
                "            \"begin-area\": {\n" +
                "                \"id\": \"ef1b7cc0-cd26-36f4-8ea0-04d9623786c7\",\n" +
                "                \"type\": \"Country\",\n" +
                "                \"type-id\": \"06dd0ae4-8c74-30bb-b43d-95dcedf961de\",\n" +
                "                \"name\": \"Netherlands\",\n" +
                "                \"sort-name\": \"Netherlands\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"life-span\": {\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": \"45eacd92-6857-4faa-9283-023e72a1d4b1\",\n" +
                "            \"type\": \"Group\",\n" +
                "            \"type-id\": \"e431f5f6-b5d2-343d-8b36-72607fffb74b\",\n" +
                "            \"score\": 55,\n" +
                "            \"name\": \"The Nirvana Experience\",\n" +
                "            \"sort-name\": \"The Nirvana Experience\",\n" +
                "            \"area\": {\n" +
                "                \"id\": \"c920948b-83e3-40b7-8fe9-9ab5abaac55b\",\n" +
                "                \"type\": \"City\",\n" +
                "                \"type-id\": \"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\n" +
                "                \"name\": \"Houston\",\n" +
                "                \"sort-name\": \"Houston\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"begin-area\": {\n" +
                "                \"id\": \"c920948b-83e3-40b7-8fe9-9ab5abaac55b\",\n" +
                "                \"type\": \"City\",\n" +
                "                \"type-id\": \"6fd8f29a-3d0a-32fc-980d-ea697b69da78\",\n" +
                "                \"name\": \"Houston\",\n" +
                "                \"sort-name\": \"Houston\",\n" +
                "                \"life-span\": {\n" +
                "                    \"ended\": null\n" +
                "                }\n" +
                "            },\n" +
                "            \"disambiguation\": \"Nirvana Cover Band\",\n" +
                "            \"life-span\": {\n" +
                "                \"begin\": \"2012\",\n" +
                "                \"ended\": null\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

}
