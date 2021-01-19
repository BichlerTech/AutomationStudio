/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Declarations</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.InputDeclarations#getDeclarations <em>Declarations</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getInputDeclarations()
 * @model
 * @generated
 */
public interface InputDeclarations extends IoVarDeclarations
{
  /**
   * Returns the value of the '<em><b>Declarations</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.InputDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Declarations</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getInputDeclarations_Declarations()
   * @model containment="true"
   * @generated
   */
  EList<InputDeclaration> getDeclarations();

} // InputDeclarations
