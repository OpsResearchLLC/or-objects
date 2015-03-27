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

package com.opsresearch.orobjects.lib.geom.geo.proj;

import com.opsresearch.orobjects.lib.geom.CoordinateSystemI;
import com.opsresearch.orobjects.lib.geom.GeomError;
import com.opsresearch.orobjects.lib.geom.GeomException;
import com.opsresearch.orobjects.lib.geom.geo.CoordinateSystem;
import com.opsresearch.orobjects.lib.geom.geo.GeodeticI;
import com.opsresearch.orobjects.lib.real.Constants;

public abstract class Projection implements ProjectionI {

	protected double _easting = 0.0;
	protected double _northing = 0.0;
	protected double _e, _e2, _er, _halfE, _oneMinusE2;
	protected GeodeticI _geodetic = null;

	public Projection() {
		_geodetic = CoordinateSystem.getInstance().getGeodetic();
	}

	public void setEasting(double easting) {
		_easting = easting;
	}

	public double getEasting() {
		return _easting;
	}

	public void setNorthing(double northing) {
		_northing = northing;
	}

	public double getNorthing() {
		return _northing;
	}

	public GeodeticI getGeodetic() {
		return _geodetic;
	}

	public void setGeodetic(GeodeticI geodetic) {
		_geodetic = geodetic;
		_e = _geodetic.getMeanEccentricity();
		_e2 = _e * _e;
		_er = _geodetic.getMeanEquatorialRadius();
		_halfE = 0.5 * _e;
		_oneMinusE2 = 1.0 - _e2;
	}

	public CoordinateSystemI inputCoordinateSystem() {
		return com.opsresearch.orobjects.lib.geom.geo.CoordinateSystem.getInstance();
	}

	public CoordinateSystemI outputCoordinateSystem() {
		return com.opsresearch.orobjects.lib.geom.rect2.CoordinateSystem
				.getInstance();
	}

	public com.opsresearch.orobjects.lib.geom.PointI forwardTransform(
			com.opsresearch.orobjects.lib.geom.PointI point) throws GeomException {
		if (!(point instanceof com.opsresearch.orobjects.lib.geom.geo.PointI))
			throw new GeomError("The argument must be a 'geo.PointI'.");
		return forward((com.opsresearch.orobjects.lib.geom.geo.PointI) point);
	}

	public com.opsresearch.orobjects.lib.geom.PointI inverseTransform(
			com.opsresearch.orobjects.lib.geom.PointI point) throws GeomException {
		if (!(point instanceof com.opsresearch.orobjects.lib.geom.rect2.PointI))
			throw new GeomError("The argument must be a 'rect2.PointI'.");
		return inverse((com.opsresearch.orobjects.lib.geom.rect2.PointI) point);
	}

	public com.opsresearch.orobjects.lib.geom.RangeI forwardTransform(
			com.opsresearch.orobjects.lib.geom.RangeI range) throws GeomException {
		if (!(range instanceof com.opsresearch.orobjects.lib.geom.geo.RangeI))
			throw new GeomError("The argument must be a 'geo.RangeI'.");
		return forward((com.opsresearch.orobjects.lib.geom.geo.RangeI) range);
	}

	public com.opsresearch.orobjects.lib.geom.RangeI inverseTransform(
			com.opsresearch.orobjects.lib.geom.RangeI range) throws GeomException {
		if (!(range instanceof com.opsresearch.orobjects.lib.geom.rect2.RangeI))
			throw new GeomError("The argument must be a 'rect2.RangeI'.");
		return inverse((com.opsresearch.orobjects.lib.geom.rect2.RangeI) range);
	}

	public com.opsresearch.orobjects.lib.geom.rect2.RangeI forward(
			com.opsresearch.orobjects.lib.geom.geo.RangeI range) throws GeomException {
		return new com.opsresearch.orobjects.lib.geom.rect2.Range(
				forward(range.southwest()), forward(range.northeast()));
	}

	public com.opsresearch.orobjects.lib.geom.geo.RangeI inverse(
			com.opsresearch.orobjects.lib.geom.rect2.RangeI range)
			throws GeomException {
		return new com.opsresearch.orobjects.lib.geom.geo.Range(inverse(range.min()),
				inverse(range.max()));
	}

	protected double msfn(double radians) {
		return msfn(Math.sin(radians), Math.cos(radians));
	}

	protected double msfn(double sin, double cos) {
		return cos / Math.sqrt(1.0 - _e2 * sin * sin);
	}

	protected double tsfn(double radians) {
		return tsfn(radians, Math.sin(radians));
	}

	protected double tsfn(double radians, double sin) {
		sin *= _e;
		return Math.tan(.5 * (Constants.halfPi - radians))
				/ Math.pow((1.0 - sin) / (1.0 + sin), 0.5 * _e);
	}

	protected double phi2(double ts) {
		double dLat, lat = Constants.halfPi - 2.0 * Math.atan(ts);
		int i = 15;
		do {
			double con = _e * Math.sin(lat);
			dLat = Constants.halfPi
					- 2.0
					* Math.atan(ts
							* Math.pow((1.0 - con) / (1.0 + con), _halfE))
					- lat;
			lat += dLat;
		} while (Math.abs(dLat) > 1.0E-10 && --i != 0);
		return (i != 0 ? lat : Double.MAX_VALUE);
	}

	protected double phi1(double qs) {
		double dLat, lat = Math.asin(0.5 * qs);
		if (_e < CoordinateSystem.getInstance().getAccuracy())
			return lat;
		int i = 15;
		do {
			double sinLat = Math.sin(lat);
			double cosLat = Math.cos(lat);
			double con = _e * sinLat;
			double com = 1.0 - con * con;
			dLat = 0.5
					* com
					* com
					/ cosLat
					* (qs / _oneMinusE2 - sinLat / com + 0.5 / _e
							* Math.log((1.0 - con) / (1.0 + con)));
			lat += dLat;
		} while (Math.abs(dLat) > 1.0E-10 && --i != 0);
		return (i != 0 ? lat : Double.MAX_VALUE);
	}

	protected double qsfn(double sin) {
		if (_e >= CoordinateSystem.getInstance().getAccuracy()) {
			double con = _e * sin;
			return _oneMinusE2
					* (sin / (1.0 - con * con) - (0.5 / _e)
							* Math.log((1. - con) / (1.0 + con)));
		} else {
			return (2.0 * sin);
		}
	}

}
