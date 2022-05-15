package com.bichler.opc.driver.calculation;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.ComDRVManager;

public class CalculationUtils {

	private final Logger LOGGER;
	private ScriptEngine engine = null;
	private ComDRVManager drvManager = ComDRVManager.getDRVManager();
	private String drvName = "";

	public String getDrvName() {
		return drvName;
	}

	public void setDrvName(String drvName) {
		this.drvName = drvName;
	}

	public long getDrvId() {
		return drvId;
	}

	public void setDrvId(long drvId) {
		this.drvId = drvId;
	}

	private long drvId = 0l;

	public CalculationUtils() {
		LOGGER = Logger.getLogger(getClass().getName());
		ScriptEngineManager scrmanager = new ScriptEngineManager(null);
		engine = scrmanager.getEngineByName("nashorn");
		if (engine == null) {
			engine = scrmanager.getEngineByName("JavaScript");
		} else {
			try {
				// if we have nashorn engine, so lod compatibility script
				engine.eval("load(\"nashorn:mozilla_compat.js\");");
			} catch (ScriptException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		try {
			engine.eval(
					"importPackage(org.opcfoundation.ua.builtintypes);\nimportPackage(org.opcfoundation.ua.common);\nimportPackage(java.lang);\nimportPackage(com.bichler.opc.comdrv);\nimportPackage(org.opcfoundation.ua.core);");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	public DataValue executeInstructionWithoutWrite(CalculationDP dp) {
		String[] ir = new String[] { null };
		String script = dp.getFuncCall().toString();
		DataValue dv = new DataValue();
		dv.setStatusCode(StatusCode.GOOD);
		if (dp.getArrayindex() > -1) {
			ir[0] = dp.getArrayindex() + "";
		}
		try {
			LOGGER.log(Level.INFO, script);
			Invocable inv = (Invocable) engine;
			Object ret = inv.invokeFunction(script);
			LOGGER.log(Level.INFO, "" + ret);
			if (ret != null) {
				// did we have a division by 0
				if (ret instanceof Double && ((Double) ret).isInfinite()) {
					dv.setStatusCode(StatusCodes.Bad_InvalidArgument);
					dv.setSourceTimestamp(new DateTime());
				} else {
					dv = dp.getTarget().getTargetValue(ret, dp.getArrayindex());
				}
				return dv;
			}
		} catch (ScriptException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			dv.setStatusCode(StatusCodes.Bad_InvalidState);
			dv.setSourceTimestamp(new DateTime());
			StatusCode[] code = drvManager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
					new UnsignedInteger[] { Attributes.Value }, ir, new DataValue[] { dv }, new Long[] { (long) -1 });
			String description = "";
			if (code == null || code[0] == null) {
				description = "Driver: " + this.drvName + " - Null statuscode for write to node service: NodeId: "
						+ dp.getTarget().getTargetId();
			} else if (code.length == 0) {
				description = "Driver: " + this.drvName
						+ " - Wrong statuscode length for write to node service: NodeId: "
						+ dp.getTarget().getTargetId();
			} else if (!code[0].equalsStatusCode(StatusCode.GOOD)) {
				description = code[0].getDescription();
			}
			if (!description.isEmpty()) {
				LOGGER.log(Level.SEVERE, description);
			}
		} catch (NoSuchMethodException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return dv;
	}

	public DataValue executeInstruction(CalculationDP dp) {
		String[] ir = new String[] { null };
		String script = dp.getFuncCall().toString();
		DataValue dv = new DataValue();
		dv.setStatusCode(StatusCode.GOOD);
		// add index ranges if required
		if (dp.getArrayindex() > -1) {
			ir[0] = dp.getArrayindex() + "";
		}
		try {
			Invocable inv = (Invocable) engine;
			Object ret = inv.invokeFunction(script);
			// LOGGER.log(Level.FINEST, script);
			if (ret != null) {
				// did we have a division by 0
				if (ret instanceof Double && ((Double) ret).isInfinite()) {
					dv.setStatusCode(StatusCodes.Bad_InvalidArgument);
					dv.setSourceTimestamp(new DateTime());
				} else {
					dv = dp.getTarget().getTargetValue(ret, dp.getArrayindex());
				}
				if (dv != null) {
					// Logger.getLogger(getClass().getName()).log(Level.INFO, "write value for node:
					// '" + dp.getTarget().getTargetId() + "' value: '" + dv + "'!");
					StatusCode[] code = drvManager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
							new UnsignedInteger[] { Attributes.Value }, ir, new DataValue[] { dv },
							new Long[] { (long) -1 });
					String description = "";
					if (code == null) {
						description = "Driver: " + this.drvName
								+ " - Null statuscode for write to node service: NodeId: "
								+ dp.getTarget().getTargetId();
					} else if (code.length == 0) {
						description = "Driver: " + this.drvName
								+ " - Wrong statuscode length for write to node service: NodeId: "
								+ dp.getTarget().getTargetId();
					} else if (code[0] == null) {
						description = "Driver: " + this.drvName
								+ " - Null statuscode for write to node service: NodeId: "
								+ dp.getTarget().getTargetId();
					} else if (!code[0].equalsStatusCode(StatusCode.GOOD)) {
						description = code[0].getDescription();
					}
					if (!description.isEmpty()) {
						if (!dp.getTarget().getLoggedError()) {
							dp.getTarget().setLoggedError(true);
							LOGGER.log(Level.SEVERE, description + " For target: " + dp.getTarget().getTargetId());
						}
					}
				}
			}
		} catch (ScriptException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			dv.setStatusCode(StatusCodes.Bad_InvalidState);
			dv.setSourceTimestamp(new DateTime());
			StatusCode[] code = drvManager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
					new UnsignedInteger[] { Attributes.Value }, ir, new DataValue[] { dv }, new Long[] { (long) -1 });
			String description = "";
			if (code == null || code[0] == null) {
				description = "Driver: " + this.drvName + " - Null statuscode for write to node service: NodeId: "
						+ dp.getTarget().getTargetId();
			} else if (code.length == 0) {
				description = "Driver: " + this.drvName
						+ " - Wrong statuscode length for write to node service: NodeId: "
						+ dp.getTarget().getTargetId();
			} else if (!code[0].equalsStatusCode(StatusCode.GOOD)) {
				description = code[0].getDescription();
			}
			if (!description.isEmpty()) {
				if (!dp.getTarget().getLoggedError()) {
					dp.getTarget().setLoggedError(true);
					LOGGER.log(Level.SEVERE, description + " For target: " + dp.getTarget().getTargetId());
				}
			}
		} catch (NoSuchMethodException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
		return dv;
	}

	public void addScript(String string) {
		try {
			engine.eval(string);
		} catch (ScriptException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
