package com.baykalsoft.diningphilos;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;


public class Philosopher implements Runnable {

  public final int id;
  public final int iteration;
  private final ArrayList<ReentrantLock> chopsticks;


  public Philosopher(int id, int iteration, ArrayList<ReentrantLock> chopsticks) {
    this.id = id;
    this.iteration = iteration;
    this.chopsticks = chopsticks;
  }


  public void run() {
    try {

      while (iteration > 0) {
        Thread.sleep(1000);
        chopsticks.get(id).lock();
        chopsticks.get((id + 1) % chopsticks.size()).lock();
        System.out.println("[Thread][" + id + "] Chopsticsks[" + id + ";" + ((id + 1) % chopsticks.size()) + "]begin eating...");
        Thread.sleep(1000);
        System.out.println("[Thread][" + id + "] Chopsticsks[" + id + ";" + ((id + 1) % chopsticks.size()) + "]finish eating...");
        chopsticks.get(id).unlock();
        chopsticks.get((id + 1) % chopsticks.size()).unlock();

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
