package com.baykalsoft.rrqueue;

import java.util.Random;

/**
 * This reference implementation demonstrates round robin queue is consumed in balanced manner. Main producer threads fill buckets faster
 * then consumer thread's consuming speed, then consumer thread fetches data from queue buckets by traversing in round robbin manner.
 *
 * @author Kemal Atik
 */
public class Main {

  private static Main main;
  private static volatile Thread thread;
  private int bucketsize = 5;
  RRQueue rrQueue;

  public Main() {

  }

  public static Main getSingleton() {
    if (main == null) {
      main = new Main();
    }
    return main;
  }

  int i;

  public int randomizer() {

    if (i > bucketsize) {
      i = 0;
    }
    i++;
    return i;

  }

  public static void main(String[] args) {
    try {
      Main starter = Main.getSingleton();
      starter.start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String generateRandomWords() {
    Random random = new Random();
    char[] word = new char[random.nextInt(2) + 1]; // words of length 3 through 10. (1 and 2 letter words are boring.)
    for (int j = 0; j < word.length; j++) {
      word[j] = (char) ('a' + random.nextInt(26));
    }
    return new String(word);
  }

  private void start() {

    try {
      rrQueue = new RRQueue("Round Robin Queue");

      System.out.println("Starting Thread.");
      Process process = new Process();
      thread = new Thread(process);
      thread.start();
      System.out.println("Done.");

      int i = 0;
      while (i < 20) {
        try {

          RRQueueElement element = new RRQueueElementImpl(randomizer(), generateRandomWords());
          rrQueue.add(element);
          Thread.sleep(100);
          i++;
        } catch (InterruptedException e) {
          e.printStackTrace();

        }
      }

      while (true) {

        Thread.sleep(1000);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * This the consumer thread which consumes data from round robbin queue without knowing from which bucket it fetches data Main producer
   * threads fill buckets faster then consumer thread's consuming speed, that's why buckets grow at the beginning then producer stops and
   * allows consumer thread to finish processing buckets
   *
   * @author Kemal Atik
   */
  private class Process implements Runnable {

    private final Object lock;
    private volatile boolean suspend, stopped;

    private Process() {

      //Do some initialization
      lock = new Object();
      suspend = false;
      stopped = false;
    }

    @Override
    public void run() {
      while (!stopped) {
        while (!suspend) {
          try {
            // do work
            RRQueueElement element = rrQueue.get();
            if (element instanceof NullElement) {
              System.out.println("Stopping thread...");
              stop();
            }

            try {
              Thread.sleep(1000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }


          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          }
        }
      }
    }

    public void suspend() {
      suspend = true;
    }

    public void stop() {
      suspend = true;
      stopped = true;
      synchronized (lock) {
        lock.notifyAll();
      }
    }

    public void resume() {
      suspend = false;
      synchronized (lock) {
        lock.notifyAll();
      }
    }
  }
}
