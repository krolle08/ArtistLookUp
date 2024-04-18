package Application.model.response;

import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;


@Data
public class ArtistResponse {

    @NotBlank
    private String name;
    @NotBlank
    private String mBID;

    private String wikiDataId;
    public ArtistResponse(String name, String mBID) {
        this.name = name;
        this.mBID = mBID;
    }

    public String getWikiDataId() {
        return wikiDataId;
    }
    public String getName() {
        return name;
    }

    public String getmBID() {
        return mBID;
    }
}
