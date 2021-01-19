/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Of Day Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.TimeOfDayLiteral#getHour <em>Hour</em>}</li>
 *   <li>{@link com.bichler.iec.iec.TimeOfDayLiteral#getMinute <em>Minute</em>}</li>
 *   <li>{@link com.bichler.iec.iec.TimeOfDayLiteral#getSecond <em>Second</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getTimeOfDayLiteral()
 * @model
 * @generated
 */
public interface TimeOfDayLiteral extends TimeLiteral
{
  /**
   * Returns the value of the '<em><b>Hour</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hour</em>' attribute.
   * @see #setHour(String)
   * @see com.bichler.iec.iec.IecPackage#getTimeOfDayLiteral_Hour()
   * @model
   * @generated
   */
  String getHour();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TimeOfDayLiteral#getHour <em>Hour</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hour</em>' attribute.
   * @see #getHour()
   * @generated
   */
  void setHour(String value);

  /**
   * Returns the value of the '<em><b>Minute</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Minute</em>' attribute.
   * @see #setMinute(String)
   * @see com.bichler.iec.iec.IecPackage#getTimeOfDayLiteral_Minute()
   * @model
   * @generated
   */
  String getMinute();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TimeOfDayLiteral#getMinute <em>Minute</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Minute</em>' attribute.
   * @see #getMinute()
   * @generated
   */
  void setMinute(String value);

  /**
   * Returns the value of the '<em><b>Second</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Second</em>' attribute.
   * @see #setSecond(String)
   * @see com.bichler.iec.iec.IecPackage#getTimeOfDayLiteral_Second()
   * @model
   * @generated
   */
  String getSecond();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TimeOfDayLiteral#getSecond <em>Second</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Second</em>' attribute.
   * @see #getSecond()
   * @generated
   */
  void setSecond(String value);

} // TimeOfDayLiteral
