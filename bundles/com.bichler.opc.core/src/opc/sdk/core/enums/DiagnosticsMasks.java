package opc.sdk.core.enums;

import org.opcfoundation.ua.builtintypes.UnsignedInteger;

/**
 * The DiagnosticsMasks enumeration.
 * 
 * @author Thomas Z&ouml;bauer
 */
public enum DiagnosticsMasks {
	/**
	 * ServiceSymbolicId = 0,
	 */
	None(0),
	/**
	 * ServiceSymbolicId = 1,
	 */
	ServiceSymbolicId(1),
	/**
	 * ServiceLocalizedText = 2,
	 */
	ServiceLocalizedText(2),
	/**
	 * ServiceAdditionalInfo = 4,
	 */
	ServiceAdditionalInfo(4),
	/**
	 * ServiceInnerStatusCode = 8,
	 */
	ServiceInnerStatusCode(8),
	/**
	 * ServiceInnerDiagnostics = 16,
	 */
	ServiceInnerDiagnostics(16),
	/**
	 * ServiceSymbolicIdAndText = 3,
	 */
	ServiceSymbolicIdAndTex(3),
	/**
	 * ServiceNoInnerStatus = 15,
	 */
	ServiceNoInnerStatus(15),
	/**
	 * ServiceAll = 31,
	 */
	ServiceAll(31),
	/**
	 * OperationSymbolicId = 32,
	 */
	OperationSymbolicId(32),
	/**
	 * OperationLocalizedText = 64,
	 */
	OperationLocalizedText(64),
	/**
	 * OperationAdditionalInfo = 128,
	 */
	OperationAdditionalInfo(128),
	/**
	 * OperationInnerStatusCode = 256,
	 */
	OperationInnerStatusCode(256),
	/**
	 * OperationInnerDiagnostics = 512,
	 */
	OperationInnerDiagnostics(512),
	/**
	 * OperationSymbolicIdAndText = 96,
	 */
	OperationSymbolicIdAndText(96),
	/**
	 * OperationNoInnerStatus = 224,
	 */
	OperationNoInnerStatus(224),
	/**
	 * OperationAll = 992,
	 */
	OperationAll(992),
	/**
	 * SymbolicId = 33,
	 */
	SymbolicId(33),
	/**
	 * LocalizedText = 66,
	 */
	LocalizedText(66),
	/**
	 * AdditionalInfo = 132,
	 */
	AdditionalInfo(132),
	/**
	 * InnerStatusCode = 264,
	 */
	InnerStatusCode(264),
	/**
	 * InnerDiagnostics = 528,
	 */
	InnerDiagnostics(528),
	/**
	 * SymbolicIdAndText = 99,
	 */
	SymbolicIdAndText(99),
	/**
	 * NoInnerStatus = 239,
	 */
	NoInnerStatus(239),
	/**
	 * All = 1023
	 */
	All(1023);

	private UnsignedInteger diagnosticMask;

	DiagnosticsMasks(int value) {
		this.diagnosticMask = new UnsignedInteger(value);
	}

	public UnsignedInteger getDiagnosticMask() {
		return this.diagnosticMask;
	}

	public static DiagnosticsMasks valueOf(Number mask) {
		for (DiagnosticsMasks value : values()) {
			if (value.getDiagnosticMask().compareTo(mask) == 0) {
				return value;
			}
		}
		return DiagnosticsMasks.None;
	}
}
