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

public interface GraphI {
	public void setSymmetric(Boolean symmetric);

	public boolean isSymmetric();

	public int getChangeCount();


	public int sizeOfVertices();

	public Iterator<VertexI> vertices();

	public VertexI getVertex(Object key);


	public int sizeOfEdges();

	public int sizeOfDirectedEdges();

	public Iterator<EdgeI> edges();

	public Iterator<EdgeI> mutableEdges();

	public EdgeI getEdge(EdgeI edge);

	public EdgeI getEdge(Object fromKey, Object toKey, Object edgeKey);

	public EdgeI getMutableEdge(VertexI from, VertexI to, Object edgeKey);

	public EdgeI getEdge(VertexI from, VertexI to, Object key);

	public boolean isSubsetOf(GraphI graph);

};
