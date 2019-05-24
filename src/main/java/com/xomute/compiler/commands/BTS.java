package com.xomute.compiler.commands;

import com.xomute.compiler.interfaces.Command;
import com.xomute.utils.AssemblerHelper;
import com.xomute.utils.StringUtils;

public class BTS implements Command {

	private String code = "";
	private int offset;

	private String operand1;
	private String operand2;

	public BTS(String operand1, String operand2) {
		this.operand1 = operand1;
		this.operand2 = operand2;
		code = "0F BA ";
		code += AssemblerHelper.getModRM("BP", operand1) + " ";
		code += StringUtils.beautifyCode(AssemblerHelper.decToHex(operand2), 2);
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
	@Deprecated
	public void updateCode() {

	}
}
