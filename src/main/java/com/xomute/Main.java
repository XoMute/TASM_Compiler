package com.xomute;

import com.xomute.lexer.Lexer;
import com.xomute.syntaxer.Syntaxer;
import com.xomute.utils.Printer;

public class Main {

  private static final String filename = Main.class.getClassLoader().getResource("test.asm").getFile();

  public static void main(String[] args) {

    Lexer lexer = new Lexer();
//    lexer.print(filename);

    Syntaxer syntaxer = new Syntaxer();

    Printer printer = new Printer(lexer, syntaxer);
    printer.doTheJob(filename);

      /**
        I have to finish lexer:
        - Process errors
        - Determine which string is macro call (collection of defined macro) (should use macro class to remember parameters of macro and it's body)
        - Create collections of user-defined names(labels or vars),
        - Finish all methods in AssemblerHelper class
       Then write syntaxer:
        - Determine indexes of fields in line
        -
       */
  }
}
