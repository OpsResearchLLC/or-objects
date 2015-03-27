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

package com.opsresearch.orobjects.lib.mp;

import java.util.Iterator;

import com.opsresearch.orobjects.lib.real.matrix.MatrixElementI;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;
import com.opsresearch.orobjects.lib.real.matrix.RealContainerI;
import com.opsresearch.orobjects.lib.util.Metadata;

public interface ProblemI extends RealContainerI {
	public Metadata getMetadata();

	public int sizeOfConstraints();

	public int sizeOfVariables();

	public int sizeOfCoefficients();

	public VariableI getVariable(String name);

	public VariableI getVariable(int columnIndex);

	public ConstraintI getConstraint(String name);

	public ConstraintI getConstraint(int rowIndex);

	public void setCoefficientAt(int constraintIndex, int variableIndex, double value);

	public void setCoefficientAt(String constraintName, String variableName, double value) throws NotFoundException;

	public double getCoefficientAt(int constraintIndex, int variableIndex);

	public double getCoefficientAt(String constraintName, String variableName) throws NotFoundException;

	public MatrixI getCoefficientMatrix();

	public Iterator<MatrixElementI> coefficients();

	public Iterator<VariableI> variables();

	public Iterator<ConstraintI> constraints();

	public int relaxInteger();

}
