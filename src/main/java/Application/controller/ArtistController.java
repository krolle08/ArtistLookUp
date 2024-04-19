package Application.controller;

import Application.model.ArtistDetails;
import Application.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API Requester for HTTP request, accepts both Name and MusicBrainzClientImpl ID
 */
@RestController
@RequestMapping("/artist")
public class ArtistController {
    @Autowired
    private ArtistService artistService;
    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }
    @GetMapping()
    public ResponseEntity<ArtistDetails> getArtistByMBId(@RequestParam(value ="mBId", defaultValue = "") String mBId) throws Exception {
        ArtistDetails artist = artistService.getData(mBId);
        return ResponseEntity.ok(artist);
    }
}
