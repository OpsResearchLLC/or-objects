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

public class LineSegment extends Rect2 implements LineSegmentI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private PointI _point1, _point2;
	private double _length;

	public LineSegment(double x1, double y1, double x2, double y2) {
		_point1 = new Point(x1, y1);
		_point2 = new Point(x2, y2);
		_length = _point1.distanceTo(_point2);
	}

	public LineSegment(PointI point1, PointI point2) {
		_point1 = new Point(point1);
		_point2 = new Point(point2);
		_length = _point1.distanceTo(_point2);
	}

	public LineSegment(LineSegmentI lineSegment) {
		_point1 = new Point(lineSegment.point1());
		_point2 = new Point(lineSegment.point2());
		_length = _point1.distanceTo(_point2);
	}

	public com.opsresearch.orobjects.lib.geom.PointI getPoint1() {
		return _point1;
	}

	public com.opsresearch.orobjects.lib.geom.PointI getPoint2() {
		return _point2;
	}

	public PointI point1() {
		return _point1;
	}

	public PointI point2() {
		return _point2;
	}

	public double x1() {
		return _point1.x();
	}

	public double y1() {
		return _point1.y();
	}

	public double x2() {
		return _point2.x();
	}

	public double y2() {
		return _point2.y();
	}

	public RangeI range() {
		return new Range(_point1, _point2);
	}

	public PointI centroid() {
		return new Point((x1() + x2()) * 0.5, (y1() + y2()) * 0.5);
	}

	public com.opsresearch.orobjects.lib.geom.PointI from() {
		return _point1;
	}

	public com.opsresearch.orobjects.lib.geom.PointI to() {
		return _point2;
	}

	public PointI nearestPointTo(PointI point) {
		double delXa = _point2.x() - _point1.x();
		double delYa = _point2.y() - _point1.y();
		double delXb = point.x() - _point1.x();
		double delYb = point.y() - _point1.y();
		double dotProduct = delXa * delXb + delYa * delYb;

		if (dotProduct < 0)
			return _point1;
		double projection = dotProduct / _length;
		if (projection > _length)
			return _point2;
		projection /= _length;
		return new Point(_point1.x() + projection * delXa, _point1.y() + projection * delYa);
	}

	public double distanceTo(PointI point) {
		return Math.sqrt(distanceProxyTo(point));
	}

	public double distanceProxyTo(PointI point) {
		PointI p = (PointI) point;
		double delXa = _point2.x() - _point1.x();
		double delYa = _point2.y() - _point1.y();
		double delXb = p.x() - _point1.x();
		double delYb = p.y() - _point1.y();
		double dotProduct = delXa * delXb + delYa * delYb;

		if (dotProduct < 0)
			return delXb * delXb + delYb * delYb;
		double projection = dotProduct / _length;
		if (projection > _length) {
			double delXc = p.x() - _point2.x();
			double delYc = p.y() - _point2.y();
			return delXc * delXc + delYc * delYc;
		}
		return delXb * delXb + delYb * delYb - (projection * projection);
	}

	public double length() {
		return _length;
	}

	public int hashCode() {
		long l = Double.doubleToLongBits(_point1.x()) + Double.doubleToLongBits(_point1.y())
				+ Double.doubleToLongBits(_point2.x()) + Double.doubleToLongBits(_point2.y());
		return (int) (l ^ (l >> 32));
	}

	public boolean equals(Object o) {
		if (!(o instanceof LineSegmentI))
			return false;
		LineSegmentI l = (LineSegmentI) o;
		if (!Point.equals(_point1, l.point1(), coordinateSystem().getAccuracy()))
			return false;
		if (!Point.equals(_point2, l.point2(), coordinateSystem().getAccuracy()))
			return false;
		return true;
	}

	public String toString() {
		return "rect2.LineSegment{" + _point1 + "; " + _point2 + "}";
	}
}
