package Application.utils;

/**
 * List of search possibilites on MusicBrainz.
 * Only Artist is implemented at the moment.
 */
public enum TypeOfSearchEnum {
    AREA("Area"),
    ARTIST("Artist"),
    EVENT("Event"),
    GENRE("Genre"),
    INSTRUMENT("Instrument"),
    LABEL("Label"),
    PLACE("Place"),
    RECORDING("Recording"),
    RELEASE_GROUP("Release_group"),
    URL("URL"),
    WORK("Work");
    private final String reason;

    TypeOfSearchEnum(String reason) {
        this.reason = reason;
    }

    public String getSearchType() {
        return reason;
    }

    public static TypeOfSearchEnum convertToEnum(String type) {
            return TypeOfSearchEnum.valueOf(type.toUpperCase());
    }
}
