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

package com.opsresearch.orobjects.lib.prob.continuous;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.opsresearch.orobjects.lib.CompareI;
import com.opsresearch.orobjects.lib.InvalidArgumentError;
import com.opsresearch.orobjects.lib.prob.ProbError;
import com.opsresearch.orobjects.lib.real.matrix.VectorElementI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;
import com.opsresearch.orobjects.lib.util.QuickSort;

public class EmpiricalDistribution extends ContinuousDistribution implements EmpiricalDistributionI {
	private static final long serialVersionUID = 1L;

	private Vector<Element> _elements;
	private HashMap<Element, Element> _index;
	private Element _tmp = new Element(1, 1.0);
	private boolean _haveMean;
	private boolean _haveVariance;
	private double _mean;
	private double _variance;
	private double _cdfStart;

	public EmpiricalDistribution() {
	}

	public EmpiricalDistribution(VectorI x, VectorI probability) {
		setParameters(x, probability);
	}

	public void setParameters(VectorI x, VectorI probability) {
		if (x.size() != probability.size())
			throw new InvalidArgumentError("The arguments must be the same size.");
		if (x.size() == 0)
			throw new InvalidArgumentError("The arguments are empty.");

		setValid(false);
		_elements = new Vector<Element>(x.size());
		_index = new HashMap<Element, Element>(x.size());
		double scale = 1.0 / probability.sum(0);
		for (Iterator<VectorElementI> e = probability.elements(); e.hasNext();) {
			VectorElementI ve = e.next();
			checkProbability(ve.getValue());
			Element el = new Element(x.elementAt(ve.getIndex()), ve.getValue() * scale);
			_elements.addElement(el);
			_index.put(el, el);
		}
		new QuickSort<Element>(_tmp).sort(_elements);
		setVariableBounds(0, x.size());
		setCdfBounds(0.0, 1.0);
		setValid(true);
	}

	public double mean() {
		if (!_haveMean) {
			_haveMean = true;
			_mean = 0.0;
			for (int i = 0; i < _elements.size(); i++) {
				Element el = (Element) _elements.elementAt(i);
				_mean += el._probability * el._x;
			}
		}
		return _mean;
	}

	public double variance() {
		if (!_haveVariance) {
			_haveVariance = true;
			double mean = mean();
			_variance = 0.0;
			for (int i = 0; i < _elements.size(); i++) {
				Element el = (Element) _elements.elementAt(i);
				double del = el._x - mean;
				_variance += el._probability * del * del;
			}
		}
		return _variance;
	}

	public double std() {
		return Math.sqrt(variance());
	}

	public double pdf(double x) {
		_tmp._x = x;
		Element fnd = (Element) _index.get(_tmp);
		if (fnd == null)
			return 0.0;
		return fnd._probability;
	}

	public double _cdf(double x) {
		double sum = _cdfStart;
		for (int i = 0; i < _elements.size(); i++) {
			Element el = (Element) _elements.elementAt(i);
			if (el._x > x)
				break;
			sum += el._probability;
		}
		return sum;
	}

	public double probability(int x1, int x2) {
		double sum = 0.0;
		for (int i = 0; i < _elements.size(); i++) {
			Element el = (Element) _elements.elementAt(i);
			if (el._x < x1)
				continue;
			if (el._x > x2)
				break;
			sum += el._probability;
		}
		return sum;
	}

	public boolean equals(Object o) {
		if (!(o instanceof EmpiricalDistribution))
			return false;
		EmpiricalDistribution d = (EmpiricalDistribution) o;
		if (d._elements.size() != _elements.size())
			return false;
		for (int i = 0; i < _elements.size(); i++) {
			Element e1 = (Element) _elements.elementAt(i);
			Element e2 = (Element) d._elements.elementAt(i);
			if (e1._x != e2._x)
				return false;
			if (e1._probability != e2._probability)
				return false;
		}
		return true;
	}

	public String toString() {
		String str = "EmpiricalDistribution(";
		for (int i = 0; i < _elements.size(); i++) {
			Element e = (Element) _elements.elementAt(i);
			if (i > 0)
				str += ", ";
			str = str + e._x + "=" + e._probability;
		}
		str += ")";
		return str;
	}

	@Override
	public double _inverseCdf(double probability) {
		throw new ProbError("Not implemented!");
	}

	private static class Element implements CompareI {
		private double _x;
		private double _probability;

		Element(double x, double probability) {
			_x = x;
			_probability = probability;
		}

		public boolean equals(Object o) {
			return ((Element) o)._x == _x;
		}

		public int hashCode() {
			return Double.valueOf(_x).hashCode();
		}

		public int compare(Object a, Object b) {
			double xa = ((Element) a)._x;
			double xb = ((Element) b)._x;
			if (xa < xb)
				return CompareI.LESS;
			if (xb < xa)
				return CompareI.GREATER;
			return CompareI.EQUAL;
		}
	}

	@Override
	public void setCdfStart(double start) {
		_cdfStart = start;
	}

}
