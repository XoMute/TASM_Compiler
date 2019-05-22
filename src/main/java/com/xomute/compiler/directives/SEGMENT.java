package com.xomute.compiler.directives;

import com.xomute.compiler.interfaces.Directive;

@Deprecated
public class SEGMENT implements Directive {
	private String code;
	private int offset;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}
}
