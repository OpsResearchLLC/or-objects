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

public interface AddEdgeI extends GraphI {
	public void ensureEdgeCapacity(int edgeCapacity);

	public EdgeI addEdge(Object fromKey, Object toKey, Object value, boolean isDirected, Object key)
			throws DuplicateEdgeException, VertexNotFoundException;

	public EdgeI addEdge(Object fromKey, Object toKey) throws DuplicateEdgeException, VertexNotFoundException;

	public EdgeI addEdge(Object fromKey, Object toKey, Object value) throws DuplicateEdgeException,
			VertexNotFoundException;

	public EdgeI addEdge(Object fromKey, Object toKey, Object value, boolean isDirected) throws DuplicateEdgeException,
			VertexNotFoundException;

	public EdgeI addEdge(EdgeI edge) throws DuplicateEdgeException, VertexNotFoundException;

};
