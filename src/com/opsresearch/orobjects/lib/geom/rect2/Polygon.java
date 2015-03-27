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

import java.util.Enumeration;
import java.util.Vector;

public class Polygon extends Rect2 implements PolygonI, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Vector<PolygonI> _holes;
	private Vector<PointI> _vertices;

	public Polygon(PointI[] vertices) {
		_holes = new Vector<PolygonI>();
		_vertices = new Vector<PointI>(vertices.length);
		for (int i = 0; i < vertices.length; i++)
			_vertices.addElement(vertices[i]);
	}

	public Polygon(PointI[] vertices, PolygonI[] holes) {
		_holes = new Vector<PolygonI>(holes.length);
		_vertices = new Vector<PointI>(vertices.length);
		for (int i = 0; i < vertices.length; i++)
			_vertices.addElement(vertices[i]);
		for (int i = 0; i < holes.length; i++)
			_holes.addElement(holes[i]);
	}

	public PointI point(int index) {
		return _vertices.elementAt(index);
	}

	public RangeI range() {
		Enumeration<PointI> e = vertices();
		if (!e.hasMoreElements())
			throw new com.opsresearch.orobjects.lib.geom.GeomError("There are no vertices");
		PointI pt = e.nextElement();
		double minX = pt.x(), maxX = pt.x();
		double minY = pt.y(), maxY = pt.y();
		while (e.hasMoreElements()) {
			pt = e.nextElement();
			minX = Math.min(minX, pt.x());
			maxX = Math.max(maxX, pt.x());
			minY = Math.min(minY, pt.y());
			maxY = Math.max(maxY, pt.y());
		}
		return new Range(minX, minY, maxX, maxY);
	}

	public PointI centroid() {
		Enumeration<PointI> e = vertices();
		if (!e.hasMoreElements())
			throw new com.opsresearch.orobjects.lib.geom.GeomError("There are no vertices");
		int cnt = 0;
		double sx = 0.0, sy = 0.0;
		while (e.hasMoreElements()) {
			PointI pt = e.nextElement();
			sx += pt.x();
			sy += pt.y();
			cnt++;
		}
		return new Point(sx / cnt, sy / cnt);
	}

	public int sizeOfVertices() {
		int size = _vertices.size();
		for (Enumeration<PolygonI> holes = holes(); holes.hasMoreElements();) {
			size += holes.nextElement().sizeOfVertices();
		}
		return size;
	}

	public int sizeOfHoles() {
		int size = _holes.size();
		for (Enumeration<PolygonI> holes = holes(); holes.hasMoreElements();) {
			size += holes.nextElement().sizeOfHoles();
		}
		return size;
	}

	public com.opsresearch.orobjects.lib.geom.PointI getPoint(int index) {
		return _vertices.elementAt(index);
	}

	public Enumeration<PointI> vertices() {
		return _vertices.elements();
	}

	public Enumeration<PolygonI> holes() {
		return _holes.elements();
	}

	public PointI nearestPointTo(PointI point) {
		LineSegmentI bestSeg = null;
		double bestDist = Double.POSITIVE_INFINITY;
		Enumeration<PointI> e = vertices();
		LineSegment seg = null;
		PointI prev, curr = null;
		PointI first = prev = e.hasMoreElements() ? e.nextElement() : null;
		while (e.hasMoreElements()) {
			curr = e.nextElement();
			seg = new LineSegment(prev, curr);
			double dp = seg.distanceProxyTo(point);
			if (dp < bestDist) {
				bestSeg = seg;
				bestDist = dp;
			}
			prev = curr;
		}
		if (curr != null) {
			seg = new LineSegment(curr, first);
			double dp = seg.distanceProxyTo(point);
			if (dp < bestDist) {
				bestSeg = seg;
				bestDist = dp;
			}
		}

		PointI bestPoint = bestSeg == null ? null : bestSeg.nearestPointTo(point);

		for (Enumeration<PolygonI> holes = holes(); holes.hasMoreElements();) {
			PointI p = holes.nextElement().nearestPointTo(point);
			double dp = p.distanceProxyTo(point);
			if (dp < bestDist) {
				bestPoint = p;
				bestDist = dp;
			}
		}

		return bestPoint;
	}

	public double distanceTo(PointI point) {
		PointI p = nearestPointTo(point);
		return p == null ? Double.POSITIVE_INFINITY : p.distanceTo(point);
	}

	public double distanceProxyTo(PointI point) {
		PointI p = nearestPointTo(point);
		return p == null ? Double.POSITIVE_INFINITY : p.distanceProxyTo(point);
	}

	public double length() {
		double length = 0.0;
		Enumeration<PointI> e = vertices();
		PointI prev, curr = null;
		PointI first = prev = e.hasMoreElements() ? e.nextElement() : null;
		while (e.hasMoreElements()) {
			curr = e.nextElement();
			length += prev.distanceTo(curr);
			prev = curr;
		}
		if (curr != null)
			length += curr.distanceTo(first);
		for (Enumeration<PolygonI> holes = holes(); holes.hasMoreElements();) {
			length += holes.nextElement().length();
		}
		return length;
	}

	private static long hashCode(PolygonI poly) {
		long l = 0;
		for (Enumeration<?> e = poly.vertices(); e.hasMoreElements();) {
			PointI p = (PointI) e.nextElement();
			l += Double.doubleToLongBits(p.x());
			l += Double.doubleToLongBits(p.y());
		}
		for (Enumeration<?> holes = poly.holes(); holes.hasMoreElements();) {
			l += hashCode((PolygonI) holes.nextElement());
		}
		return l;
	}

	public int hashCode() {
		long l = hashCode(this);
		return (int) (l ^ (l >> 32));
	}

	public boolean equals(Object o) {
		if (!(o instanceof PolygonI))
			return false;
		PolygonI p = (PolygonI) o;
		if (p.sizeOfVertices() != sizeOfVertices())
			return false;
		if (p.sizeOfHoles() != sizeOfHoles())
			return false;
		if (hashCode(this) != hashCode(p))
			return false;
		if (Math.abs(length() - p.length()) > coordinateSystem().getAccuracy())
			return false;
		return true;
	}

	public String toString() {
		return "rect2.Polygon";
	}
}
