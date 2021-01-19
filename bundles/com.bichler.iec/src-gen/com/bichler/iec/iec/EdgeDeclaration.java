/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Edge Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.EdgeDeclaration#getVar1List <em>Var1 List</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getEdgeDeclaration()
 * @model
 * @generated
 */
public interface EdgeDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Var1 List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Var1 List</em>' containment reference.
   * @see #setVar1List(Var1List)
   * @see com.bichler.iec.iec.IecPackage#getEdgeDeclaration_Var1List()
   * @model containment="true"
   * @generated
   */
  Var1List getVar1List();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.EdgeDeclaration#getVar1List <em>Var1 List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Var1 List</em>' containment reference.
   * @see #getVar1List()
   * @generated
   */
  void setVar1List(Var1List value);

} // EdgeDeclaration
