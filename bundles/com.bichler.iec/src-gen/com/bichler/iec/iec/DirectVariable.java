/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Direct Variable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.DirectVariable#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getDirectVariable()
 * @model
 * @generated
 */
public interface DirectVariable extends VariableAccess, ProgDataSource, DataSource
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.bichler.iec.iec.IecPackage#getDirectVariable_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.DirectVariable#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // DirectVariable
