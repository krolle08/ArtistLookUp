package Application.model;

import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.UUID;


@Data
public class Artist {

    private final String iD;

    @NotBlank
    private final String mBID;

    public Artist(String mBID) {
        this.iD = UUID.randomUUID().toString();
        this.mBID = mBID;
    }

    public String getiD() {
        return iD;
    }

    public String getmBID() {
        return mBID;
    }
}
