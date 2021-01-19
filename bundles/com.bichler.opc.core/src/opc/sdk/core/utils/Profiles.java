package opc.sdk.core.utils;

/**
 * Enum to store all transport profiles.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public enum Profiles {
	/**
	 * Communicates with UA TCP, UA Security and UA Binary.
	 */
	UaTcpTransport_DoesNotMatchSpec("http://opcfoundation.org/UA/profiles/transport/uatcp"),
	/**
	 * Communicates with SOAP 1.2, WS Security and UA XML.
	 */
	WsHttpXmlTransport_DoesNotMatchSpec("http://opcfoundation.org/UA/profiles/transport/wsxml"),
	/**
	 * Communicates with SOAP 1.2, WS Security and UA XML or UA Binary.
	 */
	WsHttpXmlOrBinaryTransport_DoesNotMatchSpec("http://opcfoundation.org/UA/profiles/transport/wsxmlorbinary"),
	/**
	 * Communicates with SOAP 1.2, WS Security and UA Binary.
	 */
	WsHttpBinaryTransport_DoesNotMatchSpec("http://opcfoundation.org/UA/profiles/transport/wsbinary"),
	/**
	 * Communicates with UA TCP, UA Security and UA Binary.
	 */
	UaTcpTransport("http://opcfoundation.org/UA-Profile/Transport/uatcp-uasc-uabinary"),
	/**
	 * Communicates with SOAP 1.2, WS Security and UA XML.
	 */
	WsHttpXmlTransport("http://opcfoundation.org/UA-Profile/Transport/soaphttp-wssc-uaxml"),
	/**
	 * Communicates with SOAP 1.2, WS Security and UA XML or UA Binary.
	 */
	WsHttpXmlOrBinaryTransport("http://opcfoundation.org/UA-Profile/Transport/soaphttp-wssc-uaxml-uabinary"),
	/**
	 * Communicates with SOAP 1.2, WS Security and UA Binary.
	 */
	UaHttpBinaryTransport("http://opcfoundation.org/UA-Profile/Transport/soaphttp-uasc-uabinary"),
	/**
	 * Communicates with SOAP 1.1, HTTPS and UA XML or UA Binary.
	 */
	HttpsXmlOrBinaryTransport("http://opcfoundation.org/UA-Profile/Transport/soaphttps-uaxml-uabinary");

	private String profiles = null;

	private Profiles(String profiles) {
		this.profiles = profiles;
	}

	/**
	 * Converts the URI to a URI that can be used for comparison.
	 * 
	 * @param profileUri
	 * @return
	 */
	public static String NormalizeUri(String profileUri) {
		if (profileUri == null || profileUri.isEmpty()) {
			return profileUri;
		}
		if (profileUri.equals(Profiles.UaTcpTransport_DoesNotMatchSpec.name())) {
			return Profiles.UaTcpTransport.getProfileURI();
		}
		if (profileUri.equals(Profiles.WsHttpBinaryTransport_DoesNotMatchSpec.name())) {
			return Profiles.UaHttpBinaryTransport.getProfileURI();
		}
		if (profileUri.equals(Profiles.WsHttpXmlOrBinaryTransport_DoesNotMatchSpec.name())) {
			return Profiles.WsHttpXmlOrBinaryTransport.getProfileURI();
		}
		if (profileUri.equals(Profiles.WsHttpXmlTransport_DoesNotMatchSpec.name())) {
			return Profiles.WsHttpXmlTransport.getProfileURI();
		}
		return profileUri;
	}

	private String getProfileURI() {
		return this.profiles;
	}
}
