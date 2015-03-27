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

package com.opsresearch.orobjects.lib.graph;

public class PropertiesAdapter implements PropertiesI {
	public boolean isVertexRestricted(VertexI vertex) {
		return false;
	}

	public double getVertexCost(VertexI vertex) {
		return vertex.getCost();
	}

	public double getVertexTime(VertexI vertex) {
		return vertex.getCost();
	}

	public double[] getVertexDemand(VertexI vertex) {
		return vertex.getDemand();
	}

	public boolean isSymmetric() {
		return true;
	}

	public boolean isEdgeRestricted(EdgeI edge, boolean reverse) {
		return false;
	}

	public double getEdgeCost(EdgeI edge, boolean isReverse) {
		return edge.getCost(isReverse);
	}

	public double getEdgeTime(EdgeI edge, boolean isReverse) {
		return edge.getTime(isReverse);
	}

	public double getEdgeDistance(EdgeI edge, boolean isReverse) {
		return edge.getDistance(isReverse);
	}

	public boolean isConnectionRestricted(EdgeI inEdge, VertexI vertex, EdgeI outEdge) {
		return false;
	}

	public double getConnectionCost(EdgeI inEdge, VertexI vertex, EdgeI outEdge) {
		return 0.0;
	}

	public double getConnectionTime(EdgeI inEdge, VertexI vertex, EdgeI outEdge) {
		return 0.0;
	}

}
