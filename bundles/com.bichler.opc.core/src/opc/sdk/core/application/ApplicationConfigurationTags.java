package opc.sdk.core.application;

/**
 * Tagnames for the application configuration
 * 
 * @author Thomas Z&ouml;chbauer
 * 
 */
public enum ApplicationConfigurationTags {
	ApplicationConfiguration, ProductName, ApplicationName, ApplicationUri, ManufacturerName, BuildDate, BUILDNumber,
	ProductUri, SoftwareVersion, SecurityConfiguration, TransportConfiguration, TransportQuotas, ServerConfiguration,
	Extensions, TraceConfiguration, ApplicationType, StorePath, CertKeyName, Certificate, SubjectName, Enabled,
	OperationTimeout, MaxStringLength, MaxByteStringLength, MaxArrayLength, MaxMessageSize, MaxBufferSize,
	ChannelLifetime, SecurityTokenLifetime, MaxSubscriptionCount, OutputFilePath, DeleteOnLoad, TraceMasks,
	InformationModels, HistoryConfiguration, Active, DriverName, DBUrl, DBName, User, Pw, CertificateValidity,
	ApplicationCertificate, TrustedPeerCertificates;
}
