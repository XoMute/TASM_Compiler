package com.xomute.utils;

import jdk.nashorn.internal.runtime.regexp.RegExpFactory;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.xomute.utils.StringConstants.*;

public class AssemblerHelper {

  private static final List<String> definedIdentifierTypes = Arrays.asList("DB", "DW", "DD");
  private static final List<String> definedRegisters =
      Arrays.asList("AX", "BX", "CX", "DX", "DI", "SI", "BP");
  private static final List<String> definedSegmentRegisters = Arrays.asList("DS", "CS", "ES");
  private static final List<String> definedDerectives =
      Arrays.asList("SEGMENT", "ENDS", "MACRO", "ENDM", "END");
  public static final List<String> ONE_SYMBOL_LEXEMS = Arrays.asList(":", "]", "[", ",");

  public enum Type {
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
    NOT_DEFINED;

    public static String convertToString(Type type) {
      switch (type) {
        case COMMAND:
          return COMMAND_STR;
        case IDENTIFIER:
          return DATA_IDENTIFIER_STR; // todo: replace me
        case REGISTER:
          return REGISTER_STR;
        case BIN_CONSTANT:
          return BIN_CONSTANT_STR;
        case DEC_CONSTANT:
          return DEC_CONSTANT_STR;
        case HEX_CONSTANT:
          return HEX_CONSTANT_STR;
        case STRING_CONSTANT:
          return TEXT_CONSTANT_STR;
        case DIRECTIVE:
          return DIRECTIVE_STR;
        case LABEL:
          return USER_DEFINED_IDENTIFIER_STR; // todo: replace me
        case MACRO_CALL:
          return MACRO_STR;
        default:
          return "ERROR";
      }
    }
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

  private static boolean isCommand(String word) {
    return Arrays.stream(CommandType.values())
        .map(Enum::toString)
        .collect(Collectors.toList())
        .contains(word);
  }

  @Deprecated
  private static boolean isIdentifier(String word) {
    return false;
  }

  @Deprecated
  private static boolean isRegister(String word) {
    return false;
  }

  private static boolean isBinConstant(String word) {
    return Pattern.matches("[01]+B", word);
  }

  private static boolean isDecConstant(String word) {
    try {
      Integer.valueOf(word);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private static boolean isHexConstant(String word) {
    if (Pattern.matches("[A-F]", "" + word.charAt(0))
        || (Pattern.matches("[A-F]", "" + word.charAt(1)) && word.charAt(0) != '0')) {
      return false;
    }
    return Pattern.matches("[0-9A-F]+H", word);
  }

  @Deprecated
  private static boolean isStringConstant(String word) {
    return false;
  }

  @Deprecated
  private static boolean isDirective(String word) {
    return false;
  }

  @Deprecated
  private static boolean isLabel(String word) {
    return false;
  }

  @Deprecated
  private static boolean isMacroCall(String word) {
    return false;
  }
}
