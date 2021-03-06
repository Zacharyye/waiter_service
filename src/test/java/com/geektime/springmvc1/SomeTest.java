package com.geektime.springmvc1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SomeTest {
  @Test
  public void test1() {
    Arrays.asList("Foo","Bar").stream()
        .filter(s -> s.equalsIgnoreCase("foo"))
        .map(s -> s.toUpperCase())
        .forEach(System.out::println);
    Arrays.stream(new String[] {"s1","s2","s3"})
        .map(s -> Arrays.asList(s))
        .flatMap(l -> l.stream())
        .forEach(System.out::println);
  }
}
