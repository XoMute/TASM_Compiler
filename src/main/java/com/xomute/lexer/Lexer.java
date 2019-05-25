package com.xomute.lexer;

import com.xomute.compiler.commands.WrongCommand;
import com.xomute.lexer.lexems.Macro;
import com.xomute.lexer.segments.CodeSegment;
import com.xomute.lexer.segments.DataSegment;
import com.xomute.lexer.segments.Segment;
import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.FileScanner;
import com.xomute.utils.StringConstants;
import com.xomute.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Lexer {

  private Segment currentSegment;
  private Macro macro = new Macro();
  private boolean startedMacro;
  private boolean startedSegment;

  /**
   * this list is needed for further processing without repeated reading of file and for catching
   * mistakes(so, when mistake in code is found, error line just doesn't go to this list)
   */
  private List<SourceLine> linesOfSrc = new ArrayList<>();

  public void scan(String filename) {
    FileScanner scanner = new FileScanner();
    scanner
        .readFile(filename)
        .forEach(
            line -> {
              List<SourceLine> srcLines =
                  scanOneLine(line).stream()
                      .map(
                          srcLine -> {
                            if (srcLine.getLine().isEmpty()) {
                              srcLine.setSkipByCompiler(true);
                            }
                            return srcLine;
                          })
                      .collect(Collectors.toList());
              linesOfSrc.addAll(srcLines);
            });
  }

  public List<SourceLine> scanOneLine(String line) {
    List<SourceLine> processedLine = processLine(line);

    processedLine.stream()
        .map(SourceLine::getLine)
        .forEach(
            _line ->
                StringUtils.split(_line).stream()
                    .map(AssemblerHelper::processLexem)
                    .collect(Collectors.toList()));
    return processedLine;
  }

  private List<SourceLine> processLine(String line) {
    SourceLine srcLine = new SourceLine(line);
    if (line.contains("MACRO")) {
      startMacro(line);
      srcLine.setSkipByCompiler(true);
      return Collections.singletonList(srcLine);
    } else if (line.contains("ENDM")) {
      endMacro();
      srcLine.setSkipByCompiler(true);
      return Collections.singletonList(srcLine);
    }

    if (line.trim().equals("END")) {
      srcLine.setSkipByCompiler(true);
      return Collections.singletonList(srcLine);
    }

    if (startedMacro) {
      srcLine.setSkipByCompiler(true);
      macro.addLine(srcLine);
      return Collections.singletonList(srcLine);
    }

    List<String> splittedLine = Arrays.asList(line.split("\\s+"));

    if (AssemblerHelper.isMacroCall(splittedLine.get(0))) {
      List<SourceLine> macroLines = new ArrayList<>();
      srcLine.setSkipByCompiler(true);
      macroLines.add(srcLine);
      String param = null;
      if (splittedLine.size() == 2) {
        param = splittedLine.get(1);
      }
      macroLines.addAll(AssemblerHelper.callMacro(splittedLine.get(0), param));
      return macroLines;
    }
//  if Tesla asks to process wrong commands - implement this lines below
//    if (AssemblerHelper.isWrongCommand(splittedLine.get(0))) {
//      srcLine.setMnemocode(new WrongCommand());
//    }

    return Collections.singletonList(srcLine);
  }

  private void startMacro(String line) {
    macro = new Macro();
    startedMacro = true;
    List<String> splittedLine = Arrays.asList(line.split("\\s+"));
    macro.setName(splittedLine.get(0));
    if (splittedLine.size() == 3) {
      macro.setParam(splittedLine.get(2));
    }
  }

  private void endMacro() {
    AssemblerHelper.addMacro(macro);
    startedMacro = false;
  }

  ////////////////// PRINTING PART /////////////////////////////////

  /**
   * needed only for printing
   *
   * @param lexem lexem
   * @return string type of lexem
   */
  public String scanOneWord(String lexem) {
    return AssemblerHelper.Type.convertToString(AssemblerHelper.processLexem(lexem, true));
  }

  public List<SourceLine> scanOneLineForPrint(String line) {
    List<String> processedLine = processLineForPrint(line);

    processedLine.forEach(
        _line ->
            StringUtils.split(_line).stream()
                .map(lexem -> AssemblerHelper.processLexem(lexem, true))
                .collect(Collectors.toList()));
    return processedLine.stream().map(SourceLine::new).collect(Collectors.toList());
  }

  private List<String> processLineForPrint(String line) {

    List<String> splittedLine = Arrays.asList(line.split("\\s+"));

    if (AssemblerHelper.isMacroCall(splittedLine.get(0))) {
      List<String> macroLines = new ArrayList<>();
      macroLines.add(line);
      String param = null;
      if (splittedLine.size() == 2) {
        param = splittedLine.get(1);
      }
      macroLines.addAll(
          AssemblerHelper.callMacro(splittedLine.get(0), param).stream()
              .map(SourceLine::getLine)
              .collect(Collectors.toList()));
      return macroLines;
    }

    return Collections.singletonList(line);
  }

  public void printLine(SourceLine line) {
    printProcessedLine(line.getLine(), line.getLexems());
  }

  public void printProcessedLine(String line, List<String> splittedLine) {
    System.out.println();
    System.out.println(line);
    System.out.println(StringConstants.LINE);
    int i = 0;
    for (String lexem : splittedLine) {
      System.out.printf("%3d%10s%3d%50s\n", i, lexem, lexem.length(), scanOneWord(lexem));
      i++;
    }
    System.out.println(StringConstants.LINE);
  }

  public List<SourceLine> getLinesOfSrc() {
    return linesOfSrc;
  }
}
