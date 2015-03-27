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

import com.opsresearch.orobjects.lib.graph.AddI;
import com.opsresearch.orobjects.lib.graph.DuplicateEdgeException;
import com.opsresearch.orobjects.lib.graph.DuplicateVertexException;
import com.opsresearch.orobjects.lib.graph.InvalidPropertyException;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;
import com.opsresearch.orobjects.lib.real.matrix.SizableMatrixI;

public interface AllPairsI {
	public void setProperties(PropertiesI properties);

	public void setDestination(boolean isDestination);

	public void setDestination(Object key, boolean isDestination) throws VertexNotFoundException;

	public VertexI[] getDestinationVertices();

	public void setOrigin(boolean isOrigin);

	public void setOrigin(Object key, boolean isOrigin) throws VertexNotFoundException;

	public VertexI[] getOriginVertices();

	public void fillMatrix(SizableMatrixI cost, SizableMatrixI time, SizableMatrixI distance)
			throws InvalidPropertyException, VertexNotFoundException;

	public void fillMatrix(SizableMatrixI cost, SizableMatrixI time, SizableMatrixI distance, int maxPathsOut,
			int maxPathsIn) throws InvalidPropertyException, VertexNotFoundException;

	public AddI fillGraph(AddI graph) throws InvalidPropertyException, VertexNotFoundException, DuplicateEdgeException,
			DuplicateVertexException;

	public AddI fillGraph(AddI graph, int maxPathsOut, int maxPathsIn) throws InvalidPropertyException,
			VertexNotFoundException, DuplicateEdgeException, DuplicateVertexException;

}
