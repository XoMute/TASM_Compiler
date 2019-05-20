package com.xomute;

import com.xomute.lexer.Lexer;
import com.xomute.syntaxer.Syntaxer;
import com.xomute.utils.Printer;

public class Main {
  public static void main(String[] args) {

    Lexer lexer = new Lexer();
    lexer.scan(Main.class.getClassLoader().getResource("test.asm").getFile());

    Syntaxer syntaxer = new Syntaxer();

    Printer printer = new Printer(lexer, syntaxer);
    printer.printAll();

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
