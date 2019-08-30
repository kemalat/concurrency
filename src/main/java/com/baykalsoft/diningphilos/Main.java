package com.baykalsoft.diningphilos;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.locks.ReentrantLock;


public class Main {

  private static Philosopher p;
  private static Main main;
  Hashtable<Philosopher, Thread> pTable;
  final static int noOfThreads = 5;
  final static int iterations = 10;
  private ArrayList<ReentrantLock> chopsticks = null;

  private static Main getSingleton() {

    if (main == null) {
      main = new Main();
    }
    return main;
  }

  public void startThreads(String args[]) {

    Thread t;
    Philosopher p;

    /*processorsTable: a hash table of the threads and a reference for them*/
    pTable = new Hashtable<>();

    /*Creation of Locks*/
    System.out.println("Number of Locks to be created  # " + noOfThreads);
    chopsticks = new ArrayList<>();
    for (int i = 0; i < noOfThreads; i++) {
      chopsticks.add(new ReentrantLock());
    }

    /*Creation of threads*/
    System.out.println("Number of Threads to be created # " + noOfThreads);
    for (int i = 0; i < noOfThreads; i++) {
      t = new Thread(p = new Philosopher(i, iterations, chopsticks));
      pTable.put(p, t);
      t.start();
      System.out.println("PHILOSOPHER THREAD CREATED with #### " + t.getName());
    }

  }

  public static void main(String[] args) {

    getSingleton().startThreads(args);


  }

}
