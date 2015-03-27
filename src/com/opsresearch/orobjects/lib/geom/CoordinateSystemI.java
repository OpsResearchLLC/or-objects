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

package com.opsresearch.orobjects.lib.geom;

import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public interface CoordinateSystemI {
	public double getAccuracy();

	public boolean equals(double a, double b);

	public int sizeOfDimensions();

	public String dimensionName(int i);

	public boolean isSymmetric();

	public PointI getPointInstance(PointI point);

	public PointI getPointInstance(VectorI coordinates);

	public PointI getPointInstance(double[] coordinates);

	public RangeI getRangeInstance(PointI boundaryPoint1, PointI boundaryPoint2);

	public RangeI getRangeInstance(RangeI range);

}
