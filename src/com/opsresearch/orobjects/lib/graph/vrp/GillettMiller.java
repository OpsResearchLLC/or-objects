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

public class GillettMiller extends Randomizer implements ConstructI {
	public GillettMiller() throws VRPException {
		super(new GillettMillerBase());
		setRandom(new MultiplicativeCongruential());

	}

	public GillettMiller(int count) throws VRPException {
		super(new GillettMillerBase());
		setRandom(new MultiplicativeCongruential());
		addIterations(1, 0);
		if (count > 0)
			addIterations(count, 4);
	}

	public GillettMiller(int count, int strength) throws VRPException {
		super(new GillettMillerBase());
		setRandom(new MultiplicativeCongruential());
		addIterations(1, 0);
		if (count > 0)
			addIterations(count, strength);
	}

	public GillettMiller(int count, int strength, com.opsresearch.orobjects.lib.graph.tsp.ImproveI improveSubalgorithm)
			throws VRPException {
		super(new GillettMillerBase(improveSubalgorithm));
		setRandom(new MultiplicativeCongruential());
		addIterations(1, 0);
		if (count > 0)
			addIterations(count, strength);
	}
}
