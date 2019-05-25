package com.xomute.utils;

import com.sun.istack.internal.Nullable;
import com.xomute.compiler.Compiler;
import com.xomute.compiler.commands.*;
import com.xomute.compiler.directives.ENDS;
import com.xomute.compiler.directives.SEGMENT;
import com.xomute.compiler.identifiers.DB;
import com.xomute.compiler.identifiers.DD;
import com.xomute.compiler.identifiers.DW;
import com.xomute.compiler.interfaces.Command;
import com.xomute.compiler.interfaces.DataIdentifier;
import com.xomute.compiler.interfaces.Directive;
import com.xomute.lexer.SourceLine;
import com.xomute.lexer.lexems.Identifier;
import com.xomute.lexer.lexems.Macro;
import com.xomute.lexer.segments.Segment;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.xomute.utils.StringConstants.*;

public class AssemblerHelper {

  private static final List<String> DATA_IDENTIFIERS = Arrays.asList("DB", "DW", "DD");
  private static final List<String> REGISTERS_16BIT =
      Arrays.asList("AX", "BX", "CX", "DX", "DI", "SI", "BP", "SP");
  private static final List<String> REGISTERS_8BIT =
      Arrays.asList("AL", "AH", "BL", "BH", "CL", "CH", "DL", "DH");
  private static final List<String> SEGMENT_REGISTERS = Arrays.asList("DS", "CS", "ES");
  private static final List<String> DIRECTIVES =
      Arrays.asList("SEGMENT", "ENDS", "MACRO", "ENDM", "END");
  static final List<String> ONE_SYMBOL_LEXEMS = Arrays.asList(":", "]", "[", ",");

  private static final List<String> USER_IDENTIFIERS = new ArrayList<>();
  private static final List<Macro> MACRO_LIST = new ArrayList<>();

  private static final List<Segment> SEGMENTS = new ArrayList<>();

  private static Map<String, String> REG =
      new HashMap<String, String>() {
        {
          put("AL", "000");
          put("AX", "000");
          put("CL", "001");
          put("CX", "001");
          put("DL", "010");
          put("DX", "010");
          put("BL", "011");
          put("BX", "011");
          put("AH", "100");
          put("SP", "100");
          put("CH", "101");
          put("BP", "101");
          put("DH", "110");
          put("SI", "110");
          put("BH", "111");
          put("DI", "111");
        }
      };

  private static Map<String, String> RM =
      new HashMap<String, String>() {
        {
          put("AL", "000");
          put("AX", "000");
          put("CL", "001");
          put("CX", "001");
          put("DL", "010");
          put("DX", "010");
          put("BL", "011");
          put("BX", "011");
          put("AH", "100");
          put("SP", "100");
          put("CH", "101");
          put("BP", "101");
          put("DH", "110");
          put("SI", "110");
          put("BH", "111");
          put("DI", "111");
        }
      };

  public enum Type {
    COMMAND,
    DATA_IDENTIFIER,
    USER_IDENTIFIER,
    REGISTER16,
    REGISTER8,
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
        case REGISTER16:
          return REGISTER16_STR;
        case REGISTER8:
          return REGISTER8_STR;
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

  public static boolean isWrongCommand(String command) {
    return Arrays.stream(CommandType.values())
        .map(Enum::toString)
        .anyMatch(_command -> _command.equals(command));
  }

  public static String getModRM(String reg, String rm) {
    String modrm = getMod(rm);
    modrm += REG.get(reg);
    modrm += getRm(rm);
    try {
      return binToHex(modrm);
    } catch (NumberFormatException ignored) {
    }
    return "ERRORinGetModRM";
  }

  private static String getMod(String rm) {
    if (isImm16(rm)) {
      return "00";
    } else if (isRegister8(rm) || isRegister16(rm)) {
      return "11";
    } else if (isEffectiveAddress(rm)) {
      return "10";
    } else {
      return "ERRORinGetMod";
    }
  }

  private static String getRm(String rm) {
    if (RM.get(rm) != null) {
      return RM.get(rm);
    } else {
      if (isEffectiveAddress(rm)) {
        if (isBX(rm)) {
          return "111";
        }

        if (isBP(rm)) {
          return "110";
        }

        if (isSI(rm)) {
          return "100";
        }

        if (isDI(rm)) {
          return "101";
        }
      }

      if (isImm16(rm)) {
        return "110";
      }
    }
    return "ErrorInGetRM";
  }

  private static boolean isEffectiveAddress(String ea) {
    return ea.contains("[");
  }

  private static boolean isBX(String rm) {
    return rm.substring(rm.indexOf("[") + 1, rm.indexOf("]")).equals("BX");
  }

  private static boolean isBP(String rm) {
    return rm.substring(rm.indexOf("[") + 1, rm.indexOf("]")).equals("BP");
  }

  private static boolean isSI(String rm) {
    return rm.substring(rm.indexOf("[") + 1, rm.indexOf("]")).equals("SI");
  }

  private static boolean isDI(String rm) {
    return rm.substring(rm.indexOf("[") + 1, rm.indexOf("]")).equals("DI");
  }

  public static boolean isIdentifier16(String operand) {
    operand = getIdentifierNameFromOperand(operand);

    return getIdentifier(operand).map(Identifier::isHex).orElse(false);
  }

  public static String getIdentifierNameFromOperand(String operand) {
    if (operand.contains(":")) {
      operand = operand.substring(operand.indexOf(":") + 1);
    }
    operand = operand.substring(0, operand.indexOf("["));
    return operand;
  }

  public static String getPrefix(String operand) {
    String prefix = "";
    String identifierName = getIdentifierNameFromOperand(operand);
    if (operand.contains(":")) {
      if (getIdentifierSegment(identifierName).equals("CS")) {
        prefix = "2E: ";
      } else {
        if (isBP(operand)) {
          prefix = "3E: ";
        }
      }
    } else {
      if (getIdentifierSegment(identifierName).equals("CS")) {
        prefix = "2E: ";
      } else {
        if (isBP(operand)) {
          prefix = "3E: ";
        }
      }
    }
    return prefix;
  }

  public static Optional<Identifier> getIdentifier(String identifierName) {
    for (Segment segment : SEGMENTS) {
      if (segment.hasIdentifier(identifierName)) {
        return segment.getIdentifier(identifierName);
      }
    }
    if (Compiler.getCurrentSegment().hasIdentifier(identifierName)) {
      return Compiler.getCurrentSegment().getIdentifier(identifierName);
    }

    return Optional.empty();
  }

  public static String getIdentifierSegment(String identifierName) {
    if (SEGMENTS.get(0).hasIdentifier(identifierName)) {
      return "DS";
    } else return "CS";
  }

  private static boolean isImm8(String number) {
    try {
      if (number.charAt(number.length() - 1) == 'H') {
        number = number.substring(0, number.length() - 1);
        return Long.parseLong(number, 16) < Math.pow(2, 8);
      }
      if (number.charAt(number.length() - 1) == 'B') {
        return Long.parseLong(number, 2) < Math.pow(2, 8);
      }
      return Long.parseLong(number) < Math.pow(2, 8);
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private static boolean isImm16(String number) {
    try {
      if (number.charAt(number.length() - 1) == 'H') {
        number = number.substring(0, number.length() - 1);
        return Long.parseLong(number, 16) < Integer.MAX_VALUE;
      }
      if (number.charAt(number.length() - 1) == 'B') {
        return Long.parseLong(number, 2) < Integer.MAX_VALUE;
      }
      return Long.parseLong(number) < Integer.MAX_VALUE;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static Command getCommand(String mnemocode, String line) {
    List<String> operands = StringUtils.splitByOperands(line);
    switch (mnemocode) {
      case "MOV":
        return new MOV(operands.get(1), operands.get(2));
      case "NOT":
        return new NOT(operands.get(1));
      case "CBW":
        return new CBW();
      case "CMP":
        return new CMP(operands.get(1), operands.get(2));
      case "JBE":
        return new JBE(operands.get(1));
      case "LSS":
        return new LSS(operands.get(1), operands.get(2));
      case "SBB":
        return new SBB(operands.get(1), operands.get(2));
      case "BTS":
        return new BTS(operands.get(1), operands.get(2));
      default:
        return new WrongCommand();
    }
  }

  public static DataIdentifier getDataIdentifier(String mnemocode, String line) {
    List<String> operands = StringUtils.splitByOperands(line);
    Identifier identifier = new Identifier();
    identifier.setName(operands.get(0));
    identifier.setOffset(Compiler.getOffset());

    switch (mnemocode) {
      case "DB":
        identifier.setHex(false);
        Compiler.getCurrentSegment().addIdentifier(identifier);
        return new DB(operands.get(0), operands.get(2));
      case "DW":
        identifier.setHex(true);
        Compiler.getCurrentSegment().addIdentifier(identifier);
        return new DW(operands.get(0), operands.get(2));
      case "DD":
        identifier.setHex(true);
        Compiler.getCurrentSegment().addIdentifier(identifier);
        return new DD(operands.get(0), operands.get(2));
      default:
        return null; // unreachable
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

  public static void saveSegment(Segment segment) {
    SEGMENTS.add(segment);
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
    } else if (isRegister16(lexem)) {
      return Type.REGISTER16;
    } else if (isRegister8(lexem)) {
      return Type.REGISTER8;
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

  public static String binToHex(String bin) {
    int dec = Integer.parseInt(bin, 2);
    return Integer.toHexString(dec).toUpperCase();
  }

  public static String decToHex(String num) {
    int dec = Integer.parseInt(num);
    return Integer.toHexString(dec).toUpperCase();
  }

  public static String subHexFromHex(String mainHex, String subbedHex) {
    int mainDec = Integer.parseInt(mainHex, 16);
    int subbedDec = Integer.parseInt(subbedHex, 16);
    return Integer.toHexString(mainDec - subbedDec).toUpperCase();
  }

  public static String addHexToHex(String hex1, String hex2) {
    int hex1Dec = Integer.parseInt(hex1, 16);
    int hex2Dec = Integer.parseInt(hex2, 16);
    return Integer.toHexString(hex1Dec + hex2Dec).toUpperCase();
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

  public static boolean isRegister16(String word) {
    return REGISTERS_16BIT.contains(word);
  }

  public static boolean isRegister8(String word) {
    return REGISTERS_8BIT.contains(word);
  }

  public static boolean isSegmentRegister(String word) {
    return SEGMENT_REGISTERS.contains(word);
  }

  private static boolean isOneSymbolLexem(String word) {
    return ONE_SYMBOL_LEXEMS.contains(word);
  }

  public static boolean isBinConstant(String word) {
    return Pattern.matches("[01]+B", word);
  }

  public static boolean isDecConstant(String word) {
    return Pattern.matches("\\d+", word);
  }

  public static boolean isHexConstant(String word) {
    if (word.length() < 2) return false;
    if (Pattern.matches("[A-F]", "" + word.charAt(0))
        || (Pattern.matches("[A-F]", "" + word.charAt(1)) && word.charAt(0) != '0')) {
      return false;
    }
    return Pattern.matches("[0-9A-F]+H", word);
  }

  public static boolean isStringConstant(String word) {
    if (word.length() < 2) return false;
    return word.charAt(0) == '\"' && word.charAt(word.length() - 1) == '\"';
  }

  public static boolean isDirective(String word) {
    return DIRECTIVES.contains(word);
  }

  public static boolean isMacroCall(String word) {
    return MACRO_LIST.stream().map(Macro::getName).anyMatch(macro -> macro.equals(word));
  }

  public static boolean isLabel(String line) {
    return line.trim().endsWith(":");
  }
}
