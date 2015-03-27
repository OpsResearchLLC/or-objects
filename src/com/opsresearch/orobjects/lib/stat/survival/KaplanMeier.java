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

package com.opsresearch.orobjects.lib.stat.survival;

public class KaplanMeier implements SurvivorFunctionI {

	private Trial trial;
	private int n;
	private int[] atRisk;
	private int[] deaths;
	private int[] censored;
	private double[] proportion;
	private double[] cumulative;
	private double[] time;

	@Override
	public Trial getTrial() {
		return trial;
	}

	@Override
	public void setTrial(Trial trial) {
		this.trial = trial;
		this.n = trial.getSamples().length;
		this.atRisk = new int[n];
		this.deaths = new int[n];
		this.censored = new int[n];
		this.proportion = new double[n];
		this.cumulative = new double[n];
		this.time = new double[n];

		int i = 0;
		Sample last = null;
		for (Sample s : trial.getSamples()) {
			if (last != null && s.getTime() != last.getTime())
				i++;
			time[i] = s.getTime();
			deaths[i] += s.getDeaths();
			censored[i] += s.getCensored();
			last = s;
		}
		this.n = i + 1;

		int nAtRisk = trial.getNumberAtRisk();
		double cum = 1.0;
		for (i = 0; i < this.n; i++) {
			atRisk[i] = nAtRisk;
			double pro = 1.0 - (double) deaths[i] / (double) nAtRisk;
			cum *= pro;
			proportion[i] = pro;
			cumulative[i] = cum;
			nAtRisk -= (deaths[i] + censored[i]);
		}
	}

	@Override
	public int getSampleSize() {
		return n;
	}

	@Override
	public int getAtRisk(int i) {
		return atRisk[i];
	}

	@Override
	public int getDeaths(int i) {
		return deaths[i];
	}

	@Override
	public int getCensored(int i) {
		return censored[i];
	}

	@Override
	public double getProportion(int i) {
		return proportion[i];
	}

	@Override
	public double getCumulative(int i) {
		return cumulative[i];
	}

	@Override
	public double getTime(int i) {
		return time[i];
	}

}
