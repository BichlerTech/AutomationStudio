/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Boolean</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.Boolean#getBoolInt <em>Bool Int</em>}</li>
 *   <li>{@link com.bichler.iec.iec.Boolean#isTrue <em>True</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getBoolean()
 * @model
 * @generated
 */
public interface Boolean extends Constant
{
  /**
   * Returns the value of the '<em><b>Bool Int</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Bool Int</em>' attribute.
   * @see #setBoolInt(String)
   * @see com.bichler.iec.iec.IecPackage#getBoolean_BoolInt()
   * @model
   * @generated
   */
  String getBoolInt();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Boolean#getBoolInt <em>Bool Int</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Bool Int</em>' attribute.
   * @see #getBoolInt()
   * @generated
   */
  void setBoolInt(String value);

  /**
   * Returns the value of the '<em><b>True</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>True</em>' attribute.
   * @see #setTrue(boolean)
   * @see com.bichler.iec.iec.IecPackage#getBoolean_True()
   * @model
   * @generated
   */
  boolean isTrue();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Boolean#isTrue <em>True</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>True</em>' attribute.
   * @see #isTrue()
   * @generated
   */
  void setTrue(boolean value);

} // Boolean
