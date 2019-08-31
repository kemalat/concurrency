package com.baykalsoft.rrqueue;

/**
 * Created by elchi on 09/19/16.
 */
public class RRQueueElementImpl implements RRQueueElement {
    private long distributionId;
    private long virtualMessageId;
    private String message;

    public RRQueueElementImpl() {
    }

    public RRQueueElementImpl(long distributionId) {
        this.distributionId = distributionId;
    }

    public RRQueueElementImpl(long distributionId, String message) {
        this.distributionId = distributionId;
        this.message = message;
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

    @Override
    public String toString() {
        return "{"+distributionId+","+message+"}";
    }
}
