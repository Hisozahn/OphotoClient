package com.client.ophotoclient.objects;

import static com.client.ophotoclient.Definitions.debugEmptyString;
import static com.client.ophotoclient.Definitions.debugNoCode;

public class OphotoMessage {
    private int code;
    private String message;

    public OphotoMessage() {
        this.code = debugNoCode;
        this.message = debugEmptyString;
    }
    public OphotoMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
