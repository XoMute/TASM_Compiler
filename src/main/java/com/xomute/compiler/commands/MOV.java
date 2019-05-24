package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;
import com.xomute.utils.AssemblerHelper;

@Deprecated
public class MOV implements Command {
	private String code = "";
	private int offset;

	private String operand1;
	private String operand2;

	public MOV(String operand1, String operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		if (AssemblerHelper.isRegister8(operand1)) {
			code = "kek";
		} else if (AssemblerHelper.isRegister16(operand1)) {
			code = "kek";
		} else {
			code = "ERROR";
			// todo: error
		}
		code += AssemblerHelper.getModRM(operand1, operand2);
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public void updateCode() {
		// todo: implement
	}
}
