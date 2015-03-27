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

import java.util.HashMap;
import java.util.Iterator;

public abstract class BaseGraph extends Graph implements AddVertexI {

	private static final long serialVersionUID = 1L;

	protected int _changeCount;
	protected int _sizeOfEdges;
	protected int _sizeOfDirectedEdges;
	protected HashMap<Object, VertexI> _vertexHashMap;

	public BaseGraph() {
		_changeCount = 0;
		_sizeOfEdges = 0;
		_sizeOfDirectedEdges = 0;
		_vertexHashMap = new HashMap<Object, VertexI>();
	}

	public BaseGraph(int vertexCapacity) {
		_changeCount = 0;
		_sizeOfEdges = 0;
		_sizeOfDirectedEdges = 0;
		_vertexHashMap = new HashMap<Object, VertexI>(vertexCapacity);
	}

	public int getChangeCount() {
		return _changeCount;
	}

	public Iterator<VertexI> vertices() {
		return _vertexHashMap.values().iterator();
	}

	public int sizeOfVertices() {
		return _vertexHashMap.size();
	}

	public int sizeOfEdges() {
		return _sizeOfEdges;
	}

	public int sizeOfDirectedEdges() {
		return _sizeOfDirectedEdges;
	}

	public void ensureVertexCapacity(int vertexCapacity) {
	}

	EdgeI findEdge(Object fromKey, Object toKey, Object key) {
		VertexI from = (VertexI) _vertexHashMap.get(fromKey);
		if (from == null)
			return null;
		VertexI to = (VertexI) _vertexHashMap.get(toKey);
		if (to == null)
			return null;
		return findEdge(from, to, key);
	}

	public VertexI getVertex(Object key) {
		return (VertexI) _vertexHashMap.get(key);
	}

	public void removeAllVertices() {
		_sizeOfEdges = 0;
		_vertexHashMap.clear();
	}


	protected abstract VertexI newVertex(Object key, Object value);

	public VertexI addVertex(Object key) throws DuplicateVertexException {
		return addVertex(key, null);
	}

	public VertexI addVertex(VertexI vertex) throws DuplicateVertexException {
		return addVertex(vertex.getKey(), vertex.getValue());
	}

	public VertexI addVertex(Object key, Object value) throws DuplicateVertexException {
		if (_vertexHashMap.get(key) != null)
			throw new DuplicateVertexException();
		VertexI vertex = newVertex(key, value);
		_vertexHashMap.put(key, vertex);
		_changeCount++;
		return vertex;
	}

};
