package com.bichler.opc.driver.base;

import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ComDRVStates;
import com.bichler.opc.driver.base.context.DriverContext;
import com.bichler.opc.driver.base.handler.BaseMethodHandler;
import com.bichler.opc.driver.base.handler.BaseReadHandler;
import com.bichler.opc.driver.base.handler.BaseWriteHandler;

import opc.sdk.ua.classes.BaseInstance;
import opc.sdk.ua.classes.BaseNode;
import opc.sdk.ua.classes.BaseVariableType;
import opc.sdk.ua.classes.ServerObjectType;

public class ComDRV extends com.bichler.opc.comdrv.ComDRV {
	private BaseResourceManager manager = null;
	private ComDRVManager drvManager = ComDRVManager.getDRVManager();
	// private ScriptEngineManager scrmanager = null;
	// private ScriptEngine engine = null;

	public ComDRV() {
		// this.manager = new CalculationResourceManager();
		// this.manager.setManager(drvManager.getResourceManager());
		// scrmanager = new ScriptEngineManager();
		// engine = scrmanager.getEngineByName("JavaScript");
		this.manager = new BaseResourceManager();
		this.manager.setManager(ComDRVManager.getDRVManager().getResourceManager());
		// this.manager.getManager().setLogger(Logger.getLogger("Base OPC UA Server
		// Driver"));
	}

	@Override
	public boolean stop() {
		this.state = ComDRVStates.STOPPED;
		return super.stop();
	}

	@Override
	public boolean start() {
		super.start();
		this.state = ComDRVStates.RUNNING;
		return true;
	}

	@Override
	public void setDriverId(long drvId) {
		super.setDriverId(drvId);
	}

	@Override
	public void setDriverName(String drvName) {
		super.setDriverName(drvName);
	}

	@Override
	public long getDriverId() {
		return super.getDriverId();
	}

	@Override
	public String getDriverName() {
		return super.getDriverName();
	}

	@Override
	public void initialize() {
		// read requests
		BaseReadHandler readHandler = new BaseReadHandler();
		readHandler.setManager(manager);
		readHandler.setDriverId(drvId);
		// add request listener
		this.drvManager.addReadListener(drvId, readHandler);
		// write requests
		BaseWriteHandler writeHandler = new BaseWriteHandler();
		writeHandler.setManager(manager);
		writeHandler.setDriverId(drvId);
		// add write listener
		this.drvManager.addWriteListener(drvId, writeHandler);
		// call requests
		BaseMethodHandler methodHandler = new BaseMethodHandler();
		methodHandler.setManager(manager);
		methodHandler.setDriverId(drvId);
		// add method listener
		this.drvManager.addMethodListener(methodHandler);
		ServerObjectType server = new ServerObjectType(null);
		addNodeState(Identifiers.Server, server);
	}

	public void registerNodeState(BaseNode node) {
		// register node
		this.manager.registerNode(node);
		if (node instanceof BaseVariableType) {
			// register variable
			this.manager.getManager().getDriverConnection().setDriverReadConnected(node.getNodeId(), true,
					ComDRVManager.SYNCREAD, this.drvId);
			this.manager.getManager().getDriverConnection().setDriverWriteConnected(node.getNodeId(), true,
					ComDRVManager.SYNCWRITE, this.drvId);
		}
		List<BaseInstance> children = node.getChildren();
		for (BaseInstance child : children) {
			registerNodeState(child);
		}
	}

	@Override
	public void checkCommunication() {
	}

	@Override
	public boolean doStartup() {
		return true;
	}

	@Override
	public boolean doWork() {
		boolean worked = false;
		return worked;
	}

	@Override
	public boolean doFinish() {
		return false;
	}

	@Override
	public void setDrvReconnectTimeout(String timeout) {
	}

	public void addNodeState(NodeId nodeId, BaseNode node) {
		DriverContext context = new DriverContext(drvId);
		node.create(context, nodeId);
		registerNodeState(node);
	}
}
