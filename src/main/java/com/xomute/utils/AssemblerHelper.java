package com.xomute.utils;

import java.util.Arrays;
import java.util.List;

public class AssemblerHelper {

  private static final List<String> definedIdentifierTypes = Arrays.asList("DB", "DW", "DD");
  private static final List<String> definedRegisters =
      Arrays.asList("AX", "BX", "CX", "DX", "DI", "SI", "BP");
  private static final List<String> definedSegmentRegisters = Arrays.asList("DS", "CS", "ES");
  private static final List<String> definedDerectives =
      Arrays.asList("SEGMENT", "ENDS", "MACRO", "ENDM", "END");
  public static final List<String> ONE_SYMBOL_LEXEMS = Arrays.asList(":", "]", "[", ",");

  static final String COMMAND_STRING_ANALISE = "КОМАНДА";
  static final String REFISTER_STRING_ANALISE = "РЕГІСТР 16-И РОЗРЯДНИЙ";
  static final String SEGMENT_REGISTER_STRING_ANALISE = "СЕГМЕНТНИЙ РЕГІСТР";
  static final String TEXT_CONSTANT_STRING_ANALISE = "ТЕКСТОВА КОНСТАНТА";
  static final String SIX_CONSTANT_STRING_ANALISE = "ШІСТНАДЦЯТКОВА КОНСТАНТА";
  static final String DIRECTIV_STRING_ANALISE = "ДИРЕКТИВА";
  static final String IDENTIFICATOR_DATA_STRING_ANALISE = "ІДЕНТИФІКАТОР ДАНИХ";
  static final String IDENTIFICATOR_USER_STRING_ANALISE = "ІДЕНТИФІКАТОР КОРИСТУВАЧА (ВИЗНАЧЕНИЙ)";
  static final String ONE_SYMB_LEKSEMA_STRING_ANALISE = "ОДНОСИМВОЛЬНА ЛЕКСЕМА";
  static final String IDENTIFICATOR_DONT_DEFINED_STRING_ANALISE =
      "ІДЕНТИФІКАТОР КОРИСТУВАЧА (НЕ ВИЗНАЧЕНИЙ)";
  static final String MACRO = "МАКРОС";

  enum Type {
    COMMAND,
    IDENTIFIER,
    REGISTER,
    BIN_CONSTANT,
    DEC_CONSTANT,
    HEX_CONSTANT,
    STRING_CONSTANT,
    DIRECTIVE,
    LABEL,
    MACRO_CALL,
    NOT_DEFINED,
  }

  enum Directive {
    SEGMENT,
    MACRO,
    ENDM,
    ENDS,
    END,
    WRONG_DERECTIVE
  }

  enum CommandType {
    MOV,
    NOT,
    CBW,
    CMP,
    JBE,
    LSS,
    SBB,
    BTS,
    WRONG_COMMAND
  }

  public static boolean containsOneSymbLexems(String line) {
    for (String lexem : ONE_SYMBOL_LEXEMS) {
      if (line.contains(lexem)) {
        return true;
      }
    }
    return false;
  }

  public static Type getType(String word) {
    if (isCommand(word)) {
      return Type.COMMAND;
    } else if (isIdentifier(word)) {
      return Type.IDENTIFIER;
    } else if (isRegister(word)) {
      return Type.REGISTER;
    } else if (isBinConstant(word)) {
      return Type.BIN_CONSTANT;
    } else if (isDecConstant(word)) {
      return Type.DEC_CONSTANT;
    } else if (isHexConstant(word)) {
      return Type.HEX_CONSTANT;
    } else if (isStringConstant(word)) {
      return Type.STRING_CONSTANT;
    } else if (isDirective(word)) {
      return Type.DIRECTIVE;
    } else if (isLabel(word)) {
      return Type.LABEL;
    } else if (isMacroCall(word)) {
      return Type.MACRO_CALL;
    } else return Type.NOT_DEFINED;
  }

  private static boolean isCommand(String word) {}

  private static boolean isIdentifier(String word) {}

  private static boolean isRegister(String word) {}

  private static boolean isBinConstant(String word) {}

  private static boolean isDecConstant(String word) {}

  private static boolean isHexConstant(String word) {}

  private static boolean isStringConstant(String word) {}

  private static boolean isDirective(String word) {}

  private static boolean isLabel(String word) {}

  private static boolean isMacroCall(String word) {}
}
