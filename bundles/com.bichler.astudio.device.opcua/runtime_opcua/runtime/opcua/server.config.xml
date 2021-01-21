<?xml version="1.0" encoding="UTF-8"?>
<ApplicationConfiguration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:ua="http://opcfoundation.org/UA/2008/02/Types.xsd">
	<ApplicationName>AS UAServer</ApplicationName>
	<ApplicationUri>urn:localhost:UASDK:AS-Server</ApplicationUri>
	<ProductUri>http://bichler.tech/as/opcua</ProductUri>
	<ApplicationType>Server_0</ApplicationType>
	<SecurityConfiguration>
		<ApplicationCertificate>
			<StorePath>certificatestore/certs/</StorePath>
			<CertKeyName>BTech</CertKeyName>
			<SubjectName>AS Java Server</SubjectName>
		</ApplicationCertificate>
		<TrustedPeerCertificates>
			<StorePath>irgendwo</StorePath>
			<Certificate enabled="true">BTech1</Certificate>
		</TrustedPeerCertificates>
	</SecurityConfiguration>
	<TransportQuotas>
		<OperationTimeout>6000000</OperationTimeout>
		<MaxStringLength>1048576</MaxStringLength>
		<MaxByteStringLength>1048576</MaxByteStringLength>
		<MaxArrayLength>65535</MaxArrayLength>
		<MaxMessageSize>4194304</MaxMessageSize>
		<MaxBufferSize>65535</MaxBufferSize>
		<ChannelLifetime>300000</ChannelLifetime>
		<SecurityTokenLifetime>360000</SecurityTokenLifetime>
	</TransportQuotas>
	<ServerConfiguration>
		<BaseAddresses>
			<ua:String>opc.tcp://127.0.0.1:4840</ua:String>
		</BaseAddresses>
		<SecurityPolicies>
			<ServerSecurityPolicy>
				<SecurityMode>SignAndEncrypt</SecurityMode>
				<SecurityPolicyUri>http://opcfoundation.org/UA/SecurityPolicy#Basic128Rsa15</SecurityPolicyUri>
				<SecurityLevel>3</SecurityLevel>
			</ServerSecurityPolicy>
			<ServerSecurityPolicy>
				<SecurityMode>Sign</SecurityMode>
				<SecurityPolicyUri>http://opcfoundation.org/UA/SecurityPolicy#Basic256</SecurityPolicyUri>
				<SecurityLevel>2</SecurityLevel>
			</ServerSecurityPolicy>
			<ServerSecurityPolicy>
				<SecurityMode>None</SecurityMode>
				<SecurityPolicyUri>http://opcfoundation.org/UA/SecurityPolicy#None</SecurityPolicyUri>
				<SecurityLevel>0</SecurityLevel>
			</ServerSecurityPolicy>
		</SecurityPolicies>
		<UserTokenPolicies>
			<ua:UserTokenPolicy>
				<ua:TokenType>Anonymous</ua:TokenType>
			</ua:UserTokenPolicy>
			<ua:UserTokenPolicy>
				<ua:TokenType>UserName</ua:TokenType>
			</ua:UserTokenPolicy>
			<ua:UserTokenPolicy>
				<ua:TokenType>UserName</ua:TokenType>
			</ua:UserTokenPolicy>
		</UserTokenPolicies>
		<DiagnosticsEnabled>false</DiagnosticsEnabled>
		<MaxSessionCount>1000</MaxSessionCount>
		<MinSessionTimeout>10000.0</MinSessionTimeout>
		<MaxSessionTimeout>3600000.0</MaxSessionTimeout>
		<MaxBrowseContinuationPoints>10</MaxBrowseContinuationPoints>
		<MaxQueryContinuationPoints>10</MaxQueryContinuationPoints>
		<MaxHistoryContinuationPoints>100</MaxHistoryContinuationPoints>
		<MaxRequestAge>600000</MaxRequestAge>
		<MinPublishingInterval>100.0</MinPublishingInterval>
		<MaxPublishingInterval>3600000.0</MaxPublishingInterval>
		<PublishingResolution>50</PublishingResolution>
		<MaxSubscriptionLifetime>3600000</MaxSubscriptionLifetime>
		<MaxMessageQueueSize>100</MaxMessageQueueSize>
		<MaxNotificationQueueSize>100</MaxNotificationQueueSize>
		<MaxNotificationsPerPublish>1000</MaxNotificationsPerPublish>
		<MinMetadataSamplingInterval>1000</MinMetadataSamplingInterval>
		<MaxRegistrationInterval>30000</MaxRegistrationInterval>
		<MaxSubscriptionCount>1000</MaxSubscriptionCount>
		<!-- <HistoryConfiguration>
			<DriverName>org.sqlite.JDBC</DriverName>
			<DBUrl>jdbc:sqlite:</DBUrl>
			<DBName>/Users/applemc207da/Documents/comet.db</DBName>
			<User>hannes</User>
			<Pw>hannes</Pw>
		</HistoryConfiguration>  -->
	</ServerConfiguration>
	<InformationModel>
		<!-- <InformationModelFile>boilerroom.xml</InformationModelFile> <InformationModelFile>smd001.xml</InformationModelFile> 
			<InformationModelFile>extendedNodeset.xml</InformationModelFile> <InformationModelFile>example.xml</InformationModelFile> 
			<InformationModelFile>DI-PlcOpen.xml</InformationModelFile> -->
	</InformationModel>
</ApplicationConfiguration>
