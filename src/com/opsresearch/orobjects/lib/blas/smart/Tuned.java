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

package com.opsresearch.orobjects.lib.blas.smart;

import java.util.HashMap;
import java.util.Map;

public class Tuned<BLASI> {

	private TunerI<BLASI> _tuner;
	private BLASI _defaultImplementation;
	private boolean _tuned = false;
	private int _numTuningMethods = 100;
	private int _numTuningPoints = 3;

	private long _leastPoint;
	private BLASI _leastAssignment;
	private long[][] _tuningPoints;
	private BLASI[][] _tuningAssignments;
	private Map<String, BLASI> _implementations = new HashMap<String, BLASI>();

	public Tuned(BLASI dflt, TunerI<BLASI> tuner) {
		if (dflt == null)
			throw new SmartError("The default implementation must not be null");
		if (tuner == null)
			throw new SmartError("The tuner implementation must not be null");
		_defaultImplementation = dflt;
		_tuner = tuner;
		clearTuning();
	}

	public void addImplementation(String key, BLASI blas) {
		clearTuning();
		_implementations.put(key, blas);
	}

	public BLASI getImplementation(String key) {
		return _implementations.get(key);
	}

	public void setDefaultImplementation(BLASI dflt) {
		_defaultImplementation = dflt;
	}

	public BLASI getDefaultImplementation() {
		return _defaultImplementation;
	}

	public void tune() {
		clearTuning();
		_tuningPoints = new long[_numTuningMethods][_numTuningPoints];
		_tuningAssignments = _tuner.tune(this);
		_tuned = true;
	}

	public boolean isTuned() {
		return _tuned;
	}

	public void clearTuning() {
		_tuned = false;
		_tuningPoints = null;
		_tuningAssignments = null;
		_leastPoint = Long.MAX_VALUE;
		_leastAssignment = _defaultImplementation;
	}

	protected BLASI select(int method, long m, long n) {
		return select(method, m * n);
	}

	protected BLASI select(int method, long m, long n, long k) {
		return select(method, m * n * k);
	}

	protected BLASI select(int method, long n) {
		if (n <= _leastPoint)
			return _leastAssignment;
		if (_tuned) {
			for (int i = 1; i < _numTuningPoints; i++) {
				if (n <= _tuningPoints[method][i])
					return _tuningAssignments[method][i];
			}
			return _tuningAssignments[method][_numTuningPoints];
		}
		return _defaultImplementation;
	}

	public BLASI getTuningAssignment(int method, int i) {
		if (_tuningAssignments == null)
			return null;
		return _tuningAssignments[method][i];
	}

	public void setTuningPoint(int method, int impl, long size) {
		if (_tuningPoints == null)
			_tuningPoints = new long[_numTuningMethods][_numTuningPoints];
		_tuningPoints[method][impl] = size;
	}

	public long getTuningPoint(int method, int impl) {
		if (_tuningPoints == null)
			_tuningPoints = new long[_numTuningMethods][_numTuningPoints];
		return _tuningPoints[method][impl];
	}

	public int getNumTuningPoints() {
		return _numTuningPoints;
	}

	public int getNumTuningMethods() {
		return _numTuningMethods;
	}

	protected void setNumTuningMethods(int numTuningMethods) {
		clearTuning();
		this._numTuningMethods = numTuningMethods;
	}

	public void setNumTuningPoints(int numTuningPoints) {
		clearTuning();
		this._numTuningPoints = numTuningPoints;
	}

	public TunerI<BLASI> getTuner() {
		return _tuner;
	}

	public void setTuner(TunerI<BLASI> tuner) {
		clearTuning();
		_tuner = tuner;
	}

	public long getLeastPoint() {
		return _leastPoint;
	}

	public void setLeastPoint(long leastPoint) {
		this._leastPoint = leastPoint;
	}

	public BLASI getLeastAssignment() {
		return _leastAssignment;
	}

	public void setLeastAssignment(BLASI leastAssignment) {
		this._leastAssignment = leastAssignment;
	}

}
