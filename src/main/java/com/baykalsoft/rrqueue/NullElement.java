package com.baykalsoft.rrqueue;

/**
 * Created by elchi on 09/19/16.
 */
public class NullElement implements RRQueueElement {
    private long distributionId;
    private long virtualMessageId;
    private String message;

    public NullElement() {
        this.message = "Null Element";
    }

    @Override
    public long getKey() {
        return distributionId;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
