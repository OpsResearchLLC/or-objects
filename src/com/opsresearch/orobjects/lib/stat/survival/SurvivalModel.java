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

public class SurvivalModel implements SurvivalModelI {

	private Trial trial;
	private SurvivorFunctionI survivorFunction;
	private ProportionalHazardsI proportionalHazards;

	public SurvivalModel() {
		this(new Delegates());
	}

	public SurvivalModel(Delegates delegates) {
		survivorFunction = delegates.getSurvivorFunction();
		proportionalHazards = delegates.getProportionalHazards();
	}

	@Override
	public Trial getTrial() {
		return trial;
	}

	@Override
	public void setTrial(Trial trial) {
		this.trial = trial;
		this.survivorFunction.setTrial(trial);
		this.proportionalHazards.setTrial(trial);
	}

	public SurvivorFunctionI getSurvivorFunctionI() {
		return survivorFunction;
	}

	public void setProductLimit(SurvivorFunctionI survivorFunction) {
		this.survivorFunction = survivorFunction;
		this.survivorFunction.setTrial(trial);
	}

	public ProportionalHazardsI getProportionalHazards() {
		return proportionalHazards;
	}

	public void setProportionalHazards(ProportionalHazardsI proportionalHazards) {
		this.proportionalHazards = proportionalHazards;
		this.proportionalHazards.setTrial(trial);
	}

}
