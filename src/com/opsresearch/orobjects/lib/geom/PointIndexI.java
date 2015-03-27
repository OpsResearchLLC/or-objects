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

import java.util.Enumeration;

import com.opsresearch.orobjects.lib.PairI;

public interface PointIndexI<valueType> {
	public int size();

	public int sizeOfSelected();

	public void removeAllElements();

	public void setCoordinateSystem(CoordinateSystemI coordinateSystem);

	public CoordinateSystemI coordinateSystem();

	public boolean supportsDuplicateKeys();

	public void put(PointI key, valueType value);

	public valueType get(PointI key);

	public PairI<PointI, valueType> getNearestNeighborTo(PointI point);

	public int selectNearestNeighbors(PointI point, int n);

	public int selectRange(RangeI range);

	public Enumeration<PairI<PointI, valueType>> elements();

	public Enumeration<PairI<PointI, valueType>> selectedElements();

	public RangeI range();
}
