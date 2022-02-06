package com.bichler.opc.driver.calculation;

import java.util.ArrayList;
import java.util.List;
import com.bichler.opc.driver.calculation.targets.CalculationTarget;

public class CalculationDP {
	private boolean active = false;
	private CalcEvent event = CalcEvent.ONREAD;
	private long timeout = -1;
	private long lastcalc = 0;
	private StringBuffer script = new StringBuffer();
	private CalculationTarget target = new CalculationTarget();
	private int arrayindex = -1;
	private List<CalculationObject> calculationExpressions = new ArrayList<CalculationObject>();
	private String funcCall = "";

	public StringBuffer getScript() {
		return script;
	}

	public void setScript(StringBuffer script) {
		this.script = script;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public CalcEvent getEvent() {
		return event;
	}

	public void setEvent(CalcEvent event) {
		this.event = event;
	}

	public void setEvent(String event) {
		this.event = CalcEvent.valueOf(event);
	}

	public long getLastcalc() {
		return lastcalc;
	}

	public void setLastcalc(long lastcalc) {
		this.lastcalc = lastcalc;
	}

	public List<CalculationObject> getCalculationExpressions() {
		return calculationExpressions;
	}

	public void setCalculationExpressions(List<CalculationObject> calculationExpressions) {
		this.calculationExpressions = calculationExpressions;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public CalculationTarget getTarget() {
		return target;
	}

	public void setTarget(CalculationTarget target) {
		this.target = target;
	}

	public int getArrayindex() {
		return arrayindex;
	}

	public void setArrayindex(int arrayindex) {
		this.arrayindex = arrayindex;
	}

	public void setFuncCall(String call) {
		this.funcCall = call;
	}
	
	public String getFuncCall() {
		return this.funcCall;
	}
}
