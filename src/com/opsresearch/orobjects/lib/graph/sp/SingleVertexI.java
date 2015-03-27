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

package com.opsresearch.orobjects.lib.graph.sp;

import java.util.Iterator;
import java.util.Vector;

import com.opsresearch.orobjects.lib.graph.EdgeValueI;
import com.opsresearch.orobjects.lib.graph.ElementI;
import com.opsresearch.orobjects.lib.graph.GraphError;
import com.opsresearch.orobjects.lib.graph.InvalidPropertyException;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;

public interface SingleVertexI {
	public boolean usesConnectionProperties();

	public void setListener(SingleVertexListenerI listener);

	public void setProperties(PropertiesI properties);

	public void setMaxPaths(int maxPaths);

	public void setMaxLength(int maxLength);

	public void setMaxCost(double maxCost);

	public void setMaxTime(double maxTime);

	public void setMaxDistance(double maxDistance);

	public void setCandidate(boolean isCandidate);

	public void setCandidate(Object key, boolean isCandidate) throws VertexNotFoundException;

	public int generatePathsFrom(Object rootKey) throws VertexNotFoundException, InvalidPropertyException;

	public int generatePathsTo(Object rootKey) throws VertexNotFoundException, InvalidPropertyException;

	public VertexI getNearestCandidate();

	public Iterator<VertexI> candidates();

	public Iterator<ElementI> pathElements(VertexI candidate) throws VertexNotFoundException;

	public Vector<ElementI> getPath(VertexI candidate) throws VertexNotFoundException;

	public EdgeValueI getEdgeValue(VertexI candidate) throws VertexNotFoundException;
}
