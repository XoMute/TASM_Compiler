package com.xomute.lexer.lexems;

import com.sun.istack.internal.Nullable;
import com.xomute.lexer.SourceLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Macro {

  private String name;
  private String param;
  private List<SourceLine> body = new ArrayList<>();

  public Macro() {}

  public Macro(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

  public void addLine(SourceLine line) {
    body.add(line);
  }

  public List<SourceLine> call(@Nullable String param) {
    if (param != null) {
      return body.stream()
          .map(SourceLine::getLine)
          .map(line -> {
          	if (Arrays.asList(line.split("\\s+")).contains(this.param)) {
		          return line.replace(this.param, param);
	          } else return line;
          })
          .map(SourceLine::new).collect(Collectors.toList());
    }
    return body;
  }
}
