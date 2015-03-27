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

package com.opsresearch.orobjects.lib.random;


public class BaysDurhamShuffle implements RandomI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int _sizeOfBits;
	private long _last;
	private long[] _array;
	private RandomI _random;

	public BaysDurhamShuffle() {
		_random = new LinearCongruential(System.currentTimeMillis());
		init();
	}

	public BaysDurhamShuffle(long seed) {
		_random = new LinearCongruential(seed);
		init();
	}

	public BaysDurhamShuffle(RandomI random) {
		_random = random;
		init();
	}

	public void setSeed(long seed) {
		_random.setSeed(seed);
		init();
	}

	public double nextDouble() {
		return (((next() >>> (_sizeOfBits - 26)) << 27) + (next() >>> (_sizeOfBits - 27))) / (double) (1L << 53);
	}

	public int sizeOfBits() {
		return _sizeOfBits;
	}

	public long next() {
		int idx = (int) ((_last >>> (_sizeOfBits - 5)) & 0x1f);
		_last = _array[idx];
		_array[idx] = _random.next();
		return _last;
	}

	private void init() {
		_sizeOfBits = _random.sizeOfBits();
		_array = new long[32];
		for (int i = 0; i < 8; i++)
			_random.next();
		for (int i = 0; i < 32; i++)
			_array[i] = _random.next();
		_last = _random.next();
	}

}
