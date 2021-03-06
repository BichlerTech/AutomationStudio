package opc.sdk.ua.classes;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ServerDiagnosticsSummaryDataType;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.ua.constants.BrowseNames;
import opc.sdk.ua.constants.NodeStateChangeMasks;

public class ServerDiagnosticsSummaryType extends BaseDataVariableType<ServerDiagnosticsSummaryDataType> {
	protected BaseDataVariableType<UnsignedInteger> serverViewCount = null;
	protected BaseDataVariableType<UnsignedInteger> currentSessionCount = null;
	protected BaseDataVariableType<UnsignedInteger> cumulatedSessionCount = null;
	protected BaseDataVariableType<UnsignedInteger> securityRejectedSessionCount = null;
	protected BaseDataVariableType<UnsignedInteger> rejectedSessionCount = null;
	protected BaseDataVariableType<UnsignedInteger> sessionTimeoutCount = null;
	protected BaseDataVariableType<UnsignedInteger> sessionAbortCount = null;
	protected BaseDataVariableType<UnsignedInteger> currentSubscriptionCount = null;
	protected BaseDataVariableType<UnsignedInteger> cumulatedSubscriptionCount = null;
	protected BaseDataVariableType<UnsignedInteger> publishingIntervalCount = null;
	protected BaseDataVariableType<UnsignedInteger> securityRejectedRequestCount = null;
	protected BaseDataVariableType<UnsignedInteger> rejectedRequestCount = null;
	private static final String INITIALIZATIONSTRING = "//////////8VYIkAAgAAAAAAJAAAAFNlcnZlckRpYWdub3N0aWNzU3VtbWFyeVR5cGVJbnN0YW5jZQEA"
			+ "ZggBAGYIAQBbA/////8BAf////8MAAAAFWCJCgIAAAAAAA8AAABTZXJ2ZXJWaWV3Q291bnQBAGcIAC8A"
			+ "P2cIAAAAB/////8BAf////8AAAAAFWCJCgIAAAAAABMAAABDdXJyZW50U2Vzc2lvbkNvdW50AQBoCAAv"
			+ "AD9oCAAAAAf/////AQH/////AAAAABVgiQoCAAAAAAAVAAAAQ3VtdWxhdGVkU2Vzc2lvbkNvdW50AQBp"
			+ "CAAvAD9pCAAAAAf/////AQH/////AAAAABVgiQoCAAAAAAAcAAAAU2VjdXJpdHlSZWplY3RlZFNlc3Np"
			+ "b25Db3VudAEAaggALwA/aggAAAAH/////wEB/////wAAAAAVYIkKAgAAAAAAFAAAAFJlamVjdGVkU2Vz"
			+ "c2lvbkNvdW50AQBrCAAvAD9rCAAAAAf/////AQH/////AAAAABVgiQoCAAAAAAATAAAAU2Vzc2lvblRp"
			+ "bWVvdXRDb3VudAEAbAgALwA/bAgAAAAH/////wEB/////wAAAAAVYIkKAgAAAAAAEQAAAFNlc3Npb25B"
			+ "Ym9ydENvdW50AQBtCAAvAD9tCAAAAAf/////AQH/////AAAAABVgiQoCAAAAAAAYAAAAQ3VycmVudFN1"
			+ "YnNjcmlwdGlvbkNvdW50AQBwCAAvAD9wCAAAAAf/////AQH/////AAAAABVgiQoCAAAAAAAaAAAAQ3Vt"
			+ "dWxhdGVkU3Vic2NyaXB0aW9uQ291bnQBAHEIAC8AP3EIAAAAB/////8BAf////8AAAAAFWCJCgIAAAAA"
			+ "ABcAAABQdWJsaXNoaW5nSW50ZXJ2YWxDb3VudAEAbwgALwA/bwgAAAAH/////wEB/////wAAAAAVYIkK"
			+ "AgAAAAAAHQAAAFNlY3VyaXR5UmVqZWN0ZWRSZXF1ZXN0c0NvdW50AQByCAAvAD9yCAAAAAf/////AQH/"
			+ "////AAAAABVgiQoCAAAAAAAVAAAAUmVqZWN0ZWRSZXF1ZXN0c0NvdW50AQBzCAAvAD9zCAAAAAf/////" + "AQH/////AAAAAA==";

	/**
	 * Initializes the type with its default attribute values.
	 * 
	 * @param parent
	 */
	protected ServerDiagnosticsSummaryType(BaseNode parent) {
		super(parent);
	}

	@Override
	protected NodeId getDefaultTypeDefinitionId() {
		return Identifiers.ServerDiagnosticsSummaryType;
	}

	@Override
	protected NodeId getDefaultDataTypeId() {
		return Identifiers.ServerDiagnosticsSummaryDataType;
	}

	@Override
	protected int getDefaultValueRank() {
		return ValueRanks.Scalar.getValue();
	}

	private BaseDataVariableType<UnsignedInteger> createServerViewCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.serverViewCount == null) {
			if (replacement == null) {
				this.serverViewCount = new BaseDataVariableType<>(this);
			} else {
				this.serverViewCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.serverViewCount;
	}

	private BaseDataVariableType<UnsignedInteger> createCurrentSessionCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.currentSessionCount == null) {
			if (replacement == null) {
				this.currentSessionCount = new BaseDataVariableType<>(this);
			} else {
				this.currentSessionCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.currentSessionCount;
	}

	private BaseDataVariableType<UnsignedInteger> createCumulatedSessionCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.cumulatedSessionCount == null) {
			if (replacement == null) {
				this.cumulatedSessionCount = new BaseDataVariableType<>(this);
			} else {
				this.cumulatedSessionCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.cumulatedSessionCount;
	}

	private BaseDataVariableType<UnsignedInteger> createSecurityRejectedSessionCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace) {
			if (this.securityRejectedSessionCount == null && replacement == null) {
				this.securityRejectedSessionCount = new BaseDataVariableType<>(this);
			} else {
				this.securityRejectedSessionCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.securityRejectedSessionCount;
	}

	private BaseDataVariableType<UnsignedInteger> createRejectedSessionCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.rejectedSessionCount == null) {
			if (replacement == null) {
				this.rejectedSessionCount = new BaseDataVariableType<>(this);
			} else {
				this.rejectedSessionCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.rejectedSessionCount;
	}

	private BaseDataVariableType<UnsignedInteger> createSessionTimeoutCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.sessionTimeoutCount == null) {
			if (replacement == null) {
				this.sessionTimeoutCount = new BaseDataVariableType<>(this);
			} else {
				this.sessionTimeoutCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.sessionTimeoutCount;
	}

	private BaseDataVariableType<UnsignedInteger> createSessionAbortCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.sessionAbortCount == null) {
			if (replacement == null) {
				this.sessionAbortCount = new BaseDataVariableType<>(this);
			} else {
				this.sessionAbortCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.sessionAbortCount;
	}

	private BaseDataVariableType<UnsignedInteger> createCurrentSubscriptionCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.currentSubscriptionCount == null) {
			if (replacement == null) {
				this.currentSubscriptionCount = new BaseDataVariableType<>(this);
			} else {
				this.currentSubscriptionCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.currentSubscriptionCount;
	}

	private BaseDataVariableType<UnsignedInteger> createCumulatedSubscriptionCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.cumulatedSubscriptionCount == null) {
			if (replacement == null) {
				this.cumulatedSubscriptionCount = new BaseDataVariableType<>(this);
			} else {
				this.cumulatedSubscriptionCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.cumulatedSubscriptionCount;
	}

	private BaseDataVariableType<UnsignedInteger> createPublishingIntervalCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.publishingIntervalCount == null) {
			if (replacement == null) {
				this.publishingIntervalCount = new BaseDataVariableType<>(this);
			} else {
				this.publishingIntervalCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.publishingIntervalCount;
	}

	private BaseDataVariableType<UnsignedInteger> createSecurityRejectedRequestsCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.securityRejectedRequestCount == null) {
			if (replacement == null) {
				this.securityRejectedRequestCount = new BaseDataVariableType<>(this);
			} else {
				this.securityRejectedRequestCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.securityRejectedRequestCount;
	}

	private BaseDataVariableType<UnsignedInteger> createRejectedRequestsCount(boolean createOrReplace,
			BaseInstance replacement) {
		if (createOrReplace && this.rejectedRequestCount == null) {
			if (replacement == null) {
				this.rejectedRequestCount = new BaseDataVariableType<>(this);
			} else {
				this.rejectedRequestCount = (BaseDataVariableType<UnsignedInteger>) replacement;
			}
		}
		return this.rejectedRequestCount;
	}

	/**
	 * Finds the child with the specified browse name.
	 */
	@Override
	protected BaseInstance findChild(QualifiedName browseName, boolean createOrReplace, BaseInstance replacement) {
		if (QualifiedName.isNull(browseName)) {
			return null;
		}
		BaseInstance instance = null;
		switch (browseName.getName()) {
		case BrowseNames.SERVERVIEWCOUNT:
			instance = createServerViewCount(createOrReplace, replacement);
			break;
		case BrowseNames.CURRENTSESSIONCOUNT:
			instance = createCurrentSessionCount(createOrReplace, replacement);
			break;
		case BrowseNames.CUMULATEDSESSIONCOUNT:
			instance = createCumulatedSessionCount(createOrReplace, replacement);
			break;
		case BrowseNames.SECURITYREJECTEDSESSIONCOUNT:
			instance = createSecurityRejectedSessionCount(createOrReplace, replacement);
			break;
		case BrowseNames.REJECTEDSESSIONCOUNT:
			instance = this.createRejectedSessionCount(createOrReplace, replacement);
			break;
		case BrowseNames.SESSIONTIMEOUTCOUNT:
			instance = createSessionTimeoutCount(createOrReplace, replacement);
			break;
		case BrowseNames.SESSIONABORTCOUNT:
			instance = createSessionAbortCount(createOrReplace, replacement);
			break;
		case BrowseNames.CURRENTSUBSCRIPTIONCOUNT:
			instance = createCurrentSubscriptionCount(createOrReplace, replacement);
			break;
		case BrowseNames.CUMULATEDSUBSCRIPTIONCOUNT:
			instance = createCumulatedSubscriptionCount(createOrReplace, replacement);
			break;
		case BrowseNames.PUBLISHINGINTERVALCOUNT:
			instance = createPublishingIntervalCount(createOrReplace, replacement);
			break;
		case BrowseNames.SECURITYREJECTEDREQUESTSCOUNT:
			instance = createSecurityRejectedRequestsCount(createOrReplace, replacement);
			break;
		case BrowseNames.REJECTEDREQUESTSCOUNT:
			instance = createRejectedRequestsCount(createOrReplace, replacement);
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
	 * Populates a list with the children that belong to the node.
	 */
	@Override
	public List<BaseInstance> getChildren() {
		List<BaseInstance> children = new ArrayList<>();
		if (this.serverViewCount != null) {
			children.add(this.serverViewCount);
		}
		if (this.currentSessionCount != null) {
			children.add(this.currentSessionCount);
		}
		if (this.cumulatedSessionCount != null) {
			children.add(this.cumulatedSessionCount);
		}
		if (this.securityRejectedSessionCount != null) {
			children.add(this.securityRejectedSessionCount);
		}
		if (this.rejectedSessionCount != null) {
			children.add(this.rejectedSessionCount);
		}
		if (this.sessionTimeoutCount != null) {
			children.add(this.sessionTimeoutCount);
		}
		if (this.sessionAbortCount != null) {
			children.add(this.sessionAbortCount);
		}
		if (this.currentSubscriptionCount != null) {
			children.add(this.currentSubscriptionCount);
		}
		if (this.cumulatedSubscriptionCount != null) {
			children.add(this.cumulatedSubscriptionCount);
		}
		if (this.publishingIntervalCount != null) {
			children.add(this.publishingIntervalCount);
		}
		if (this.securityRejectedRequestCount != null) {
			children.add(this.securityRejectedRequestCount);
		}
		if (this.rejectedRequestCount != null) {
			children.add(this.rejectedRequestCount);
		}
		children.addAll(super.getChildren());
		return children;
	}

	/**
	 * Get the description for the ServerViewCount Variable.
	 * 
	 * @return ServerViewCount
	 */
	public BaseDataVariableType<UnsignedInteger> getServerViewCount() {
		return serverViewCount;
	}

	/**
	 * Set the description for the ServerViewCount Variable.
	 * 
	 * @param Value ServerViewCount
	 */
	public void setServerViewCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.serverViewCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.serverViewCount = value;
	}

	/**
	 * Get the description for the CurrentSessionCount Variable.
	 * 
	 * @return CurrentSessionCount
	 */
	public BaseDataVariableType<UnsignedInteger> getCurrentSessionCount() {
		return currentSessionCount;
	}

	/**
	 * Set the description for the CurrentSessionCount Variable.
	 * 
	 * @param Value CurrentSessionCount
	 */
	public void setCurrentSessionCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.currentSessionCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.currentSessionCount = value;
	}

	/**
	 * Get the description for the CumulatedSessionCount Variable.
	 * 
	 * @return CumulatedSessionCount
	 */
	public BaseDataVariableType<UnsignedInteger> getCumulatedSessionCount() {
		return cumulatedSessionCount;
	}

	/**
	 * Set the description for the CumulatedSessionCount Variable.
	 * 
	 * @param Value CumulatedSessionCount
	 */
	public void setCumulatedSessionCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.cumulatedSessionCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.cumulatedSessionCount = value;
	}

	/**
	 * Get the description for the SecurityRejectedSessionCount Variable.
	 * 
	 * @return SecurityRejectedSessionCount
	 */
	public BaseDataVariableType<UnsignedInteger> getSecurityRejectedSessionCount() {
		return securityRejectedSessionCount;
	}

	/**
	 * Set the description for the SecurityRejectedSessionCount Variable.
	 * 
	 * @param Value SecurityRejectedSessionCount
	 */
	public void setSecurityRejectedSessionCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.securityRejectedSessionCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.securityRejectedSessionCount = value;
	}

	/**
	 * Get the description for the RejectedSessionCount Variable.
	 * 
	 * @return RejectedSessionCount
	 */
	public BaseDataVariableType<UnsignedInteger> getRejectedSessionCount() {
		return rejectedSessionCount;
	}

	/**
	 * Set the description for the RejectedSessionCount Variable.
	 * 
	 * @param Value RejectedSessionCount
	 */
	public void setRejectedSessionCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.rejectedSessionCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.rejectedSessionCount = value;
	}

	/**
	 * Get the description for the SessionTimeoutCount Variable.
	 * 
	 * @return SessionTimeoutCount
	 */
	public BaseDataVariableType<UnsignedInteger> getSessionTimeoutCount() {
		return sessionTimeoutCount;
	}

	/**
	 * Set the description for the SessionTimeoutCount Variable.
	 * 
	 * @param Value SessionTimeoutCount
	 */
	public void setSessionTimeoutCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.sessionTimeoutCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.sessionTimeoutCount = value;
	}

	/**
	 * Get the description for the SessionAbortCount Variable.
	 * 
	 * @return SessionAbortCount
	 */
	public BaseDataVariableType<UnsignedInteger> getSessionAbortCount() {
		return sessionAbortCount;
	}

	/**
	 * Set the description for the SessionAbortCount Variable.
	 * 
	 * @param Value SessionAbortCount
	 */
	public void setSessionAbortCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.sessionAbortCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.sessionAbortCount = value;
	}

	/**
	 * Get the description for the CurrentSubscriptionCount Variable.
	 * 
	 * @return CurrentSubscriptionCount
	 */
	public BaseDataVariableType<UnsignedInteger> getCurrentSubscriptionCount() {
		return currentSubscriptionCount;
	}

	/**
	 * Set the description for the CurrentSubscriptionCount Variable.
	 * 
	 * @param Value CurrentSubscriptionCount
	 */
	public void setCurrentSubscriptionCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.currentSubscriptionCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.currentSubscriptionCount = value;
	}

	/**
	 * Get the description for the CumulatedSubscriptionCount Variable.
	 * 
	 * @return CumulatedSubscriptionCount
	 */
	public BaseDataVariableType<UnsignedInteger> getCumulatedSubscriptionCount() {
		return cumulatedSubscriptionCount;
	}

	/**
	 * Set the description for the CumulatedSubscriptionCount Variable.
	 * 
	 * @param Value CumulatedSubscriptionCount
	 */
	public void setCumulatedSubscriptionCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.cumulatedSubscriptionCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.cumulatedSubscriptionCount = value;
	}

	/**
	 * Get the description for the PublishingIntervalCount Variable.
	 * 
	 * @return PublishingIntervalCount
	 */
	public BaseDataVariableType<UnsignedInteger> getPublishingIntervalCount() {
		return publishingIntervalCount;
	}

	/**
	 * Set the description for the PublishingIntervalCount Variable.
	 * 
	 * @param Value PublishingIntervalCount
	 */
	public void setPublishingIntervalCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.publishingIntervalCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.publishingIntervalCount = value;
	}

	/**
	 * Get the description for the SecurityRejectedRequestCount Variable.
	 * 
	 * @return SecurityRejectedRequestCount
	 */
	public BaseDataVariableType<UnsignedInteger> getSecurityRejectedRequestCount() {
		return securityRejectedRequestCount;
	}

	/**
	 * Set the description for the SecurityRejectedRequestCount Variable.
	 * 
	 * @param Value SecurityRejectedRequestCount
	 */
	public void setSecurityRejectedRequestCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.securityRejectedRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.securityRejectedRequestCount = value;
	}

	/**
	 * Get the description for the RejectedRequestCount Variable.
	 * 
	 * @return RejectedRequestCount
	 */
	public BaseDataVariableType<UnsignedInteger> getRejectedRequestCount() {
		return rejectedRequestCount;
	}

	/**
	 * Set the description for the RejectedRequestCount Variable.
	 * 
	 * @param Value RejectedRequestCount
	 */
	public void setRejectedRequestCount(BaseDataVariableType<UnsignedInteger> value) {
		if (this.rejectedRequestCount != value) {
			setChangeMask(NodeStateChangeMasks.CHILDREN);
		}
		this.rejectedRequestCount = value;
	}

	public String getInitializationString() {
		return INITIALIZATIONSTRING;
	}
}
