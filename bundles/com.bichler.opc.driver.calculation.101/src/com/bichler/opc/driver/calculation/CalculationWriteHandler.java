package com.bichler.opc.driver.calculation;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.opc.comdrv.IWriteListener;

public class CalculationWriteHandler implements IWriteListener
{
  private long drvId = 0;

  public long getDrvId()
  {
    return drvId;
  }

  public void setDrvId(long drvId)
  {
    this.drvId = drvId;
  }
  private CalculationResourceManager manager = null;
  private ScriptEngineManager scrmanager = null;
  private ScriptEngine engine = null;
  private ComDRVManager drvmanager = ComDRVManager.getDRVManager();

  public CalculationWriteHandler()
  {
    scrmanager = new ScriptEngineManager();
    engine = scrmanager.getEngineByName("Nashorn");
  }

  @Override
  public boolean prepareWrite(NodeId nodeId)
  {
    return false;
  }

  @Override
  public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState)
  {
    return null;
  }

  /**
   * not used in this modbus because we can not send an message direct, before
   * the server ask us.
   */
  @Override
  public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, long senderState)
  {
    String drvName = this.drvmanager.getDriver(drvId).getDriverName();
    // if (manager.getCalcInstructionsValueChange().containsKey(nodeId)) {
    CalculationDP dp = manager.getCalcInstructionsValueChange().get(nodeId);
    if (dp == null)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE,
          "Driver: " + drvName + " - No node with nodeid: " + nodeId.toString() + " to calculate onwritevalue found!");
      return null;
    }
    DataValue dv = new DataValue();
    String script = dp.getScript().toString();
    try
    {
      Object ret = engine.eval(script);
      if (ret != null)
      {
        // did we have a division by 0
        if (ret instanceof Double && ((Double) ret).isInfinite())
        {
          dv.setStatusCode(StatusCodes.Bad_InternalError);
          dv.setSourceTimestamp(new DateTime());
        }
        else
        {
          dv = dp.getTarget().getTargetValue(ret, dp.getArrayindex());
        }
        if (dv != null)
        {
          StatusCode[] code = drvmanager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
              new UnsignedInteger[] { Attributes.Value }, null, new DataValue[] { dv }, new Long[] { (long) drvId });
          String description = "";
          if (code == null)
          {
            description = "Driver: " + drvName + " - Null statuscode for write to node service: NodeId: "
                + dp.getTarget().getTargetId();
          }
          else if (code.length == 0)
          {
            description = "Driver: " + drvName + " - Wrong statuscode length for write to node service: NodeId: "
                + dp.getTarget().getTargetId();
          }
          else if (code[0] == null)
          {
            description = "Driver: " + drvName + " - Null statuscode for write to node service: NodeId: "
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
    catch (ScriptException e)
    {
      dv.setStatusCode(StatusCodes.Bad_InvalidArgument);
      dv.setSourceTimestamp(new DateTime());
      StatusCode[] code = drvmanager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
          new UnsignedInteger[] { Attributes.Value }, new String[] { null }, new DataValue[] { dv },
          new Long[] { (long) drvId });
      String description = "";
      if (code == null)
      {
        description = "Driver: " + drvName + " - Null statuscode for write to node service: NodeId: "
            + dp.getTarget().getTargetId();
      }
      else if (code.length == 0)
      {
        description = "Driver: " + drvName + " - Wrong statuscode length for write to node service: NodeId: "
            + dp.getTarget().getTargetId();
      }
      else if (code[0] == null)
      {
        description = "Driver: " + drvName + " - Null statuscode for write to node service: NodeId: "
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
    // }
    return null;
  }

  @Override
  public StatusCode asyncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
      long senderState)
  {
    return null;
  }

  /**
   * not used in this modbus because we can not send an message direct, before
   * the server ask us.
   */
  @Override
  public StatusCode syncWriteValue(NodeId nodeId, DataValue value, DataValue oldValue, String indexRange,
      long senderState)
  {
    // if (manager.getCalcInstructionsValueChange().containsKey(nodeId)) {
    String drvName = this.drvmanager.getDriver(drvId).getDriverName();
    CalculationDP dp = manager.getCalcInstructionsValueChange().get(nodeId);
    if (dp == null)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE,
          "Driver: " + drvName + " - No node with nodeid: " + nodeId.toString() + " to calculate onwritevalue found!");
      return null;
    }
    DataValue dv = new DataValue();
    String script = dp.getScript().toString();
    String[] ir = new String[] { null };
    // add index ranges if required
    if (dp.getArrayindex() > -1)
    {
      ir[0] = dp.getArrayindex() + "";
    }
    try
    {
    	System.out.println(script);
      Object ret = engine.eval(script);
      if (ret != null)
      {
        // did we have a division by 0
        if (ret instanceof Double && ((Double) ret).isInfinite())
        {
          dv.setStatusCode(StatusCodes.Bad_InternalError);
          dv.setSourceTimestamp(new DateTime());
        }
        else
        {
          dv = dp.getTarget().getTargetValue(ret, dp.getArrayindex());
        }
        if (dv != null)
        {
          StatusCode[] code = drvmanager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
              new UnsignedInteger[] { Attributes.Value }, ir, new DataValue[] { dv }, new Long[] { (long) drvId });
          String description = "";
          if (code == null)
          {
            description = "Driver: " + drvName + " - Null statuscode for write to node service: NodeId: "
                + dp.getTarget().getTargetId();
          }
          else if (code.length == 0)
          {
            description = "Driver: " + drvName + " - Wrong statuscode length for write to node service: NodeId: "
                + dp.getTarget().getTargetId();
          }
          else if (code[0] == null)
          {
            description = "Driver: " + drvName + " - Null statuscode for write to node service: NodeId: "
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
          return StatusCode.GOOD;
        }
      }
    }
    catch (ScriptException e)
    {
      dv.setStatusCode(StatusCodes.Bad_InvalidArgument);
      dv.setSourceTimestamp(new DateTime());
      StatusCode[] code = drvmanager.writeFromDriver(new NodeId[] { dp.getTarget().getTargetId() },
          new UnsignedInteger[] { Attributes.Value }, ir, new DataValue[] { dv }, new Long[] { (long) drvId });
      String description = "";
      if (code == null || code[0] == null)
      {
        description = "Driver: " + drvName + " - Null statuscode for write to node service: NodeId: "
            + dp.getTarget().getTargetId();
      }
      else if (code.length == 0)
      {
        description = "Driver: " + drvName + " - Wrong statuscode length for write to node service: NodeId: "
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
      return new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
    }
    return StatusCode.BAD;
  }

  public CalculationResourceManager getManager()
  {
    return manager;
  }

  public void setManager(CalculationResourceManager manager)
  {
    this.manager = manager;
  }
}
