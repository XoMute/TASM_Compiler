package com.xomute.utils;

import com.sun.istack.internal.Nullable;
import com.xomute.compiler.interfaces.Command;
import com.xomute.compiler.interfaces.DataIdentifier;
import com.xomute.compiler.interfaces.Directive;
import com.xomute.compiler.commands.*;
import com.xomute.compiler.directives.ENDS;
import com.xomute.compiler.directives.SEGMENT;
import com.xomute.compiler.identifiers.DB;
import com.xomute.compiler.identifiers.DD;
import com.xomute.compiler.identifiers.DW;
import com.xomute.lexer.SourceLine;
import com.xomute.lexer.lexems.Macro;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.xomute.utils.StringConstants.*;

public class AssemblerHelper {

  private static final List<String> DATA_IDENTIFIERS = Arrays.asList("DB", "DW", "DD");
  private static final List<String> REGISTERS =
      Arrays.asList("AX", "BX", "CX", "DX", "DI", "SI", "BP");
  private static final List<String> SEGMENT_REGISTERS = Arrays.asList("DS", "CS", "ES");
  private static final List<String> DIRECTIVES =
      Arrays.asList("SEGMENT", "ENDS", "MACRO", "ENDM", "END");
  static final List<String> ONE_SYMBOL_LEXEMS = Arrays.asList(":", "]", "[", ",");

  private static final List<String> USER_IDENTIFIERS = new ArrayList<>();
  private static final List<Macro> MACRO_LIST = new ArrayList<>();

  public enum Type {
    COMMAND,
    DATA_IDENTIFIER,
    USER_IDENTIFIER,
    REGISTER,
    SEGMENT_REGISTER,
    ONE_SYMBOL_LEXEM,
    BIN_CONSTANT,
    DEC_CONSTANT,
    HEX_CONSTANT,
    STRING_CONSTANT,
    DIRECTIVE,
    MACRO_CALL,
    NOT_DEFINED;

    public static String convertToString(Type type) {
      switch (type) {
        case ONE_SYMBOL_LEXEM:
          return ONE_SYMBOL_LEXEM_STR;
        case COMMAND:
          return COMMAND_STR;
        case USER_IDENTIFIER:
          return USER_DEFINED_IDENTIFIER_STR;
        case DATA_IDENTIFIER:
          return DATA_IDENTIFIER_STR;
        case REGISTER:
          return REGISTER_STR;
        case SEGMENT_REGISTER:
          return SEGMENT_REGISTER_STR;
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
        case MACRO_CALL:
          return MACRO_STR;
        default:
          return USER_UNDEFINED_IDENTIFIER_STR;
      }
    }
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

  public static Command getCommand(String mnemocode) {
    switch (mnemocode) {
      case "MOV":
        return new MOV();
      case "NOT":
        return new NOT();
      case "CBW":
        return new CBW();
      case "CMP":
        return new CMP();
      case "JBE":
        return new JBE();
      case "LSS":
        return new LSS();
      case "SBB":
        return new SBB();
      case "BTS":
        return new BTS();
      default:
        return null;
    }
  }

  public static DataIdentifier getDataIdentifier(String mnemocode) {
    switch (mnemocode) {
      case "DB":
        return new DB();
      case "DW":
        return new DW();
      case "DD":
        return new DD();
      default:
        return null;
    }
  }

  public static Directive getDirective(String mnemocode) {
    switch (mnemocode) {
      case "SEGMENT":
        return new SEGMENT();
      case "ENDS":
        return new ENDS();
      default:
        return null;
    }
  }

  public static boolean containsOneSymbLexems(String line) {
    for (String lexem : ONE_SYMBOL_LEXEMS) {
      if (line.contains(lexem)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isName(String name) {
    switch (processLexem(name, true)) {
      case USER_IDENTIFIER:
      case NOT_DEFINED:
        return true;
      default:
        return false;
    }
  }

  public static boolean isMnemocode(String mnemocode) {
    return (isDataIdentifier(mnemocode) || isDirective(mnemocode) || isCommand(mnemocode));
  }

  /**
   * this method adds macro to macro list and removes it's name from identifiers list
   *
   * @param macro macro
   */
  public static void addMacro(Macro macro) {
    MACRO_LIST.add(macro);
    USER_IDENTIFIERS.remove(macro.getName());
  }

  public static List<SourceLine> callMacro(String name, @Nullable String param) {
    for (Macro macro : MACRO_LIST) {
      if (macro.getName().equals(name)) {
        return macro.call(param);
      }
    }
    System.out.println(
        "SOME VERY BAD ERROR DELETE ME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    return Collections.emptyList();
  }

  /** usage of some pattern (I forgot it's name) */
  public static Type processLexem(String lexem) {
    return processLexem(lexem, false);
  }

  /**
   * @param lexem lexem to parse
   * @param print if true, this method will act as pure function (no outer changes)
   * @return type of lexem
   */
  public static Type processLexem(String lexem, boolean print) {
    if (isCommand(lexem)) {
      return Type.COMMAND;
    } else if (isUserIdentifier(lexem)) {
      return Type.USER_IDENTIFIER;
    } else if (isDataIdentifier(lexem)) {
      return Type.DATA_IDENTIFIER;
    } else if (isRegister(lexem)) {
      return Type.REGISTER;
    } else if (isSegmentRegister(lexem)) {
      return Type.SEGMENT_REGISTER;
    } else if (isOneSymbolLexem(lexem)) {
      return Type.ONE_SYMBOL_LEXEM;
    } else if (isBinConstant(lexem)) {
      return Type.BIN_CONSTANT;
    } else if (isDecConstant(lexem)) {
      return Type.DEC_CONSTANT;
    } else if (isHexConstant(lexem)) {
      return Type.HEX_CONSTANT;
    } else if (isStringConstant(lexem)) {
      return Type.STRING_CONSTANT;
    } else if (isDirective(lexem)) {
      return Type.DIRECTIVE;
    } else if (isMacroCall(lexem)) {
      return Type.MACRO_CALL;
    } else {
      if (!print) {
        USER_IDENTIFIERS.add(lexem);
      }
      return Type.NOT_DEFINED;
    }
  }

  public static boolean isCommand(String word) {
    return Arrays.stream(CommandType.values())
        .map(Enum::toString)
        .collect(Collectors.toList())
        .contains(word);
  }

  private static boolean isUserIdentifier(String word) {
    return USER_IDENTIFIERS.contains(word);
  }

  public static boolean isDataIdentifier(String word) {
    return DATA_IDENTIFIERS.contains(word);
  }

  private static boolean isRegister(String word) {
    return REGISTERS.contains(word);
  }

  private static boolean isSegmentRegister(String word) {
    return SEGMENT_REGISTERS.contains(word);
  }

  private static boolean isOneSymbolLexem(String word) {
    return ONE_SYMBOL_LEXEMS.contains(word);
  }

  private static boolean isBinConstant(String word) {
    return Pattern.matches("[01]+B", word);
  }

  private static boolean isDecConstant(String word) {
    return Pattern.matches("\\d+", word);
  }

  private static boolean isHexConstant(String word) {
    if (word.length() < 2) return false;
    if (Pattern.matches("[A-F]", "" + word.charAt(0))
        || (Pattern.matches("[A-F]", "" + word.charAt(1)) && word.charAt(0) != '0')) {
      return false;
    }
    return Pattern.matches("[0-9A-F]+H", word);
  }

  private static boolean isStringConstant(String word) {
    if (word.length() < 2) return false;
    return word.charAt(0) == '\"' && word.charAt(word.length() - 1) == '\"';
  }

  private static boolean isDirective(String word) {
    return DIRECTIVES.contains(word);
  }

  public static boolean isMacroCall(String word) {
    return MACRO_LIST.stream().map(Macro::getName).anyMatch(macro -> macro.equals(word));
  }
}
