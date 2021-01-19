package opc.client.application;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.transport.UriUtil;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.EndpointUtil;

import opc.client.util.IPValidater;
import opc.sdk.core.discovery.ConfiguredEndpoint;
import opc.sdk.core.enums.RequestType;
import opc.sdk.core.utils.Utils;

/**
 * An object used by clients to access a UA discovery service.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class UADiscoveryClient {
	public static final String ID = "opc.client.application.UADiscoveryClient";
	/** Instance with the Service */
	private Client discoverClientInstance = null;
	private static final String UNKNOWN = "[Unknown]";
	private static final String NULLURI = "[NULLURI]";

	/** logging handler instance from the client framework */
	/**
	 * Discovery Client
	 * 
	 * @param uri
	 * @param logHandler
	 */
	public UADiscoveryClient() {
		this.discoverClientInstance = new Client(new Application());
	}

	/**
	 * Discovery Client
	 * 
	 * @param uri
	 * @param logHandler
	 */
	public UADiscoveryClient(Application application) {
		this.discoverClientInstance = new Client(application);
	}

	/**
	 * Filter the given Endpoints with a secure mode.
	 * 
	 * @param Endpoints    Endpoints to filter.
	 * @param SecurityMode OPC UA server security mode
	 * 
	 * @return Filtered Endpoints with the security mode.
	 */
	public EndpointDescription[] filterEndpoints(EndpointDescription[] endpoints, SecurityMode securityMode) {
		EndpointDescription[] endp;
		if (endpoints == null || endpoints.length == 0) {
			throw new IllegalArgumentException("No Endpoints to filter!");
		}
		MessageSecurityMode message = securityMode.getMessageSecurityMode();
		endp = EndpointUtil.selectByMessageSecurityMode(endpoints, message);
		return endp;
	}

	/**
	 * FindServer-Service.
	 * 
	 * This Service returns the Servers known to an OPC UA server or OPC UA
	 * discovery server.
	 * 
	 * @param ServerUris Uris to find OPC UA servers from.
	 * 
	 * @return All discovered server applications.
	 * 
	 * @throws ServiceResultException
	 */
	public Map<String, ApplicationDescription[]> findServers(String... serverUris) throws ServiceResultException {
		Map<String, ApplicationDescription[]> servers = new HashMap<>();
		for (String serverUri : serverUris) {
			if (servers.containsKey(serverUri)) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Double discovered servers with uri {1}! Skipping the last server!",
						new String[] { RequestType.FindServers.name(), serverUri });
			} else {
				ApplicationDescription[] discoverdServer = findServer(serverUri);
				if (discoverdServer != null) {
					servers.put(serverUri, discoverdServer);
				}
			}
		}
		return servers;
	}

	/**
	 * FindServer-Service.
	 * 
	 * This Service returns the Servers known to an OPC UA server or OPC UA
	 * discovery server.
	 * 
	 * @param ServerUri Uri to find an OPC UA server from.
	 * 
	 * @return All discovered server applications.
	 * 
	 * @throws ServiceResultException
	 */
	public ApplicationDescription[] findServer(String serverUri) throws ServiceResultException {
		if (!validUri(serverUri)) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_ServerUriInvalid,
					"ServerUri " + serverUri + " is invalid!");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.FindServers.name(), sre);
			return new ApplicationDescription[0];
		}
		ApplicationDescription[] discoverdServer = null;
		try {
			discoverdServer = this.discoverClientInstance.discoverApplications(serverUri);
		} catch (ServiceResultException sre) {
			// CTT 6.1-001 - 008
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.FindServers + " " + sre.getMessage());
			throw sre;
		}
		if (discoverdServer != null) {
			discoverdServer = validateServers(discoverdServer, serverUri);
		}
		Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} FindServers response with {1} found servers!",
				new String[] { RequestType.FindServers.name(),
						discoverdServer != null ? Integer.toString(discoverdServer.length) : "0" });
		return discoverdServer;
	}

	public EndpointDescription[] getEndpoints(String endpointUri) throws ServiceResultException {
		return getEndpoints(true, endpointUri);
	}

	public Map<String, EndpointDescription[]> getEndpoints(String... endpointUris) throws ServiceResultException {
		return getEndpoints(true, endpointUris);
	}

	/**
	 * GetEndpoint-Service.
	 * 
	 * This Service returns the Endpoints supported by a Server and all of the
	 * configuration inform required to establish a SecureChannel and a Session.
	 * 
	 * @param EndpointUri An uri of an endpoint to discover.
	 * 
	 * @return All available Endpoints from the given uri.
	 * @throws ServiceResultException
	 */
	public EndpointDescription[] getEndpoints(boolean onlyValid, String endpointUri) throws ServiceResultException {
		return getEndpoints(endpointUri, onlyValid, true);
	}

	/**
	 * GetEndpoint-Service.
	 * 
	 * This Service returns the Endpoints supported by a Server and all of the
	 * configuration inform required to establish a SecureChannel and a Session.
	 * 
	 * @param EndpointUris Endpoint uris to discover.
	 * @return all available Endpoints for the uris return in a <Uri,
	 *         EndpointDescription> Pair
	 * @throws ServiceResultException
	 */
	public Map<String, EndpointDescription[]> getEndpoints(boolean onlyValid, String... endpointUris)
			throws ServiceResultException {
		Map<String, EndpointDescription[]> endpoints = new HashMap<>();
		for (String endpointUri : endpointUris) {
			if (endpoints.containsKey(endpointUri)) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Double discovered endpoint with uri {1}! Skipping the last endpoint!",
						new String[] { RequestType.GetEndpoints.name(), endpointUri });
			} else {
				EndpointDescription[] discoverdEndpoints = getEndpoints(endpointUri, onlyValid, true);
				if (discoverdEndpoints != null && discoverdEndpoints.length > 0) {
					endpoints.put(endpointUri, discoverdEndpoints);
				}
			}
		}
		// CTT 6.2-40
		if (!endpoints.isEmpty()) {
			List<EndpointDescription> description = new ArrayList<>();
			for (EndpointDescription[] endpoint2select : endpoints.values()) {
				EndpointDescription e = null;
				try {
					e = EndpointUtil.select(endpoint2select);
					description.add(e);
				} catch (ServiceResultException noSecureEndpoint) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, noSecureEndpoint);
				}
			}
			if (!description.isEmpty()) {
				selectRecommendedEndpoint(description.toArray(new EndpointDescription[0]));
			}
		}
		return endpoints;
	}

	/**
	 * Selects the Endpoint with the Highest-Security-Level. Do not return Security
	 * NONE Endpoints.
	 * 
	 * @param Endpoints Endpoints to filter.
	 * @return EndpointDescription with the Highest Security Level from the
	 *         Endpoints parameter, or NULL.
	 * @throws ServiceResultException
	 */
	public EndpointDescription selectEndpointWithHighestSecurity(EndpointDescription[] endpoints)
			throws ServiceResultException {
		return EndpointUtil.select(endpoints);
	}

	/**
	 * Selects an Endpoint with NO - SECURITY.
	 * 
	 * @param Endpoints Endpoints to filter.
	 * @return EndpointDescription with the NO - Security Level from the Endpoints
	 *         parameter, or NULL.
	 */
	public EndpointDescription selectEndpointNone(EndpointDescription[] endpoints) {
		EndpointDescription[] endpointsTmp;
		endpointsTmp = EndpointUtil.selectBySecurityPolicy(endpoints, SecurityPolicy.NONE);
		endpointsTmp = EndpointUtil.selectByMessageSecurityMode(endpointsTmp, MessageSecurityMode.None);
		if (endpointsTmp.length > 0) {
			return endpointsTmp[0];
		}
		return new EndpointDescription();
	}

	/**
	 * Selects a recommended Endpoint usually the Endpoint with the
	 * Highest-Security-Level. Do not return Security NONE Endpoints.
	 * 
	 * @param Endpoints Endpoints to filter.
	 * @return EndpointDescription with the Highest Security Level from the
	 *         Endpoints parameter, or NULL.
	 */
	public EndpointDescription selectRecommendedEndpoint(EndpointDescription[] endpoints) {
		try {
			EndpointDescription endpoint = EndpointUtil.select(endpoints);
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"{0} Endpoint {1} with security policy {2}, secure level {3} and mode {4} is the recommended endpoint to connect!",
					new Object[] { RequestType.GetEndpoints.name(), endpoint.getEndpointUrl(),
							endpoint.getSecurityPolicyUri(), endpoint.getSecurityLevel(),
							endpoint.getSecurityMode().name() });
			return endpoint;
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * Updates an endpoint with information from the server's discovery endpoint
	 * 
	 * @param endpoint
	 * @throws ServiceResultException
	 */
	public void updateFromServer(ConfiguredEndpoint endpoint) throws ServiceResultException {
		updateFromServer(endpoint.getEndpointUrl(), endpoint.getDescription().getSecurityMode(),
				endpoint.getDescription().getSecurityPolicyUri(), endpoint);
	}

	/**
	 * Updates an endpoint with information from the server's discovery endpoint
	 * 
	 * @param server
	 * @param endpointUrl
	 * @param securityMode
	 * @param securityPolicyUri
	 * 
	 * @throws ServiceResultException
	 */
	public void updateFromServer(URI endpointUrl, MessageSecurityMode securityMode, String securityPolicyUri,
			ConfiguredEndpoint endpoint) throws ServiceResultException {
		URI discoveryUrl = endpoint.getDiscoveryUrl(endpointUrl);
		// get the Endpoints
		EndpointDescription[] endpoints = getEndpoints(false, discoveryUrl.toASCIIString());
		if (endpoints == null || endpoints.length == 0) {
			return;
		}
		// find list of mathcing endpoints
		List<EndpointDescription> matches = new ArrayList<>();
		// first pass - match on the requested securirty parameters
		for (EndpointDescription description : endpoints) {
			// check for match on security policy
			if (securityPolicyUri != null && !securityPolicyUri.isEmpty()
					&& !securityPolicyUri.equals(description.getSecurityPolicyUri())) {
				continue;
			}
			// check for match on security mode
			if (!MessageSecurityMode.Invalid.equals(securityMode)
					&& securityMode.equals(description.getSecurityMode())) {
				continue;
			}
			matches.add(description);
		}
		// no matches (security parameters may have changed)
		if (matches.isEmpty()) {
			matches = Arrays.asList(endpoints);
		}
		// check if list has to be narrowed down further
		if (!matches.isEmpty()) {
			endpoints = matches.toArray(new EndpointDescription[matches.size()]);
			matches = new ArrayList<>();
			// second pass - match on the url scheme
			for (EndpointDescription description : endpoints) {
				// parse the endpoint url
				URI sessionUrl = Utils.parseUri(description.getEndpointUrl());
				if (sessionUrl == null) {
					continue;
				}
				// check for matching protocol
				if (!sessionUrl.getScheme().equals(endpointUrl.getScheme())) {
					continue;
				}
				matches.add(description);
			}
		}
		// no matches protocol may not be supported
		if (matches.isEmpty()) {
			matches = Arrays.asList(endpoints);
		}
		// choose first in the list by default
		EndpointDescription match = matches.get(0);
		// check if list has to be narrowed down further
		if (!matches.isEmpty()) {
			// third pass - match based on security level
			for (EndpointDescription description : matches) {
				if (description.getSecurityMode().getValue() > match.getSecurityMode().getValue()) {
					match = description;
				}
			}
		}
		URI matchUrl = Utils.parseUri(match.getEndpointUrl());
		if (matchUrl == null || !discoveryUrl.getHost().equalsIgnoreCase(matchUrl.getHost())) {
			/* TODO: c# -> ConfiguredEndpoints -> line 1301 */
		}
		endpoint.update(match);
	}

	/**
	 * Returns the locales from the discovery client application.
	 * 
	 * @return Language locales from the discovery client.
	 */
	public Locale[] getLocales() {
		return this.discoverClientInstance.getApplication().getLocales();
	}

	public void addLocales(Locale... locale) {
		for (Locale l : locale) {
			this.discoverClientInstance.getApplication().addLocale(l);
		}
	}

	private EndpointDescription[] getEndpoints(String endpointUri, boolean onlyValid, boolean isInternal)
			throws ServiceResultException {
		if (!validUri(endpointUri)) {
			ServiceResultException sre = new ServiceResultException(StatusCodes.Bad_ServerUriInvalid,
					"EndpointUri " + endpointUri + " is invalid!");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.GetEndpoints.name(), sre);
			return new EndpointDescription[0];
		}
		EndpointDescription[] discoverdEndpoints = null;
		try {
			// this.discoverClientInstance.getApplicatioOpcTcpSettings().setHandshakeTimeout(1000);
			discoverdEndpoints = this.discoverClientInstance.discoverEndpoints(endpointUri);
			// CTT 6.2-009
			if (discoverdEndpoints == null || discoverdEndpoints.length == 0) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "{0} No endpoints to discover for uri {1}",
						new String[] { RequestType.GetEndpoints.name(), endpointUri });
				return new EndpointDescription[0];
			}
			String name = RequestType.GetEndpoints.name();
			Logger.getLogger(getClass().getName()).info(name);
		} catch (ServiceResultException sre) {
			// CTT 6.2-001-008
			// Logger.getLogger(getClass().getName()).log(Level.SEVERE,
			// "GetEndpoints failed, due to following error: " + sre.getMessage());
			throw sre;
		}
		// if a server is behind a firewall it may return URLs that are not
		// accessible to the client.
		// This problem can be avoided by assuming that the domain in the URL
		// used to call
		// GetEndpoints can be used to access any of the endpoints. This code
		// makes that conversion.
		// Note that the conversion only makes sense if discovery uses the same
		// protocol as the endpoint.
		URI uri = null;
		try {
			uri = new URI(endpointUri);
		} catch (URISyntaxException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			throw new ServiceResultException(StatusCodes.Bad_UnexpectedError);
		}
		for (EndpointDescription endpoint : discoverdEndpoints) {
			// parse the endpointUrl
			URI epUri = null;
			try {
				if (endpoint.getEndpointUrl() == null) {
					throw new URISyntaxException("nulluri", "nullpointer");
				}
				epUri = new URI(endpoint.getEndpointUrl());
				if (uri.getScheme().equalsIgnoreCase(epUri.getScheme())) {
					String host = epUri.getHost();
					int port = epUri.getPort();
					URI epUri2 = new URI(epUri.getScheme(), epUri.getUserInfo(), host, port, epUri.getPath(),
							epUri.getQuery(), epUri.getFragment());
					endpoint.setEndpointUrl(epUri2.toString());
				}
			} catch (URISyntaxException e) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						RequestType.GetEndpoints.name() + " Could not convert the uri "
								+ (epUri == null ? "[EMPTYURI]" : epUri.toASCIIString())
								+ " over the firewall! If the server is behind a firewall, it is maybe not accessable!",
						e);
				continue;
			}
		}
		// validate
		discoverdEndpoints = validateEndpoints(discoverdEndpoints, endpointUri, onlyValid);
		if (!isInternal) {
			selectRecommendedEndpoint(discoverdEndpoints);
		}
		return discoverdEndpoints;
	}

	private boolean validUri(String uri) {
		// serveruri
		try {
			if (uri == null || uri.isEmpty()) {
				return false;
			}
			UriUtil.getTransportProtocol(uri);
			UriUtil.getSocketAddress(uri);
		} catch (ServiceResultException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			return false;
		}
		return true;
	}

	private EndpointDescription[] validateEndpoints(EndpointDescription[] discoverdEndpoints, String endpointUri,
			boolean onlyValid) {
		String tmpEndpointUri = endpointUri;
		if (discoverdEndpoints == null) {
			return new EndpointDescription[0];
		}
		String scheme = UriUtil.SCHEME_OPCTCP;
		boolean isDiscovery = tmpEndpointUri.contains("/discovery");
		if (isDiscovery) {
			scheme = UriUtil.SCHEME_HTTP;
		}
		// check security
		List<EndpointDescription> validEndpoints = new ArrayList<>();
		for (int i = 0; i < discoverdEndpoints.length; i++) {
			EndpointDescription endpoint = discoverdEndpoints[i];
			boolean isValid = true;
			if (endpoint.getEndpointUrl() == null || !(endpoint.getEndpointUrl().toLowerCase().startsWith(scheme))) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} The endpoint uri {1} does not match the scheme {2} and is rejected!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl(), scheme });
				isValid = false;
			}
			// CTT 6.2- 014-020
			if (endpoint.getServer() == null || ((endpoint.getServer().getApplicationName() == null
					|| endpoint.getServer().getApplicationName().getText() == null
					|| endpoint.getServer().getApplicationName().getText().isEmpty())
					&& endpoint.getServer().getApplicationUri() == null && endpoint.getServer().getProductUri() == null
					&& (endpoint.getServer().getDiscoveryUrls() == null
							|| endpoint.getServer().getDiscoveryUrls().length == 0))) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Endpoint {1} has no description of a server application and is rejected!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl() });
				isValid = false;
			} else if (endpoint.getServer().getApplicationUri() == null
					|| endpoint.getServer().getApplicationUri().isEmpty()) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Endpoint {1} has no valid server application uri and is rejected!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl() });
				isValid = false;
			} else if (endpoint.getServer().getDiscoveryUrls() == null
					|| endpoint.getServer().getDiscoveryUrls().length == 0) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Endpoint {1} has no valid discovery urls and is rejected!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl() });
				isValid = false;
			} else if (ApplicationType.Client.equals(endpoint.getServer().getApplicationType())
					&& (endpoint.getServer().getGatewayServerUri() != null
							&& !endpoint.getServer().getGatewayServerUri().isEmpty())) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Could not use an endpoint {1} from a application of type {2}",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl(),
								endpoint.getServer().getApplicationType().name() });
				isValid = false;
			} else if (ApplicationType.Client.equals(endpoint.getServer().getApplicationType())
					&& (endpoint.getServer().getDiscoveryProfileUri() != null
							&& !endpoint.getServer().getDiscoveryProfileUri().isEmpty())) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Could not use an endpoint {1} from a application of type {2}, because of its invalid DISCOVERYPROFILEURI!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl(),
								endpoint.getServer().getApplicationType().name() });
				isValid = false;
			} else if (ApplicationType.Client.equals(endpoint.getServer().getApplicationType())
					&& (endpoint.getServer().getDiscoveryUrls() != null
							&& endpoint.getServer().getDiscoveryUrls().length > 0)) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Could not use an endpoint {1} from a application of type {2}, because of its invalid DISCOVERYURLS!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl(),
								endpoint.getServer().getApplicationType().name() });
				isValid = false;
			} else if ((ApplicationType.Client.compareTo(endpoint.getServer().getApplicationType()) != 0)
					&& (endpoint.getServer().getDiscoveryUrls() == null
							|| endpoint.getServer().getDiscoveryUrls().length == 0)) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Endpoint {1} has no valid endpoints to discover and is rejected!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl() });
				isValid = false;
			}
			// CTT 6.2-028
			if (MessageSecurityMode.Invalid.compareTo(endpoint.getSecurityMode()) == 0) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Endpoint {1} has an invalid security mode and is rejected!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl() });
				isValid = false;
			}
			// CTT 6.2-029-032
			try {
				if (endpoint.getSecurityPolicyUri() == null || endpoint.getSecurityPolicyUri().isEmpty()
						|| SecurityPolicy.getSecurityPolicy(endpoint.getSecurityPolicyUri()) == null) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} Endpoint {1} has an invalid security policy uri and is rejected!",
							new String[] { RequestType.GetEndpoints.name(),
									endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl() });
					isValid = false;
				}
			} catch (ServiceResultException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, RequestType.GetEndpoints.name() + " Endpoint "
						+ (endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl())
						+ " has an invalid security policy " + endpoint.getSecurityPolicyUri() + " and is rejected!",
						e);
				isValid = false;
			}
			// CTT 6.2- 035-038
			if (endpoint.getTransportProfileUri() == null || endpoint.getTransportProfileUri().isEmpty()) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} Endpoint {1} has an empty transportprofile uri and is rejected!",
						new String[] { RequestType.GetEndpoints.name(),
								endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl() });
				isValid = false;
			} else if (endpoint.getTransportProfileUri() != null) {
				// invalid
				String tpUri = endpoint.getTransportProfileUri();
				try {
					/** URI u = */
					new URI(tpUri);
				} catch (URISyntaxException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							RequestType.GetEndpoints.name() + " Endpoint "
									+ (endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl())
									+ " has an invalid transportprofile " + tpUri + " uri and is rejected!",
							e);
					isValid = false;
				}
			}
			// CTT 6.2- 033-034
			if (endpoint.getUserIdentityTokens() != null && endpoint.getUserIdentityTokens().length > 0) {
				for (UserTokenPolicy token : endpoint.getUserIdentityTokens()) {
					if (token.getTokenType() != null && UserTokenType.IssuedToken.compareTo(token.getTokenType()) == 0
							&& (token.getIssuedTokenType() == null || token.getIssuedTokenType().isEmpty())) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"{0} Endpoint {1} has invalid user issued token and is rejected!",
								new String[] { RequestType.GetEndpoints.name(),
										endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl() });
						isValid = false;
					}
					if (token.getTokenType() != null && UserTokenType.IssuedToken.compareTo(token.getTokenType()) != 0
							&& (token.getIssuedTokenType() != null && !token.getIssuedTokenType().isEmpty())) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"{0}: Endpoint {1} has an user token with an issuedtoken type entry but is not a type of {2}!",
								new String[] { RequestType.GetEndpoints.name(),
										endpoint.getEndpointUrl() == null ? NULLURI : endpoint.getEndpointUrl(),
										UserTokenType.IssuedToken.name() });
					}
				}
			}
			// CTT 6.2-039
			if (endpoint.getSecurityLevel() == null || UnsignedByte.ZERO.compareTo(endpoint.getSecurityLevel()) == 0) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING,
						"{0} Endpoint {1} security {2} using an insecure {3} security level!",
						new Object[] { RequestType.GetEndpoints.name(), endpoint.getEndpointUrl(),
								endpoint.getSecurityPolicyUri(), endpoint.getSecurityLevel() });
			}
			if (!isValid && onlyValid) {
				continue;
			}
			isValid = true;
			// CTT 6.2-10
			for (EndpointDescription validEndpoint : validEndpoints) {
				/** check duplicate endpoints */
				if ((endpoint.getEndpointUrl() != null
						&& validEndpoint.getEndpointUrl().equals(endpoint.getEndpointUrl()))
						&& (endpoint.getSecurityLevel() != null
								&& validEndpoint.getSecurityLevel().equals(endpoint.getSecurityLevel()))
						&& (endpoint.getSecurityMode() != null
								&& endpoint.getSecurityMode() == validEndpoint.getSecurityMode())
						&& (endpoint.getSecurityPolicyUri() != null
								&& endpoint.getSecurityPolicyUri().equals(validEndpoint.getSecurityPolicyUri()))
						&& (endpoint.getServer() != null && endpoint.getServer().getApplicationUri()
								.equals(validEndpoint.getServer().getApplicationUri()))
						&& (endpoint.getTransportProfileUri() != null
								&& endpoint.getTransportProfileUri().equals(validEndpoint.getTransportProfileUri()))
						&& (endpoint.getUserIdentityTokens() != null && endpoint
								.getUserIdentityTokens().length == validEndpoint.getUserIdentityTokens().length)) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} Duplicated endpoints {1} returned from the discovery service! Skipping the endpoint from server {2}!",
							new String[] { RequestType.GetEndpoints.name(), validEndpoint.getEndpointUrl(),
									validEndpoint.getServer().getApplicationUri() });
					isValid = false;
					break;
				}
			}
			if (isValid) {
				validEndpoints.add(endpoint);
			}
		}
		return validEndpoints.toArray(new EndpointDescription[0]);
	}

	private ApplicationDescription[] validateServers(ApplicationDescription[] discoverdServer, String serverUri) {
		List<ApplicationDescription> descriptions = new ArrayList<>();
		for (ApplicationDescription description : discoverdServer) {
			// CTT 6.1- 018-019
			boolean isValid = false;
			// validate application name
			if (description.getApplicationName() != null) {
				// CHECK APPLICATION LOCALE
				Locale locale2check = description.getApplicationName().getLocale();
				if ((locale2check == null || LocalizedText.NO_LOCALE.equals(locale2check))
						&& description.getApplicationName().getText() != null
						&& !description.getApplicationName().getText().isEmpty()) {
					isValid = true;
				}
				if (!isValid) {
					Locale[] locales = this.discoverClientInstance.getApplication().getLocales();
					for (Locale l : locales) {
						if (l.equals(locale2check)) {
							isValid = true;
							break;
						}
					}
				}
			}
			if (!isValid) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} The server {1} has an invalid application name and/or locale {2}!",
						new Object[] { RequestType.FindServers.name(), description.getApplicationUri(),
								description.getApplicationName() });
			}
			// CTT 6.1-023
			if ((ApplicationType.Server.compareTo(description.getApplicationType()) == 0
					|| ApplicationType.ClientAndServer.compareTo(description.getApplicationType()) == 0)
					&& (description.getDiscoveryUrls() == null || description.getDiscoveryUrls().length == 0)) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} The server {1}, [{2} has no urls to discover!",
						new Object[] { RequestType.GetEndpoints.name(), description.getApplicationUri(),
								description.getApplicationName() });
			}
			// CTT 6.1-022
			if (ApplicationType.Client.compareTo(description.getApplicationType()) == 0) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} The server {1}, {2} is a CLIENT application type and is rejected!",
						new Object[] { RequestType.GetEndpoints.name(), description.getApplicationUri(),
								description.getApplicationName() });
				continue;
			}
			// CTT 6.1-024
			if (ApplicationType.DiscoveryServer.compareTo(description.getApplicationType()) == 0
					&& serverUri.equalsIgnoreCase(description.getGatewayServerUri())) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} The server {1}, {2} is a DISCOVERYSERVER type and links to the current server uri {3} and is rejected!",
						new Object[] { RequestType.GetEndpoints.name(), description.getApplicationUri(),
								description.getApplicationName(), serverUri });
				continue;
			}
			// checks address CTT 6.1- 015-016
			isValid = false;
			try {
				try {
					// check for null
					if (description.getApplicationUri() != null) {
						URI uri = new URI(description.getApplicationUri());
						InetSocketAddress socketAddress = UriUtil.getSocketAddress(uri);
						InetAddress address = socketAddress.getAddress();
						if (address != null) {
							String hostAddress = address.getHostAddress();
							boolean validIP4 = IPValidater.isIPv4LiteralAddress(hostAddress);
							boolean validIP6 = IPValidater.isIPv6LiteralAddress(hostAddress);
							isValid = validIP4 || validIP6;
						}
					} else {
						isValid = false;
					}
					// invalid ip
				} catch (IllegalArgumentException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				if (!isValid) {
					Logger.getLogger(getClass().getName())
							.log(Level.SEVERE, "{0} The server {1}, ApplicationUri {2} has not a valid server uri!",
									new Object[] { RequestType.GetEndpoints.name(),
											description.getApplicationName() == null ? UNKNOWN
													: description.getApplicationName(),
											description.getApplicationUri() });
				}
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						RequestType.GetEndpoints.name() + " The server "
								+ (description.getApplicationName() == null ? UNKNOWN
										: description.getApplicationName())
								+ ", ApplicationUri " + description.getApplicationUri() + " is unknown! ",
						e);
			}
			// CTT 6.1-017
			if (description.getProductUri() == null || description.getProductUri().isEmpty()) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"{0} The server {1}, DiscoverdUri {2} has no product uri!",
						new Object[] { RequestType.GetEndpoints.name(),
								description.getApplicationName() == null ? UNKNOWN : description.getApplicationName(),
								serverUri });
			}
			boolean isDuplicate = false;
			// CTT 6.1-13
			for (ApplicationDescription d : descriptions) {
				if (((d.getApplicationName() != null && description.getApplicationName() != null)
						&& d.getApplicationName().equals(description.getApplicationName()))
						&& (d.getApplicationType() != null && description.getApplicationType() != null)
						&& (d.getApplicationType().compareTo(description.getApplicationType()) == 0)
						&& (d.getApplicationUri() != null && description.getApplicationUri() != null)
						&& ((d.getApplicationUri().equals(description.getApplicationUri()))
								&& (d.getDiscoveryUrls() != null && description.getDiscoveryUrls() != null))
						&& (Arrays.deepEquals(d.getDiscoveryUrls(), description.getDiscoveryUrls()))) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"{0} Duplicated server {1} returned from the discovery service! Skipping the server {2}!",
							new Object[] { RequestType.GetEndpoints.name(), d.getApplicationName(),
									d.getApplicationUri() });
					isDuplicate = true;
					break;
				}
			}
			if (isDuplicate) {
				continue;
			}
			descriptions.add(description);
		}
		return descriptions.toArray(new ApplicationDescription[0]);
	}
}
