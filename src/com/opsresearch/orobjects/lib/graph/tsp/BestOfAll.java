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

package com.opsresearch.orobjects.lib.graph.tsp;

import com.opsresearch.orobjects.lib.graph.GraphI;

public class BestOfAll extends BestOf implements ConstructI {

	public BestOfAll() {
		init();
	}

	public BestOfAll(GraphI graph) {
		setGraph(graph);
		init();

	}

	private void init() {
		addConstruct(new FullIterator(), 2, 8);
		addConstruct(new NearestAddition(), 9, Integer.MAX_VALUE);
		addConstruct(new NearestInsertion(), 9, Integer.MAX_VALUE);
		addConstruct(new FarthestInsertion(), 9, Integer.MAX_VALUE);
		addConstruct(new CheapestInsertion(), 9, 200);
		addConstruct(new Geni(9), 9, 50);
		addConstruct(new Geni(7), 51, 500);
		addConstruct(new Geni(5), 501, Integer.MAX_VALUE);

		addImprove(new ThreeOpt(), 9, 300);
		addImprove(new TwoOpt(), 301, Integer.MAX_VALUE);
		addImprove(new Us(7), 9, 50);
		addImprove(new Us(4), 51, Integer.MAX_VALUE);
	}

}
