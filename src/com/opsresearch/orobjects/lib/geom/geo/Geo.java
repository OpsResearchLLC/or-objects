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

public abstract class Geo implements GeoI {
	public GeodeticI getGeodetic() {
		return CoordinateSystem.getInstance().getGeodetic();
	}

	public com.opsresearch.orobjects.lib.geom.CoordinateSystemI coordinateSystem() {
		return CoordinateSystem.getInstance();
	}

	public double getDistanceTo(com.opsresearch.orobjects.lib.geom.PointI point) {
		return distanceTo((PointI) point);
	}

	public double getDistanceProxyTo(com.opsresearch.orobjects.lib.geom.PointI point) {
		return distanceProxyTo((PointI) point);
	}

	public com.opsresearch.orobjects.lib.geom.RangeI getRange() {
		return range();
	}

	public com.opsresearch.orobjects.lib.geom.PointI getCentroid() {
		return centroid();
	}

	public com.opsresearch.orobjects.lib.geom.PointI getNearestPointTo(com.opsresearch.orobjects.lib.geom.PointI point) {
		return nearestPointTo((PointI) point);
	}

}
