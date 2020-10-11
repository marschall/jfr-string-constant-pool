package com.github.marschall.jfr.stringconstantpool;

import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jdk.jfr.Event;
import jdk.jfr.Recording;

public final class Demo {

  private static final String STRING_ATTRIBUTE_VALUE = "a long and unique string that is easy to find the the recording";

  public static void main(String[] args) throws IOException {
    Path recordingPath = Path.of(Demo.class.getSimpleName() + "-JDK-" + Runtime.version().feature() + ".jfr");
    Recording recording = startRecording(recordingPath);
    generateEvents();
    stopRecording(recording);
    int occurrences = readRecording(recordingPath);
    System.out.println("String occurrences in file: " + occurrences);
  }

  private static Recording startRecording(Path recordingPath) throws IOException {
    Recording recording = new Recording();
    recording.enable(CustomEvent.class);
    recording.setMaxSize(1L * 1024L * 1024L);
    recording.setToDisk(true);
    recording.setDestination(recordingPath);
    recording.start();
    return recording;
  }

  private static void generateEvents() {
    for (int i = 0; i < 10; i++) {
      CustomEvent event = new CustomEvent();
      event.stringAttribute = STRING_ATTRIBUTE_VALUE;
      event.commit();
    }
  }

  private static void stopRecording(Recording recording) {
    recording.stop();
  }

  private static int readRecording(Path recordingPath) throws IOException {
    byte[] content = Files.readAllBytes(recordingPath);
    return countOccurrences(content, STRING_ATTRIBUTE_VALUE.getBytes(US_ASCII));
  }

  static int countOccurrences(byte[] a, byte[] pattern) {
    int occurrences = 0;
    for (int i = 0; i < a.length; i++) {
      byte b = a[i];
      if (b == pattern[0]) {
        boolean match = true;
        for (int j = 1; (j < pattern.length) && ((j + i) < a.length); j++) {
          if (pattern[j] !=  a[j + i]) {
            match = false;
            break;
          }
        }
        if (match) {
          occurrences += 1;
        }
      }
    }
    return occurrences;
  }

  static class CustomEvent extends Event {

    String stringAttribute;

  }

}
