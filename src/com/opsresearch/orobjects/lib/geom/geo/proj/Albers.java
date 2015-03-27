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

import com.opsresearch.orobjects.lib.geom.GeomException;
import com.opsresearch.orobjects.lib.geom.geo.CoordinateSystem;
import com.opsresearch.orobjects.lib.geom.geo.GeodeticI;
import com.opsresearch.orobjects.lib.real.Constants;

public class Albers extends Conic implements ProjectionI, ConicI {
	private double _n;
	private double _c;
	private double _ec;
	private double _dd;

	public Albers(com.opsresearch.orobjects.lib.geom.geo.RangeI range) throws GeomException {
		super(range);
	}

	public Albers(double latitude1, double latitude2) throws GeomException {
		super(latitude1, latitude2);
	}

	public void setGeodetic(GeodeticI geidetic) {
		super.setGeodetic(geidetic);
		double sinLat = _n = Math.sin(_south);
		double cosLat = Math.cos(_south);
		boolean secant = Math.abs(_south - _north) >= CoordinateSystem.getInstance().getAccuracy();
		double m1 = msfn(sinLat, cosLat);
		double ml1 = qsfn(sinLat);
		if (secant) {
			sinLat = Math.sin(_north);
			cosLat = Math.cos(_north);
			double m2 = msfn(sinLat, cosLat);
			double ml2 = qsfn(sinLat);
			_n = (m1 * m1 - m2 * m2) / (ml2 - ml1);
		}
		_ec = 1.0 - 0.5 * _oneMinusE2 * Math.log((1.0 - _e) / (1.0 + _e)) / _e;
		_c = m1 * m1 + _n * ml1;
		_dd = 1.0 / _n;
	}

	public com.opsresearch.orobjects.lib.geom.rect2.PointI forward(com.opsresearch.orobjects.lib.geom.geo.PointI point)
			throws GeomException {
		double x = 0, y = 0;
		double lat = Constants.radiansPerDegree * point.latitude();
		double lon = Constants.radiansPerDegree * point.longitude();
		double rho = _c - _n * qsfn(Math.sin(lat));
		if (rho < 0.0)
			throw new GeomException("Less than zero");
		rho = _dd * Math.sqrt(rho);
		lon *= _n;
		x = rho * Math.sin(lon);
		y = rho * Math.cos(lon);
		return new com.opsresearch.orobjects.lib.geom.rect2.Point(x * _er + _easting, y * _er + _northing);
	}

	public com.opsresearch.orobjects.lib.geom.geo.PointI inverse(com.opsresearch.orobjects.lib.geom.rect2.PointI point)
			throws GeomException {
		double lon = 0, lat = 0;
		double x = (point.x() - _easting) / _er;
		double y = (point.y() - _northing) / _er;
		double rho = Math.sqrt(x * x + y * y);
		if (rho == 0.0) {
			lon = 0.;
			lat = _n > 0.0 ? Constants.halfPi : -Constants.halfPi;
		} else {
			if (_n < 0.0) {
				rho = -rho;
				x = -x;
				y = -y;
			}
			lat = rho / _dd;
			lat = (_c - lat * lat) / _n;
			if (Math.abs(_ec - Math.abs(lat)) > CoordinateSystem.getInstance().getAccuracy()) {
				lat = phi1(lat);
			} else {
				lat = lat > 0.0 ? Constants.halfPi : -Constants.halfPi;
			}
			lon = Math.atan2(x, y) / _n;
		}
		return new com.opsresearch.orobjects.lib.geom.geo.Point(lon / Constants.radiansPerDegree, lat
				/ Constants.radiansPerDegree);
	}
}
