package com.bichler.astudio.editor.aggregated.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.utils.CertificateUtils;

import com.bichler.astudio.editor.aggregated.AggregatedActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.events.CreateOPCUADriverParameter;
import com.bichler.astudio.opcua.handlers.AbstractOPCCreateDriverModel;

public class CreateOPCUADriverHandler extends AbstractOPCCreateDriverModel
{
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    CreateOPCUADriverParameter trigger = getCommandParameter(event);
    IFileSystem filesystem = trigger.getFilesystem();
    String driverPath = trigger.getDriverpath();
    File defaultDrvStore = AggregatedActivator.getDefault().getFile(AggregatedActivator.getDefault().getBundle(),
        Path.ROOT.append("driver").append("config").append("studio.temp"));
    if (!filesystem.isDir(driverPath))
    {
      try
      {
        filesystem.addDir(driverPath);
        // create cert folder
        filesystem.addDir(new Path(driverPath).append("cert").toOSString());
        // create clientconfig folder
        filesystem.addDir(new Path(driverPath).append("clientconfig").toOSString());
        // create clientconfig file
        filesystem.addFile(new Path(driverPath).append("clientconfig").append("clientconfig.xml").toOSString());
        OutputStream output = null;
        try
        {
          output = filesystem
              .writeFile(new Path(driverPath).append("clientconfig").append("clientconfig.xml").toOSString());
          output.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
              + "<ApplicationConfiguration xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://opcfoundation.org/UA/SDK/Configuration.xsd\">"
              + "  <ApplicationName>Comet OPC UA Aggregated Client</ApplicationName>"
              + "  <ApplicationUri>urn:hb-softsolution.com:UASDK:CometClient</ApplicationUri>"
              + "  <ProductUri>http://hb-softsolution.com/UASDK/CometClient</ProductUri>"
              + "  <ApplicationType>Client_1</ApplicationType>" + "  <SecurityConfiguration>"
              + "    <ApplicationCertificate>" + "      <StorePath>Cert</StorePath>"
              + "      <SubjectName>CN=Comet OPC Client,O=HB-Softsolution, DC=http://hb-softsoluiton.com</SubjectName>"
              + "      <PrivateKeyPassword>com.hbsoft.comet.client</PrivateKeyPassword>"
              + "    </ApplicationCertificate>" + "    <TrustedIssuerCertificates>"
              + "      <TrustedCertificates></TrustedCertificates>" + "    </TrustedIssuerCertificates>"
              + "    <TrustedPeerCertificates>" + "      <StoreType></StoreType>" + "      <StorePath></StorePath>"
              + "      <TrustedCertificates></TrustedCertificates>" + "    </TrustedPeerCertificates>"
              + "    <NonceLength>32</NonceLength>"
              + "    <InvalidCertificateDirectory i:nil=\"true\"></InvalidCertificateDirectory>"
              + "    <RejectedCertificateStore i:nil=\"true\"></RejectedCertificateStore>"
              + "    <!--<ConfigureFirewall>false</ConfigureFirewall>-->" + "  </SecurityConfiguration>"
              + "  <TransportConfigurations></TransportConfigurations>" + "  <TransportQuotas>"
              + "    <OperationTimeout>600000</OperationTimeout>" + "    <MaxStringLength>1048576</MaxStringLength>"
              + "    <MaxByteStringLength>1048576</MaxByteStringLength>" + "    <MaxArrayLength>65535</MaxArrayLength>"
              + "    <MaxMessageSize>4194304</MaxMessageSize>" + "   <MaxBufferSize>65535</MaxBufferSize>"
              + "    <ChannelLifetime>300000</ChannelLifetime>"
              + "   <SecurityTokenLifetime>3600000</SecurityTokenLifetime>" + "  </TransportQuotas>"
              + "  <ClientConfiguration>" + "    <DefaultSessionTimeout>60000</DefaultSessionTimeout>"
              + "      <WellKnownDiscoveryUrls xmlns:d3p1=\"http://opcfoundation.org/UA/2008/02/Types.xsd\">"
              + "        <d3p1:String>opc.tcp://localhost:6006/Test</d3p1:String>"
              + "        <d3p1:String>opc.tcp://localhost:1234</d3p1:String>" + "      </WellKnownDiscoveryUrls>"
              + "      <DiscoveryServers xmlns:d3p1=\"http://opcfoundation.org/UA/2008/02/Types.xsd\"></DiscoveryServers>"
              + "      <MinSubscriptionLifetime>10000</MinSubscriptionLifetime>" + "    </ClientConfiguration>"
              + "    <TraceConfiguration>" + "    <OutputFilePath></OutputFilePath>"
              + "    <DeleteOnLoad></DeleteOnLoad>" + "    <TraceMasks></TraceMasks>" + "  </TraceConfiguration>"
              + "</ApplicationConfiguration>").getBytes());
        }
        finally
        {
          if (output != null)
          {
            try
            {
              output.close();
            }
            catch (IOException e)
            {
              Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
            }
          }
        }
        // try to create certificate
        KeyPair clientApplicationInstanceCertificate = null;
        clientApplicationInstanceCertificate = CertificateUtils.createApplicationInstanceCertificate("Comet UA Client",
            "HB-Softsolution", "hb-softsolution.com/comet_ua", 365);
        OutputStream certificateFile = null;
        OutputStream privateKeyFile = null;
        try
        {
          certificateFile = filesystem
              .writeFile(new Path(driverPath).append("cert").append("clientcertificate.der").toOSString());
          privateKeyFile = filesystem
              .writeFile(new Path(driverPath).append("cert").append("clientkey.pfx").toOSString());
          // save to outputstream
          certificateFile.write(clientApplicationInstanceCertificate.getCertificate().getEncoded());
          privateKeyFile.write(clientApplicationInstanceCertificate.getPrivateKey().getEncodedPrivateKey());
        }
        finally
        {
          if (certificateFile != null)
          {
            try
            {
              certificateFile.close();
            }
            catch (IOException e)
            {
              Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
            }
          }
          if (privateKeyFile != null)
          {
            try
            {
              privateKeyFile.close();
            }
            catch (IOException e)
            {
              Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
            }
          }
        }
        // create driver.com file
        filesystem.addFile(new Path(driverPath).append("driver.com").toOSString());
        // add content to driver file
        BufferedReader reader = null;
        try
        {
          output = filesystem.writeFile(new Path(driverPath).append("driver.com").toOSString());
          // open drvconfig file
          InputStream is = filesystem.readFile(defaultDrvStore.getAbsolutePath());
          reader = new BufferedReader(new InputStreamReader(is));
          String line = "";
          while ((line = reader.readLine()) != null)
          {
            output.write((line + "\n").getBytes());
          }
        }
        finally
        {
          if (reader != null)
          {
            try
            {
              reader.close();
            }
            catch (IOException e)
            {
              Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
            }
          }
          if (output != null)
          {
            try
            {
              output.close();
            }
            catch (IOException e)
            {
              Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
            }
          }
        }
      }
      catch (Exception e)
      {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
      }
    }
    return null;
  }
}
