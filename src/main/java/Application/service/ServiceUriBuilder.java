package Application.service;

import org.springframework.stereotype.Component;
@Component
public class ServiceUriBuilder {
    /*
    private String component;
    private String protocol;
    private String host;
    private String port;
    private String path;
    private Map<String, String> params = new HashMap<>();

    private String postPath;

    private ServiceUriBuilder() {
    }

    private ServiceUriBuilder(String component) {
        this.component = component;
    }

    public static ServiceUriBuilder uri() {
        return new ServiceUriBuilder();
    }


    public ServiceUriBuilder withProtocol(String protocol) {
        this.protocol = ConfigurationReader.getPropertyValue(protocol);
        return this;
    }

    public ServiceUriBuilder withHostProperty(String hostPropertyKey) {
        this.host = ConfigurationReader.getPropertyValue(hostPropertyKey);
        return this;
    }

    public ServiceUriBuilder withPortProperty(String portPropertyKey) {
        port = ConfigurationReader.getPropertyValue(portPropertyKey);
        return this;
    }

    public ServiceUriBuilder withPathProperty(String pathPropertyKey) {
        this.path = ConfigurationReader.getPropertyValue(pathPropertyKey);
        return this;
    }

    public String build() {
        if ((protocol == null && component == null) ||
                host == null) {
            throw new IllegalArgumentException("Required builder parameter is null. " + this.toString());
        }

        StringBuilder uri = new StringBuilder();

        if (component != null) {
            uri.append(component).append(":");
        }

        uri.append(protocol).append("://").append(host);

        if (StringUtils.isNotBlank(port)) {
            uri.append(":").append(port);
        }

        if (StringUtils.isNotBlank(path)) {
            if (!path.startsWith("/")) {
                uri.append("/");
            }
            uri.append(path);
        }

        if (!params.isEmpty()) {
            uri.append("?");
            params.forEach((key, value) -> uri.append(key).append("=").append(value).append("&"));
            uri.deleteCharAt(uri.length() - 1);
        }

        return uri.toString();
    }

     */
}
