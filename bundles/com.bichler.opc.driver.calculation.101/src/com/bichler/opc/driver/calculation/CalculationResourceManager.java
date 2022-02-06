package com.bichler.opc.driver.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opc.comdrv.ComResourceManager;

public class CalculationResourceManager {
	private ComResourceManager manager = null;
	private Map<NodeId, CalculationDP> calcInstructionsOnRead = new HashMap<NodeId, CalculationDP>();
	private List<CalculationDP> calcInstructionsCyclic = new ArrayList<CalculationDP>();
	private Map<NodeId, List<CalculationDP>> calcInstructionsValueChange = new HashMap<NodeId, List<CalculationDP>>();

	public ComResourceManager getManager() {
		return manager;
	}

	public void setManager(ComResourceManager manager) {
		this.manager = manager;
	}

	public CalculationResourceManager() {
	}

	public Map<NodeId, List<CalculationDP>> getCalcInstructionsValueChange() {
		return calcInstructionsValueChange;
	}

	public void setCalcInstructionsValueChange(Map<NodeId, List<CalculationDP>> calcInstructionsValueChange) {
		this.calcInstructionsValueChange = calcInstructionsValueChange;
	}

	public void addCalcInstructionValueChange(NodeId nodeid, CalculationDP calcInstructionValueChange) {
		List<CalculationDP> dpList = this.calcInstructionsValueChange.get(nodeid);
		if (dpList == null) {
			dpList = new ArrayList<CalculationDP>();
			this.calcInstructionsValueChange.put(nodeid, dpList);
		}
		dpList.add(calcInstructionValueChange);
	}

	public Map<NodeId, CalculationDP> getCalcInstructionsOnRead() {
		return calcInstructionsOnRead;
	}

	public void setCalcInstructionsOnRead(Map<NodeId, CalculationDP> calcInstructionsOnRead) {
		this.calcInstructionsOnRead = calcInstructionsOnRead;
	}

	public void addCalcInstructionOnRead(NodeId nodeid, CalculationDP calcInstructionOnRead) {
		this.calcInstructionsOnRead.put(nodeid, calcInstructionOnRead);
	}

	public List<CalculationDP> getCalcInstructionsCyclic() {
		return calcInstructionsCyclic;
	}

	public void setCalcInstructionsCyclic(List<CalculationDP> calcInstructionsCyclic) {
		this.calcInstructionsCyclic = calcInstructionsCyclic;
	}

	public void addCalcInstructionCyclic(CalculationDP calcInstructionCyclic) {
		this.calcInstructionsCyclic.add(calcInstructionCyclic);
	}
}
