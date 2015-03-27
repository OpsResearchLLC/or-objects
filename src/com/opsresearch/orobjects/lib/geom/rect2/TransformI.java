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

package com.opsresearch.orobjects.lib.geom.rect2;

import com.opsresearch.orobjects.lib.geom.GeomException;

public interface TransformI extends com.opsresearch.orobjects.lib.geom.TransformI {
	public double tx();

	public double ty();

	public double r11();

	public double r12();

	public double r21();

	public double r22();

	public void set(double r11, double r12, double r21, double r22, double tx, double ty);

	public void combine(TransformI transform);

	public void translate(double deltaX, double deltaY);

	public void scale(double scaleX, double scaleY);

	public void rotate(double angleInRadians);

	public PointI forward(PointI point) throws GeomException;

	public PointI inverse(PointI point) throws GeomException;

	public RangeI forward(RangeI range) throws GeomException;

	public RangeI inverse(RangeI range) throws GeomException;
}
