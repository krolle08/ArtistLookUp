package Application.service;

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

    public static boolean isSearchTypePossible(String type) {
        for (TypeOfSearchEnum searchType : TypeOfSearchEnum.values()) {
            if (searchType.getSearchType().equalsIgnoreCase(type)) {
                return false;
            }
        }
        return true;
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

    public static TypeOfSearchEnum isSearchTypePossible() {
        String type = ""; //Skal sættes som parameter hvis der skal tjekkes for om parameteren er tilstede i enumlisten
        try {
            return TypeOfSearchEnum.valueOf(type.toUpperCase()); // Assuming the input string is in uppercase
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid search type");
        }
    }

}
