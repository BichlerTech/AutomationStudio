/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Date Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.DateLiteral#getYear <em>Year</em>}</li>
 *   <li>{@link com.bichler.iec.iec.DateLiteral#getMonth <em>Month</em>}</li>
 *   <li>{@link com.bichler.iec.iec.DateLiteral#getDay <em>Day</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getDateLiteral()
 * @model
 * @generated
 */
public interface DateLiteral extends TimeLiteral
{
  /**
   * Returns the value of the '<em><b>Year</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Year</em>' attribute.
   * @see #setYear(String)
   * @see com.bichler.iec.iec.IecPackage#getDateLiteral_Year()
   * @model
   * @generated
   */
  String getYear();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.DateLiteral#getYear <em>Year</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Year</em>' attribute.
   * @see #getYear()
   * @generated
   */
  void setYear(String value);

  /**
   * Returns the value of the '<em><b>Month</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Month</em>' attribute.
   * @see #setMonth(String)
   * @see com.bichler.iec.iec.IecPackage#getDateLiteral_Month()
   * @model
   * @generated
   */
  String getMonth();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.DateLiteral#getMonth <em>Month</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Month</em>' attribute.
   * @see #getMonth()
   * @generated
   */
  void setMonth(String value);

  /**
   * Returns the value of the '<em><b>Day</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Day</em>' attribute.
   * @see #setDay(String)
   * @see com.bichler.iec.iec.IecPackage#getDateLiteral_Day()
   * @model
   * @generated
   */
  String getDay();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.DateLiteral#getDay <em>Day</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Day</em>' attribute.
   * @see #getDay()
   * @generated
   */
  void setDay(String value);

} // DateLiteral
