package Application.utils;

public class RestTemplateConfig {
    private String protocol;
    private String host;
    private String port;
    private String pathPrefix;
    private String version;
    private String pathPostFix;
    private String json;
    private String postPreFix;
    private String queryTypeArtist;

    public RestTemplateConfig(String protocol, String host, String port, String pathPostFix, String version,
                              String queryTypeArtist, String pathPrefix, String json) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.pathPostFix = pathPostFix;
        this.version = version;
        this.queryTypeArtist = queryTypeArtist;
        this.pathPrefix = pathPrefix;
        this.json = json;
    }

    public RestTemplateConfig() {
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public String getVersion() {
        return version;
    }

    public String getPathPostFix() {
        return pathPostFix;
    }

    public String getJson() {
        return json;
    }

    public String getPostPreFix() {
        return postPreFix;
    }

    public String getQueryTypeArtist() {
        return queryTypeArtist;
    }


}


