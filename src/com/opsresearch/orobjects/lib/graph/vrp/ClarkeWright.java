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

import com.opsresearch.orobjects.lib.random.MultiplicativeCongruential;

public class ClarkeWright extends Randomizer implements ConstructI {
	public ClarkeWright() throws VRPException {
		super(new ClarkeWrightBase());
		setRandom(new MultiplicativeCongruential());
	}

	public ClarkeWright(int count) throws VRPException {
		super(new ClarkeWrightBase());
		setRandom(new MultiplicativeCongruential());
		addIterations(1, 0);
		if (count > 0)
			addIterations(count, 4);
	}

	public ClarkeWright(int count, int strength) throws VRPException {
		super(new ClarkeWrightBase());
		setRandom(new MultiplicativeCongruential());
		addIterations(1, 0);
		if (count > 0)
			addIterations(count, strength);
	}

	public ClarkeWright(int count, int strength, com.opsresearch.orobjects.lib.graph.tsp.ImproveI improveSubalgorithm)
			throws VRPException {
		super(new ClarkeWrightBase(improveSubalgorithm));
		setRandom(new MultiplicativeCongruential());
		addIterations(1, 0);
		if (count > 0)
			addIterations(count, strength);
	}
}
