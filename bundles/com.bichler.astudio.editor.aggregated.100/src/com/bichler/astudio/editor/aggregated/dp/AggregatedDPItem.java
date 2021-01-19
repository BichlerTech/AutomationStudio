package com.bichler.astudio.editor.aggregated.dp;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;

import com.bichler.astudio.editor.aggregated.transformation.AGGREGATED_MAPPING_TYPE;
import com.bichler.astudio.editor.aggregated.transformation.Aggregated_Transformation;
import com.bichler.opc.comdrv.utils.ValueOutOfRangeException;
public class AggregatedDPItem
{
  protected Object[] targetArray;
  protected Object[] internArray;
  private String displayName;
  protected DataValue dv = new DataValue();
  protected Aggregated_Transformation transform = null;
  protected int arrayLength = 0;

  public NodeId getTargetNodeId()
  {
    return targetNodeId;
  }

  public void setTargetNodeId(NodeId targetNodeId)
  {
    this.targetNodeId = targetNodeId;
  }

  public String getServerBrowsePath()
  {
    return serverBrowsePath;
  }

  public void setServerBrowsePath(String serverBrowsePath)
  {
    this.serverBrowsePath = serverBrowsePath;
  }

  public String getTargetBrowsePath()
  {
    return targetBrowsePath;
  }

  public void setTargetBrowsePath(String targetBrowsePath)
  {
    this.targetBrowsePath = targetBrowsePath;
  }

  public String getServerDisplayName()
  {
    return serverDisplayName;
  }

  public void setServerDisplayName(String serverDisplayName)
  {
    this.serverDisplayName = serverDisplayName;
  }

  public String getTargetDisplayName()
  {
    return targetDisplayName;
  }

  public void setTargetDisplayName(String targetDisplayName)
  {
    this.targetDisplayName = targetDisplayName;
  }

  public boolean isRead()
  {
    return read;
  }

  public void setRead(boolean read)
  {
    this.read = read;
  }

  public boolean isWrite()
  {
    return write;
  }

  public void setWrite(boolean write)
  {
    this.write = write;
  }
  /**
   * id of node to which the dp is connected
   */
  protected NodeId serverNodeId = null;
  /**
   * id of node to which the dp is connected
   */
  protected NodeId targetNodeId = null;
  /**
   * id of node to which the dp is connected
   */
  protected String loadedTargetNodeId = null;

  public String getLoadedTargetNodeId()
  {
    return loadedTargetNodeId;
  }

  public void setLoadedTargetNodeId(String loadedTargetNodeId)
  {
    this.loadedTargetNodeId = loadedTargetNodeId;
  }
  /**
   * opc ua browsepath from objects folder
   */
  private String serverBrowsePath = "";
  /**
   * opc ua browsepath from objects folder
   */
  private String targetBrowsePath = "";
  private String serverDisplayName = "";
  private String targetDisplayName = "";
  /**
   * flag if the datapoint is active or not
   */
  protected boolean active = false;
  /**
   * flag if the datapoint should be read or not
   */
  protected boolean read = false;
  /**
   * flag if the datapoint should be written or not
   */
  protected boolean write = false;
  private AGGREGATED_MAPPING_TYPE mapping = AGGREGATED_MAPPING_TYPE.SCALAR;

  public AGGREGATED_MAPPING_TYPE getMapping()
  {
    return mapping;
  }

  public void setMapping(AGGREGATED_MAPPING_TYPE mapping)
  {
    this.mapping = mapping;
  }

  public NodeId getServerNodeId()
  {
    return serverNodeId;
  }

  public void setServerNodeId(NodeId serverNodeId)
  {
    this.serverNodeId = serverNodeId;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive(boolean active)
  {
    this.active = active;
  }

  public Aggregated_Transformation getTransform()
  {
    return transform;
  }

  public void setTransform(Aggregated_Transformation transform)
  {
    this.transform = transform;
  }

  /**
   * 
   * @param data
   * @return
   */
  public DataValue drv2Prog(DataValue data)
  {
    dv.setSourceTimestamp(new DateTime());
    try
    {
      if (this.transform == null)
      {
        dv.setStatusCode(StatusCode.BAD);
      }
      else
      {
        if (this.mapping == AGGREGATED_MAPPING_TYPE.SCALAR)
        {
          dv.setValue(new Variant(this.transform.transToIntern(data.getValue().getValue())));
        }
        else if (this.mapping == AGGREGATED_MAPPING_TYPE.ARRAY)
        {
          Object[] obj = (Object[]) data.getValue().getValue();
          for (int i = 0; i < obj.length; i++)
          {
            this.internArray[i] = this.transform.transToIntern(obj);
          }
        }
        dv.setStatusCode(StatusCode.GOOD);
      }
    }
    catch (ClassCastException ex)
    {
      // this.logger.error1(
      // "Couldn't cast remote value to intern value! Node: "
      // + this.getDisplayName(), CometModuls.STR_DRV,
      // CometModuls.INT_DRV, CometDRV.BUNDLEID, CometDRV.VERSIONID);
      dv.setStatusCode(StatusCodes.Bad_TypeMismatch);
    }
    catch (ValueOutOfRangeException e)
    {
      // this.logger.error1(e.getMessage(), this.getDisplayName(),
      // CometModuls.INT_DRV, CometDRV.BUNDLEID, CometDRV.VERSIONID);
      dv.setStatusCode(StatusCodes.Bad_OutOfRange);
    }
    return dv;
  }

  public String getDisplayName()
  {
    return displayName;
  }

  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  public Object[] getTargetArray()
  {
    return targetArray;
  }

  public void setTargetArray(Object[] targetArray)
  {
    this.targetArray = targetArray;
  }

  public int getArrayLength()
  {
    return arrayLength;
  }

  public void setArrayLength(int arrayLength)
  {
    this.arrayLength = arrayLength;
  }
}
