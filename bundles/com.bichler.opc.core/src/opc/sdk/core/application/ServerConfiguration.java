package opc.sdk.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Content;
import org.jdom.Element;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.transport.security.SecurityPolicy;

/**
 * The configuration of a server application.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class ServerConfiguration {
	private List<String> baseAddresses = null;
	private List<ServerSecurityPolicy> securityPolicies = null;
	private List<UserTokenPolicy> userTokenPolicies = null;
	private String diagnosticsEnabled = "";
	private String maxSessionCount = "";
	private String minSessionTimeout = "";
	private String maxSessionTimeout = "";
	private String maxBrowseContinuationPoints = "";
	private String maxQueryContinuationPoints = "";
	private String maxHistoryContinuationPoints = "";
	private String maxRequestAge = "";
	private String minPublishingInterval = "";
	private String maxPublishingInterval = "";
	private String publishingResolution = "";
	private String maxSubscriptionLifetime = "";
	private String minSubscriptionLifetime = "";
	private String maxMessageQueueSize = "";
	private String maxNotificationQueueSize = "";
	private String maxNotificationPerPublish = "";
	private String minMetaDataSamplingInterval = "";
	private String maxRegistrationInterval = "";
	private String maxSubscriptionCount = "";
	private String maxPublishRequests = "65535";
	private String registerLocalDiscovery = "true";
	private boolean useServerCertificateStore = false;
	private Logger logger = Logger.getLogger(getClass().getName());

	public ServerConfiguration() {
		this.baseAddresses = new ArrayList<>();
		this.securityPolicies = new ArrayList<>();
		this.userTokenPolicies = new ArrayList<>();
	}

	public UnsignedInteger getMaxPublishRequests() {
		UnsignedInteger request = new UnsignedInteger();
		try {
			request = UnsignedInteger.parseUnsignedInteger(maxPublishRequests);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
			request = UnsignedInteger.parseUnsignedInteger("65535");
		}
		return request;
	}

	public void setMaxPublishRequests(String maxPublishRequests) {
		this.maxPublishRequests = maxPublishRequests;
	}

	public void setRegisterLocalDiscovery(String registerLocalDiscovery) {
		this.registerLocalDiscovery = registerLocalDiscovery;
	}

	public boolean getRegisterLocalDiscovery() {
		return Boolean.parseBoolean(this.registerLocalDiscovery);
	}

	@SuppressWarnings("unchecked")
	public void setEndpoints(Element content) {
		for (Content c : (List<Content>) content.getContent()) {
			if (c instanceof Element) {
				this.baseAddresses.add(((Content) ((Element) c).getContent().get(0)).getValue());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void setSecurityPolicies(Element content) {
		for (Content c : (List<Content>) content.getContent()) {
			if (c instanceof Element) {
				String secmode = "";
				String secpolicyuri = "";
				String seclevel = null;
				for (Content cc : (List<Content>) ((Element) c).getContent()) {
					if (cc instanceof Element) {
						if (((Element) cc).getName().equals(ServerConfigurationTags.SecurityMode.getName())) {
							secmode = ((Content) ((Element) cc).getContent().get(0)).getValue();
						} else if (((Element) cc).getName()
								.equals(ServerConfigurationTags.SecurityPolicyUri.getName())) {
							secpolicyuri = ((Content) ((Element) cc).getContent().get(0)).getValue();
						} else if (((Element) cc).getName().equals(ServerConfigurationTags.SecurityLevel.getName())) {
							seclevel = ((Content) ((Element) cc).getContent().get(0)).getValue();
						}
					}
				}
				addServerSecurityPolicy(secmode, secpolicyuri, seclevel);
			}
		}
	}

	public void setUseServerCertificateStore(Element element) {
		setUseServerCertificateStore(Boolean.parseBoolean(element.getContent(element.getContentSize() - 1).getValue()));
	}

	@SuppressWarnings("unchecked")
	public void setUserTokenPolicies(Element content) {
		for (Content c : (List<Content>) content.getContent()) {
			if (c instanceof Element) {
				for (Content cc : (List<Content>) ((Element) c).getContent()) {
					if (cc instanceof Element) {
						String tokentype = cc.getValue();
						String[] token = tokentype.split("_");
						String tokenValue = token[0];
						String tokenPolicy = "";
						if (token.length > 1) {
							tokenPolicy = token[1];
						}
						if (UserTokenType.valueOf(tokenValue).equals(UserTokenType.Anonymous)) {
							this.userTokenPolicies.add(UserTokenPolicy.ANONYMOUS);
						} else if (UserTokenType.valueOf(tokenValue).equals(UserTokenType.UserName)) {
							if ("256".equals(tokenPolicy)) {
								this.userTokenPolicies.add(UserTokenPolicy.SECURE_USERNAME_PASSWORD_BASIC256);
							} else {
								this.userTokenPolicies.add(UserTokenPolicy.SECURE_USERNAME_PASSWORD);
							}
						} else if (UserTokenType.valueOf(tokenValue).equals(UserTokenType.Certificate)) {
							if ("256".equals(tokenPolicy)) {
								this.userTokenPolicies.add(UserTokenPolicy.SECURE_CERTIFICATE_BASIC256);
							} else {
								this.userTokenPolicies.add(UserTokenPolicy.SECURE_CERTIFICATE);
							}
						}
					}
				}
			}
		}
	}

	private void addServerSecurityPolicy(String secmode, String secpolicyuri, String seclevel) {
		MessageSecurityMode valof = MessageSecurityMode.valueOf(secmode.split("_")[0]);
		SecurityPolicy valofsecPolicy = null;
		try {
			valofsecPolicy = SecurityPolicy.getSecurityPolicy(secpolicyuri);
		} catch (ServiceResultException e) {
			logger.log(Level.SEVERE, null, e);
		}
		SecurityMode securityMode = new SecurityMode(valofsecPolicy, valof);
		this.securityPolicies.add(new ServerSecurityPolicy(securityMode, seclevel));
	}

	public void setDiagnosticsEnabled(Element element) {
		this.diagnosticsEnabled = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxSessionCount(Element element) {
		this.maxSessionCount = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMinSessionTimeout(Element element) {
		this.minSessionTimeout = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxSessionTimeout(Element element) {
		this.maxSessionTimeout = element.getContent(element.getContentSize() - 1).getValue();
	}

	/***/
	public void setMaxQueryContinuationPoints(Element element) {
		this.maxQueryContinuationPoints = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxHistoryContinuationPoints(Element element) {
		this.maxHistoryContinuationPoints = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxRequestAge(Element element) {
		this.maxRequestAge = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMinPublishingInterval(Element element) {
		this.minPublishingInterval = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxPublishingInterval(Element element) {
		this.maxPublishingInterval = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setPublishingResolution(Element element) {
		this.publishingResolution = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxSubscriptionLifetime(Element element) {
		this.maxSubscriptionLifetime = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMinSubscriptionLifetime(Element element) {
		this.minSubscriptionLifetime = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxSubscriptionCount(Element element) {
		this.maxSubscriptionCount = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxMessageQueueSize(Element element) {
		this.maxMessageQueueSize = element.getContent(element.getContentSize() - 1).getValue();
	}

	/***/
	public void setMaxNotificationsPerPublish(Element element) {
		this.maxNotificationPerPublish = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMinMetadatasamplingInterval(Element element) {
		this.minMetaDataSamplingInterval = element.getContent(element.getContentSize() - 1).getValue();
	}

	/***/
	@SuppressWarnings("unchecked")
	public void setAvailableSamplingRates(Element element) {
		for (Content c : (List<Content>) element.getContent())
			if (c instanceof Element) {
				SamplingRateGroup srg = new SamplingRateGroup();
				for (Content cc : (List<Content>) ((Element) c).getContent()) {
					if (cc instanceof Element) {
						if (((Element) cc).getName().equals(ServerConfigurationTags.Start.getName())) {
							srg.setStart(cc.getValue());
						} else if (((Element) cc).getName().equals(ServerConfigurationTags.Increment.getName())) {
							srg.setIncrement(cc.getValue());
						} else if (((Element) cc).getName().equals(ServerConfigurationTags.Count.getName())) {
							srg.setCount(cc.getValue());
						}
					}
				}
			}
	}

	public void setMaxRegistrationInterval(Element element) {
		this.maxRegistrationInterval = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxNotificationQueueSize(Element element) {
		this.maxNotificationQueueSize = element.getContent(element.getContentSize() - 1).getValue();
	}

	public void setMaxBrowseContinuationPoints(Element element) {
		this.maxBrowseContinuationPoints = element.getContent(element.getContentSize() - 1).getValue();
	}

	public List<UserTokenPolicy> getUserTokenPolicies() {
		return this.userTokenPolicies;
	}

	public List<String> getEndpoints() {
		return this.baseAddresses;
	}

	public List<ServerSecurityPolicy> getSecurityPolicies() {
		return this.securityPolicies;
	}

	public String getSessionTimeout() {
		return this.maxSessionTimeout;
	}

	public double getMaxSessionTimeout() {
		Double session = 0d;
		try {
			session = Double.parseDouble(maxSessionTimeout);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return session;
	}

	public UnsignedInteger getMaxSessionCount() {
		UnsignedInteger session = new UnsignedInteger();
		try {
			session = UnsignedInteger.parseUnsignedInteger(maxSessionCount);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return session;
	}

	public double getMinSessionTimeout() {
		Double session = 0d;
		try {
			session = Double.parseDouble(this.minSessionTimeout);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return session;
	}

	public UnsignedInteger getMaxRequestAge() {
		UnsignedInteger age = new UnsignedInteger();
		try {
			age = UnsignedInteger.parseUnsignedInteger(this.maxRequestAge);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return age;
	}

	public int getMaxBrowseContinuationPoints() {
		return Integer.parseInt(this.maxBrowseContinuationPoints);
	}

	public int getMaxHistoryContinuationPoints() {
		return Integer.parseInt(this.maxHistoryContinuationPoints);
	}

	public Double getMinPublishingInterval() {
		Double interval = 0d;
		try {
			interval = Double.parseDouble(this.minPublishingInterval);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return interval;
	}

	public Double getMaxPublishingInterval() {
		Double interval = 0d;
		try {
			interval = Double.parseDouble(this.maxPublishingInterval);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return interval;
	}

	public int getMaxResolution() {
		int resolution = 0;
		try {
			resolution = Integer.parseInt(this.publishingResolution);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return resolution;
	}

	public UnsignedInteger getMaxSubscriptionLifetime() {
		UnsignedInteger lifetime = new UnsignedInteger();
		try {
			lifetime = UnsignedInteger.parseUnsignedInteger(this.maxSubscriptionLifetime);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return lifetime;
	}

	/**
	 * 
	 * @return
	 */
	public UnsignedInteger getMinSubscriptionLifetime() {
		UnsignedInteger lifetime = new UnsignedInteger();
		try {
			lifetime = UnsignedInteger.parseUnsignedInteger(this.minSubscriptionLifetime);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
			lifetime = new UnsignedInteger(1000);
		}
		return lifetime;
	}

	public Integer getMaxMessageCount() {
		Integer size = 0;
		try {
			size = Integer.parseInt(this.maxMessageQueueSize);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return size;
	}

	public UnsignedInteger getMaxNotificationPerPublish() {
		UnsignedInteger publish = new UnsignedInteger();
		try {
			publish = new UnsignedInteger(this.maxNotificationPerPublish);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return publish;
	}

	public UnsignedInteger getMaxNotificationQueueSize() {
		UnsignedInteger size = new UnsignedInteger();
		try {
			size = UnsignedInteger.parseUnsignedInteger(this.maxNotificationQueueSize);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return size;
	}

	public int getMaxRegistrationInterval() {
		int interval = 0;
		try {
			interval = Integer.parseInt(this.maxRegistrationInterval);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return interval;
	}

	public List<String> getBaseAddresses() {
		return this.baseAddresses;
	}

	public void validate() {
		if (securityPolicies.isEmpty()) {
			this.securityPolicies.add(new ServerSecurityPolicy());
		}
		if (this.userTokenPolicies.isEmpty()) {
			this.userTokenPolicies.add(new UserTokenPolicy());
		}
	}

	public int getPublishResolution() {
		int resolution = 0;
		try {
			resolution = Integer.parseInt(this.publishingResolution);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return resolution;
	}

	public void setPublishResolution(Integer publishResolution) {
		this.publishingResolution = publishResolution.toString();
	}

	public String getDiagnosticsEnabled() {
		return diagnosticsEnabled;
	}

	public void setDiagnosticsEnabled(String diagnosticsEnabled) {
		this.diagnosticsEnabled = diagnosticsEnabled;
	}

	public int getMaxQueryContinuationPoints() {
		int points = 0;
		try {
			points = Integer.parseInt(maxQueryContinuationPoints);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return points;
	}

	public void setMaxQueryContinuationPoints(String maxQueryContinuationPoints) {
		this.maxQueryContinuationPoints = maxQueryContinuationPoints;
	}

	public String getPublishingResolution() {
		return publishingResolution;
	}

	public void setPublishingResolution(String publishingResolution) {
		this.publishingResolution = publishingResolution;
	}

	public String getMaxMessageQueueSize() {
		return maxMessageQueueSize;
	}

	public void setMaxMessageQueueSize(String maxMessageQueueSize) {
		this.maxMessageQueueSize = maxMessageQueueSize;
	}

	public String getMinMetaDataSamplingInterval() {
		return minMetaDataSamplingInterval;
	}

	public void setMinMetaDataSamplingInterval(String minMetaDataSamplingInterval) {
		this.minMetaDataSamplingInterval = minMetaDataSamplingInterval;
	}

	public void setBaseAddresses(List<String> baseAddresses) {
		this.baseAddresses = baseAddresses;
	}

	public void setSecurityPolicies(List<ServerSecurityPolicy> securityPolicies) {
		this.securityPolicies = securityPolicies;
	}

	public void setUserTokenPolicies(List<UserTokenPolicy> userTokenPolicies) {
		this.userTokenPolicies = userTokenPolicies;
	}

	public void setMaxSessionCount(String maxSessionCount) {
		this.maxSessionCount = maxSessionCount;
	}

	public void setMinSessionTimeout(String minSessionTimeout) {
		this.minSessionTimeout = minSessionTimeout;
	}

	public void setMaxSessionTimeout(String maxSessionTimeout) {
		this.maxSessionTimeout = maxSessionTimeout;
	}

	public void setMaxBrowseContinuationPoints(String maxBrowseContinuationPoints) {
		this.maxBrowseContinuationPoints = maxBrowseContinuationPoints;
	}

	public void setMaxHistoryContinuationPoints(String maxHistoryContinuationPoints) {
		this.maxHistoryContinuationPoints = maxHistoryContinuationPoints;
	}

	public void setMaxRequestAge(String maxRequestAge) {
		this.maxRequestAge = maxRequestAge;
	}

	public void setMinPublishingInterval(String minPublishingInterval) {
		this.minPublishingInterval = minPublishingInterval;
	}

	public void setMaxPublishingInterval(String maxPublishingInterval) {
		this.maxPublishingInterval = maxPublishingInterval;
	}

	public void setMaxSubscriptionLifetime(String maxSubscriptionLifetime) {
		this.maxSubscriptionLifetime = maxSubscriptionLifetime;
	}

	public void setMinSubscriptionLifetime(String minSubscriptionLifetime) {
		this.minSubscriptionLifetime = minSubscriptionLifetime;
	}

	public void setMaxNotificationQueueSize(String maxNotificationQueueSize) {
		this.maxNotificationQueueSize = maxNotificationQueueSize;
	}

	public void setMaxNotificationPerPublish(String maxNotificationPerPublish) {
		this.maxNotificationPerPublish = maxNotificationPerPublish;
	}

	public void setMaxRegistrationInterval(String maxRegistrationInterval) {
		this.maxRegistrationInterval = maxRegistrationInterval;
	}

	public int getMaxSubscriptionCount() {
		int count = 0;
		try {
			count = Integer.parseInt(this.maxSubscriptionCount);
		} catch (IllegalArgumentException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return count;
	}

	public void setMaxSubscriptionCount(String maxSubscriptionCount) {
		this.maxSubscriptionCount = maxSubscriptionCount;
	}

	public boolean isUseServerCertificateStore() {
		return useServerCertificateStore;
	}

	public void setUseServerCertificateStore(boolean useServerCertificateStore) {
		this.useServerCertificateStore = useServerCertificateStore;
	}
}
