package com.bichler.astudio.editor.events.properties;

import com.bichler.astudio.opcua.properties.driver.AbstractDriverPropertySource;
import com.bichler.astudio.opcua.properties.driver.IDriverNode;

public class EventsPropertySource extends AbstractDriverPropertySource
{
  public EventsPropertySource(IDriverNode adapter)
  {
    super(adapter);
  }
}
