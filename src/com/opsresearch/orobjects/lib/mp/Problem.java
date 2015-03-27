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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.opsresearch.orobjects.lib.real.matrix.CompressedColumnMatrix;
import com.opsresearch.orobjects.lib.real.matrix.MatrixElementI;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;
import com.opsresearch.orobjects.lib.real.matrix.RealContainer;
import com.opsresearch.orobjects.lib.real.matrix.SizableMatrixI;
import com.opsresearch.orobjects.lib.real.matrix.SparseMatrix;
import com.opsresearch.orobjects.lib.util.KeyGenerator;
import com.opsresearch.orobjects.lib.util.Metadata;

public class Problem extends RealContainer implements SizableProblemI,
		java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String _name;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public boolean isMaximize() {
		return _isMaximize;
	}

	public void setMaximize(boolean isMaximize) {
		_isMaximize = isMaximize;
	}

	private boolean _isMaximize = true;
	private Metadata _metadata = new Metadata();
	private Vector<VariableI> _variables = null;
	private Vector<ConstraintI> _constraints = null;
	private HashMap<String, VariableI> _variableIndex = null;
	private HashMap<String, ConstraintI> _constraintIndex = null;

	SizableMatrixI _coefficients = null;

	public Problem(int capacityOfConstraints, int capacityOfVariables) {
		_variables = new Vector<VariableI>(capacityOfVariables);
		_constraints = new Vector<ConstraintI>(capacityOfConstraints);
		_variableIndex = new HashMap<String, VariableI>(capacityOfVariables);
		_constraintIndex = new HashMap<String, ConstraintI>(
				capacityOfConstraints);
		_coefficients = new SparseMatrix(0, 0);
	}

	public Problem(int capacityOfConstraints, int capacityOfVariables,
			SizableMatrixI matrix) {
		_variables = new Vector<VariableI>(capacityOfVariables);
		_constraints = new Vector<ConstraintI>(capacityOfConstraints);
		_variableIndex = new HashMap<String, VariableI>(capacityOfVariables);
		_constraintIndex = new HashMap<String, ConstraintI>(
				capacityOfConstraints);
		_coefficients = matrix;
	}

	public Metadata getMetadata() {
		return _metadata;
	}

	public int sizeOfConstraints() {
		return _constraints.size();
	}

	public int sizeOfVariables() {
		return _variables.size();
	}

	public int sizeOfCoefficients() {
		return _coefficients.sizeOfElements();
	}

	public void setCapacity(int capacityOfConstraints, int capacityOfVariables) {
		_variables.ensureCapacity(capacityOfVariables);
		_constraints.ensureCapacity(capacityOfConstraints);
		_coefficients.setCapacity(capacityOfConstraints, capacityOfVariables);
	}

	public VariableI newVariable(String name) throws DuplicateException {
		Variable var = null;
		if (name != null)
			var = (Variable) _variableIndex.get(name);
		if (var != null)
			throw new DuplicateException("The variable exists: " + name);
		var = new Variable(_variables.size(), name);
		_variableIndex.put(name, var);
		_variables.addElement(var);
		_coefficients.addColumn(null);
		return var;
	}

	public ConstraintI newConstraint(String name) throws DuplicateException {
		Constraint cst = null;
		if (name != null)
			cst = (Constraint) _constraintIndex.get(name);
		if (cst != null)
			throw new DuplicateException("The constraint exists: " + name);
		cst = new Constraint(_constraints.size(), name);
		_constraintIndex.put(name, cst);
		_constraints.addElement(cst);
		_coefficients.addRow(null);
		return cst;
	}

	public VariableI getVariable(String name) {
		return (VariableI) _variableIndex.get(name);
	}

	public VariableI getVariable(int columnIndex) {
		return (VariableI) _variables.elementAt(columnIndex);
	}

	public ConstraintI getConstraint(String name) {
		return (ConstraintI) _constraintIndex.get(name);
	}

	public ConstraintI getConstraint(int rowIndex) {
		return (ConstraintI) _constraints.elementAt(rowIndex);
	}

	public void setCoefficientAt(int constraintIndex, int variableIndex,
			double value) {
		_coefficients.setElementAt(constraintIndex, variableIndex, value);
	}

	public void setCoefficientAt(String constraintName, String variableName,
			double value) throws NotFoundException {
		Variable var = (Variable) _variableIndex.get(variableName);
		if (var == null)
			throw new NotFoundException("Can't find variable: " + variableName);
		Constraint cst = (Constraint) _constraintIndex.get(constraintName);
		if (cst == null)
			throw new NotFoundException("Can't find constraint: "
					+ constraintName);
		_coefficients.setElementAt(cst._rowIndex, var._columnIndex, value);
	}

	public double getCoefficientAt(int constraintIndex, int variableIndex) {
		return _coefficients.elementAt(constraintIndex, variableIndex);
	}

	public double getCoefficientAt(String constraintName, String variableName)
			throws NotFoundException {
		Variable var = (Variable) _variableIndex.get(variableName);
		if (var == null)
			throw new NotFoundException("Can't find variable: " + variableName);
		Constraint cst = (Constraint) _constraintIndex.get(constraintName);
		if (cst == null)
			throw new NotFoundException("Can't find constraint: "
					+ constraintName);
		return _coefficients.elementAt(cst._rowIndex, var._columnIndex);
	}

	public String toString() {
		return toString(0, Math.min(10, sizeOfConstraints()), 0,
				Math.min(10, sizeOfVariables()));
	}

	public String toString(int beginConstraint, int endConstraint,
			int beginVariable, int endVariable) {
		String nl = "\n";
		String s = "Problem: ";
		s += "vars=" + sizeOfVariables();
		s += ", consts=" + sizeOfConstraints();
		s += ", coefs=" + sizeOfCoefficients();
		s += nl;

		s += _metadata;

		s += nl + "--- Constraints ---" + nl;
		for (int i = beginConstraint; i < endConstraint; i++) {
			Constraint cst = (Constraint) _constraints.elementAt(i);
			s += cst + nl;
		}

		s += nl + "--- Variables ---" + nl;
		for (int i = beginVariable; i < endVariable; i++) {
			Variable var = (Variable) _variables.elementAt(i);
			s += var + nl;
		}

		s += nl + "--- Coefficients ---" + nl;
		for (Iterator<MatrixElementI> e = _coefficients.elements(); e.hasNext();) {
			MatrixElementI el = e.next();
			int i = el.getRowIndex();
			int j = el.getColumnIndex();
			if (j >= endVariable)
				continue;
			if (j < beginVariable)
				continue;
			if (i >= endConstraint)
				continue;
			if (i < beginConstraint)
				continue;
			s += "[" + i + "][" + j + "] " + el.getValue() + nl;
		}

		return s;
	}


	public void maskNames(Iterator<String> names) {
		if (names == null)
			names = new KeyGenerator(8);

		String[] vn = new String[_variables.size()];
		for (int i = 0; i < vn.length; i++) {
			if (!names.hasNext())
				throw new MpError("Names doesn't have enough iterator.");
			vn[i] = (String) names.next();
		}

		String[] cn = new String[_constraints.size()];
		for (int i = 0; i < cn.length; i++) {
			if (!names.hasNext())
				throw new MpError("Names doesn't have enough iterator.");
			cn[i] = (String) names.next();
		}

		_variableIndex = new HashMap<String, VariableI>(_variables.size());
		int j = 0;
		for (Iterator<VariableI> e = _variables.iterator(); e.hasNext(); j++) {
			Variable var = (Variable) e.next();
			var._name = vn[j];
			_variableIndex.put(var._name, var);
		}

		_constraintIndex = new HashMap<String, ConstraintI>(_constraints.size());
		j = 0;
		for (Iterator<ConstraintI> e = _constraints.iterator(); e.hasNext(); j++) {
			Constraint cst = (Constraint) e.next();
			cst._name = cn[j];
			_constraintIndex.put(cst._name, cst);
		}
	}

	public int relaxInteger() {
		int cnt = 0;
		for (Iterator<VariableI> e = _variables.iterator(); e.hasNext();) {
			Variable var = (Variable) e.next();
			if (var.getType() == VariableI.INTEGER) {
				var.setType(VariableI.REAL);
				cnt++;
			} else if (var.getType() == VariableI.BOOLEAN) {
				var.setType(VariableI.REAL);
				var.setLowerBound(0.0);
				var.setUpperBound(1.0);
				cnt++;
			}
		}
		return cnt;
	}

	public MatrixI getCoefficientMatrix() {
		return _coefficients;
	}

	public Iterator<MatrixElementI> coefficients() {
		return _coefficients.elements();
	}

	public Iterator<VariableI> variables() {
		return _variables.iterator();
	}

	public Iterator<ConstraintI> constraints() {
		return _constraints.iterator();
	}

	public boolean equals(Object o) {
		if (!(o instanceof ProblemI))
			return false;
		ProblemI problem = (ProblemI) o;

		if (problem.sizeOfVariables() != sizeOfVariables())
			return false;
		if (problem.sizeOfConstraints() != sizeOfConstraints())
			return false;

		for (Iterator<VariableI> e = problem.variables(); e.hasNext();) {
			VariableI var = e.next();
			Variable v = (Variable) _variables.elementAt(var.getColumnIndex());
			if (!v.equals(var)) {
				return false;
			}
		}

		for (Iterator<ConstraintI> e = problem.constraints(); e.hasNext();) {
			ConstraintI cst = e.next();
			Constraint c = (Constraint) _constraints.elementAt(cst
					.getRowIndex());
			if (!c.equals(cst)) {
				return false;
			}
		}

		return true;
	}

}
