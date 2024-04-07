package Application.service.Area;

import java.util.UUID;

public class AreaInfoObj {

    private final String iD;
    private String name;
    private String Address;
    private Integer mBStatusCode;

    public AreaInfoObj(String name, String mBid) {
        this.iD = UUID.randomUUID().toString();
        this.name = name;
        this.Address = mBid;
    }

    public String getiD() {
        return iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Integer getmBStatusCode() {
        return mBStatusCode;
    }

    public void setmBStatusCode(Integer mBStatusCode) {
        this.mBStatusCode = mBStatusCode;
    }

    public boolean isEmpty() {
        return
                (name == null || name.isEmpty()) &&
                        (Address == null || Address.isEmpty());
    }
}
