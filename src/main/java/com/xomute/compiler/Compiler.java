package com.xomute.compiler;

import com.xomute.compiler.interfaces.Mnemocode;
import com.xomute.compiler.utils.CompilerUtils;
import com.xomute.lexer.SourceLine;
import com.xomute.lexer.lexems.Label;
import com.xomute.lexer.segments.CodeSegment;
import com.xomute.lexer.segments.DataSegment;
import com.xomute.lexer.segments.Segment;
import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.StringConstants;
import com.xomute.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Compiler {

  /**
   * TODO list for tomorrow:
   *  - remember offset of label // DONE (?)
   *  - implement JBE // DONE
   *  - fix macro printing // DONE
   *  - figure out how to work with MOV (the hard and long one) // DONE
   *  - implement MOV // DONE
   *  - implement SBB (will be easy after MOV)
   *  - implement LSS (same)
   *  - chill;
   */

  private static Segment currentSegment;
  private boolean startedSegment;

  private static int offset;

  private List<SourceLine> srcLinesFor2ndGT = new ArrayList<>();

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
    secondGoThrough();
  }



  ///////////////// FIRST GO THROUGH ////////////////////////

  /** this method adds offset in each SourceLine */
  private void firstGoThrough(List<SourceLine> srcLines) {
    offset = 0;
    for (SourceLine srcLine : srcLines) {
      if (srcLine.isSkipByCompiler()) continue;

      srcLine.setOffset(CompilerUtils.getOffset(offset));

      processSegment(srcLine);

      Optional<Mnemocode> opt = srcLine.getMnemocode();
      if (!opt.isPresent()) {
        continue;
      }
      Mnemocode mnemocode = opt.get();

      if (mnemocode.getCode().equals(StringConstants.SECOND_GT)) {
        this.addLineTo2ndGT(srcLine);
      }

      if (mnemocode.getOffset() == -1) { // means this mnemocode is 'ENDS'
        offset = 0;
      } else {
        offset += mnemocode.getOffset();
      }
    }
  }

  private void processSegment(SourceLine srcLine) {
    String line = srcLine.getLine().trim();

    if (line.contains("SEGMENT")) {
      startSegment(line);
    } else if (line.contains("ENDS")) {
      endSegment(line);
    }

    if (startedSegment) {
      if (AssemblerHelper.isLabel(line)) {
        Label label = new Label(line.substring(0, line.length() - 1), srcLine.getOffset(), "CODE");
        currentSegment.addLabel(label);
      }
    }
  }

  private void startSegment(String line) {
    List<String> operands = StringUtils.splitByOperands(line);
    if (currentSegment == null) { // means it's first segment
      currentSegment = new DataSegment(operands.get(0));
    } else {
      currentSegment = new CodeSegment(operands.get(0));
    }
    startedSegment = true;
  }

  private void endSegment(String line) {
    AssemblerHelper.saveSegment(currentSegment);
    startedSegment = false;
  }

  public static Segment getCurrentSegment() {
    return currentSegment;
  }

  public static int getOffset() {
    return offset;
  }

  ///////////////////////////////////////////////////////////

  ///////////////// SECOND GO THROUGH ///////////////////////

  public void addLineTo2ndGT(SourceLine line) {
    srcLinesFor2ndGT.add(line);
  }

  /** this method adds byteCode in each SourceLine */
  private void secondGoThrough() {
    for (SourceLine srcLine : srcLinesFor2ndGT) {
      Optional<Mnemocode> opt = srcLine.getMnemocode();
      if (!opt.isPresent()) {
        continue;
      }
      Mnemocode mnemocode = opt.get();
      // todo: update line offset if needed (in case of MOV, maybe)
      mnemocode.updateCode();
    }
  }

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
