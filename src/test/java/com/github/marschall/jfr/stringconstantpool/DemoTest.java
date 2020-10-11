package com.github.marschall.jfr.stringconstantpool;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DemoTest {

  @Test
  void testCountOccurrences() {
    byte[] content = new byte[] {0, 1, 2, 1, 0, 1, 2, 0};
    byte[] pattern = new byte[] {1, 2};
    int occurrences = Demo.countOccurrences(content, pattern);
    assertEquals(2, occurrences);
  }

}
