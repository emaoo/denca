package com.denca.denca;

/**
 * Created by mdnah on 9/9/2017.
 */

public class RightWrong {

    private String rightUrl;
    private String wrongUrl;

    public RightWrong(String rightUrl, String wrongUrl) {
        this.rightUrl = rightUrl;
        this.wrongUrl = wrongUrl;
    }

    public String getRightUrl() {
        return rightUrl;
    }

    public void setRightUrl(String rightUrl) {
        this.rightUrl = rightUrl;
    }

    public String getWrongUrl() {
        return wrongUrl;
    }

    public void setWrongUrl(String wrongUrl) {
        this.wrongUrl = wrongUrl;
    }
}
