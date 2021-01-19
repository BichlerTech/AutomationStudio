/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Global Var Decl</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.GlobalVarDecl#getSpec <em>Spec</em>}</li>
 *   <li>{@link com.bichler.iec.iec.GlobalVarDecl#getSpecInit <em>Spec Init</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getGlobalVarDecl()
 * @model
 * @generated
 */
public interface GlobalVarDecl extends EObject
{
  /**
   * Returns the value of the '<em><b>Spec</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Spec</em>' containment reference.
   * @see #setSpec(GlobalVarSpec)
   * @see com.bichler.iec.iec.IecPackage#getGlobalVarDecl_Spec()
   * @model containment="true"
   * @generated
   */
  GlobalVarSpec getSpec();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.GlobalVarDecl#getSpec <em>Spec</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Spec</em>' containment reference.
   * @see #getSpec()
   * @generated
   */
  void setSpec(GlobalVarSpec value);

  /**
   * Returns the value of the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Spec Init</em>' containment reference.
   * @see #setSpecInit(SpecInit)
   * @see com.bichler.iec.iec.IecPackage#getGlobalVarDecl_SpecInit()
   * @model containment="true"
   * @generated
   */
  SpecInit getSpecInit();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.GlobalVarDecl#getSpecInit <em>Spec Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Spec Init</em>' containment reference.
   * @see #getSpecInit()
   * @generated
   */
  void setSpecInit(SpecInit value);

} // GlobalVarDecl
