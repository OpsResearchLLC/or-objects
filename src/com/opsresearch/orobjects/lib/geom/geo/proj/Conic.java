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

public abstract class Conic extends Projection implements ProjectionI {

	protected double _north;
	protected double _south;

	public Conic(com.opsresearch.orobjects.lib.geom.geo.RangeI range)
			throws GeomException {
		double del = (range.north() - range.south()) / 3.0;
		init(range.north() - del, range.south() + del);
	}

	public Conic(double latitude1, double latitude2) throws GeomException {
		init(latitude1, latitude2);
	}

	private void init(double lat1, double lat2) throws GeomException {
		if (Math.abs(lat1 + lat2) < CoordinateSystem.getInstance()
				.getAccuracy())
			throw new GeomException("Latitude1 equals -Latitude2");
		_north = Constants.radiansPerDegree
				* Math.min(+90.0, Math.max(lat1, lat2));
		_south = Constants.radiansPerDegree
				* Math.max(-90.0, Math.min(lat1, lat2));
		setGeodetic(_geodetic);
	}

	public double getNorthParallel() {
		return _north / Constants.radiansPerDegree;
	}

	public double getSouthParallel() {
		return _south / Constants.radiansPerDegree;
	}
}
