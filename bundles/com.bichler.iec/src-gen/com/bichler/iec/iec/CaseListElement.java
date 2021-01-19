/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Case List Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.CaseListElement#getSubRange <em>Sub Range</em>}</li>
 *   <li>{@link com.bichler.iec.iec.CaseListElement#getInteger <em>Integer</em>}</li>
 *   <li>{@link com.bichler.iec.iec.CaseListElement#getEnumeratedValue <em>Enumerated Value</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getCaseListElement()
 * @model
 * @generated
 */
public interface CaseListElement extends EObject
{
  /**
   * Returns the value of the '<em><b>Sub Range</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sub Range</em>' attribute.
   * @see #setSubRange(String)
   * @see com.bichler.iec.iec.IecPackage#getCaseListElement_SubRange()
   * @model
   * @generated
   */
  String getSubRange();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.CaseListElement#getSubRange <em>Sub Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sub Range</em>' attribute.
   * @see #getSubRange()
   * @generated
   */
  void setSubRange(String value);

  /**
   * Returns the value of the '<em><b>Integer</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Integer</em>' attribute.
   * @see #setInteger(String)
   * @see com.bichler.iec.iec.IecPackage#getCaseListElement_Integer()
   * @model
   * @generated
   */
  String getInteger();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.CaseListElement#getInteger <em>Integer</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Integer</em>' attribute.
   * @see #getInteger()
   * @generated
   */
  void setInteger(String value);

  /**
   * Returns the value of the '<em><b>Enumerated Value</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Enumerated Value</em>' reference.
   * @see #setEnumeratedValue(EnumeratedValue)
   * @see com.bichler.iec.iec.IecPackage#getCaseListElement_EnumeratedValue()
   * @model
   * @generated
   */
  EnumeratedValue getEnumeratedValue();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.CaseListElement#getEnumeratedValue <em>Enumerated Value</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Enumerated Value</em>' reference.
   * @see #getEnumeratedValue()
   * @generated
   */
  void setEnumeratedValue(EnumeratedValue value);

} // CaseListElement
