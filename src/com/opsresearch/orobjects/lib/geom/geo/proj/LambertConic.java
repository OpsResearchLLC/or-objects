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

public class LambertConic extends Conic implements ProjectionI, ConicI {
	private double _n, _c;

	public LambertConic(com.opsresearch.orobjects.lib.geom.geo.RangeI range) throws GeomException {
		super(range);
	}

	public LambertConic(double latitude1, double latitude2) throws GeomException {
		super(latitude1, latitude2);
	}

	public void setGeodetic(GeodeticI geodetic) {
		super.setGeodetic(geodetic);

		double sinLat = _n = Math.sin(_south);
		double cosLat = Math.cos(_south);
		boolean secant = Math.abs(_north - _south) >= CoordinateSystem.getInstance().getAccuracy();

		double m1 = msfn(sinLat, cosLat);
		double ml1 = tsfn(_south, sinLat);
		if (secant) {
			sinLat = Math.sin(_north);
			_n = Math.log(m1 / msfn(sinLat, Math.cos(_north))) / Math.log(ml1 / tsfn(_north, sinLat));
		}
		_c = (m1 * Math.pow(ml1, -_n) / _n);
	}

	public com.opsresearch.orobjects.lib.geom.rect2.PointI forward(com.opsresearch.orobjects.lib.geom.geo.PointI point)
			throws GeomException {
		double x = 0, y = 0;
		double lat = Constants.radiansPerDegree * point.latitude();
		double lon = Constants.radiansPerDegree * point.longitude();
		double rho;
		if (Math.abs(Math.abs(lat) - Constants.halfPi) < 1.0E-11)
			rho = 0.0;
		else
			rho = _c * Math.pow(tsfn(lat, Math.sin(lat)), _n);
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
		if (rho != 0.0) {
			if (_n < 0.0) {
				rho = -rho;
				x = -x;
				y = -y;
			}
			lat = phi2(Math.pow(rho / _c, 1.0 / _n));
			lon = Math.atan2(x, y) / _n;
		} else {
			lon = 0.0;
			lat = _n > 0.0 ? Constants.halfPi : -Constants.halfPi;
		}
		return new com.opsresearch.orobjects.lib.geom.geo.Point(lon / Constants.radiansPerDegree, lat
				/ Constants.radiansPerDegree);
	}
}
