package opc.sdk.core.classes.ua.core;

public class ProgramStateMachineType extends FiniteStateMachineType {
	private static final org.opcfoundation.ua.builtintypes.NodeId id = CoreIdentifiers.ProgramStateMachineType;
	private TransitionType haltedToReady;
	private opc.sdk.core.classes.ua.base.BaseMethodGen halt;
	private TransitionType suspendedToRunning;
	private opc.sdk.core.classes.ua.base.BaseMethodGen suspend;
	private TransitionType suspendedToReady;
	private StateType ready;
	private opc.sdk.core.classes.ua.base.BaseMethodGen resume;
	private TransitionType suspendedToHalted;
	private PropertyType instanceCount;
	private PropertyType recycleCount;
	private PropertyType deletable;
	private TransitionType readyToHalted;
	private TransitionType readyToRunning;
	private TransitionType runningToReady;
	private BaseObjectType finalResultData;
	private PropertyType creatable;
	private TransitionType runningToSuspended;
	private opc.sdk.core.classes.ua.base.BaseMethodGen start;
	private opc.sdk.core.classes.ua.base.BaseMethodGen reset;
	private ProgramDiagnosticType programDiagnostics;
	private StateType running;
	private PropertyType maxRecycleCount;
	private TransitionType runningToHalted;
	private PropertyType maxInstanceCount;
	private FiniteStateVariableType currentState;
	private FiniteTransitionVariableType lastTransition;
	private StateType suspended;
	private StateType halted;
	private PropertyType autoDelete;

	public ProgramStateMachineType() {
		super();
	}

	@Override
	public org.opcfoundation.ua.builtintypes.NodeId getTypeId() {
		return id;
	}

	public TransitionType getHaltedToReady() {
		return haltedToReady;
	}

	public void setHaltedToReady(TransitionType value) {
		haltedToReady = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getHalt() {
		return halt;
	}

	public void setHalt(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		halt = value;
	}

	public TransitionType getSuspendedToRunning() {
		return suspendedToRunning;
	}

	public void setSuspendedToRunning(TransitionType value) {
		suspendedToRunning = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getSuspend() {
		return suspend;
	}

	public void setSuspend(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		suspend = value;
	}

	public TransitionType getSuspendedToReady() {
		return suspendedToReady;
	}

	public void setSuspendedToReady(TransitionType value) {
		suspendedToReady = value;
	}

	public StateType getReady() {
		return ready;
	}

	public void setReady(StateType value) {
		ready = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getResume() {
		return resume;
	}

	public void setResume(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		resume = value;
	}

	public TransitionType getSuspendedToHalted() {
		return suspendedToHalted;
	}

	public void setSuspendedToHalted(TransitionType value) {
		suspendedToHalted = value;
	}

	public PropertyType getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(PropertyType value) {
		instanceCount = value;
	}

	public PropertyType getRecycleCount() {
		return recycleCount;
	}

	public void setRecycleCount(PropertyType value) {
		recycleCount = value;
	}

	public PropertyType getDeletable() {
		return deletable;
	}

	public void setDeletable(PropertyType value) {
		deletable = value;
	}

	public TransitionType getReadyToHalted() {
		return readyToHalted;
	}

	public void setReadyToHalted(TransitionType value) {
		readyToHalted = value;
	}

	public TransitionType getReadyToRunning() {
		return readyToRunning;
	}

	public void setReadyToRunning(TransitionType value) {
		readyToRunning = value;
	}

	public TransitionType getRunningToReady() {
		return runningToReady;
	}

	public void setRunningToReady(TransitionType value) {
		runningToReady = value;
	}

	public BaseObjectType getFinalResultData() {
		return finalResultData;
	}

	public void setFinalResultData(BaseObjectType value) {
		finalResultData = value;
	}

	public PropertyType getCreatable() {
		return creatable;
	}

	public void setCreatable(PropertyType value) {
		creatable = value;
	}

	public TransitionType getRunningToSuspended() {
		return runningToSuspended;
	}

	public void setRunningToSuspended(TransitionType value) {
		runningToSuspended = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getStart() {
		return start;
	}

	public void setStart(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		start = value;
	}

	public opc.sdk.core.classes.ua.base.BaseMethodGen getReset() {
		return reset;
	}

	public void setReset(opc.sdk.core.classes.ua.base.BaseMethodGen value) {
		reset = value;
	}

	public ProgramDiagnosticType getProgramDiagnostics() {
		return programDiagnostics;
	}

	public void setProgramDiagnostics(ProgramDiagnosticType value) {
		programDiagnostics = value;
	}

	public StateType getRunning() {
		return running;
	}

	public void setRunning(StateType value) {
		running = value;
	}

	public PropertyType getMaxRecycleCount() {
		return maxRecycleCount;
	}

	public void setMaxRecycleCount(PropertyType value) {
		maxRecycleCount = value;
	}

	public TransitionType getRunningToHalted() {
		return runningToHalted;
	}

	public void setRunningToHalted(TransitionType value) {
		runningToHalted = value;
	}

	public PropertyType getMaxInstanceCount() {
		return maxInstanceCount;
	}

	public void setMaxInstanceCount(PropertyType value) {
		maxInstanceCount = value;
	}

	@Override
	public FiniteStateVariableType getCurrentState() {
		return currentState;
	}

	@Override
	public void setCurrentState(FiniteStateVariableType value) {
		currentState = value;
	}

	@Override
	public FiniteTransitionVariableType getLastTransition() {
		return lastTransition;
	}

	@Override
	public void setLastTransition(FiniteTransitionVariableType value) {
		lastTransition = value;
	}

	public StateType getSuspended() {
		return suspended;
	}

	public void setSuspended(StateType value) {
		suspended = value;
	}

	public StateType getHalted() {
		return halted;
	}

	public void setHalted(StateType value) {
		halted = value;
	}

	public PropertyType getAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(PropertyType value) {
		autoDelete = value;
	}

	@Override
	public String toString() {
		return "ProgramStateMachineType:" + org.opcfoundation.ua.utils.ObjectUtils.printFieldsDeep(this);
	}
}
