/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Var Init Decl</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.VarInitDecl#getVar1List <em>Var1 List</em>}</li>
 *   <li>{@link com.bichler.iec.iec.VarInitDecl#getSpecInit <em>Spec Init</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getVarInitDecl()
 * @model
 * @generated
 */
public interface VarInitDecl extends EObject
{
  /**
   * Returns the value of the '<em><b>Var1 List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Var1 List</em>' containment reference.
   * @see #setVar1List(Var1List)
   * @see com.bichler.iec.iec.IecPackage#getVarInitDecl_Var1List()
   * @model containment="true"
   * @generated
   */
  Var1List getVar1List();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.VarInitDecl#getVar1List <em>Var1 List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Var1 List</em>' containment reference.
   * @see #getVar1List()
   * @generated
   */
  void setVar1List(Var1List value);

  /**
   * Returns the value of the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Spec Init</em>' containment reference.
   * @see #setSpecInit(SpecInit)
   * @see com.bichler.iec.iec.IecPackage#getVarInitDecl_SpecInit()
   * @model containment="true"
   * @generated
   */
  SpecInit getSpecInit();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.VarInitDecl#getSpecInit <em>Spec Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Spec Init</em>' containment reference.
   * @see #getSpecInit()
   * @generated
   */
  void setSpecInit(SpecInit value);

} // VarInitDecl
