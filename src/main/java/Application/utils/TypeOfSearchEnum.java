package Application.utils;

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
        try {
            return TypeOfSearchEnum.valueOf(type.toUpperCase()); // Assuming the input string is in uppercase
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid search type");
        }
    }
}
