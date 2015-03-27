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

import com.opsresearch.orobjects.lib.CompareI;
import com.opsresearch.orobjects.lib.CorruptedError;
import com.opsresearch.orobjects.lib.InvalidPriorityException;
import com.opsresearch.orobjects.lib.PairI;

public class BinomialHeap<priorityType, valueType> implements PriorityQueueI<priorityType, valueType>,
		java.io.Serializable {

	private static final long serialVersionUID = 1L;

	class _Elem implements PairI<priorityType, valueType>, java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private byte _deg = 0;
		private valueType _value = null;
		private priorityType _priority = null;
		private _Elem _child, _parent, _sibling;

		_Elem(priorityType priority, valueType object) {
			_priority = priority;
			_value = object;
			_child = _parent = _sibling = null;
		}

		public String toString() {
			return "Element[deg=" + String.valueOf(_deg) + ",priority=" + _priority.toString() + "]";
		}

		public priorityType getFirst() {
			return _priority;
		}

		public valueType getSecond() {
			return _value;
		}
	}

	private class _Enum implements Iterator<PairI<priorityType, valueType>> {
		private _Elem _element;

		_Enum(_Elem element) {
			_element = element;
		}

		public boolean hasNext() {
			return _element != null;
		}

		public PairI<priorityType, valueType> next() {

			if (_element == null)
				return null;

			_Elem ret = _element;

			if (_element._child != null) {
				_element = _element._child;
				return ret;
			}

			if (_element._sibling != null) {
				_element = _element._sibling;
				return ret;
			}

			_element = _element._parent;
			while (_element != null)
				if (_element._sibling != null)
					_element = _element._sibling;

			return ret;
		}

		@Override
		public void remove() {
		}
	}

	private int _size;
	private _Elem _head;
	private CompareI _compare;

	public BinomialHeap(CompareI compare) {
		_size = 0;
		_compare = compare;
	}


	public int size() {
		return _size;
	}

	public Iterator<PairI<priorityType, valueType>> elements() {
		return new _Enum(_head);
	}

	public void removeAllElements() {
		_size = 0;
		_head = null;
	}

	public void ensureCapacity(int capacity) {
	}

	public PairI<priorityType, valueType> getHead() {
		if (_head == null)
			return null;
		_Elem element = _head._sibling;
		_Elem best = _head;
		while (element != null) {
			if (_compare.compare(element._priority, best._priority) == CompareI.GREATER) {
				best = element;
			}
			element = element._sibling;
		}
		return best;
	}

	public PairI<priorityType, valueType> popHead() {
		if (_head == null)
			return null;
		_Elem prev = _head;
		_Elem element = _head._sibling;
		_Elem best = _head;
		_Elem bestPrev = null;
		while (element != null) {
			if (_compare.compare(element._priority, best._priority) == CompareI.GREATER) {
				best = element;
				bestPrev = prev;
			}
			prev = element;
			element = element._sibling;
		}

		if (bestPrev == null)
			_head = best._sibling;
		else
			bestPrev._sibling = best._sibling;

		_union(_reverseSiblingList(best._child));
		_size--;
		return best;
	}

	public PairI<priorityType, valueType> insert(priorityType priority) {
		_Elem element = new _Elem(priority, null);
		_insertElement(element);
		return element;
	}

	public PairI<priorityType, valueType> insert(priorityType priority, valueType value) {
		_Elem element = new _Elem(priority, value);
		_insertElement(element);
		return element;
	}

	public void setCompare(CompareI compare) {
		removeAllElements();
		_compare = compare;
	}

	public void changePriority(PairI<priorityType, valueType> pelement, priorityType priority)
			throws InvalidPriorityException {
		_Elem element = (_Elem) pelement;
		int cmp = _compare.compare(priority, element._priority);
		if (cmp == CompareI.EQUAL)
			return;
		if (cmp == CompareI.LESS)
			throw new InvalidPriorityException();

		int i;
		element._priority = priority;
		for (i = 0; i < 100 && element._parent != null; i++)
			if (_compare.compare(element._priority, element._parent._priority) == CompareI.GREATER)
				_bubbleUp(element._parent, element);
			else
				break;

		if (i >= 100)
			throw new CorruptedError();
	}

	public boolean check() {
		return _checkElement(_head, null, 0, 0);
	}


	private void _insertElement(_Elem element) {
		element._sibling = _head;
		_head = element;
		_joinRoots();
		_size++;
	}

	private void _setParentInSiblingList(_Elem list, _Elem parent) {
		if (list == null)
			return;
		_Elem element = list;
		for (int i = 0; i < 100 && element != null; i++) {
			element._parent = parent;
			element = element._sibling;
		}
		if (element != null)
			throw new CorruptedError();
	}

	private _Elem _reverseSiblingList(_Elem list) {
		if (list == null)
			return null;
		_Elem prev = null;
		_Elem element = list;
		_Elem next = element._sibling;
		for (int i = 0; i < 100 && element != null; i++) {
			element._parent = null;
			element._sibling = prev;
			prev = element;
			element = next;
			if (next != null)
				next = next._sibling;
		}
		if (element != null)
			throw new CorruptedError();
		return prev;
	}

	private void _union(_Elem head) {
		if (_head == null) {
			_head = head;
			return;
		}

		if (head == null)
			return;

		_Elem h1 = _head;
		_Elem h2 = head;
		_Elem element;

		if (h1._deg <= h2._deg) {
			_head = element = h1;
			h1 = h1._sibling;
		} else {
			_head = element = h2;
			h2 = h2._sibling;
		}

		element._sibling = null;

		while (element != null) {
			if (h1 == null && h2 == null) {
				element._sibling = null;
			} else if (h1 == null) {
				element._sibling = h2;
				h2 = h2._sibling;
			} else if (h2 == null) {
				element._sibling = h1;
				h1 = h1._sibling;
			} else {
				if (h1._deg <= h2._deg) {
					element._sibling = h1;
					h1 = h1._sibling;
				} else {
					element._sibling = h2;
					h2 = h2._sibling;
				}
			}
			element = element._sibling;
			if (element != null)
				element._sibling = null;
		}
	}

	private void _joinRoots() {
		if (_head == null)
			return;
		_Elem next;
		_Elem prev = null;
		_Elem element = _head;
		while ((next = element._sibling) != null) {

			if (element._deg != next._deg) {
				prev = element;
				element = next;
				continue;
			}

			if (next._sibling != null && next._sibling._deg == element._deg) {
				prev = element;
				element = next;
				continue;
			}

			if (_compare.compare(element._priority, next._priority) == CompareI.GREATER) {
				element._sibling = next._sibling;
				_binomialLink(next, element);
			} else {

				if (prev == null)
					_head = next;
				else
					prev._sibling = next;

				_binomialLink(element, next);
				element = next;
			}

			next = element._sibling;
		}
	}

	private void _binomialLink(_Elem child, _Elem parent) {
		child._parent = parent;
		child._sibling = parent._child;
		parent._child = child;
		parent._deg++;
	}

	private void _bubbleUp(_Elem parent, _Elem child) {
		if (parent == null || child == null || parent == child)
			throw new CorruptedError();

		_Elem parentReferrer = null;
		boolean parentReferencedFromHead = false;
		boolean parentReferencedFromParent = false;
		boolean parentReferencedFromSibling = false;

		if (_head == parent) {
			parentReferencedFromHead = true;
		} else if (parent._parent == null) {
			parentReferrer = _getElementReferrer(_head, parent);
			parentReferencedFromSibling = true;
			if (parentReferrer == null)
				throw new CorruptedError();
		} else if (parent._parent._child == parent) {
			parentReferrer = parent._parent;
			parentReferencedFromParent = true;
		} else {
			parentReferrer = _getElementReferrer(parent._parent._child, parent);
			parentReferencedFromSibling = true;
			if (parentReferrer == null)
				throw new CorruptedError();
		}

		_Elem childReferrer = null;
		boolean childReferencedFromParent = false;
		boolean childReferencedFromSibling = false;

		if (child._parent._child == child) {
			childReferrer = child._parent;
			childReferencedFromParent = true;
			if (childReferrer == null)
				throw new CorruptedError();
		} else {
			childReferrer = _getElementReferrer(child._parent._child, child);
			childReferencedFromSibling = true;
			if (childReferrer == null)
				throw new CorruptedError();
		}

		byte childDeg = child._deg;
		byte parentDeg = parent._deg;
		_Elem childSibling = child._sibling;
		_Elem parentSibling = parent._sibling;
		_Elem childParent = child._parent;
		_Elem parentParent = parent._parent;
		_Elem childChild = child._child;
		_Elem parentChild = parent._child;

		child._deg = parentDeg;
		child._child = parentChild;
		child._parent = parentParent;
		child._sibling = parentSibling;

		parent._deg = childDeg;
		parent._child = childChild;
		parent._parent = childParent;
		parent._sibling = childSibling;

		if (parentReferencedFromHead) {
			_head = child;
		} else if (parentReferencedFromParent) {
			parentReferrer._child = child;
		} else if (parentReferencedFromSibling) {
			parentReferrer._sibling = child;
		} else
			throw new CorruptedError();

		if (childReferencedFromParent) {
			child._child = parent;
		} else if (childReferencedFromSibling) {
			childReferrer._sibling = parent;
		} else
			throw new CorruptedError();

		_setParentInSiblingList(child._child, child);
		_setParentInSiblingList(parent._child, parent);
	}

	private _Elem _getElementReferrer(_Elem list, _Elem find) throws CorruptedError {
		_Elem element = list;

		for (int i = 0; i < 100 && element != null; i++) {
			if (element._sibling == find)
				return element;
			element = element._sibling;
		}

		if (element != null)
			throw new CorruptedError();
		return null;
	}


	private boolean _checkElement(_Elem element, _Elem parent, int level, int r) {
		if (element == null)
			return true;
		if (level > 100)
			return false;
		if (element._parent != parent) {
			return false;
		}

		if (element._parent != null
				&& _compare.compare(element._priority, element._parent._priority) == CompareI.GREATER)
			return false;

		if (element._sibling != null && level == 0 && element._sibling._deg <= element._deg)
			return false;

		if (element._sibling != null && level != 0 && element._sibling._deg >= element._deg)
			return false;

		if (!_checkElement(element._child, element, level + 1, r + 1))
			return false;
		if (!_checkElement(element._sibling, parent, level, r + 1))
			return false;

		return true;
	}
}
