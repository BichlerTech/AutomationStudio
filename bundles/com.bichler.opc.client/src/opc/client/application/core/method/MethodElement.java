package opc.client.application.core.method;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.encoding.EncoderContext;
import org.opcfoundation.ua.encoding.IEncodeable;

import opc.client.service.Browser;
import opc.client.service.ClientSession;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public class MethodElement {
	private NodeId methodId;
	private Variant[] inputArguments;
	private NodeId[] dataTypes2inputArgumentValues = null;

	public MethodElement(NodeId methodId, Variant[] inputArguments) {
		this.methodId = methodId;
		this.inputArguments = inputArguments;
	}

	public void validate(ClientSession session) throws ServiceResultException {
		Argument[] inputArgumentValues;
		Integer[] valueRanks2inputArgumentValues;
		Browser browser = new Browser(session);
		Variant arguments = browser.browseMethodInputArguments(this.methodId);
		if (arguments != null && !arguments.isEmpty()) {
			if (arguments.getValue() instanceof ExtensionObject[]) {
				List<Argument> inarguments = new ArrayList<>();
				for (ExtensionObject extObj : (ExtensionObject[]) arguments.getValue()) {
					IEncodeable arg = extObj.decode(EncoderContext.getDefaultInstance());
					inarguments.add((Argument) arg);
				}
				inputArgumentValues = inarguments.toArray(new Argument[0]);
			} else {
				inputArgumentValues = (Argument[]) arguments.getValue();
			}
			this.dataTypes2inputArgumentValues = new NodeId[inputArgumentValues.length];
			valueRanks2inputArgumentValues = new Integer[inputArgumentValues.length];
			for (int i = 0; i < inputArgumentValues.length; i++) {
				Argument arg = inputArgumentValues[i];
				this.dataTypes2inputArgumentValues[i] = arg.getDataType();
				valueRanks2inputArgumentValues[i] = arg.getValueRank();
			}
		}
		if (this.inputArguments != null) {
			for (int i = 0; i < this.inputArguments.length; i++) {
				if (this.dataTypes2inputArgumentValues == null || this.dataTypes2inputArgumentValues.length < i) {
					throw new ServiceResultException(StatusCodes.Bad_InvalidArgument,
							"Error, One ore more arguments are invalid!");
				}
			}
		}
	}

	public NodeId getMethodId() {
		return this.methodId;
	}
}
