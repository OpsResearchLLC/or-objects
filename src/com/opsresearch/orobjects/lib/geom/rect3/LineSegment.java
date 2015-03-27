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

package com.opsresearch.orobjects.lib.geom.rect3;

public class LineSegment extends Rect3 implements LineSegmentI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private PointI _point1, _point2;
	private double _length;

	public LineSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		this(new Point(x1, y1, z1), new Point(x2, y2, z2));

	}

	public LineSegment(PointI from, PointI to) {
		_point2 = new Point(to);
		_point1 = new Point(from);
		_length = from.distanceTo(to);
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

	public double z1() {
		return _point1.z();
	}

	public double x2() {
		return _point2.x();
	}

	public double y2() {
		return _point2.y();
	}

	public double z2() {
		return _point2.z();
	}

	public com.opsresearch.orobjects.lib.geom.PointI from() {
		return _point1;
	}

	public com.opsresearch.orobjects.lib.geom.PointI to() {
		return _point2;
	}

	public RangeI range() {
		return new Range(_point1, _point2);
	}

	public PointI centroid() {
		return new Point((x1() + x2()) * 0.5, (y1() + y2()) * 0.5, (z1() + z2()) * 0.5);
	}

	public PointI nearestPointTo(PointI point) {
		double delXa = _point2.x() - _point1.x();
		double delYa = _point2.y() - _point1.y();
		double delZa = _point2.z() - _point1.z();
		double delXb = point.x() - _point1.x();
		double delYb = point.y() - _point1.y();
		double delZb = point.z() - _point1.z();
		double dotProduct = delXa * delXb + delYa * delYb + delZa * delZb;

		if (dotProduct < 0)
			return _point1;
		double projection = dotProduct / _length;
		if (projection > _length)
			return _point2;
		projection /= _length;
		return new Point(_point1.x() + projection * delXa, _point1.y() + projection * delYa, _point1.z() + projection
				* delZa);
	}

	public double distanceTo(PointI point) {
		return Math.sqrt(distanceProxyTo(point));
	}

	public double distanceProxyTo(PointI point) {
		double delXa = _point2.x() - _point1.x();
		double delYa = _point2.y() - _point1.y();
		double delZa = _point2.z() - _point1.z();
		double delXb = point.x() - _point1.x();
		double delYb = point.y() - _point1.y();
		double delZb = point.z() - _point1.z();
		double dotProduct = delXa * delXb + delYa * delYb + delZa * delZb;

		if (dotProduct < 0)
			return delXb * delXb + delYb * delYb + delZb * delZb;
		double projection = dotProduct / _length;
		if (projection > _length) {
			double delXc = point.x() - _point2.x();
			double delYc = point.y() - _point2.y();
			double delZc = point.z() - _point2.z();
			return delXc * delXc + delYc * delYc + delZc * delZc;
		}
		return delXb * delXb + delYb * delYb + delZb * delZb - (projection * projection);
	}

	public double length() {
		return _length;
	}

	public int hashCode() {
		long l = Double.doubleToLongBits(_point1.x()) + Double.doubleToLongBits(_point1.y())
				+ Double.doubleToLongBits(_point1.z()) + Double.doubleToLongBits(_point2.x())
				+ Double.doubleToLongBits(_point2.y()) + Double.doubleToLongBits(_point2.z());
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
		return "rect3.LineSegment{" + _point1 + "; " + _point2 + "}";
	}

}
