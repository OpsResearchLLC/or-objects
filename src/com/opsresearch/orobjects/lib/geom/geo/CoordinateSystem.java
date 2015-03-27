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

import com.opsresearch.orobjects.lib.geom.CoordinateSystemI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public class CoordinateSystem implements CoordinateSystemI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static String[] _dimensionNames = { "longitude", "latitude" };
	private static CoordinateSystem _coordSys = new CoordinateSystem();

	public static CoordinateSystem getInstance() {
		return _coordSys;
	}

	private static GeodeticI _geodetic = Ellipsoid.getInstance("WGS 84");
	private static double _accuracy = 1.0E-6;

	private CoordinateSystem() {
	}

	public double getAccuracy() {
		return _accuracy;
	}

	static public void setAccuracy(double accuracy) {
		_accuracy = accuracy;
	}

	public boolean equals(double a, double b) {
		return Math.abs(a - b) <= _accuracy;
	}

	public GeodeticI getGeodetic() {
		return _geodetic;
	}

	public static void setGeodetic(GeodeticI geodetic) {
		_geodetic = geodetic;
	}

	public String toString() {
		return "geo";
	}

	public int sizeOfDimensions() {
		return 2;
	}

	public String dimensionName(int i) {
		return _dimensionNames[i];
	}

	public boolean isSymmetric() {
		return true;
	}

	public com.opsresearch.orobjects.lib.geom.PointI getPointInstance(com.opsresearch.orobjects.lib.geom.PointI point) {
		return new Point((PointI) point);
	}

	public com.opsresearch.orobjects.lib.geom.PointI getPointInstance(VectorI coordinates) {
		return new Point(coordinates);
	}

	public com.opsresearch.orobjects.lib.geom.PointI getPointInstance(double[] coordinates) {
		return new Point(coordinates);
	}

	public com.opsresearch.orobjects.lib.geom.RangeI getRangeInstance(com.opsresearch.orobjects.lib.geom.PointI boundaryPoint1,
			com.opsresearch.orobjects.lib.geom.PointI boundaryPoint2) {
		return new Range((PointI) boundaryPoint1, (PointI) boundaryPoint2);
	}

	public com.opsresearch.orobjects.lib.geom.RangeI getRangeInstance(com.opsresearch.orobjects.lib.geom.RangeI range) {
		return new Range((RangeI) range);
	}

	public boolean equals(Object o) {
		return o instanceof CoordinateSystem;
	}
}
