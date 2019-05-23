package com.xomute.compiler.interfaces;

import com.xomute.utils.AssemblerHelper;

public abstract class DataIdentifier implements Mnemocode {

	protected String code = "";
	protected int offset;

	protected String identifier;
	protected String value;

	public DataIdentifier(String identifier, String value) {
		this.identifier = identifier;
		this.value = value;
	}

	protected void calculate() {
		String hexCode = "";
		int dec = 0;
		switch (AssemblerHelper.processLexem(value, true)) {

			case BIN_CONSTANT:
				dec = Integer.parseInt(value.substring(0, value.length() - 1), 2);
				hexCode = Integer.toString(dec, 16);
				break;
			case DEC_CONSTANT:
				dec = Integer.parseInt(value);
				hexCode = Integer.toString(dec, 16);
				break;
			case HEX_CONSTANT:
				hexCode = value.substring(0, value.length() - 1);
		}
		this.code = hexCode.toUpperCase();
	}

	protected void beautifyCode(int size) {
		StringBuilder strOffset = new StringBuilder(code);
		while (strOffset.length() < size) {
			strOffset.insert(0, "0");
		}
		code = strOffset.toString().toUpperCase();
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
