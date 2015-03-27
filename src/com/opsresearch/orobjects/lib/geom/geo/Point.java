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
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public class Point extends Geo implements com.opsresearch.orobjects.lib.geom.PointI, PointI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private double _longitude, _latitude;

	public Point(double longitude, double latitude) {
		if (latitude < -90.0)
			throw new GeomError("The latitude can't be less than -90.0");
		if (latitude > +90.0)
			throw new GeomError("The latitude can't be greater than +90.0");
		_longitude = Math.IEEEremainder(longitude, 360.0);
		_latitude = latitude;
	}

	public Point(PointI point) {
		this(point.longitude(), point.latitude());
	}

	public Point(VectorI coordinates) {
		this(coordinates.elementAt(0), coordinates.elementAt(1));
	}

	public Point(double[] coordinates) {
		this(coordinates[0], coordinates[1]);
	}

	public double latitude() {
		return _latitude;
	}

	public double longitude() {
		return _longitude;
	}

	public double getCoordinate(int i) {
		return i == 0 ? _longitude : _latitude;
	}

	public RangeI range() {
		return new Range(this, this);
	}

	public PointI centroid() {
		return this;
	}

	public PointI nearestPointTo(PointI point) {
		return this;
	}

	public int hashCode() {
		long l = Double.doubleToLongBits(_longitude) + Double.doubleToLongBits(_latitude);
		return (int) (l ^ (l >> 32));
	}

	public boolean equals(Object o) {
		if (!(o instanceof PointI))
			return false;
		double ac = CoordinateSystem.getInstance().getAccuracy();
		if (Math.abs(_latitude - ((PointI) o).latitude()) > ac)
			return false;
		double d = Math.IEEEremainder(_longitude - ((PointI) o).longitude(), 360.0);
		if (Math.abs(d) > ac)
			return false;
		return true;
	}

	public String toString() {
		return "geo.Point(" + "longitude = " + _longitude + ", latitude = " + _latitude + ")";
	}

	public double getDirectionTo(com.opsresearch.orobjects.lib.geom.PointI point) {
		PointI p = (PointI) point;
		double a = Math.atan2(p.longitude() - _longitude, p.latitude() - _latitude);
		return a;
	}

	public double directionTo(PointI point) {
		double dlon = point.longitude() - _longitude;
		if (dlon > +180.0)
			dlon -= 360.0;
		if (dlon < -180)
			dlon += 360.0;
		double a = Math.atan2(dlon, point.latitude() - _latitude);
		return a;
	}

	public double distanceProxyTo(PointI point) {
		return getGeodetic().distanceProxy(this, (PointI) point);
	}

	public double distanceTo(PointI point) {
		return getGeodetic().greatCircleDistance(this, (PointI) point);
	}

}
