package opc.sdk.ua.classes;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.context.ISystemContext;
import opc.sdk.ua.constants.NodeStateChangeMasks;
import opc.sdk.ua.constants.VariableCopyPolicy;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.utils.NumericRange;

public class BaseVariableValue {
	private Object lock = null;
	private VariableCopyPolicy copyPolicy = null;
	private BaseInstance[] updateList = null;
	private ServiceResult error = null;
	private DateTime timestamp = null;

	public BaseVariableValue(Object dataLock) {
		this.lock = dataLock;
		this.copyPolicy = VariableCopyPolicy.COPYONREAD;
		if (this.lock == null)
			this.lock = new Object();
	}

	/**
	 * Get the behavoir to use when reading or writing all or part of the object.
	 * 
	 * @return CopyPolicy
	 */
	public VariableCopyPolicy getCopyPolicy() {
		return this.copyPolicy;
	}

	/**
	 * An object used to synchronize access to the value.
	 * 
	 * @return Lock
	 */
	public Object getLock() {
		return this.lock;
	}

	/**
	 * Gets the current error state.
	 * 
	 * @return Error
	 */
	public ServiceResult getError() {
		return this.error;
	}

	/**
	 * Gets the timestamp associated with the value.
	 * 
	 * @return Timestamp
	 */
	public DateTime getTimeStamp() {
		return this.timestamp;
	}

	/**
	 * Set the behavoir to use when reading or writing all or part of the object.
	 * 
	 * @param CopyPolicy
	 */
	public void setCopyPolicy(VariableCopyPolicy policy) {
		this.copyPolicy = policy;
	}

	/**
	 * Sets the current error state.�
	 * 
	 * @param Error
	 */
	public void setError(ServiceResult error) {
		this.error = error;
	}

	/**
	 * Sets the timestamp associated with the value.
	 * 
	 * @param Timestamp
	 */
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Clears the chagne mask for all nodes in the update list.
	 * 
	 * @param Context SystemContext
	 */
	public void changesComplete() {
		synchronized (this.lock) {
			if (this.updateList != null) {
				for (int ii = 0; ii < this.updateList.length; ii++) {
					BaseInstance instance = this.updateList[ii];
					if (instance != null) {
						instance.updateChangeMasks(NodeStateChangeMasks.VALUE);
						instance.clearChangeMasks(false);
					}
				}
			}
		}
	}

	/**
	 * Does any processing before a read operation takes place.
	 * 
	 * @param context
	 * @param node
	 * 
	 *                protected void doBeforeReadProcessing( ISystemContext context,
	 *                NodeState node) { if (onBeforeRead != null) {
	 *                onBeforeRead(context, this, node); } }
	 */
	/**
	 * Reads the value or a component of the value.
	 * 
	 * @param context
	 * @param node
	 * @param indexRange
	 * @param dataEncoding
	 * @param value
	 * @param statusCode
	 * @param timestamp
	 * @return
	 * 
	 */
	protected ServiceResult read(ISystemContext context, NumericRange indexRange, QualifiedName dataEncoding,
			Object value, StatusCode statusCode, DateTime timestamp) {
		synchronized (this.lock) {
			// ensure a value timestamp exists.
			if (this.timestamp.equals(DateTime.MIN_VALUE)) {
				this.timestamp = DateTime.currentTime();
			}
			timestamp = this.timestamp;
			// check for errors.
			if (this.error.isBad()) {
				value = null;
				return this.error;
			}
			// apply the index range and encoding. ServiceResult result =
			ServiceResult result = BaseVariableType.applyIndexRangeAndDataEncoding(context, indexRange, dataEncoding,
					value);
			if (result.isBad()) {
				return result;
			}
			// apply copy policy
			if (this.copyPolicy != null && VariableCopyPolicy.COPYONREAD.getValue() != 0) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "Clone Value");
			}
			return new ServiceResult(StatusCode.GOOD);
		}
	}

	/**
	 * Reads the current Value.
	 * 
	 * @param CurrentValue
	 * 
	 * @param ValueToRead
	 * 
	 * @return
	 */
	protected ServiceResult read(Object currentValue, Object valueToRead) {
		synchronized (this.lock) {
			if (this.error.isBad()) {
				return this.error;
			}
			if ((this.copyPolicy.getValue() & VariableCopyPolicy.COPYONREAD.getValue()) != 0) {
				System.err.println("CurrentValueCopy");
			} else {
				// valueToRead = currentValue;
			}
			return new ServiceResult(StatusCode.GOOD);
		}
	}

	/**
	 * Writes the current value.
	 * 
	 * @param valueToWrite
	 * @return
	 */
	protected Object write(Object valueToWrite) {
		synchronized (lock) {
			if (this.copyPolicy != null && VariableCopyPolicy.COPYONWRITE.getValue() != 0) {
				System.err.println("ErrorWritenBaseVariableValue");
				return null;// Utils.clone(valueToWrite);
			}
			return valueToWrite;
		}
	}

	/**
	 * Sets the list of nodes which are updated when ClearChangeMasks is called.
	 * 
	 * @param updateList
	 */
	protected void setUpdateList(List<BaseInstance> updateList) {
		synchronized (lock) {
			this.updateList = null;
			if (updateList != null && updateList.size() > 0) {
				this.updateList = new BaseInstance[updateList.size()];
				for (int ii = 0; ii < this.updateList.length; ii++) {
					this.updateList[ii] = updateList.get(ii);
					// the copy copy is enforced by the value wrapper.
					BaseVariableType<?> variable = (BaseVariableType<?>) this.updateList[ii];
					if (variable != null) {
						variable.setCopyPolicy(VariableCopyPolicy.NEVER);
					}
				}
			}
		}
	}
}
