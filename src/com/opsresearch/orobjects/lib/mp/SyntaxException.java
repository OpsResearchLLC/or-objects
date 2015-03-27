/* ***** BEGIN LICENSE BLOCK *****
 * 
 * Copyright (C) 2012 OpsResearch LLC (a Delaware company)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * ***** END LICENSE BLOCK ***** */

package com.opsresearch.orobjects.lib.mp;

public class SyntaxException extends MpException {
	private static final long serialVersionUID = 1L;
	private int _lineNumber = 0;
	private String _line = null;

	public SyntaxException() {
	}

	public SyntaxException(String s) {
		super(s);
	}

	public SyntaxException(String s, int lineNumber, String line) {
		super(s);
		_line = line;
		_lineNumber = lineNumber;
	}

	public int getLineNumber() {
		return _lineNumber;
	}

	public String getLine() {
		return _line;
	}

}
