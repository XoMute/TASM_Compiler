package com.xomute.lexer;

import com.xomute.utils.AssemblerHelper;
import org.junit.Test;

import java.util.Arrays;

import static com.xomute.utils.StringConstants.*;
import static org.junit.Assert.*;

public class LexerTest {

  @Test
  public void scanOneLine() {
    Lexer lexer = new Lexer();
    assertEquals(
        Arrays.asList(AssemblerHelper.Type.COMMAND, AssemblerHelper.Type.REGISTER),
        lexer.scanOneLine("NOT AX"));
    assertEquals(
        Arrays.asList(
            AssemblerHelper.Type.COMMAND,
            AssemblerHelper.Type.REGISTER,
            AssemblerHelper.Type.ONE_SYMBOL_LEXEM,
            AssemblerHelper.Type.REGISTER),
        lexer.scanOneLine("CMP  AX, DX"));
    assertEquals(
        Arrays.asList(
            AssemblerHelper.Type.COMMAND,
            AssemblerHelper.Type.NOT_DEFINED,
            AssemblerHelper.Type.ONE_SYMBOL_LEXEM,
            AssemblerHelper.Type.REGISTER,
            AssemblerHelper.Type.ONE_SYMBOL_LEXEM,
            AssemblerHelper.Type.ONE_SYMBOL_LEXEM,
            AssemblerHelper.Type.HEX_CONSTANT),
        lexer.scanOneLine("MOV  MYW1[BP], 0CH"));
    assertEquals(
        Arrays.asList(AssemblerHelper.Type.NOT_DEFINED,
            AssemblerHelper.Type.ONE_SYMBOL_LEXEM),
        lexer.scanOneLine("M1:"));
    assertEquals(
        Arrays.asList(
            AssemblerHelper.Type.MACRO_CALL,
            AssemblerHelper.Type.SEGMENT_REGISTER,
            AssemblerHelper.Type.ONE_SYMBOL_LEXEM,
            AssemblerHelper.Type.NOT_DEFINED,
            AssemblerHelper.Type.ONE_SYMBOL_LEXEM,
            AssemblerHelper.Type.REGISTER,
            AssemblerHelper.Type.ONE_SYMBOL_LEXEM),
        lexer.scanOneLine("MYMC2 DS:MYDW1[BP]"));
  }

  @Test
  public void scanOneWord() {
    Lexer lexer = new Lexer();
    assertEquals(HEX_CONSTANT_STR, lexer.scanOneWord("02H"));
    assertEquals(HEX_CONSTANT_STR, lexer.scanOneWord("0AH"));
    assertEquals(HEX_CONSTANT_STR, lexer.scanOneWord("229H"));
    assertEquals(DEC_CONSTANT_STR, lexer.scanOneWord("229"));
    assertEquals(DEC_CONSTANT_STR, lexer.scanOneWord("224"));
    assertNotEquals(DEC_CONSTANT_STR, lexer.scanOneWord("224H"));
    assertEquals(BIN_CONSTANT_STR, lexer.scanOneWord("100101010B"));
    assertEquals(BIN_CONSTANT_STR, lexer.scanOneWord("1B"));
    assertNotEquals(BIN_CONSTANT_STR, lexer.scanOneWord("1"));
    assertNotEquals(BIN_CONSTANT_STR, lexer.scanOneWord("B"));
    assertEquals(COMMAND_STR, lexer.scanOneWord("MOV"));
    assertEquals(REGISTER_STR, lexer.scanOneWord("AX"));
    assertEquals(SEGMENT_REGISTER_STR, lexer.scanOneWord("DS"));
    assertEquals(COMMAND_STR, lexer.scanOneWord("NOT"));
    assertEquals(COMMAND_STR, lexer.scanOneWord("LSS"));
    assertEquals(REGISTER_STR, lexer.scanOneWord("BP"));
    assertEquals(ONE_SYMBOL_LEXEM_STR, lexer.scanOneWord("["));
    assertEquals(ONE_SYMBOL_LEXEM_STR, lexer.scanOneWord("]"));
    assertEquals(ONE_SYMBOL_LEXEM_STR, lexer.scanOneWord(","));
    assertEquals(ONE_SYMBOL_LEXEM_STR, lexer.scanOneWord(":"));
  }
}
