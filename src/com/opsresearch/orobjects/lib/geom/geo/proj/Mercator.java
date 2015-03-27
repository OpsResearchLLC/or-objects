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
import com.opsresearch.orobjects.lib.real.Constants;

public class Mercator extends Projection implements ProjectionI {
	private double _centralMeridian;

	public Mercator(com.opsresearch.orobjects.lib.geom.geo.RangeI range) {
		this(range.centroid().longitude());
	}

	public Mercator(double centralMeridian) {
		centralMeridian = Math.IEEEremainder(centralMeridian, 360.0);
		_centralMeridian = centralMeridian;
		setGeodetic(_geodetic);
	}

	public double getCentralMeridian() {
		return _centralMeridian;
	}

	public com.opsresearch.orobjects.lib.geom.rect2.PointI forward(com.opsresearch.orobjects.lib.geom.geo.PointI point)
			throws GeomException {
		double lat = point.latitude();
		double lon = point.longitude();
		if (Math.abs(lat) >= 90.0)
			throw new GeomException("The latitude must be strictly greater than -90.0 and less than 90.0.");
		while ((lon - _centralMeridian) < -180.0)
			lon += 360.0;
		while ((lon - _centralMeridian) > 180.0)
			lon -= 360.0;
		double x = (lon - _centralMeridian) * Constants.radiansPerDegree * _er;
		double latRad = lat * Constants.radiansPerDegree;
		double tmp = Math.pow((1.0 - _e * Math.sin(latRad)) / (1.0 + _e * Math.sin(latRad)), _halfE);
		double y = Math.log(Math.tan((45.0 + 0.5 * lat) * Constants.radiansPerDegree) * tmp) * _er;
		return new com.opsresearch.orobjects.lib.geom.rect2.Point(x + _easting, y + _northing);
	}

	public com.opsresearch.orobjects.lib.geom.geo.PointI inverse(com.opsresearch.orobjects.lib.geom.rect2.PointI point)
			throws GeomException {
		double x = point.x() - _easting;
		double y = point.y() - _northing;
		double lat = 0;
		double lon = (x / _er) / Constants.radiansPerDegree + _centralMeridian;
		double t = Math.exp(-y / _er);
		double tlat = 90.0 - 2.0 * Math.atan(t) / Constants.radiansPerDegree;
		double delta = 1.0;
		double ac = CoordinateSystem.getInstance().getAccuracy();
		for (int i = 0; delta > ac && i < 100; i++) {
			double tlatSin = Math.sin(tlat * Constants.radiansPerDegree);
			double tmp = (1.0 - _e * tlatSin) / (1.0 + _e * tlatSin);
			lat = 90.0 - 2.0 * Math.atan(t * Math.pow(tmp, _halfE)) / Constants.radiansPerDegree;
			delta = Math.abs(Math.abs(lat) - Math.abs(tlat));
			tlat = lat;
		}
		return new com.opsresearch.orobjects.lib.geom.geo.Point(lon, lat);
	}
}
