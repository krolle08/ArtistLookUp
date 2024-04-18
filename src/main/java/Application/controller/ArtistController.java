package Application.controller;

import Application.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API Requester for HTTP request, accepts both Name and MusicBrainz ID
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
    @GetMapping("/{mBId}")
    public ResponseEntity<String> getArtistByMBId(@RequestParam(value ="mBId", defaultValue = "") String mBId) throws Exception {
        String artist = artistService.getArtistData(mBId);
        return ResponseEntity.ok(artist);
    }
}
