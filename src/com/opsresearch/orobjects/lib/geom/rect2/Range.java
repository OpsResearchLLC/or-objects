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

package com.opsresearch.orobjects.lib.geom.rect2;

import com.opsresearch.orobjects.lib.geom.GeomError;

public class Range extends Rect2 implements RangeI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Point _min, _max;

	public Range(double x1, double y1, double x2, double y2) {
		double minX = Math.min(x1, x2);
		double maxX = Math.max(x1, x2);
		double minY = Math.min(y1, y2);
		double maxY = Math.max(y1, y2);
		_min = new Point(minX, minY);
		_max = new Point(maxX, maxY);

	}

	public Range(PointI point1, PointI point2) {
		this(point1.x(), point1.y(), point2.x(), point2.y());
	}

	public Range(RangeI range) {
		this(range.min(), range.max());
	}

	public RangeI range() {
		return this;
	}

	public PointI centroid() {
		return new Point((minX() + maxX()) * 0.5, (minY() + maxY()) * 0.5);
	}

	public PointI min() {
		return _min;
	}

	public PointI max() {
		return _max;
	}

	public PointI lowerLeft() {
		return min();
	}

	public PointI upperRight() {
		return max();
	}

	public PointI upperLeft() {
		return new Point(minX(), maxY());
	}

	public PointI lowerRight() {
		return new Point(maxX(), minY());
	}

	public double minX() {
		return _min.x();
	}

	public double minY() {
		return _min.y();
	}

	public double maxX() {
		return _max.x();
	}

	public double maxY() {
		return _max.y();
	}

	public com.opsresearch.orobjects.lib.geom.PointI boundPoint(com.opsresearch.orobjects.lib.geom.PointI point) {
		return bound((PointI) point);
	}

	public com.opsresearch.orobjects.lib.geom.PointI getMin() {
		return min();
	}

	public com.opsresearch.orobjects.lib.geom.PointI getMax() {
		return max();
	}

	public int hashCode() {
		long l = Double.doubleToLongBits(minX()) + Double.doubleToLongBits(minY()) + Double.doubleToLongBits(maxX())
				+ Double.doubleToLongBits(maxY());
		return (int) (l ^ (l >> 32));
	}

	public boolean equals(Object o) {
		if (!(o instanceof RangeI))
			return false;
		RangeI r = (RangeI) o;
		if (!_min.equals(r.min()))
			return false;
		if (!_max.equals(r.max()))
			return false;
		return true;
	}

	public String toString() {
		return "rect2.Range{" + _min.toString() + "; " + _max.toString() + "}";
	}

	public PointI bound(PointI point) {
		boolean nw = false;
		double x = point.x(), y = point.y();
		if (x < minX()) {
			x = minX();
			nw = true;
		} else if (x > maxX()) {
			x = maxX();
			nw = true;
		}
		if (y < minY()) {
			y = minY();
			nw = true;
		} else if (y > maxY()) {
			y = maxY();
			nw = true;
		}
		return nw ? new Point(x, y) : point;
	}

	public PointI nearestPointTo(PointI point) {
		PointI p = bound(point);
		if (p != point)
			return p;

		double x = p.x();
		double y = p.y();
		double maxDX = maxX() - x;
		double minDX = x - minX();
		double maxDY = maxY() - y;
		double minDY = y - minY();
		double best = Math.min(Math.min(maxDX, minDX), Math.min(maxDY, minDY));
		if (maxDX == best)
			return new Point(maxX(), y);
		else if (minDX == best)
			return new Point(minX(), y);
		else if (maxDY == best)
			return new Point(x, maxY());
		else
			return new Point(x, minY());
	}

	public double distanceTo(PointI point) {
		return ((PointI) point).distanceTo(nearestPointTo((PointI) point));
	}

	public double distanceProxyTo(PointI point) {
		return point.distanceProxyTo(nearestPointTo((PointI) point));
	}

	public boolean includes(com.opsresearch.orobjects.lib.geom.PointI point) {
		if (!(point instanceof PointI))
			throw new com.opsresearch.orobjects.lib.geom.GeomError(
					"The point must be on the same coordinate system as the range.");
		PointI p = (PointI) point;
		return _min.x() <= p.x() && p.x() <= _max.x() && _min.y() <= p.y() && p.y() <= _max.y();
	}

	public com.opsresearch.orobjects.lib.geom.RangeI getExpandedRange(com.opsresearch.orobjects.lib.geom.PointI point) {
		if (includes(point))
			return this;
		PointI p = (PointI) point;
		return new Range(Math.min(_min.x(), p.x()), Math.min(_min.y(), p.y()), Math.max(_max.x(), p.x()), Math.max(
				_max.y(), p.y()));
	}
}
