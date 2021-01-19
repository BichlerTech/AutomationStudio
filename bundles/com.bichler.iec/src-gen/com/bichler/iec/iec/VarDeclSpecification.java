/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Var Decl Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.VarDeclSpecification#getSpecInit <em>Spec Init</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getVarDeclSpecification()
 * @model
 * @generated
 */
public interface VarDeclSpecification extends DeclSpecification
{
  /**
   * Returns the value of the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Spec Init</em>' containment reference.
   * @see #setSpecInit(SpecInit)
   * @see com.bichler.iec.iec.IecPackage#getVarDeclSpecification_SpecInit()
   * @model containment="true"
   * @generated
   */
  SpecInit getSpecInit();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.VarDeclSpecification#getSpecInit <em>Spec Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Spec Init</em>' containment reference.
   * @see #getSpecInit()
   * @generated
   */
  void setSpecInit(SpecInit value);

} // VarDeclSpecification
