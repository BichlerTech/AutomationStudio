package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.Identifiers;

import opc.sdk.ua.constants.BrowseNames;

public class ServerDiagnosticsType extends BaseObjectType {
	protected ServerDiagnosticsSummaryType serverDiagnosticsSummary = null;
	protected SamplingIntervalDiagnosticsArrayType samplingIntervalDiagnosticsArray = null;
	protected SubscriptionDiagnosticsArrayType subscriptionDiagnosticsArray = null;
	protected SessionsDiagnosticsSummaryType sessionsDiagnosticsSummary = null;
	protected PropertyVariableType<Boolean> enabledFlag = null;

	/**
	 * Initializes the type with its default attribute values.
	 * 
	 * @param parent
	 */
	public ServerDiagnosticsType(BaseNode parent) {
		super(parent);
	}

	/**
	 * Get the id of the default type definition node for the instance.
	 */
	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ServerDiagnosticsType;
	}

	/**
	 * Populates a list with the children that belong to the node.
	 * 
	 */
	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.serverDiagnosticsSummary != null) {
			children.add(this.serverDiagnosticsSummary);
		}
		if (this.samplingIntervalDiagnosticsArray != null) {
			children.add(this.samplingIntervalDiagnosticsArray);
		}
		if (this.subscriptionDiagnosticsArray != null) {
			children.add(this.subscriptionDiagnosticsArray);
		}
		if (this.sessionsDiagnosticsSummary != null) {
			children.add(this.sessionsDiagnosticsSummary);
		}
		if (this.enabledFlag != null) {
			children.add(this.enabledFlag);
		}
		children.addAll(super.getChildren());
		return children;
	}

	private ServerDiagnosticsSummaryType createServerDiagnosticsSummary(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.serverDiagnosticsSummary == null) {
			if (replacement == null) {
				this.serverDiagnosticsSummary = new ServerDiagnosticsSummaryType(this);
			} else {
				this.serverDiagnosticsSummary = (ServerDiagnosticsSummaryType) replacement;
			}
		}
		return this.serverDiagnosticsSummary;
	}

	private SamplingIntervalDiagnosticsArrayType createSamplingIntervalDiagnosticsArray(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.samplingIntervalDiagnosticsArray == null) {
			if (replacement == null) {
				this.samplingIntervalDiagnosticsArray = new SamplingIntervalDiagnosticsArrayType(this);
			} else {
				this.samplingIntervalDiagnosticsArray = (SamplingIntervalDiagnosticsArrayType) replacement;
			}
		}
		return this.samplingIntervalDiagnosticsArray;
	}

	private SubscriptionDiagnosticsArrayType createSubscriptionDiagnosticsArray(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.subscriptionDiagnosticsArray == null) {
			if (replacement == null) {
				this.subscriptionDiagnosticsArray = new SubscriptionDiagnosticsArrayType(this);
			} else {
				this.subscriptionDiagnosticsArray = (SubscriptionDiagnosticsArrayType) replacement;
			}
		}
		return this.subscriptionDiagnosticsArray;
	}

	private SessionsDiagnosticsSummaryType creatSessionDiagnosticsSummary(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.sessionsDiagnosticsSummary == null) {
			if (replacement == null) {
				this.sessionsDiagnosticsSummary = new SessionsDiagnosticsSummaryType(this);
			} else {
				this.sessionsDiagnosticsSummary = (SessionsDiagnosticsSummaryType) replacement;
			}
		}
		return this.sessionsDiagnosticsSummary;
	}

	private PropertyVariableType<Boolean> creatEnabledFlag(boolean createOrReplace, BaseInstance replacement) {
		if (createOrReplace && this.enabledFlag == null) {
			if (replacement == null) {
				this.enabledFlag = new PropertyVariableType<>(this);
			} else {
				this.enabledFlag = (PropertyVariableType<Boolean>) replacement;
			}
		}
		return this.enabledFlag;
	}

	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.SERVERDIAGNOSTICSSUMMARY:
			instance = createServerDiagnosticsSummary(createOrReplace, replacement);
			break;
		case BrowseNames.SAMPLINGINTERVALDIAGNOSTICSARRAY:
			instance = createSamplingIntervalDiagnosticsArray(createOrReplace, replacement);
			break;
		case BrowseNames.SUBSCRIPTIONDIAGNOSTICSARRAY:
			instance = createSubscriptionDiagnosticsArray(createOrReplace, replacement);
			break;
		case BrowseNames.SESSIONSDIAGNOSTICSSUMMARY:
			instance = creatSessionDiagnosticsSummary(createOrReplace, replacement);
			break;
		case BrowseNames.ENABLEDFLAG:
			instance = creatEnabledFlag(createOrReplace, replacement);
			break;
		default:
			break;
		}
		if (instance != null) {
			return instance;
		}
		return super.findChild(browseName, createOrReplace, replacement);
	}

	/**
	 * Get the description for the ServerDiagnosticsSummary Variable.
	 * 
	 * @return ServerDiagnosticssummary
	 */
	public ServerDiagnosticsSummaryType getServerDiagnosticsSummary() {
		return serverDiagnosticsSummary;
	}

	/**
	 * Set the description for the ServerDiagnosticsSummary Variable.
	 * 
	 * @param Value ServerDiagnosticsSummary
	 */
	public void setServerDiagnosticsSummary(ServerDiagnosticsSummaryType value) {
		this.serverDiagnosticsSummary = value;
	}

	/**
	 * Get the description for the SamplingIntervalDiagnosticsArray Variable.
	 * 
	 * @return SamplingIntervaldiagnosticsArray
	 */
	public SamplingIntervalDiagnosticsArrayType getSamplingIntervalDiagnosticsArray() {
		return samplingIntervalDiagnosticsArray;
	}

	/**
	 * Set the description for the SamplingIntervalDiagnosticsArray Variable.
	 * 
	 * @param Value SamplingINtervalDiagnosticsArray
	 */
	public void setSamplingIntervalDiagnosticsArray(SamplingIntervalDiagnosticsArrayType value) {
		this.samplingIntervalDiagnosticsArray = value;
	}

	/**
	 * Get the descriptino for the SubscriptionDiagnosticsArray.
	 * 
	 * @return SubscriptionDiagnosticsArray
	 */
	public SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArray() {
		return subscriptionDiagnosticsArray;
	}

	/**
	 * Set the description for the SubscriptionDiagnosticsArray Object.
	 * 
	 * @param Value SubscriptionDiagnosticsArray
	 */
	public void setSubscriptionDiagnosticsArray(SubscriptionDiagnosticsArrayType value) {
		this.subscriptionDiagnosticsArray = value;
	}

	/**
	 * Get the description for the SessionsDiagnosticsSummary Object.
	 * 
	 * @return SessionsDiagnosticsSummary
	 */
	public SessionsDiagnosticsSummaryType getSessionsDiagnosticsSummary() {
		return sessionsDiagnosticsSummary;
	}

	/**
	 * Set the description for the SessionsDiagnosticsSummary Object.
	 * 
	 * @param Value SessionsDiagnosticsSummary
	 */
	public void setSessionsDiagnosticsSummary(SessionsDiagnosticsSummaryType value) {
		this.sessionsDiagnosticsSummary = value;
	}

	/**
	 * Get the description for the EnabledFlag Property.
	 * 
	 * @return EnabledFlag
	 */
	public PropertyVariableType<Boolean> getEnabledFlag() {
		return enabledFlag;
	}

	/**
	 * Set the description for the EnabledFlag Property.
	 * 
	 * @param Value EnabledFlag
	 */
	public void setEnabledFlag(PropertyVariableType<Boolean> value) {
		this.enabledFlag = value;
	}
}
