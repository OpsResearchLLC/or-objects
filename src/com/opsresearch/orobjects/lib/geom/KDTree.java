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

package com.opsresearch.orobjects.lib.geom;

import java.util.Enumeration;
import java.util.Iterator;

import com.opsresearch.orobjects.lib.CompareNumber;
import com.opsresearch.orobjects.lib.PairI;
import com.opsresearch.orobjects.lib.cont.PriorityQueue;
import com.opsresearch.orobjects.lib.cont.PriorityQueueI;

public class KDTree<valueType> implements PointIndexI<valueType> {

	private class Enum implements Enumeration<PairI<PointI, valueType>> {

		private Node _node;

		Enum(Node node) {
			_node = node;
		}

		public boolean hasMoreElements() {
			return _node != null;
		}

		public Node nextElement() {
			if (_node == null)
				return null;
			Node ret = _node;
			_node = _node._next;
			return ret;
		}
	}

	private class SelEnum implements Enumeration<PairI<PointI, valueType>> {
		private Node _node;

		SelEnum(Node node) {
			_node = node;
		}

		public boolean hasMoreElements() {
			return _node != null;
		}

		public Node nextElement() {
			if (_node == null)
				return null;
			Node ret = _node;
			_node = _node._nextSel;
			return ret;
		}
	}

	private class Node implements PairI<PointI, valueType> {

		private valueType _value;
		private PointI _key;
		private Node _left, _right, _next, _nextSel;

		Node(PointI point, valueType info) {
			_key = point;
			_value = info;
			_left = _right = _next = _nextSel = null;
		}

		public PointI getFirst() {
			return _key;
		}

		public valueType getSecond() {
			return _value;
		}
	}

	private int _dim;
	private int _size, _sizeOfSelected;
	private Node _root, _list, _selected;
	private RangeI _range, _selectRange;
	private CoordinateSystemI _coordinateSystem;
	private PriorityQueueI<Double, Node> _priorityQueue;

	public KDTree() {
		this(new PriorityQueue<Double, Node>(new CompareNumber()));

	}

	public KDTree(PriorityQueue<Double, Node> priorityQueue) {
		_dim = -1;
		removeAllElements();
		_coordinateSystem = null;
		_priorityQueue = priorityQueue;
		_priorityQueue.setCompare(new CompareNumber());
	}

	public int size() {
		return _size;
	}

	public int sizeOfSelected() {
		return _sizeOfSelected;
	}

	public void removeAllElements() {
		_size = _sizeOfSelected = 0;
		_list = _root = _selected = null;
		_range = null;
	}

	public RangeI range() {
		return _range;
	}

	public void setCoordinateSystem(CoordinateSystemI coordinateSystem) {
		removeAllElements();
		_coordinateSystem = coordinateSystem;
		_dim = _coordinateSystem == null ? -1 : _coordinateSystem.sizeOfDimensions();
	}

	public CoordinateSystemI coordinateSystem() {
		return _coordinateSystem;
	}

	public boolean supportsDuplicateKeys() {
		return true;
	}

	public valueType get(PointI point) {
		int depth = 0;
		Node node = _root;
		while (node != null) {
			if (point.equals(node._key))
				break;
			int idx = depth % _dim;
			if (point.getCoordinate(idx) <= node._key.getCoordinate(idx)) {
				node = node._left;
			} else
				node = node._right;
			depth++;
		}
		return node._value;
	}

	public PairI<PointI, valueType> getNearestNeighborTo(PointI point) {
		if (_list == null)
			return null;
		Node best = null;
		double min = Double.MAX_VALUE;
		for (Node node = _list; node != null; node = node._next) {
			double d = point.getDistanceProxyTo(node._key);
			if (d < min) {
				min = d;
				best = node;
			}
		}
		return best;
	}

	public int selectNearestNeighbors(PointI point, int n) {
		_selected = null;
		_sizeOfSelected = 0;
		if (_list == null || n <= 0)
			return 0;

		int cnt = 0;
		double min = Double.MAX_VALUE;
		_priorityQueue.removeAllElements();
		for (Node node = _list; node != null; node = node._next) {
			double d = point.getDistanceProxyTo(node._key);
			if (cnt < n) {
				_priorityQueue.insert(new Double(d), node);
				min = ((Double) _priorityQueue.getHead().getFirst()).doubleValue();
				cnt++;
			} else if (d < min) {
				_priorityQueue.popHead();
				_priorityQueue.insert(new Double(d), node);
				min = ((Double) _priorityQueue.getHead().getFirst()).doubleValue();
			}
		}
		for (Iterator<PairI<Double, Node>> e = _priorityQueue.elements(); e.hasNext();) {
			Node node = e.next().getSecond();
			node._nextSel = _selected;
			_selected = node;
			_sizeOfSelected++;
		}
		_priorityQueue.removeAllElements();
		return _sizeOfSelected;
	}

	public Enumeration<PairI<PointI, valueType>> elements() {
		return new Enum(_list);
	}

	public Enumeration<PairI<PointI, valueType>> selectedElements() {
		return new SelEnum(_selected);
	}

	public void put(PointI key, valueType value) {
		if (_coordinateSystem == null)
			setCoordinateSystem(key.coordinateSystem());

		_range = (_range == null) ? _coordinateSystem.getRangeInstance(key, key) : _range.getExpandedRange(key);

		Node node = new Node(key, value);
		node._next = _list;
		_list = node;
		_size++;

		if (_root == null) {
			_root = node;
			return;
		}

		int depth = 0;
		Node parent = _root;
		while (parent != null) {
			int idx = depth % _dim;
			if (node._key.getCoordinate(idx) <= parent._key.getCoordinate(idx)) {
				if (parent._left == null) {
					parent._left = node;
					break;
				} else
					parent = parent._left;
			} else {
				if (parent._right == null) {
					parent._right = node;
					break;
				} else
					parent = parent._right;
			}
			depth++;
		}
	}

	public int selectRange(RangeI range) {
		_selected = null;
		_sizeOfSelected = 0;
		_selectRange = range;
		_selectRange(_root, 0);
		return _sizeOfSelected;
	}

	private void _selectRange(Node node, int depth) {
		if (node == null)
			return;
		if (_selectRange.includes(node._key)) {
			node._nextSel = _selected;
			_selected = node;
			_sizeOfSelected++;
		}
		int idx = depth % _dim;
		double coord = node._key.getCoordinate(idx);
		if (_selectRange.getMin().getCoordinate(idx) <= coord)
			_selectRange(node._left, depth + 1);
		if (_selectRange.getMax().getCoordinate(idx) >= coord)
			_selectRange(node._right, depth + 1);
	}
}
