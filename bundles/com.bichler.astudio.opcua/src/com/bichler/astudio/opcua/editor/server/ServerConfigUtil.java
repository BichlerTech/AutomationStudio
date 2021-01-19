package com.bichler.astudio.opcua.editor.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Path;
import org.jdom.Namespace;
import org.opcfoundation.ua.core.UserTokenPolicy;

import com.bichler.astudio.filesystem.IFileSystem;

import opc.sdk.core.application.ApplicationConfiguration;
import opc.sdk.core.application.ServerSecurityPolicy;

public class ServerConfigUtil {
	public static void doSaveServerConfiguration(IFileSystem filesystem, ApplicationConfiguration configuration) {
		String path = new Path(filesystem.getRootPath()).append("serverconfig").append("server.config.xml")
				.toOSString();
		persist(filesystem, path, configuration, false);
		path = new Path(filesystem.getRootPath()).append("serverconfig").append("server.config.com").toOSString();
		persist(filesystem, path, configuration, true);
	}

	/**
	 * we persist a previously loaded and changed application config file
	 */
	private static void persist(IFileSystem filesystem, String file, ApplicationConfiguration configuration,
			boolean skipEndpoints) {
		StringBuffer output = new StringBuffer();
		output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		output.append("<ApplicationConfiguration");
		for (Namespace ns : configuration.getXmlNamespaces()) {
			output.append(" xmlns:" + ns.getPrefix() + "=\"");
			output.append(ns.getURI() + "\"");
		}
		output.append(">\n");
		output.append("  <ApplicationName>");
		output.append(configuration.getApplicationName());
		output.append("</ApplicationName>\n");
		output.append("  <ApplicationUri>");
		output.append(configuration.getApplicationUri());
		output.append("</ApplicationUri>\n");
		output.append("  <ProductUri>");
		output.append(configuration.getProductUri());
		output.append("</ProductUri>\n");
		output.append("  <ApplicationType>");
		output.append(configuration.getApplicationType());
		output.append("</ApplicationType>\n");
		output.append("  <CertificateValidity>");
		output.append(configuration.getCertificateValidity());
		output.append("</CertificateValidity>\n");
		output.append("  <SecurityConfiguration>\n");
		output.append("    <ApplicationCertificate>\n");
		output.append("      <StorePath>");
		if (!skipEndpoints) {
			output.append(configuration.getSecurityConfiguration().getApplicationCertificateStorePath());
		} else {
			output.append("certificatestore/certs/");
		}
		output.append("</StorePath>\n");
		output.append("      <CertKeyName>");
		output.append(configuration.getSecurityConfiguration().getApplicationCertificate().getCertKeyName());
		output.append("</CertKeyName>\n");
		output.append("      <SubjectName>");
		output.append(configuration.getSecurityConfiguration().getApplicationCertificate().getSubjectName());
		output.append("</SubjectName>\n");
		output.append("    </ApplicationCertificate>\n");
		output.append("    <TrustedPeerCertificates>\n");
		output.append("      <StorePath>");
		output.append(configuration.getSecurityConfiguration().getTrustedPeerCertificates().getStorePath());
		output.append("</StorePath>\n");
		// ArrayList<TrustedPeerCertificates.PeerCertificate> certs =
		// (ArrayList<TrustedPeerCertificates.PeerCertificate>)
		// tblv_trustedCerts
		// .getInput();
		// if (certs != null) {
		// for (PeerCertificate cert : certs) {
		// output.append(" <Certificate enabled=");
		// output.append("\"" + cert.isEnabled() + "\"");
		// output.append(">");
		// output.append(cert.getCertName());
		// output.append("</Certificate>\n");
		// }
		// }
		output.append("    </TrustedPeerCertificates>\n");
		output.append("  </SecurityConfiguration>\n");
		output.append("  <TransportQuotas>\n");
		output.append("    <OperationTimeout>");
		output.append(configuration.getOperationTimeout());
		output.append("</OperationTimeout>\n");
		output.append("    <MaxStringLength>");
		output.append(configuration.getMaxStringLength());
		output.append("</MaxStringLength>\n");
		output.append("    <MaxByteStringLength>");
		output.append(configuration.getMaxByteStringLength());
		output.append("</MaxByteStringLength>\n");
		output.append("    <MaxArrayLength>");
		output.append(configuration.getMaxArrayLength());
		output.append("</MaxArrayLength>\n");
		output.append("    <MaxMessageSize>");
		output.append(configuration.getMaxMessageSize());
		output.append("</MaxMessageSize>\n");
		output.append("    <MaxBufferSize>");
		output.append(configuration.getMaxBufferSize());
		output.append("</MaxBufferSize>\n");
		output.append("    <ChannelLifetime>");
		output.append(configuration.getChannelLifetime());
		output.append("</ChannelLifetime>\n");
		output.append("    <SecurityTokenLifetime>");
		output.append(configuration.getSecurityTokenLifetime());
		output.append("</SecurityTokenLifetime>\n");
		output.append("  </TransportQuotas>\n");
		output.append("  <ServerConfiguration>\n");
		output.append("    <BaseAddresses>\n");
		/**
		 * server.config.xml has given endpoints
		 */
		if (!skipEndpoints) {
			for (String endpoint : configuration.getServerConfiguration().getEndpoints()) {
				output.append("      <ua:String>");
				output.append(endpoint);
				output.append("</ua:String>\n");
			}
		}
		/**
		 * server.config.com is serverconfiguration for local server
		 */
		else {
			output.append("      <ua:String>opc.tcp://127.0.0.1:1234</ua:String>");
		}
		output.append("    </BaseAddresses>\n");
		/**
		 * fill in security policies
		 */
		output.append("    <SecurityPolicies>\n");
		for (ServerSecurityPolicy policy : configuration.getSecurityPolicy()) {
			output.append("      <ServerSecurityPolicy>\n");
			output.append("        <SecurityMode>");
			output.append(policy.getSecurityMode().getMessageSecurityMode().name());
			output.append("</SecurityMode>\n");
			output.append("        <SecurityPolicyUri>");
			output.append(policy.getSecurityMode().getSecurityPolicy().getPolicyUri());
			output.append("</SecurityPolicyUri>\n");
			output.append("        <SecurityLevel>");
			output.append(policy.getSecurityLevel());
			output.append("</SecurityLevel>\n");
			output.append("      </ServerSecurityPolicy>\n");
		}
		output.append("    </SecurityPolicies>\n");
		/**
		 * fill in user token policies
		 */
		output.append("    <UserTokenPolicies>\n");
		for (UserTokenPolicy policy : configuration.getUserTokenPolicies()) {
			output.append("      <ua:UserTokenPolicy>\n");
			output.append("        <ua:TokenType>");
			output.append(policy.getTokenType().toString());
			output.append("</ua:TokenType>\n");
			output.append("      </ua:UserTokenPolicy>\n");
		}
		output.append("    </UserTokenPolicies>\n");
		/**
		 * fill in additional infomations
		 */
		output.append("    <DiagnosticsEnabled>");
		output.append(configuration.getServerConfiguration().getDiagnosticsEnabled());
		output.append("</DiagnosticsEnabled>\n");
		output.append("    <MaxSessionCount>");
		output.append(configuration.getServerConfiguration().getMaxSessionCount());
		output.append("</MaxSessionCount>\n");
		output.append("    <UseCertificateStore>");
		output.append(configuration.getServerConfiguration().isUseServerCertificateStore());
		output.append("</UseCertificateStore>\n");
		output.append("    <MinSessionTimeout>");
		output.append(configuration.getServerConfiguration().getMinSessionTimeout());
		output.append("</MinSessionTimeout>\n");
		output.append("    <MaxSessionTimeout>");
		output.append(configuration.getServerConfiguration().getMaxSessionTimeout());
		output.append("</MaxSessionTimeout>\n");
		output.append("    <MaxBrowseContinuationPoints>");
		output.append(configuration.getServerConfiguration().getMaxBrowseContinuationPoints());
		output.append("</MaxBrowseContinuationPoints>\n");
		output.append("    <MaxQueryContinuationPoints>");
		output.append(configuration.getServerConfiguration().getMaxQueryContinuationPoints());
		output.append("</MaxQueryContinuationPoints>\n");
		output.append("    <MaxHistoryContinuationPoints>");
		output.append(configuration.getServerConfiguration().getMaxHistoryContinuationPoints());
		output.append("</MaxHistoryContinuationPoints>\n");
		output.append("    <MaxRequestAge>");
		output.append(configuration.getServerConfiguration().getMaxRequestAge());
		output.append("</MaxRequestAge>\n");
		output.append("    <MinPublishingInterval>");
		output.append(configuration.getServerConfiguration().getMinPublishingInterval());
		output.append("</MinPublishingInterval>\n");
		output.append("    <MaxPublishingInterval>");
		output.append(configuration.getServerConfiguration().getMaxPublishingInterval());
		output.append("</MaxPublishingInterval>\n");
		output.append("    <PublishingResolution>");
		output.append(configuration.getServerConfiguration().getPublishingResolution());
		output.append("</PublishingResolution>\n");
		output.append("    <MaxSubscriptionLifetime>");
		output.append(configuration.getServerConfiguration().getMaxSubscriptionLifetime());
		output.append("</MaxSubscriptionLifetime>\n");
		output.append("    <MaxMessageQueueSize>");
		output.append(configuration.getServerConfiguration().getMaxMessageQueueSize());
		output.append("</MaxMessageQueueSize>\n");
		output.append("    <MaxNotificationQueueSize>");
		output.append(configuration.getServerConfiguration().getMaxNotificationQueueSize());
		output.append("</MaxNotificationQueueSize>\n");
		output.append("    <MaxNotificationsPerPublish>");
		output.append(configuration.getServerConfiguration().getMaxNotificationPerPublish());
		output.append("</MaxNotificationsPerPublish>\n");
		output.append("    <MinMetadataSamplingInterval>");
		output.append(configuration.getServerConfiguration().getMinMetaDataSamplingInterval());
		output.append("</MinMetadataSamplingInterval>\n");
		output.append("    <MaxRegistrationInterval>");
		output.append(configuration.getServerConfiguration().getMaxRegistrationInterval());
		output.append("</MaxRegistrationInterval>\n");
		output.append("    <MaxSubscriptionCount>");
		output.append(configuration.getServerConfiguration().getMaxSubscriptionCount());
		output.append("</MaxSubscriptionCount>\n");
		output.append("    <HistoryConfiguration>\n");
		output.append("        <Active>");
		output.append(configuration.getHistoryConfiguration().isActive());
		output.append("</Active>\n");
		output.append("        <DriverName>");
		output.append(configuration.getHistoryConfiguration().getDrvName());
		output.append("</DriverName>\n");
		output.append("         <DBUrl>");
		output.append(configuration.getHistoryConfiguration().getDatabase());
		output.append("</DBUrl>\n");
		output.append("        <DBName>");
		output.append(configuration.getHistoryConfiguration().getDatabaseName());
		output.append("</DBName>\n");
		output.append("        <User>");
		output.append(configuration.getHistoryConfiguration().getUser());
		output.append("</User>\n");
		output.append("        <Pw>");
		output.append(configuration.getHistoryConfiguration().getPw());
		output.append("</Pw>\n");
		output.append("    </HistoryConfiguration>\n");
		output.append("  </ServerConfiguration>\n");
		output.append("  <InformationModel>\n");
		for (String info : configuration.getServerInformationModels()) {
			output.append("    <InformationModelFile>");
			output.append(info);
			output.append("</InformationModelFile>\n");
		}
		output.append("  </InformationModel>\n");
		output.append("</ApplicationConfiguration>\n");
		BufferedWriter writer = null;
		try {
			if (filesystem.isFile(file)) {
				filesystem.addFile(file);
			}
			OutputStream os = filesystem.writeFile(file);
			writer = new BufferedWriter(new OutputStreamWriter(os));
			writer.write(output.toString());
			writer.flush();
		} catch (IOException e1) {
			Logger.getLogger(ServerConfigUtil.class.getName()).log(Level.SEVERE, e1.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					Logger.getLogger(ServerConfigUtil.class.getName()).log(Level.SEVERE, e.getMessage());
				}
			}
		}
	}

	public static ApplicationConfiguration readServerConfiguration(IFileSystem filesystem, String path) {
		ApplicationConfiguration appConfig = new ApplicationConfiguration();
		/*
		 * appConfig.setApplicationName(name);
		 * appConfig.setApplicationType(applicationType);
		 * appConfig.setApplicationUri(uri); appConfig.setBuildDate(date);
		 * appConfig.setBuildNumber(date); appConfig.setChannelLifetime(date);
		 * appConfig.setHistoryConfig(config); appConfig.setManufacturerName(name);
		 * appConfig.setMaxArrayLength(maxArrayLength);
		 * appConfig.setMaxBufferSize(maxBufferSize);
		 * appConfig.setMaxByteStringLength(maxByteStringLength);
		 * appConfig.setMaxMessageSize(maxMessageSize);
		 * appConfig.setMaxStringLength(maxStringLength);
		 * appConfig.setOperationTimeout(operationTimeout);
		 * appConfig.setProductName(prodName); appConfig.setProductUri(uri);
		 */

		return appConfig;
	}
}
