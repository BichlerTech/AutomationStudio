/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Named Variable Access</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.NamedVariableAccess#getNamedVariable <em>Named Variable</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getNamedVariableAccess()
 * @model
 * @generated
 */
public interface NamedVariableAccess extends SymbolicVariableAccess
{
  /**
   * Returns the value of the '<em><b>Named Variable</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Named Variable</em>' reference.
   * @see #setNamedVariable(Variable)
   * @see com.bichler.iec.iec.IecPackage#getNamedVariableAccess_NamedVariable()
   * @model
   * @generated
   */
  Variable getNamedVariable();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.NamedVariableAccess#getNamedVariable <em>Named Variable</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Named Variable</em>' reference.
   * @see #getNamedVariable()
   * @generated
   */
  void setNamedVariable(Variable value);

} // NamedVariableAccess
