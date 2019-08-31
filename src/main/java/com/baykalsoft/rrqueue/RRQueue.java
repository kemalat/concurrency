package com.baykalsoft.rrqueue;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RRQueue {

    private final Map<Long, Queue<RRQueueElement>> buckets;
    private Queue<RRQueueElement> bucketElements = new LinkedList<>();
    private String name = RRQueue.class.getName();

    public RRQueue() {
        this.buckets = new HashMap<>();

    }

    public RRQueue(String name) {
        this();
        this.name = name;
    }

    public synchronized void initElementQueue(RRQueueElement element) {
        Queue<RRQueueElement> queue;
        if (!buckets.containsKey(element.getKey())) {
            queue = new LinkedList<>();
            buckets.put(element.getKey(), queue);
        }
    }

    public synchronized void add(RRQueueElement element) throws InterruptedException {
        initElementQueue(element);
        buckets.get(element.getKey()).add(element);
        notify();
    }

    public synchronized RRQueueElement get() throws InterruptedException {
        RRQueueElement element = null;
        while (true) {
            if(bucketElements.isEmpty()){
                for (Queue<RRQueueElement> distributionQueue: buckets.values()) {
                    if (!distributionQueue.isEmpty()) {
                        element = distributionQueue.remove();
                        bucketElements.add(element);
                    }
                }
            }
            if(!bucketElements.isEmpty()){
              System.out.println(buckets);
              return bucketElements.remove();
            }
            System.out.println(Thread.currentThread().getName() + " will be waiting. no data in " + name);
            wait();
        }
    }

    public boolean isElementListEmpty(long key) {
        if (!buckets.containsKey(key)) {
            throw new RRQueueException("element with key->" + key + " not found.");
        } else {
            if (buckets.get(key).size() > 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public Queue<RRQueueElement> getElementList(long key) {
        return buckets.get(key);
    }

    public boolean removeElementList(long key) {
        if (buckets.remove(key) != null) {
            return true;
        }
        return false;
    }

    public boolean emptyElementList(long key) {
        if (buckets.containsKey(key)) {
            buckets.get(key).clear();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "RRQueue{" +
                "buckets=" + buckets +
                '}';
    }
}
