package com.bichler.opc.driver.calculation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.opcfoundation.ua.builtintypes.*;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.opc.comdrv.ComCommunicationStates;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.ComDRVStates;
import com.bichler.opc.comdrv.utils.ComStatusUtils;

public class ComDRV extends com.bichler.opc.comdrv.ComDRV
{
  private CalculationResourceManager manager = null;
  // private CometDRVManager drvman = CometDRVManager.getDRVManager();
  private ScriptEngineManager scrmanager = null;
  private ScriptEngine engine = null;
  private ComStatusUtils utils = new ComStatusUtils();

  public ComDRV()
  {
    this.manager = new CalculationResourceManager();
    this.manager.setManager(drvManager.getResourceManager());
    scrmanager = new ScriptEngineManager(null);
    // try to load nashorn script engine it is present since java 8.0
    engine = scrmanager.getEngineByName("nashorn");
    if (engine == null)
    {
      engine = scrmanager.getEngineByName("JavaScript");
    }
    else
    {
      try
      {
        // if we have nashorn engine, so lod compatibility script
        engine.eval("load(\"nashorn:mozilla_compat.js\");");
      }
      catch (ScriptException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean stop()
  {
    this.state = ComDRVStates.STOPPED;
    return super.stop();
  }

  @Override
  public boolean start()
  {
    this.state = ComDRVStates.RUNNING;
    return true;
  }

  /**
   * get state of the Device
   * 
   * @return actual Device state
   */
  public long getDeviceState()
  {
    return ComCommunicationStates.OPEN;
  }

  @Override
  public void initialize()
  {
  }

  @Override
  public void checkCommunication()
  {
    // TODO Auto-generated method stub
  }

  @Override
  public boolean doStartup()
  {
    CalculationReadHandler readHandler = new CalculationReadHandler();
    readHandler.setManager(manager);
    CalculationWriteHandler writeHandler = new CalculationWriteHandler();
    writeHandler.setManager(manager);
    // CometDRVManager
    DataValue response = drvManager.readFromDriver(Identifiers.Server_NamespaceArray, Attributes.Value, null, null,
        (long) drvId, 0.0, TimestampsToReturn.Neither);
    /* check the namespace object */
    if (response == null || response.getValue() == null)
    {
      // now we return an empty datapoints list
      return true;
    }
    String[] namespaces = (String[]) response.getValue().getValue();
    NamespaceTable uris = NamespaceTable.createFromArray(namespaces);
    InputStream input;
    try
    {
      input = new FileInputStream("drivers/" + this.getDriverName() + "/datapoints.com");
      // now load all datapoints from xml
      CalculationImporter importer = new CalculationImporter();
      List<CalculationDP> dps = importer.loadDPs(input, uris, manager, drvId);
      // all nodes to map nodeid to datapoint
      for (CalculationDP dp : dps)
      {
        if (!dp.isActive())
        {
          continue;
        }
        switch (dp.getEvent())
        {
        case CYCLIC:
          manager.addCalcInstructionCyclic(dp);
          break;
        case ONREAD:
          manager.addCalcInstructionOnRead(dp.getTarget().getTargetId(), dp);
          break;
        case VALUECHANGE:
          break;
        }
        // this.manager.getNodesFromServer().put(dp.getServerNodeId(),
        // dp);
        // this.manager.getNodesFromDRV().put(dp.getStartAddress(), dp);
        if (dp.getEvent() == CalcEvent.ONREAD)
          // set read flag to node
          manager.getManager().getDriverConnection().setDriverReadConnected(dp.getTarget().getTargetId(),
              true, /**
                     * 
                     * this node is connected to a driver
                     */
              ComDRVManager.SYNCREAD, (long) drvId);
      }
      for (NodeId id : manager.getCalcInstructionsValueChange().keySet())
      {
        // set read flag to node
        manager.getManager().getDriverConnection().setDriverWriteConnected(id,
            true, /**
                   * 
                   * this node is connected to a driver
                   */
            ComDRVManager.SYNCWRITE, (long) drvId);
      }
    }
    catch (FileNotFoundException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    readHandler.setDrvId(drvId);
    drvManager.addReadListener((long) drvId, readHandler);
    writeHandler.setDrvId(drvId);
    drvManager.addWriteListener((long) drvId, writeHandler);
    try {
		engine.eval("importPackage(org.opcfoundation.ua.builtintypes);\nimportPackage(org.opcfoundation.ua.common);\nimportPackage(java.lang);\nimportPackage(com.bichler.opc.comdrv);\nimportPackage(org.opcfoundation.ua.core);");
	} catch (ScriptException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    // monitoring is not supported from underlying system
    // CometDRVManager.getDRVManager().addMonitoringListener(monitoringHandler);
   // utils.writeLED_Status(ComStatusUtils.RUNNING);
    return true;
  }

  @Override
  public boolean doWork()
  {
    if (lastwatchdogwrite + (1000L * 1000000L * 10L) < System.nanoTime())
    {
      // lastwatchdogwrite = System.currentTimeMillis();
      lastwatchdogwrite = System.nanoTime();
      /**
       * now we finished run - so update watchdog state
       */
      utils.updateWatchdog();
     // utils.updateServerWatchdog();
    }
    boolean worked = false;
    // long startwork = System.currentTimeMillis();
    long startwork = System.nanoTime();
    int calcnodecount = 0;
    int wrongnodecount = 0;
    for (CalculationDP dp : this.manager.getCalcInstructionsCyclic())
    {
      // if (dp.getLastcalc() + dp.getTimeout() <
      // System.currentTimeMillis()) {
      if (dp.getLastcalc() + dp.getTimeout() < System.nanoTime())
      {
        worked = true;
        String script = dp.getScript().toString();
        // dp.setLastcalc(System.currentTimeMillis());
        dp.setLastcalc(System.nanoTime());
        String[] ir = new String[] { null };
        // add index ranges if required
        if (dp.getArrayindex() > -1)
        {
          ir[0] = dp.getArrayindex() + "";
        }
        try
        {
          DataValue dv = new DataValue();
          // long starteval = System.currentTimeMillis();
          long starteval = System.nanoTime();
        //  System.out.println(script);
          Object ret = engine.eval(script);
          if (this.drvManager.getResourceManager().isActivatedebug()
              && (this.drvManager.getResourceManager().getDebug() & 8) == 8)
          {
            if (this.drvManager.getResourceManager().getNids().contains(dp.getTarget()))
            {
              Logger.getLogger(getClass().getName()).info("Driver: " + this.drvName + " - Execute script in "
              // + (System.currentTimeMillis() -
              // starteval)
                  + (System.nanoTime() - starteval) / 1000000L + " milliseconds for node: "
                  + dp.getTarget().getTargetId() + "!");
            }
          }
          if (ret != null)
          {
            // did we have a division by 0
            if (ret instanceof Double && ((Double) ret).isInfinite())
            {
              dv.setStatusCode(StatusCodes.Bad_InvalidArgument);
              dv.setSourceTimestamp(new DateTime());
            }
            else
            {
              dv = dp.getTarget().getTargetValue(ret, dp.getArrayindex());
            }
            if (dv != null)
            {
              // long startwrite = System.currentTimeMillis();
              long startwrite = System.nanoTime();
              StatusCode[] code = drvManager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
                  new UnsignedInteger[] { Attributes.Value }, ir, new DataValue[] { dv }, new Long[] { (long) drvId });
              String description = "";
              if (code == null)
              {
                description = "Driver: " + this.drvName + " - Null statuscode for write to node service: NodeId: "
                    + dp.getTarget().getTargetId();
              }
              else if (code.length == 0)
              {
                description = "Driver: " + this.drvName
                    + " - Wrong statuscode length for write to node service: NodeId: " + dp.getTarget().getTargetId();
              }
              else if (code[0] == null)
              {
                description = "Driver: " + this.drvName + " - Null statuscode for write to node service: NodeId: "
                    + dp.getTarget().getTargetId();
              }
              else if (!code[0].equalsStatusCode(StatusCode.GOOD))
              {
                description = code[0].getDescription();
              }
              if (!description.isEmpty())
              {
                wrongnodecount++;
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, description);
              }
              else
              {
                calcnodecount++;
                if (this.drvManager.getResourceManager().isActivatedebug()
                    && (this.drvManager.getResourceManager().getDebug()
                        & this.drvManager.getResourceManager().DEBUG_READNODE) == this.drvManager
                            .getResourceManager().DEBUG_READNODE)
                {
                  if (this.drvManager.getResourceManager().getNids().contains(dp.getTarget()))
                  {
                    Logger.getLogger(getClass().getName())
                        .info("Driver: " + this.drvName + " - Write node to address space! Nodeid: "
                            + dp.getTarget().getTargetId() + " in "
                            + (System
                                // .currentTimeMillis()
                                // - startwrite)
                                .nanoTime() - startwrite) / 1000000L
                            + " milliseconds!");
                  }
                }
              }
            }
          }
        }
        catch (ScriptException e)
        {
          Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
          wrongnodecount++;
          DataValue dv = new DataValue();
          dv.setStatusCode(StatusCodes.Bad_InvalidState);
          dv.setSourceTimestamp(new DateTime());
          StatusCode[] code = drvManager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
              new UnsignedInteger[] { Attributes.Value }, ir, new DataValue[] { dv }, new Long[] { (long) drvId });
          String description = "";
          if (code == null || code[0] == null)
          {
            description = "Driver: " + this.drvName + " - Null statuscode for write to node service: NodeId: "
                + dp.getTarget().getTargetId();
          }
          else if (code.length == 0)
          {
            description = "Driver: " + this.drvName + " - Wrong statuscode length for write to node service: NodeId: "
                + dp.getTarget().getTargetId();
          }
          else if (!code[0].equalsStatusCode(StatusCode.GOOD))
          {
            description = code[0].getDescription();
          }
          if (!description.isEmpty())
          {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, description);
          }
        }
      }
    }
    if (this.drvManager.getResourceManager().isActivatedebug()
        && (this.drvManager.getResourceManager().getDebug() & 2) == 2)
    {
      Logger.getLogger(getClass().getName())
          .info("Driver " + this.drvName + " - Calculationcycle finished : total|successfull|failure : "
              + (calcnodecount + wrongnodecount) + "|" + calcnodecount + "|" + wrongnodecount + " in: "
              // + (System.currentTimeMillis() - startwork)
              + (System.nanoTime() - startwork) / 1000000L + " milliseconds.");
    }
    return worked;
  }
  private long lastwatchdogwrite = 0;

  @Override
  public boolean doFinish()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setDrvReconnectTimeout(String timeout)
  {
    // TODO Auto-generated method stub
  }

//  @Override
//  public long getLicenseProductCode()
//  {
//    return 1103;
//  }
}
