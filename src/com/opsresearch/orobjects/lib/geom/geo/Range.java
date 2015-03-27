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

package com.opsresearch.orobjects.lib.geom.geo;

import com.opsresearch.orobjects.lib.geom.GeomError;

public class Range extends Geo implements RangeI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Point _southwest, _northeast;

	public Range(double west, double latitude1, double east, double latitude2) {
		double north = Math.max(latitude1, latitude2);
		double south = Math.min(latitude1, latitude2);
		_southwest = new Point(west, Math.min(north, south));
		_northeast = new Point(east, Math.max(north, south));
	}

	public Range(PointI point1, PointI point2) {
		this(point1.longitude(), point1.latitude(), point2.longitude(), point2.latitude());
	}

	public Range(RangeI range) {
		this(range.southwest(), range.northeast());
	}

	public com.opsresearch.orobjects.lib.geom.PointI boundPoint(com.opsresearch.orobjects.lib.geom.PointI point) {
		return bound((PointI) point);
	}

	public com.opsresearch.orobjects.lib.geom.PointI getMin() {
		return southwest();
	}

	public com.opsresearch.orobjects.lib.geom.PointI getMax() {
		return northeast();
	}

	public RangeI range() {
		return this;
	}

	public PointI centroid() {
		double east = (west() < east()) ? east() : east() + 360.0;
		return new Point((east + west()) * 0.5, (north() + south()) * 0.5);
	}

	public double east() {
		return _northeast.longitude();
	}

	public double west() {
		return _southwest.longitude();
	}

	public double north() {
		return _northeast.latitude();
	}

	public double south() {
		return _southwest.latitude();
	}

	public PointI southwest() {
		return _southwest;
	}

	public PointI northeast() {
		return _northeast;
	}

	public PointI bound(PointI point) {
		if (includes(point))
			return point;
		double lon = point.longitude(), lat = point.latitude();

		if (lat < south())
			lat = south();
		else if (lat > north())
			lat = north();

		if (Math.abs(Math.IEEEremainder(lon - west(), 360.0)) < Math.abs(Math.IEEEremainder(lon - east(), 360.0)))
			lon = west();
		else
			lon = east();

		return new Point(lon, lat);
	}

	public PointI nearestPointTo(PointI point) {
		PointI p = bound(point);
		if (p != point)
			return p;
		double lon = point.longitude(), lat = point.latitude();
		Point p1 = new Point(west(), lat);
		Point p2 = new Point(east(), lat);
		Point p3 = new Point(lon, north());
		Point p4 = new Point(lon, south());
		double dp1 = p1.distanceProxyTo(point);
		double dp2 = p1.distanceProxyTo(point);
		double dp3 = p1.distanceProxyTo(point);
		double dp4 = p1.distanceProxyTo(point);
		double best = Math.min(Math.min(dp1, dp2), Math.min(dp3, dp4));
		if (dp1 == best)
			return p1;
		else if (dp2 == best)
			return p2;
		else if (dp3 == best)
			return p3;
		else
			return p4;
	}

	public double distanceTo(PointI point) {
		return ((PointI) point).distanceTo(nearestPointTo((PointI) point));
	}

	public double distanceProxyTo(PointI point) {
		return point.distanceProxyTo(nearestPointTo((PointI) point));
	}

	public int hashCode() {
		return (int) (Double.doubleToLongBits(north()) + Double.doubleToLongBits(south())
				+ Double.doubleToLongBits(east()) + Double.doubleToLongBits(west()));
	}

	public boolean equals(Object o) {
		if (!(o instanceof RangeI))
			return false;
		if (!_southwest.equals(((RangeI) o).southwest()))
			return false;
		if (!_northeast.equals(((RangeI) o).northeast()))
			return false;
		return true;
	}

	public String toString() {
		return "geo.Range{" + _southwest.toString() + "; " + _northeast.toString() + "}";
	}

	public boolean includes(com.opsresearch.orobjects.lib.geom.PointI point) {
		if (!(point instanceof PointI))
			throw new com.opsresearch.orobjects.lib.geom.GeomError(
					"The point must be in the same coordinate system as the range.");
		PointI p = (PointI) point;
		if (p.latitude() > north())
			return false;
		if (p.latitude() < south())
			return false;
		if (west() < east()) {
			if (p.longitude() < west())
				return false;
			if (p.longitude() > east())
				return false;
		} else {
			if (p.longitude() < west() && p.longitude() > east())
				return false;
		}
		return true;
	}

	public com.opsresearch.orobjects.lib.geom.RangeI getExpandedRange(com.opsresearch.orobjects.lib.geom.PointI point) {
		if (!(point instanceof PointI))
			throw new com.opsresearch.orobjects.lib.geom.GeomError(
					"The point must be in the same coordinate system as the range.");
		if (includes(point))
			return this;
		PointI p = (PointI) point;
		double north = north(), south = south(), east = east(), west = west();
		double dn = Math.abs(p.latitude() - north);
		double ds = Math.abs(p.latitude() - south);
		double dw = Math.abs(Math.IEEEremainder(p.longitude() - west, 360.0));
		double de = Math.abs(Math.IEEEremainder(p.longitude() - east, 360.0));
		if (dn < ds)
			north = p.longitude();
		else
			south = p.longitude();
		if (de < dw)
			east = p.longitude();
		else
			west = p.longitude();
		return new Range(west, south, east, north);
	}
}
