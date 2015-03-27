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

package com.opsresearch.orobjects.lib.graph.vrp;

import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;

public interface ConstructI extends VRPI {
	public void selectVertex(boolean[] select);

	public void selectVertex(boolean select);

	public void selectVertex(Object key, boolean select) throws VertexNotFoundException;

	public double constructOutboundTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException;

	public double constructInboundTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException;

	public double constructClosedTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException;

}
