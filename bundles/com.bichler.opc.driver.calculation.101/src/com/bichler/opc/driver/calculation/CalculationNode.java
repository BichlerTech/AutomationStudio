package com.bichler.opc.driver.calculation;

public class CalculationNode extends CalculationObject
{
  private int arrayIndex = -1;

  public CalculationNode(String operation)
  {
    this.content = operation;
  }

  public int getArrayIndex()
  {
    return arrayIndex;
  }

  public void setArrayIndex(int arrayIndex)
  {
    this.arrayIndex = arrayIndex;
  }
}
