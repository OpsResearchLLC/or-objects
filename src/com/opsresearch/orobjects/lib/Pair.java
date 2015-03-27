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

package com.opsresearch.orobjects.lib;

public class Pair<first, second> implements PairI<first, second>,
		java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private first first;
	private second second;

	public Pair(first firstItem, second secondItem) {
		first = firstItem;
		second = secondItem;
	}

	public Pair(PairI<first, second> p) {
		first = p.getFirst();
		second = p.getSecond();
	}

	public first getFirst() {
		return first;
	}

	public second getSecond() {
		return second;
	}

	public Object clone() {
		return new Pair<first, second>(first, second);
	}

	public boolean equals(Object other) {
		return (other instanceof PairI<?, ?>)
				&& getFirst().equals(((PairI<?, ?>) other).getFirst())
				&& getSecond().equals(((PairI<?, ?>) other).getSecond());
	}

	public int hashCode() {
		return first.hashCode() + second.hashCode();
	}

	public String toString() {
		return "(" + first.toString() + "," + second.toString() + ")";
	}

}
