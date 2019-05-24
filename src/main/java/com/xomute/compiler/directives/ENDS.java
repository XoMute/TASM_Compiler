package com.xomute.compiler.directives;

import com.xomute.compiler.interfaces.Directive;

@Deprecated
public class ENDS implements Directive {
	private String code = "";
	private int offset = -1;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	@Deprecated
	public void updateCode() {

	}
}
