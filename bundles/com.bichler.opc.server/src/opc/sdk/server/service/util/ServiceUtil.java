package opc.sdk.server.service.util;

import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.StatusCodes;

public class ServiceUtil {
	public static boolean isSecurityError(StatusCode error) {
		if (error.getValueAsIntBits() == StatusCodes.Bad_UserSignatureInvalid.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_UserAccessDenied.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_SecurityPolicyRejected.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_SecurityModeRejected.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_SecurityChecksFailed.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_SecureChannelTokenUnknown.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_SecureChannelIdInvalid.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_NoValidCertificates.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_IdentityTokenInvalid.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_IdentityTokenRejected.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_IdentityChangeNotSupported.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_CertificateUseNotAllowed.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_CertificateUriInvalid.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_CertificateUntrusted.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_CertificateIssuerTimeInvalid.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_CertificateRevoked.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_CertificateRevocationUnknown.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_CertificateInvalid.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_CertificateHostNameInvalid.getValue()
				|| error.getValueAsIntBits() == StatusCodes.Bad_ApplicationSignatureInvalid.getValue()) {
			return true;
		}
		return false;
	}
}
