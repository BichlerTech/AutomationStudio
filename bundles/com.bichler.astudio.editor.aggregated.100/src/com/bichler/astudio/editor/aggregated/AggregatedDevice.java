package com.bichler.astudio.editor.aggregated;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import opc.client.application.UAClient;
import opc.client.application.UADiscoveryClient;
import opc.client.application.listener.ReconnectListener;
import opc.client.service.ClientSession;

import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ActivateSessionResponse;
import org.opcfoundation.ua.core.AnonymousIdentityToken;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserNameIdentityToken;
import org.opcfoundation.ua.core.X509IdentityToken;
import org.opcfoundation.ua.encoding.binary.BinaryEncoder;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.EndpointUtil;

import com.bichler.opc.comdrv.ComDP;
import com.bichler.opc.comdrv.OPCUADevice;


public class AggregatedDevice extends OPCUADevice
{
  @Override
  public boolean setDeviceAddress(String address)
  {
    // TODO Auto-generated method stub
    System.out.println("address: " + address);
    return super.setDeviceAddress(address);
  }

  public AggregatedDevice()
  {
    this.setUaSecurityType(-1);
  }

  @Override
  public long connect()
  {
    // connect to target server
    return super.connect();
  }

  public void loadOPCUAServerConnection(InputStream stream)
  {
    // AggregatedDevice device = new AggregatedDevice();
    if (stream != null)
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line = null;
      try
      {
        while ((line = reader.readLine()) != null)
        {
          if (line.compareTo("servername") == 0)
          {
            this.setUaServerName(reader.readLine());
          }
          else if (line.compareTo("serveruri") == 0)
          {
            this.setUaServerUri(reader.readLine());
          }
          else if (line.compareTo("securitypolicy") == 0)
          {
            this.setUaSecurityPolicy(reader.readLine());
          }
          else if (line.compareTo("securitymode") == 0)
          {
            this.setUaSecurityMode(reader.readLine());
          }
          else if (line.compareTo("securitytype") == 0)
          {
            try
            {
              this.setUaSecurityType(Integer.parseInt(reader.readLine()));
            }
            catch (NumberFormatException ex)
            {
              this.setUaSecurityType(0);
            }
          }
          else if (line.compareTo("username") == 0)
          {
            this.setUaUserName(reader.readLine());
          }
          else if (line.compareTo("password") == 0)
          {
            this.setUaPassword(reader.readLine());
          }
          else if (line.compareTo("certificate") == 0)
          {
            this.setUaCertificate(reader.readLine());
          }
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
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
            e.printStackTrace();
          }
        }
      }
    }
  }

  @Override
  public void disconnect()
  {
    super.disconnect();
  }

  @Override
  public boolean addDP(NodeId arg0)
  {
    return false;
  }

  @Override
  public ComDP addDP(NodeId arg0, Object arg1)
  {
    return null;
  }

  @Override
  public boolean addDPs(NodeId arg0, Object arg1)
  {
    return false;
  }

  @Override
  public void checkCommunication()
  {
  }

  @Override
  public byte[] read()
  {
    return null;
  }

  @Override
  public void storeReadReq(ComDP arg0)
  {
  }

  @Override
  public int write(byte[] arg0)
  {
    return 0;
  }

  public void connect2Server(File configurationFile, File certFile, File privKeyFile)
  {
    switch (this.getUaSecurityType())
    {
    case 0:
      AnonymousIdentityToken anoToken = new AnonymousIdentityToken();
      this.setUaIdentityToken(anoToken);
      break;
    case 1:
      UserNameIdentityToken usrToken = new UserNameIdentityToken();
      usrToken.setUserName(this.getUaUserName());
      usrToken.setPassword(ByteString.valueOf(this.getUaPassword().getBytes()));
      this.setUaIdentityToken(usrToken);
      break;
    case 2:
      break;
    default:
      break;
    }
    UADiscoveryClient discoverClient = new UAClient().createDiscoveryClient();
    if (discoverClient == null)
    {
      // discovery not found so we couldn't fill endpoints
      System.out.println("can not create discovery client - AggregatedDevice.java");
      return;
    }
    try
    {
      EndpointDescription[] endpoints = discoverClient.getEndpoints(false, this.getUaServerUri());
      if (endpoints == null)
      {
        System.out.println("can not find endpoints - Aggregated Device");
        return;
      }
      try
      {
        SecurityPolicy policy = SecurityPolicy.getSecurityPolicy(this.getUaSecurityPolicy());
        endpoints = EndpointUtil.selectBySecurityPolicy(endpoints, policy);
      }
      catch (Exception e2)
      {
      }
      MessageSecurityMode mode = MessageSecurityMode.valueOf(this.getUaSecurityMode());
      if (endpoints != null)
      {
        endpoints = EndpointUtil.selectByMessageSecurityMode(endpoints, mode);
      }
      if (endpoints != null && endpoints.length > 0)
      {
        this.setUaEndpointDescription(endpoints[0]);
      }
      uaclient = new UAClient();
      uaclient.setClientConfiguration(configurationFile, certFile, privKeyFile);
      ClientSession session = uaclient.createSession(endpoints[0], "OPCUA HB-Studio Session");
      UserIdentityToken identity = this.getUaIdentityToken();
      ActivateSessionResponse activated = null;
      try
      {
        if (identity instanceof AnonymousIdentityToken)
        {
          activated = uaclient.activateSession(session, null);
        }
        else if (identity instanceof UserNameIdentityToken)
        {
          String pw = new String(((UserNameIdentityToken) identity).getPassword().getValue(), BinaryEncoder.UTF8);
          activated = uaclient.activateSession(session, ((UserNameIdentityToken) identity).getUserName(), pw, null);
        }
        else if (identity instanceof X509IdentityToken)
        {
//          activated = uaclient.activateSession(session, new Cert(((X509IdentityToken) identity).getCertificateData().getValue()),
//              null);
        }
        if (activated != null)
        {
          uaclient.getActiveSession().setReconnectionPeriod(1000);
          uaclient.getActiveSession().addReconnectListener(new ReconnectListener()
          {
            @Override
            public void onReconnectStarted(ClientSession session)
            {
            }

            @Override
            public void onReconnectFinished(ClientSession session, boolean successfull)
            {
            }

            @Override
            public void onConnectionLost(ClientSession session)
            {
            }

			@Override
			public void onReconnectStopped(ClientSession session) {
				
			}
          });
        }
      }
      catch (ServiceResultException e)
      {
      }
    }
    catch (ServiceResultException e)
    {
      e.printStackTrace();
    }
  }
}
