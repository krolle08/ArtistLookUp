package Application.model.response;

public class WikiDataResponse {
    private String wikipediaSearchTerm;

    public WikiDataResponse(String wikipediaSearchTerm) {
        this.wikipediaSearchTerm = wikipediaSearchTerm;
    }

    public String getWikipediaSearchTerm() {
        return wikipediaSearchTerm;
    }

    public void setWikipediaSearchTerm(String wikipediaSearchTerm) {
        this.wikipediaSearchTerm = wikipediaSearchTerm;
    }
}
