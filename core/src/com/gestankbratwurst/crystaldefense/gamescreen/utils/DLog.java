package com.gestankbratwurst.crystaldefense.gamescreen.utils;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of crystaldefense and was created at the 13.06.2020
 *
 * crystaldefense can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class DLog {

  @Getter
  private static boolean enabled;
  @Getter
  private static boolean logStreamEnabled;
  private static PrintStream logStream;
  private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
  private static final ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<>();
  private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
  private static int counter = 0;

  private static void flushPrint() {
    while (!logQueue.isEmpty()) {
      String line = logQueue.poll();
      if (line == null) {
        return;
      }
      logStream.println(line);
    }
  }

  private static void logPrint() {
    long start = System.currentTimeMillis();
    while (!logQueue.isEmpty()) {
      String line = logQueue.poll();
      if (line == null || System.currentTimeMillis() - start > 950) {
        break;
      }
      logStream.println(line);
    }
    if (counter++ % 60 == 0) {
      logStream.flush();
    }
  }

  public static void setEnabled(boolean value) {
    enabled = value;
  }

  public static void setLogStreamEnabled(boolean value) {
    if (value) {
      if (logStream == null) {
        try {
          logStream = new PrintStream(new BufferedOutputStream(new FileOutputStream("latest.log")));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
      executorService.scheduleAtFixedRate(DLog::logPrint, 0, 1, TimeUnit.SECONDS);
    } else {
      executorService.shutdown();
    }
    logStreamEnabled = value;
  }

  public static void log(Object object) {
    LocalTime time = LocalTime.now();
    if (object instanceof Iterable) {

      String output = "[" + timeFormatter.format(time) + "] " + "Multilog:";
      System.out.println(output);
      if (logStreamEnabled) {
        logQueue.add(output);
      }

      ((Iterable<?>) object).forEach(e -> {
        String line = "  > " + e.toString();
        System.out.println(line);
        if (logStreamEnabled) {
          logQueue.add(line);
        }
      });
    } else {
      String output = "[" + timeFormatter.format(time) + "] " + object.toString();
      System.out.println(output);
      if (logStreamEnabled) {
        logQueue.add(output);
      }
    }
  }

  public static void close() {
    flushPrint();
    if (logStream != null) {
      logStream.close();
    }
    executorService.shutdownNow();
  }

}
