package opc.sdk.core.node;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.CallMethodResult;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceNode;
import org.opcfoundation.ua.core.StatusCodes;

/**
 * Custom method node.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class UAMethodNode extends MethodNode {
	private String methodScript = "";

	public UAMethodNode() {
		super();
	}

	public UAMethodNode(NodeId NodeId, NodeClass NodeClass, QualifiedName BrowseName, LocalizedText DisplayName,
			LocalizedText Description, UnsignedInteger WriteMask, UnsignedInteger UserWriteMask,
			ReferenceNode[] References, Boolean Executable, Boolean UserExecutable) {
		super(NodeId, NodeClass, BrowseName, DisplayName, Description, WriteMask, UserWriteMask, References, Executable,
				UserExecutable);
	}

	public void setMethodScript(String script) {
		this.methodScript = script;
	}

	/**
	 * UserExecutes a method.
	 * 
	 * @param scriptEngine
	 * 
	 * @param result
	 * 
	 * @param validatedArguments
	 */
	public Object userExecute(ScriptEngine scriptEngine, Object[] arguments, CallMethodResult result) {
		if (this.UserExecutable) {
			return execute(scriptEngine, arguments, result);
		} else {
			result.setStatusCode(new StatusCode(StatusCodes.Bad_UserAccessDenied));
			return null;
		}
	}

	/**
	 * Executes a method
	 * 
	 * @param scriptEngine
	 * 
	 * @param result
	 * @param arguments
	 * @return
	 */
	public Object execute(ScriptEngine scriptEngine, Object[] arguments, CallMethodResult result) {
		if (this.Executable) {
			return executeMethod(scriptEngine, arguments, result);
		} else {
			result.setStatusCode(new StatusCode(StatusCodes.Bad_UserAccessDenied));
			return null;
		}
	}

	private Object executeMethod(ScriptEngine scriptEngine, Object[] arguments, CallMethodResult result) {
		scriptEngine.put("args", arguments);
		try {
			scriptEngine.eval(this.methodScript);
			return scriptEngine.get("result");
		} catch (ScriptException e) {
			result.setStatusCode(new StatusCode(StatusCodes.Bad_MethodInvalid));
			return null;
		}
	}
}
