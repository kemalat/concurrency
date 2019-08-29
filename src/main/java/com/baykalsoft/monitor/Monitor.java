package com.baykalsoft.monitor;

import java.util.Random;

/**
 * This class demonstrates how to control thread state using simple Java object as monitor object.
 *
 * {@link com.baykalsoft.monitor.Monitor} fires thread, thread suspends or resumes according to random value
 * @author Kemal Atik
 *
 */
public class Monitor {

  private static Monitor monitor;
  private static volatile Thread thread;

  public Monitor() {

  }

  public static Monitor getSingleton() {
    if (monitor == null) {
      monitor = new Monitor();
    }
    return monitor;
  }

  //Method to generate random integer
  public int randomizer() {
    Random rand = new Random();
    int value = rand.nextInt();
    return value;
  }

  public static void main(String[] args) {
    try {
      Monitor starter = Monitor.getSingleton();
      starter.start();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void start() {

    try {
      System.out.println("Starting Thread.");
      Process process = new Process();
      thread = new Thread(process);
      thread.start();
      System.out.println("Done.");

      while (true) {
        try {

          int val = randomizer();
          if (val % 7 == 0) {

            process.resume();
          } else {
            process.suspend();

          }
          Thread.sleep(1500);
          System.out.println("Sleeping 1500 milliseconds.");
        } catch (InterruptedException e) {
          e.printStackTrace();

        }
      }


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Thread class
   *
   * {@link com.baykalsoft.monitor.Monitor.Process} runs as long as it is not suspended. Once it is suspended, waits on monitor object till unlock notifications.
   * @author Kemal Atik
   *
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
          // do work
          System.out.println("I am doing something");
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

        }
        System.out.println("I will wait");

        synchronized (lock) {
          try {
            lock.wait();
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
