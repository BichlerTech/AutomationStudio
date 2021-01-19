package com.bichler.astudio.opcua.opcmodeler.exporter;

import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.Variant;

/**
 * Base Attributes read from a XML - File as String format.
 * 
 * @author Thomas Zöchbauer
 * 
 */
public abstract class BaseAttributes {
}

/**
 * Object Node Attributes.<br>
 * -EventNotifier
 * 
 * @author Thomas Zöchbauer
 * 
 */
class ObjectAttribute extends BaseAttributes {
	/** String EventNotifier representation */
	private String eventNotifier = null;

	/**
	 * Object defined Attributes
	 * 
	 * @param EventNotifier Whether the Node can be used to subscribe to Events or
	 *                      not is vendor specific
	 */
	public ObjectAttribute(Object[] eventNotifier) {
		this.eventNotifier = (String) eventNotifier[0];
	}

	/**
	 * Getter from the EventNotifier Attribute.
	 * 
	 * @return EventNotifier
	 */
	public String getEventNotifier() {
		return this.eventNotifier;
	}

	/**
	 * Setter for the EventNotifier Attribute.
	 * 
	 * @param EventNotifier Whether the Node can be used to subscribe to Events or
	 *                      not is vendor specific
	 */
	public void setEventNotifier(String[] eventNotifier) {
		this.eventNotifier = eventNotifier[0];
	}
}

/**
 * Object Type Node Attributes. <br>
 * - IsAbstract
 * 
 * @author Thomas Zöchbauer
 */
class ObjectTypeAttribute extends BaseAttributes {
	/** String IsAbstract representation */
	private String isAbstract = null;

	/**
	 * Object Type defined Attributes
	 * 
	 * @param IsAbstract A boolean Attribute with the following values: <br>
	 *                   TRUE it is an abstract ReferenceType, i.e. no References of
	 *                   this type shall exist, only of its subtypes. <br>
	 *                   FALSE it is not an abstract ReferenceType, i.e. References
	 *                   of this type can exist.
	 */
	public ObjectTypeAttribute(Object[] isAbstract) {
		this.isAbstract = (String) isAbstract[0];
	}

	/**
	 * Getter from the IsAbstract Attribute.
	 * 
	 * @return IsAbstract
	 */
	public String getIsAbstract() {
		return this.isAbstract;
	}

	/**
	 * Setter for the IsAbstract Attribute.
	 * 
	 * @param IsAbstract A boolean Attribute with the following values:<br>
	 *                   TRUE it is an abstract ObjectType, i.e. no Objects of this
	 *                   type shall exist, only of its subtypes.<br>
	 *                   FALSE it is not an abstract ObjectType, i.e. Objects of
	 *                   this type can exist.
	 */
	public void setIsAbstract(String isAbstract) {
		this.isAbstract = isAbstract;
	}
}

/**
 * Variable Node Attributes.<br>
 * -Value<br>
 * -DataType<br>
 * -ValueRank<br>
 * -ArrayDimension<br>
 * -AccessLevel<br>
 * -UserAccessLevel<br>
 * -MinimumSamplingInterval<br>
 * -Historizing
 * 
 * @author Thomas Zöchbauer
 * 
 */
class VariableAttribute extends BaseAttributes {
	/** Variant converted Value */
	private Variant value = null;
	/** String DataType representation */
	private String dataType = null;
	/** String ValueRank representation */
	private String valueRank = null;
	/** String ArrayDimension representation */
	private String arrayDimensions = null;
	/** String AccessLevel representation */
	private String accessLevel = null;
	/** String UserAccessLevel representation */
	private String userAccessLevel = null;
	/** String MinimumSamplingInterval representation */
	private String minimumSamplingInterval = null;
	/** String Historizing representation */
	private String historizing = null;

	/**
	 * Variable defined Attributes.
	 * 
	 * @param Attributes Attributes Array for the String imported Attributes.
	 */
	public VariableAttribute(Object[] attributes) {
		this.value = (Variant) attributes[0];
		this.dataType = (String) attributes[1];
		this.valueRank = (String) attributes[2];
		this.arrayDimensions = (String) attributes[3];
		this.accessLevel = (String) attributes[4];
		this.userAccessLevel = (String) attributes[5];
		this.minimumSamplingInterval = (String) attributes[6];
		this.historizing = (String) attributes[7];
	}

	/**
	 * Getter for the Value.
	 * 
	 * @return Value
	 */
	public Variant getValue() {
		return this.value;
	}

	/**
	 * Getter for the DataType.
	 * 
	 * @return DataType
	 */
	public String getDataType() {
		return this.dataType;
	}

	/**
	 * Getter for the ValueRank.
	 * 
	 * @return ValueRank
	 */
	public String getValueRank() {
		return this.valueRank;
	}

	/**
	 * Getter for the ArrayDimension.
	 * 
	 * @return ArrayDimension
	 */
	public String getArrayDimension() {
		return this.arrayDimensions;
	}

	/**
	 * Getter for the AccessLevel.
	 * 
	 * @return AccessLevel
	 */
	public String getAccessLevel() {
		return this.accessLevel;
	}

	/**
	 * Getter for the UserAccessLevel.
	 * 
	 * @return UserAccessLevel
	 */
	public String getUserAccessLevel() {
		return this.userAccessLevel;
	}

	/**
	 * Getter for the MinimumSamplingInterval.
	 * 
	 * @return MinimuimSamplingInterval
	 */
	public String getMinimumSamplingInterval() {
		return this.minimumSamplingInterval;
	}

	/**
	 * Getter for the Historizing.
	 * 
	 * @return Historizing
	 */
	public String getHistorizing() {
		return this.historizing;
	}

	/**
	 * Setter for the Value Attribute.
	 * 
	 * @param Value The most recent value of the Variable that the server has. Its
	 *              data type is defined by the DataType Attribute. It is the only
	 *              Attribute that does not have a data type associated with it.
	 *              This allows all Variables to have a value defined by the same
	 *              Value Attribute.
	 */
	public void setValue(Variant value) {
		this.value = value;
	}

	/**
	 * Setter for the DataType Attribute.
	 * 
	 * @param DataType NodeId of the DataType definition for the Value Attribute.
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * Setter for the ValueRank Attribute.
	 * 
	 * @param ValueRank This Attribute indicates whether the Value Attribute of the
	 *                  Variable is an array and how many dimensions the array has.
	 *                  It may have the following values:<br>
	 *                  n>1: the Value is an array with the specified number of
	 *                  dimensions. <br>
	 *                  OneDimension (1): The value is an array with one
	 *                  dimension.<br>
	 *                  OneOrMoreDimensions (0): The value is an array with one or
	 *                  more dimensions. <br>
	 *                  Scalar (-1): The value is not an array.<br>
	 *                  Any (-2): The value can be a scalar or an array with any
	 *                  number of dimensions.<br>
	 *                  ScalarOrOneDimension (-3): The value can be a scalar or a
	 *                  one dimensional array.
	 */
	public void setValueRank(String valueRank) {
		this.valueRank = valueRank;
	}

	/**
	 * Setter for the ArrayDimension Attribute.
	 * 
	 * @param ArrayDimensions This Attribute specifies the length of each dimension
	 *                        for an array value. The Attribute is intended to
	 *                        describe the capability of the Variable, not the
	 *                        current size. The number of elements shall be equal to
	 *                        the value of the ValueRank Attribute. Shall be null if
	 *                        ValueRank <= 0. A value of 0 for an individual
	 *                        dimension indicates that the dimension has a variable
	 *                        length.
	 */
	public void setArrayDimensions(String arrayDimensions) {
		this.arrayDimensions = arrayDimensions;
	}

	/**
	 * Setter for the AccessLevel Attribute.
	 * 
	 * @param AccessLevel The AccessLevel Attribute is used to indicate how the
	 *                    Value of a Variable can be accessed (read/write) and if it
	 *                    contains current and/or historic data. The AccessLevel
	 *                    does not take any user access rights into account, i.e.
	 *                    although the Variable is writeable this may be restricted
	 *                    to a certain user / user group.
	 */
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	/**
	 * Setter for the UserAccessLevel Attribute.
	 * 
	 * @param UserAccessLevel The UserAccessLevel Attribute is used to indicate how
	 *                        the Value of a Variable can be accessed (read/write)
	 *                        and if it contains current or historic data taking
	 *                        user access rights into account.
	 */
	public void setUserAccessLevel(String userAccessLevel) {
		this.userAccessLevel = userAccessLevel;
	}

	/**
	 * Setter for the MinimumSamplingInterval Attribute.
	 * 
	 * @param MinimumSamplingInterval The MinimumSamplingInterval Attribute
	 *                                indicates how “current” the Value of the
	 *                                Variable will be kept. It specifies (in
	 *                                milliseconds) how fast the server can
	 *                                reasonably sample the value for changes.
	 */
	public void setMinimumSamplingInterval(String minimumSamplingInterval) {
		this.minimumSamplingInterval = minimumSamplingInterval;
	}

	/**
	 * Setter for the Historizing Attribute.
	 * 
	 * @param Historizing The Historizing Attribute indicates whether the Server is
	 *                    actively collecting data for the history of the Variable.
	 *                    This differs from the AccessLevel Attribute which
	 *                    identifies if the Variable has any historical data. A
	 *                    value of TRUE indicates that the Server is actively
	 *                    collecting data. A value of FALSE indicates the Server is
	 *                    not actively collecting data.
	 */
	public void setHistorizing(String historizing) {
		this.historizing = historizing;
	}
}

/**
 * Variable Node Attributes.<br>
 * -Value<br>
 * -DataType<br>
 * -ValueRank<br>
 * -ArrayDimension<br>
 * -AccessLevel<br>
 * -UserAccessLevel<br>
 * -MinimumSamplingInterval<br>
 * -Historizing
 * 
 * @author Thomas Zöchbauer
 * 
 */
class VariableTypeAttribute extends BaseAttributes {
	/** Variant converted Value */
	private Variant value = null;
	/** String DataType representation */
	private String dataType = null;
	/** String ValueRank representation */
	private String valueRank = null;
	/** String ArrayDimensions representation */
	private String arrayDimensions = null;
	/** String IsAbstract representation */
	private String isAbstract = null;

	/**
	 * VariableType defined Attributes.
	 * 
	 * @param Attributes Attributes Array for the String imported Attributes.
	 */
	public VariableTypeAttribute(Object[] attributes) {
		this.value = (Variant) attributes[0];
		this.dataType = (String) attributes[1];
		this.valueRank = (String) attributes[2];
		this.arrayDimensions = (String) attributes[3];
		this.isAbstract = (String) attributes[4];
	}

	/**
	 * Getter for the Value.
	 * 
	 * @return Value
	 */
	public Variant getValue() {
		return this.value;
	}

	/**
	 * Getter for the DataType.
	 * 
	 * @return DataType
	 */
	public String getDataType() {
		return this.dataType;
	}

	/**
	 * Getter for the ValueRank.
	 * 
	 * @return ValueRank
	 */
	public String getValueRank() {
		return this.valueRank;
	}

	/**
	 * Getter for the ArrayDimension.
	 * 
	 * @return ArrayDimensions
	 */
	public String getArrayDimensions() {
		return this.arrayDimensions;
	}

	/**
	 * Getter for the IsAbstract.
	 * 
	 * @return IsAbstract
	 */
	public String getIsAbstract() {
		return this.isAbstract;
	}

	/**
	 * Setter for the Value Attribute.
	 * 
	 * @param Value The most recent value of the Variable that the server has. Its
	 *              data type is defined by the DataType Attribute. It is the only
	 *              Attribute that does not have a data type associated with it.
	 *              This allows all Variables to have a value defined by the same
	 *              Value Attribute.
	 */
	public void setValue(Variant value) {
		this.value = value;
	}

	/**
	 * Setter for the DataType Attribute.
	 * 
	 * @param DataType NodeId of the DataType definition for the Value Attribute.
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * Setter for the ValueRank Attribute.
	 * 
	 * @param ValueRank This Attribute indicates whether the Value Attribute of the
	 *                  Variable is an array and how many dimensions the array has.
	 *                  It may have the following values:<br>
	 *                  n>1: the Value is an array with the specified number of
	 *                  dimensions. <br>
	 *                  OneDimension (1): The value is an array with one
	 *                  dimension.<br>
	 *                  OneOrMoreDimensions (0): The value is an array with one or
	 *                  more dimensions. <br>
	 *                  Scalar (-1): The value is not an array.<br>
	 *                  Any (-2): The value can be a scalar or an array with any
	 *                  number of dimensions.<br>
	 *                  ScalarOrOneDimension (-3): The value can be a scalar or a
	 *                  one dimensional array.
	 */
	public void setValueRank(String valueRank) {
		this.valueRank = valueRank;
	}

	/**
	 * Setter for the ArrayDimensions Attribute.
	 * 
	 * @param ArrayDimensions This Attribute specifies the length of each dimension
	 *                        for an array value. The Attribute is intended to
	 *                        describe the capability of the Variable, not the
	 *                        current size. The number of elements shall be equal to
	 *                        the value of the ValueRank Attribute. Shall be null if
	 *                        ValueRank <= 0. A value of 0 for an individual
	 *                        dimension indicates that the dimension has a variable
	 *                        length.
	 */
	public void setArrayDimensions(String arrayDimensions) {
		this.arrayDimensions = arrayDimensions;
	}

	/**
	 * Setter for the IsAbstract Attribute.
	 * 
	 * @param IsAbstract A boolean Attribute with the following values:<br>
	 *                   TRUE it is an abstract VariableType, i.e. no Variable of
	 *                   this type shall exist, only of its subtypes.<br>
	 *                   FALSE it is not an abstract VariableType, i.e. Variables of
	 *                   this type can exist.
	 */
	public void setIsAbstract(String isAbstract) {
		this.isAbstract = isAbstract;
	}
}

/**
 * DataType Node Attributes.<br>
 * -IsAbstract
 * 
 * @author Thomas Zöchbauer
 * 
 */
class DataTypeAttribute extends BaseAttributes {
	/** String IsAbstract representation */
	private String isAbstract = null;

	/**
	 * DataType defined Attributes.
	 * 
	 * @param IsAbstract A boolean Attribute with the following values:<br>
	 *                   TRUE it is an abstract DataType. <br>
	 *                   FALSE it is not an abstract DataType.
	 */
	public DataTypeAttribute(Object[] isAbstract) {
		this.isAbstract = (String) isAbstract[0];
	}

	/**
	 * Getter for the IsAbstract.
	 * 
	 * @return IsAbstract
	 */
	public String getIsAbstract() {
		return this.isAbstract;
	}

	/**
	 * Setter for the IsAbstract Attribute.
	 * 
	 * @param IsAbstract A boolean Attribute with the following values:<br>
	 *                   TRUE it is an abstract DataType.<br>
	 *                   FALSE it is not an abstract DataType.
	 */
	public void setIsAbstract(String isAbstract) {
		this.isAbstract = isAbstract;
	}
}

/**
 * Variable Node Attributes.<br>
 * -Executeable<br>
 * -UserExecuteable<br>
 * 
 * @author Thomas Zöchbauer
 * 
 */
class MethodAttribute extends BaseAttributes {
	/** String Executeable representation */
	private String executeable = null;
	/** String UserExecuteable representation */
	private String userExecuteable = null;

	/**
	 * Method defined Attributes.
	 * 
	 * @param Attributes Attributes Array for the String imported Attributes.
	 */
	public MethodAttribute(Object[] attributes) {
		this.executeable = (String) attributes[0];
		this.userExecuteable = (String) attributes[1];
	}

	/**
	 * Getter for the Executeable.
	 * 
	 * @return Executeable
	 */
	public String getExecuteable() {
		return this.executeable;
	}

	/**
	 * Getter for the UserExecuteable.
	 * 
	 * @return UserExecuteable
	 */
	public String getUserExecuteable() {
		return this.userExecuteable;
	}

	/**
	 * Setter for the Executeable Attribute.
	 * 
	 * @param Executeable The Executable Attribute indicates if the Method is
	 *                    currently executable (“False” means not executable, “True”
	 *                    means executable). The Executable Attribute does not take
	 *                    any user access rights into account, i.e. although the
	 *                    Method is executable this may be restricted to a certain
	 *                    user / user group.
	 */
	public void setExecuteable(String executeable) {
		this.executeable = executeable;
	}

	/**
	 * Setter for the UserExecuteable Attribute.
	 * 
	 * @param UserExecuteable The UserExecutable Attribute indicates if the Method
	 *                        is currently executable taking user access rights into
	 *                        account (“False” means not executable, “True” means
	 *                        executable).
	 */
	public void setUserExecuteable(String userExecuteable) {
		this.userExecuteable = userExecuteable;
	}
}

/**
 * Variable Node Attributes.<br>
 * -IsAbstract<br>
 * -Symmetric<br>
 * -InverseName
 * 
 * @author Thomas Zöchbauer
 * 
 */
class ReferenceAttributes extends BaseAttributes {
	/** String IsAbstract representation */
	private String isAbstract = null;
	/** String Symmetric representation */
	private String symmetric = null;
	/** String InverseName representation */
	private LocalizedText inverseName = null;

	/**
	 * Reference defined Attributes.
	 * 
	 * @param Attributes Attributes Array for the String imported Attributes.
	 */
	public ReferenceAttributes(Object[] attributes) {
		this.isAbstract = (String) attributes[0];
		this.symmetric = (String) attributes[1];
		this.inverseName = (LocalizedText) attributes[2];
	}

	/**
	 * Getter for the IsAbstract.
	 * 
	 * @return IsAbstract
	 */
	public String getIsAbstract() {
		return this.isAbstract;
	}

	/**
	 * Getter for the Symmetric.
	 * 
	 * @return Symmetric
	 */
	public String getSymmetric() {
		return this.symmetric;
	}

	/**
	 * Getter for the InverseName.
	 * 
	 * @return InverseName
	 */
	public LocalizedText getInverseName() {
		return this.inverseName;
	}

	/**
	 * Setter for the IsAbstract Attribute.
	 * 
	 * @param IsAbstract A boolean Attribute with the following values: <br>
	 *                   TRUE it is an abstract ReferenceType, i.e. no References of
	 *                   this type shall exist, only of its subtypes. <br>
	 *                   FALSE it is not an abstract ReferenceType, i.e. References
	 *                   of this type can exist.
	 */
	public void setIsAbstract(String isAbstract) {
		this.isAbstract = isAbstract;
	}

	/**
	 * Setter for the Symmetric Attribute.
	 * 
	 * @param Symmetric A boolean Attribute with the following values:<br>
	 *                  TRUE the meaning of the ReferenceType is the same as seen
	 *                  from both the SourceNode and the TargetNode.<br>
	 *                  FALSE the meaning of the ReferenceType as seen from the
	 *                  TargetNode is the inverse of that as seen from the
	 *                  SourceNode.
	 */
	public void setSymmetric(String symmetric) {
		this.symmetric = symmetric;
	}

	/**
	 * Setter for the InverseName Attribute.
	 * 
	 * @param InverseName The inverse name of the Reference, i.e. the meaning of the
	 *                    ReferenceType as seen from the TargetNode.
	 * 
	 */
	public void setInverseName(LocalizedText inverseName) {
		this.inverseName = inverseName;
	}
}

/**
 * Reference Node Attributes. -ReferenceNodeStructure -IsInverse -TargetId
 * 
 * @author Thomas Zöchbauer
 */
class ReferenceNodeStructure {
	/** String ReferenceTypeId representation */
	private String referenceTypeId = null;
	/** String IsInverse representation */
	private String isInverse = null;
	/** String TargetId representation */
	private String targetId = null;

	public ReferenceNodeStructure(String referenceType, String isInverse, String targetId) {
		this.referenceTypeId = referenceType;
		this.isInverse = isInverse;
		this.targetId = targetId;
	}

	public String getReferenceTypeId() {
		return referenceTypeId;
	}

	public void setReferenceTypeId(String referenceTypeId) {
		this.referenceTypeId = referenceTypeId;
	}

	public String getIsInverse() {
		return isInverse;
	}

	public void setIsInverse(String isInverse) {
		this.isInverse = isInverse;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
}
