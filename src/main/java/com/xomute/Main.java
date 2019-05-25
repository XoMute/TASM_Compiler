package com.xomute;

import com.xomute.lexer.Lexer;
import com.xomute.syntaxer.Syntaxer;
import com.xomute.compiler.Compiler;
import com.xomute.utils.Printer;

public class Main {

  private static final String filename = Main.class.getClassLoader().getResource("test.asm").getFile();

  public static void main(String[] args) {

    startCompiler();
//    printLexerAndSyntaxer();
  }

  private static void startCompiler() {
    Lexer lexer = new Lexer();
    lexer.scan(filename);

    Syntaxer syntaxer = new Syntaxer();

    Compiler compiler = new Compiler();
    compiler.compileToConsole(lexer.getLinesOfSrc());
  }

  private static void printLexerAndSyntaxer() {
    Printer printer = new Printer();
    printer.doTheJob(filename);
  }
}
