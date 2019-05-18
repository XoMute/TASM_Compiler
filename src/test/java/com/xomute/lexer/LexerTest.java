package com.xomute.lexer;

import com.xomute.utils.StringConstants;
import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

  @Test
  public void scanOneLine() {
    Lexer lexer = new Lexer();
    assertTrue(lexer.scanOneLine("NOT AX").contains("s"));
    assertEquals("", lexer.scanOneLine("CMP  AX, DX"));
    assertEquals("", lexer.scanOneLine("MOV  MYW1[BP], 0CH"));
    assertEquals("", lexer.scanOneLine("M1:"));
    assertEquals("", lexer.scanOneLine("MYMC2 DS:MYDW1[BP]"));
  }
}