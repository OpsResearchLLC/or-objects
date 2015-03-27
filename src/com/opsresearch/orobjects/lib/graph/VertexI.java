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

package com.opsresearch.orobjects.lib.graph;

import java.util.Iterator;

public interface VertexI extends ElementI, VertexValueI {
	public int getIndex();

	public int getInDegree();

	public int getOutDegree();

	public Object getKey();

	public Object getValue();

	public Iterator<EdgeI> inEdges();

	public Iterator<EdgeI> mutableInEdges();

	public Iterator<EdgeI> outEdges();

	public Iterator<EdgeI> mutableOutEdges();

}
