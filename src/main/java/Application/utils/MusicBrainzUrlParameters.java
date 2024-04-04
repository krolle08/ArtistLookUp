package Application.utils;

public class MusicBrainzUrlParameters {
    private final String protocol;
    private final String host;
    private final Integer port;
    private final String pathPrefix;
    private final String version;
    private final String queryTypeArtist;
    private static final String pathPostFix = "?fmt=json&inc=url-rels+release-groups";

    public MusicBrainzUrlParameters(String protocol, String host, Integer port, String pathPrefix, String version, String queryTypeArtist) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.pathPrefix = pathPrefix;
        this.version = version;
        this.queryTypeArtist = queryTypeArtist;
    }

    // Getters for all parameters
    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public String getVersion() {
        return version;
    }

    public String getQueryTypeArtist() {
        return queryTypeArtist;
    }

    // Getter for path postfix
    public static String getPathPostFix() {
        return pathPostFix;
    }
}

