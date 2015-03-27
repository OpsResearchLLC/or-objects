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
import com.opsresearch.orobjects.lib.geom.TransformI;
import com.opsresearch.orobjects.lib.geom.geo.GeodeticI;

public interface ProjectionI extends TransformI {
	public void setGeodetic(GeodeticI geidetic);

	public void setEasting(double easting);

	public double getEasting();

	public void setNorthing(double northing);

	public double getNorthing();

	public com.opsresearch.orobjects.lib.geom.rect2.PointI forward(com.opsresearch.orobjects.lib.geom.geo.PointI point)
			throws GeomException;

	public com.opsresearch.orobjects.lib.geom.geo.PointI inverse(com.opsresearch.orobjects.lib.geom.rect2.PointI point)
			throws GeomException;

	public com.opsresearch.orobjects.lib.geom.rect2.RangeI forward(com.opsresearch.orobjects.lib.geom.geo.RangeI range)
			throws GeomException;

	public com.opsresearch.orobjects.lib.geom.geo.RangeI inverse(com.opsresearch.orobjects.lib.geom.rect2.RangeI range)
			throws GeomException;

}
