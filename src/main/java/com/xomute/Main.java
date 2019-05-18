package com.xomute;

import com.xomute.lexer.Lexer;

public class Main {
  public static void main(String[] args) {

    Lexer lexer = new Lexer();
    lexer.scan(Main.class.getClassLoader().getResource("test.asm").getFile());


  }
}
