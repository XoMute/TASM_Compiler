package com.xomute.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class StringUtilsTest {

  @Test
  public void split() {
    assertEquals(Arrays.asList("MOV", "AX", ",", "BX"), StringUtils.split("MOV  AX, BX"));
    assertEquals(
        Arrays.asList("STRING", "DB", "\"Some String Constant\""),
        StringUtils.split("STRING DB \"Some String Constant\""));
    assertEquals(Arrays.asList("LSS", "DX", ",", "STR"), StringUtils.split("  LSS  DX,    STR"));
    assertEquals(Arrays.asList("M2", ":"), StringUtils.split("M2:     "));
    assertEquals(Arrays.asList("MOV", "MYW1", "[", "BP", "]", ",", "0CH"), StringUtils.split("MOV MYW1[BP], 0CH"));
    assertEquals(Arrays.asList("MYMC2", "DS", ":", "MYDW1", "[", "BP", "]"), StringUtils.split("MYMC2 DS:MYDW1[BP]"));
  }
}