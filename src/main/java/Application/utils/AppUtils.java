package Application.utils;

import Application.exception.InvalidMusicBrainzId;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class AppUtils {

    public static void validateMusicBrainzId(String mBId) {
        if (!ValidID(mBId)) {
            throw new InvalidMusicBrainzId(HttpStatus.BAD_REQUEST, "Invalid Music Brainz Id.");
        }

    }

    public static boolean ValidID(String id) {
        // Regular expression for UUID format
        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        // Check if the ID matches the UUID pattern
        return Pattern.matches(uuidPattern, id);
    }
}
