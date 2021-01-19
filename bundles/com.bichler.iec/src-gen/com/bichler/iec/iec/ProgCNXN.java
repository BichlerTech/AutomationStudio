/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Prog CNXN</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ProgCNXN#getVariablename <em>Variablename</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ProgCNXN#getProgd <em>Progd</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getProgCNXN()
 * @model
 * @generated
 */
public interface ProgCNXN extends ProgConfElement
{
  /**
   * Returns the value of the '<em><b>Variablename</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variablename</em>' attribute.
   * @see #setVariablename(String)
   * @see com.bichler.iec.iec.IecPackage#getProgCNXN_Variablename()
   * @model
   * @generated
   */
  String getVariablename();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgCNXN#getVariablename <em>Variablename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variablename</em>' attribute.
   * @see #getVariablename()
   * @generated
   */
  void setVariablename(String value);

  /**
   * Returns the value of the '<em><b>Progd</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Progd</em>' containment reference.
   * @see #setProgd(EObject)
   * @see com.bichler.iec.iec.IecPackage#getProgCNXN_Progd()
   * @model containment="true"
   * @generated
   */
  EObject getProgd();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgCNXN#getProgd <em>Progd</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Progd</em>' containment reference.
   * @see #getProgd()
   * @generated
   */
  void setProgd(EObject value);

} // ProgCNXN
