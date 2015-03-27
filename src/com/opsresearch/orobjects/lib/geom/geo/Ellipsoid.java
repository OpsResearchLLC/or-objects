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

import com.opsresearch.orobjects.lib.real.Constants;

public class Ellipsoid extends Geodetic implements EllipsoidI, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	static private Ellipsoid[] _ellipsoids = { new Ellipsoid("WGS 84", 6378137.000, 1.0 / 298.257223563),
			new Ellipsoid("WGS 72", 6378135.000, 1.0 / 298.26), new Ellipsoid("WGS 66", 6378145.000, 1.0 / 298.25),
			new Ellipsoid("WGS 60", 6378165.000, 1.0 / 298.30),
			new Ellipsoid("South American 1969", 6378160.000, 1.0 / 298.25),
			new Ellipsoid("Krassovsky 1940", 6378245.000, 1.0 / 298.30),
			new Ellipsoid("Hough 1956", 6378270.000, 1.0 / 297.00),
			new Ellipsoid("GRS 1980", 6378137.000, 1.0 / 298.257222101),
			new Ellipsoid("GRS 1975", 6378140.000, 1.0 / 298.257),
			new Ellipsoid("GRS 1967", 6378160.000, 1.0 / 298.247167427),
			new Ellipsoid("Fischer 1968", 6378150.000, 1.0 / 298.30),
			new Ellipsoid("Fischer 1960", 6378166.000, 1.0 / 298.30),
			new Ellipsoid("Everest 1830", 6377276.345, 1.0 / 300.8017),
			new Ellipsoid("Clarke 1880", 6378249.145, 1.0 / 293.465),
			new Ellipsoid("Clarke 1866", 6378206.400, 1.0 / 294.9786982),
			new Ellipsoid("Bessel 1841", 6377397.155, 1.0 / 299.1528128),
			new Ellipsoid("Airy 1830", 6377563.396, 1.0 / 299.3249646), };


	static public Ellipsoid getInstance(String name) {
		for (int i = 0; i < _ellipsoids.length; i++)
			if (name.equals(_ellipsoids[i].getName()))
				return _ellipsoids[i];
		return null;
	}

	static public String[] getEllipsoidNames() {
		String[] names = new String[_ellipsoids.length];
		for (int i = 0; i < _ellipsoids.length; i++)
			names[i] = _ellipsoids[i].getName();
		return names;
	}

	private String _name;
	private double _flattening;
	private double _meanRadius;
	private double _polarRadius;
	private double _equatorialRadius;
	private double _eccentricity;
	private double _eccentricitySquared;

	public Ellipsoid(String name, double equatorialRadius, double flattening) {
		_name = name;
		_flattening = flattening;
		_equatorialRadius = equatorialRadius;
		_polarRadius = equatorialRadius - (flattening * equatorialRadius);
		_meanRadius = (_polarRadius + _equatorialRadius) * 0.5;
		_eccentricitySquared = 2.0 * flattening - flattening * flattening;
		_eccentricity = Math.sqrt(_eccentricitySquared);
	}

	public String getName() {
		return _name;
	}

	public double getFlattening() {
		return _flattening;
	}

	@Override
	public double getMeanEccentricity() {
		return _eccentricity;
	}

	public double getMeanPolarRadius() {
		return _polarRadius;
	}

	public double getMeanEquatorialRadius() {
		return _equatorialRadius;
	}

	public double getMeanRadius() {
		return _meanRadius;
	}

	public double distanceProxy(PointI point1, PointI point2) {
		return greatCircleSphericalDistance(point1, point2);
	}

	public double greatCircleDistance(PointI point1, PointI point2) {
		return greatCircleSphericalDistance(point1, point2);
	}

	public double greatCircleSphericalDistance(PointI point1, PointI point2) {
		double lat1 = Constants.radiansPerDegree * point1.latitude();
		double lat2 = Constants.radiansPerDegree * point2.latitude();
		double q1 = Math.cos(Constants.radiansPerDegree * point1.longitude() - Constants.radiansPerDegree
				* point2.longitude());
		double d = _meanRadius
				* Math.acos(0.5 * ((1.0 + q1) * Math.cos(lat1 - lat2) - (1.0 - q1) * Math.cos(lat1 + lat2)));
		return d;
	}

}
