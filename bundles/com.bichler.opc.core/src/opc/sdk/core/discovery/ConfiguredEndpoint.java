package opc.sdk.core.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.application.BinaryEncodingSupport;
import opc.sdk.core.utils.Profiles;
import opc.sdk.core.utils.Utils;

import org.opcfoundation.ua.core.EndpointConfiguration;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.transport.UriUtil;

/**
 * Endpoint to configure.
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class ConfiguredEndpoint {
	private EndpointConfiguration configuration = null;
	private EndpointDescription description = null;
	private BinaryEncodingSupport binaryEncodingSupport = null;
	private boolean updateBeforeConnect = false;
	private int selectUserTokenPolicyIndex = 0;

	/**
	 * The default constructor
	 * 
	 * @param configuredEndpoints
	 * @param endpoint
	 * @param configuration2
	 */
	public ConfiguredEndpoint(ConfiguredEndpoints collection, EndpointDescription description,
			EndpointConfiguration configuration) {
		if (description == null) {
			throw new NullPointerException("description");
		}
		this.description = description;
		this.updateBeforeConnect = true;
		// ensure a default configuration
		if (configuration == null) {
			if (collection != null) {
				configuration = collection.getDefaultConfiguration();
			} else {
				configuration = EndpointConfiguration.defaults();
			}
		}
		update(configuration);
	}

	public EndpointConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(EndpointConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Sets private members to default values
	 *
	 * private void initialize() { this.collection = null; this.description = new
	 * EndpointDescription(); this.configuration = null; this.updateBeforeConnect =
	 * true; this.binaryEncodingSupport = BinaryEncodingSupport.Optional;
	 * this.setSelectUserTokenPolicyIndex(0); }
	 */
	private BinaryEncodingSupport checkBinaryEncodingSupport() {
		if ((this.description.getEndpointUrl() != null && !this.description.getEndpointUrl().isEmpty())
				&& this.description.getEndpointUrl().startsWith(UriUtil.SCHEME_OPCTCP)) {
			return BinaryEncodingSupport.REQUIRED;
		}
		String transportProfileUri = Profiles.NormalizeUri(this.description.getTransportProfileUri());
		if (Profiles.WsHttpXmlOrBinaryTransport.name().compareTo(transportProfileUri) == 0) {
			return BinaryEncodingSupport.OPTIONAL;
		}
		if (Profiles.UaHttpBinaryTransport.name().compareTo(transportProfileUri) == 0) {
			return BinaryEncodingSupport.REQUIRED;
		}
		return BinaryEncodingSupport.NONE;
	}

	public EndpointDescription getDescription() {
		return this.description;
	}

	public boolean getUpdateBeforeConnect() {
		return this.updateBeforeConnect;
	}

	/**
	 * Returns a discovery url that can be used to update the endpoint description
	 * 
	 * @param endpointUrl
	 * @return
	 */
	public URI getDiscoveryUrl(URI endpointUrl) {
		// update the endpoint description
		if (endpointUrl != null) {
			this.description.setEndpointUrl(endpointUrl.toString());
		} else {
			endpointUrl = Utils.parseUri(this.description.getEndpointUrl());
		}
		// get the know discovery URLs
		String[] discoveryUrls = null;
		if (this.description.getServer() != null) {
			discoveryUrls = this.description.getServer().getDiscoveryUrls();
		}
		// attempt to construct a discovery url by appending 'discovery' to the
		// endpoint.
		if (discoveryUrls == null || discoveryUrls.length == 0) {
			if (!UriUtil.SCHEME_OPCTCP.equals(endpointUrl.getScheme())) {
				try {
					return new URI(endpointUrl.resolve("/discovery").toString());
				} catch (URISyntaxException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
			} else {
				return endpointUrl;
			}
		}
		// choose the URL that uses the same protocol if one exists
		for (String discoveryUrl : discoveryUrls) {
			if (discoveryUrl.startsWith(endpointUrl.getScheme())) {
				return Utils.parseUri(discoveryUrl);
			}
		}
		// return the first in the list
		if (discoveryUrls != null && discoveryUrls.length > 0)
			return Utils.parseUri(discoveryUrls[0]);
		return null;
	}

	public URI getEndpointUrl() {
		if (this.description.getEndpointUrl() == null || this.description.getEndpointUrl().isEmpty()) {
			return null;
		}
		return Utils.parseUri(this.description.getEndpointUrl());
	}

	/**
	 * Updates the endpoint description
	 * 
	 * @param match
	 */
	public void update(EndpointDescription description) {
		if (description == null)
			throw new IllegalArgumentException("EndpointDescription is Null!");
		this.description = description.clone();
		// normalize transport profile uri.
		if (this.description.getTransportProfileUri() != null) {
			this.description.setTransportProfileUri(Profiles.NormalizeUri(this.description.getTransportProfileUri()));
		}
	}

	/**
	 * Updates the endpoint configuration
	 * 
	 * @param configuration2
	 */
	public void update(EndpointConfiguration configuration) {
		if (configuration == null) {
			throw new NullPointerException("Endpoint Configuration");
		}
		this.configuration = configuration.clone();
		BinaryEncodingSupport locbinaryEncodingSupport = checkBinaryEncodingSupport();
		// check if the configuration restricts the encoding if the endpoint
		// supports both.
		if (BinaryEncodingSupport.OPTIONAL.equals(locbinaryEncodingSupport)) {
			locbinaryEncodingSupport = this.binaryEncodingSupport;
		}
		if (BinaryEncodingSupport.NONE.equals(locbinaryEncodingSupport)) {
			this.configuration.setUseBinaryEncoding(false);
		}
		if (BinaryEncodingSupport.REQUIRED.equals(locbinaryEncodingSupport)) {
			this.configuration.setUseBinaryEncoding(true);
		}
	}

	public void setUpdateBeforeConnect(boolean updateBeforeConnect) {
		this.updateBeforeConnect = updateBeforeConnect;
	}

	public int getSelectUserTokenPolicyIndex() {
		return selectUserTokenPolicyIndex;
	}

	public void setSelectUserTokenPolicyIndex(int selectUserTokenPolicyIndex) {
		this.selectUserTokenPolicyIndex = selectUserTokenPolicyIndex;
	}
}
