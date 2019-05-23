package com.xomute.compiler;

import com.xomute.compiler.interfaces.Mnemocode;
import com.xomute.lexer.SourceLine;

import java.util.List;
import java.util.Optional;

public class Compiler {

  /**
   * TODO list for tomorrow:
   *  - remember offset of label;
   *  - implement JBE
   *  - figure out how to work with MOV (the hard and long one);
   *  - implement MOV
   *  - implement SBB (will be easy after MOV)
   *  - implement LSS (same)
   *  - chill;
   */

  public void compileToFile(List<SourceLine> srcLines) {
    compile(srcLines);
  }

  public void compileToConsole(List<SourceLine> srcLines) {
    compile(srcLines);
    int i = 1;
    for (SourceLine srcLine : srcLines) {
      System.out.println(String.format("%3d\t", i++) + convertToLSTString(srcLine));
    }
  }

  private void compile(List<SourceLine> srcLines) {
    firstGoThrough(srcLines);
    secondGoThrough(srcLines);
  }



  ///////////////// FIRST GO THROUGH ////////////////////////

  /** this method adds offset in each SourceLine */
  private void firstGoThrough(List<SourceLine> srcLines) {
    int offset = 0;
    for (SourceLine srcLine : srcLines) {
      if (srcLine.isSkipByCompiler()) continue;
      srcLine.setOffset(this.getOffset(offset));

      Optional<Mnemocode> opt = srcLine.getMnemocode();

      if (!opt.isPresent()) {
        continue;
      }
      Mnemocode mnemocode = opt.get();

      if (mnemocode.getOffset() == -1) { // means this mnemocode is 'ENDS'
        offset = 0;
      } else {
        offset += mnemocode.getOffset();
      }
    }
  }

  private String getOffset(int offset) {
    StringBuilder strOffset = new StringBuilder(Integer.toHexString(offset));
    while (strOffset.length() < 4) {
      strOffset.insert(0, "0");
    }
    return strOffset.toString().toUpperCase();
  }

  ///////////////////////////////////////////////////////////

  ///////////////// SECOND GO THROUGH ///////////////////////

  /** this method adds byteCode in each SourceLine */
  private void secondGoThrough(List<SourceLine> srcLines) {}

  ///////////////////////////////////////////////////////////

  ///////////////////////////////////// PRINTING /////////////////////////////////////

  private String convertToLSTString(SourceLine srcLine) {
    StringBuilder line = new StringBuilder();
    String code = srcLine.getByteCode();
    // this if is created only for beautifying string constant bytecode
    if (code != null && code.split(" ").length > 7) {
      return beautifyStringConstant(srcLine);
    } else {
      String offset = "";
      if (!srcLine.isSkipByCompiler()) {
        offset = srcLine.getOffset();
      }
      line = new StringBuilder(String.format("\t%4s\t %-16s\t %s", offset, code, srcLine.getLine()));
    }
    return line.toString();
  }

  private String beautifyStringConstant(SourceLine srcLine) {
    StringBuilder line = new StringBuilder();
    String code = srcLine.getByteCode();
    String tmpCode = code.substring(0, 3 * 7 - 1) + "+";
    line =
        new StringBuilder(
            String.format("\t%4s\t %s\t %s", srcLine.getOffset(), tmpCode, srcLine.getLine()));
    int i = 1;
    do {
      tmpCode = code.substring(constrain(3 * 7 * i, -1, code.length()), constrain(3 * 7 * (i + 1) - 1, 0, code.length()));
      if (tmpCode.length() == 3 * 7 - 1) {
        tmpCode += "+";
      }
      line.append(String.format("\n\t\t\t\t%s", tmpCode));
      i++;
    } while (!tmpCode.isEmpty());
    return line.toString();
  }

  private int constrain(int n, int a, int b){
    if (n < a) return a;
    if (n > b) return b;
    return n;
  }

  //////////////////////////////////////////////////////////////////////////////////////////
}
