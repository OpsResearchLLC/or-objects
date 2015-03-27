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

package com.opsresearch.orobjects.lib.graph.vrp;

import com.opsresearch.orobjects.lib.graph.GraphI;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;
import com.opsresearch.orobjects.lib.util.Array;

public abstract class ConstructBase extends VRPBase {
	protected com.opsresearch.orobjects.lib.graph.tsp.ImproveI _improve;
	protected boolean[] _selected = null;

	public ConstructBase() {
		_improve = null;
	}

	public ConstructBase(com.opsresearch.orobjects.lib.graph.tsp.ImproveI improveSubalgorithm) {
		_improve = improveSubalgorithm;
	}

	public void setGraph(GraphI graph) {
		super.setGraph(graph);
		if (_improve != null)
			_improve.setGraph(graph);
		if (graph == null)
			_selected = null;
	}

	public void setProperties(PropertiesI properties) {
		super.setProperties(properties);
		if (_improve != null)
			_improve.setProperties(properties);
	}

	public void setEdgeKey(Object edgeKey) {
		super.setEdgeKey(edgeKey);
		if (_improve != null)
			_improve.setEdgeKey(edgeKey);
	}

	public int sizeOfSelected() {
		return _graph == null ? 0 : _selected == null ? _graph.sizeOfVertices() : Array.count(_selected.length,
				_selected, 0, 1, true);
	}

	public boolean isSelected(VertexI vertex) {
		if (_selected == null)
			return true;
		return _selected[vertex.getIndex()];
	}

	public void selectVertex(boolean[] select) {
		if (select == null) {
			_selected = null;
			return;
		}

		if (_graph == null)
			throw new VRPError("The graph is not set.");
		if (select.length != _graph.sizeOfVertices())
			throw new VRPError("The size of the select array  must equal the number of vertices in the graph.");
		_selected = new boolean[_graph.sizeOfVertices()];
		for (int i = 0; i < _selected.length; i++)
			_selected[i] = select[i];
	}

	public void selectVertex(boolean select) {
		if (select) {
			_selected = null;
			return;
		}
		if (_graph == null)
			throw new VRPError("The graph is not set.");
		if (_selected == null)
			_selected = Array.resize(_graph.sizeOfVertices(), _selected, true);
	}

	public void selectVertex(Object key, boolean select) throws VertexNotFoundException {
		if (_graph == null)
			throw new VRPError("The graph is not set.");
		VertexI vertex = _graph.getVertex(key);
		if (vertex == null)
			throw new VertexNotFoundException();
		if (_selected == null) {
			_selected = new boolean[_graph.sizeOfVertices()];
			for (int i = 0; i < _selected.length; i++)
				_selected[i] = true;
		}
		_selected[vertex.getIndex()] = select;
	}

}
