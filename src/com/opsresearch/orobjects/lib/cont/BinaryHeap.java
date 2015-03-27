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

package com.opsresearch.orobjects.lib.cont;

import java.util.Iterator;
import java.util.Vector;

import com.opsresearch.orobjects.lib.CompareI;
import com.opsresearch.orobjects.lib.InvalidPriorityException;
import com.opsresearch.orobjects.lib.PairI;

public class BinaryHeap<priority, value> implements PriorityQueueI<priority, value>, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	class _Enum implements Iterator<PairI<priority, value>> {
		private int _idx;
		private BinaryHeap<priority, value> _heap;

		_Enum(BinaryHeap<priority, value> heap) {
			_idx = 0;
			_heap = heap;
		}

		public boolean hasNext() {
			return _idx < _heap.size();
		}

		public PairI<priority, value> next() {
			if (_idx >= _heap.size())
				return null;
			return _heap.elementAt(_idx++);
		}

		@Override
		public void remove() {
		}

	}

	class _Elem implements PairI<priority, value>, java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private int _idx;
		private priority _priority = null;
		private value _value = null;

		_Elem(priority p, value v) {
			_priority = p;
			_value = v;
		}

		public String toString() {
			return "Element[idx=" + String.valueOf(_idx) + ",priority=" + _priority.toString() + "]";
		}

		public priority getFirst() {
			return _priority;
		}

		public value getSecond() {
			return _value;
		}
	}

	private CompareI _compare;
	private Vector<_Elem> _elements;

	public BinaryHeap(CompareI compare) {
		_compare = compare;
		_elements = new Vector<_Elem>();
	}

	public BinaryHeap(CompareI compare, int capacity) {
		_compare = compare;
		_elements = new Vector<_Elem>(capacity);
	}

	public PairI<priority, value> elementAt(int i) {
		return (PairI<priority, value>) _elements.elementAt(i);
	}


	public int size() {
		return _elements.size();
	}

	public void removeAllElements() {
		_elements.removeAllElements();
	}

	public void ensureCapacity(int capacity) {
		_elements.ensureCapacity(capacity);
	}

	public Iterator<PairI<priority, value>> elements() {
		return new _Enum(this);
	}

	public boolean check() {
		for (int i = 0; i < _elements.size(); i++)
			if (((_Elem) _elements.elementAt(i))._idx != i)
				return false;
		return true;
	}

	public void changePriority(PairI<priority, value> pelement, priority p) throws InvalidPriorityException {
		_Elem element = (_Elem) pelement;

		if (_elements.size() == 1) {
			element._priority = p;
			return;
		}

		int cmp = _compare.compare(p, element._priority);
		if (cmp == CompareI.EQUAL)
			return;
		element._priority = p;
		if (cmp == CompareI.LESS)
			_moveDown(element);
		else
			_moveUp(element);
	}

	public PairI<priority, value> getHead() {
		if (_elements.size() == 0)
			return null;
		return (PairI<priority, value>) _elements.elementAt(0);
	}

	public PairI<priority, value> popHead() {
		int size_ = _elements.size();
		if (size_ == 0)
			return null;
		_Elem ret_ = (_Elem) _elements.elementAt(0);
		_Elem mov_ = (_Elem) _elements.elementAt(size_ - 1);
		_elements.removeElementAt(size_ - 1);
		if (size_ == 1)
			return ret_;
		_elements.setElementAt(mov_, 0);
		if (size_ == 2)
			return ret_;
		mov_._idx = 0;
		_moveDown(mov_);
		return ret_;
	}

	public void setCompare(CompareI compare) {
		removeAllElements();
		_compare = compare;
	}

	public PairI<priority, value> insert(priority p, value v) {
		return _insert(new _Elem(p, v));
	}


	private PairI<priority, value> _insert(_Elem element_) {
		int size = _elements.size();
		element_._idx = size;
		_elements.addElement(element_);
		_moveUp(element_);
		return element_;
	}

	private void _moveUp(_Elem element_) {
		int i = element_._idx;
		int p = (i - 1) / 2;
		_Elem parent = (_Elem) _elements.elementAt(p);
		while (i > 0 && _compare.compare(element_._priority, parent._priority) == CompareI.GREATER) {
			parent._idx = i;
			_elements.setElementAt(parent, parent._idx);
			i = p;
			p = (i - 1) / 2;
			parent = (_Elem) _elements.elementAt(p);
		}
		element_._idx = i;
		_elements.setElementAt(element_, element_._idx);
	}

	private void _moveDown(_Elem element_) {
		int size = _elements.size();
		_Elem best = null;
		while (best != element_) {
			best = element_;
			int i = element_._idx;

			int l = 2 * i + 1;
			if (l < size) {
				_Elem left = (_Elem) _elements.elementAt(l);
				if (_compare.compare(left._priority, best._priority) == CompareI.GREATER)
					best = left;
			}

			int r = l + 1;
			if (r < size) {
				_Elem right = (_Elem) _elements.elementAt(r);
				if (_compare.compare(right._priority, best._priority) == CompareI.GREATER)
					best = right;
			}

			if (best != element_) {
				element_._idx = best._idx;
				best._idx = i;
				_elements.setElementAt(best, best._idx);
				_elements.setElementAt(element_, element_._idx);
			}
		}
	}

}
