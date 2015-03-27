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

import com.opsresearch.orobjects.lib.geom.CoordinateSystemI;
import com.opsresearch.orobjects.lib.geom.GeomError;
import com.opsresearch.orobjects.lib.geom.GeomException;

public class Transform implements TransformI {
	private double _r11, _r12, _r21, _r22, _tx, _ty;
	private double _ir11, _ir12, _ir21, _ir22, _itx, _ity;

	public Transform() {
		set(1, 0, 0, 1, 0, 0);

	}

	public Transform(double r11, double r12, double r21, double r22, double tx, double ty) {
		set(r11, r12, r21, r22, tx, ty);
	}

	public Transform(RangeI inRange, RangeI outRange) {
		this(inRange, outRange, true);
	}

	public Transform(RangeI inRange, RangeI outRange, boolean preserveAspectRatio) {
		this(inRange.min(), inRange.max(), outRange.min(), outRange.max(), preserveAspectRatio);
	}

	public Transform(PointI inPoint1, PointI inPoint2, PointI outPoint1, PointI outPoint2) {
		this(inPoint1, inPoint2, outPoint1, outPoint2, true);
	}

	public Transform(PointI inPoint1, PointI inPoint2, PointI outPoint1, PointI outPoint2, boolean preserveAspectRatio) {
		double sx = (outPoint2.x() - outPoint1.x()) / (inPoint2.x() - inPoint1.x());
		double sy = (outPoint2.y() - outPoint1.y()) / (inPoint2.y() - inPoint1.y());
		if (preserveAspectRatio) {
			double sgx = sx < 0.0 ? -1.0 : 1.0;
			double sgy = sy < 0.0 ? -1.0 : 1.0;
			sx = sy = Math.min(Math.abs(sx), Math.abs(sy));
			sx *= sgx;
			sy *= sgy;
		}
		set(1, 0, 0, 1, 0, 0);
		translate(-0.5 * (inPoint1.x() + inPoint2.x()), -0.5 * (inPoint1.y() + inPoint2.y()));
		scale(sx, sy);
		translate(0.5 * (outPoint1.x() + outPoint2.x()), 0.5 * (outPoint1.y() + outPoint2.y()));
	}

	public double tx() {
		return _tx;
	}

	public double ty() {
		return _ty;
	}

	public double r11() {
		return _r11;
	}

	public double r12() {
		return _r12;
	}

	public double r21() {
		return _r21;
	}

	public double r22() {
		return _r22;
	}

	public void set(double r11, double r12, double r21, double r22, double tx, double ty) {
		_r11 = r11;
		_r12 = r12;
		_r21 = r21;
		_r22 = r22;
		_tx = tx;
		_ty = ty;
		double a = r11 * r22 - r12 * r21;
		_ir11 = r22 / a;
		_ir21 = -r21 / a;
		_itx = (r21 * ty - r22 * tx) / a;
		_ir22 = r11 / a;
		_ir12 = -r12 / a;
		_ity = (r12 * tx - r11 * ty) / a;
	}

	public void combine(TransformI transform) {
		double r11 = _r11 * transform.r11() + _r12 * transform.r21();
		double r12 = _r11 * transform.r12() + _r12 * transform.r22();
		double r21 = _r21 * transform.r11() + _r22 * transform.r21();
		double r22 = _r21 * transform.r12() + _r22 * transform.r22();
		double tx = _tx * transform.r11() + _ty * transform.r21() + transform.tx();
		double ty = _tx * transform.r12() + _ty * transform.r22() + transform.ty();
		set(r11, r12, r21, r22, tx, ty);
	}

	public void translate(double deltaX, double deltaY) {
		combine(new Transform(1, 0, 0, 1, deltaX, deltaY));
	}

	public void scale(double scaleX, double scaleY) {
		combine(new Transform(scaleX, 0, 0, scaleY, 0, 0));
	}

	public void rotate(double angleInRadians) {
		double sin = Math.sin(angleInRadians);
		double cos = Math.cos(angleInRadians);
		combine(new Transform(cos, sin, -sin, cos, 0, 0));
	}

	public PointI forward(PointI point) throws GeomException {
		double x = point.x();
		double y = point.y();
		return new Point(x * _r11 + y * _r21 + _tx, x * _r12 + y * _r22 + _ty);
	}

	public PointI inverse(PointI point) throws GeomException {
		double x = point.x();
		double y = point.y();
		return new Point(x * _ir11 + y * _ir21 + _itx, x * _ir12 + y * _ir22 + _ity);
	}

	public RangeI forward(RangeI range) throws GeomException {
		return new Range(forward(range.min()), forward(range.max()));
	}

	public RangeI inverse(RangeI range) throws GeomException {
		return new Range(inverse(range.min()), inverse(range.max()));
	}

	public String toString() {
		return "rect2.TranslateScaleRotate(" + _r11 + ", " + _r12 + ", " + _r21 + ", " + _r22 + ", " + _tx + ", " + _ty
				+ ")";
	}

	public CoordinateSystemI inputCoordinateSystem() {
		return com.opsresearch.orobjects.lib.geom.rect2.CoordinateSystem.getInstance();
	}

	public CoordinateSystemI outputCoordinateSystem() {
		return com.opsresearch.orobjects.lib.geom.rect2.CoordinateSystem.getInstance();
	}

	public com.opsresearch.orobjects.lib.geom.PointI forwardTransform(com.opsresearch.orobjects.lib.geom.PointI point)
			throws GeomException {
		if (!(point instanceof com.opsresearch.orobjects.lib.geom.rect2.PointI))
			throw new GeomError("The argument must be a 'rect2.PointI'.");
		return forward((com.opsresearch.orobjects.lib.geom.rect2.PointI) point);
	}

	public com.opsresearch.orobjects.lib.geom.PointI inverseTransform(com.opsresearch.orobjects.lib.geom.PointI point)
			throws GeomException {
		if (!(point instanceof com.opsresearch.orobjects.lib.geom.rect2.PointI))
			throw new GeomError("The argument must be a 'rect2.PointI'.");
		return inverse((com.opsresearch.orobjects.lib.geom.rect2.PointI) point);
	}

	public com.opsresearch.orobjects.lib.geom.RangeI forwardTransform(com.opsresearch.orobjects.lib.geom.RangeI range)
			throws GeomException {
		if (!(range instanceof com.opsresearch.orobjects.lib.geom.rect2.RangeI))
			throw new GeomError("The argument must be a 'rect2.RangeI'.");
		return forward((com.opsresearch.orobjects.lib.geom.rect2.RangeI) range);
	}

	public com.opsresearch.orobjects.lib.geom.RangeI inverseTransform(com.opsresearch.orobjects.lib.geom.RangeI range)
			throws GeomException {
		if (!(range instanceof com.opsresearch.orobjects.lib.geom.rect2.RangeI))
			throw new GeomError("The argument must be a 'rect2.RangeI'.");
		return inverse((com.opsresearch.orobjects.lib.geom.rect2.RangeI) range);
	}
}
