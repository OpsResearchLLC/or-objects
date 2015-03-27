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

public abstract class Geodetic implements GeodeticI {
	public double greatCircleAngle(PointI point1, PointI point2) {
		double a = Constants.radiansPerDegree * (90.0 - point2.latitude());
		double b = Constants.radiansPerDegree * (90.0 - point1.latitude());
		double c = Constants.radiansPerDegree * (point2.longitude() - point1.longitude());
		double cc = Math.cos(a) * Math.cos(b) + Math.sin(a) * Math.sin(b) * Math.cos(c);
		if (cc <= -1.0)
			return 180.0;
		if (cc >= 1.0)
			return 0.0;
		return Math.acos(cc);
	}

}
