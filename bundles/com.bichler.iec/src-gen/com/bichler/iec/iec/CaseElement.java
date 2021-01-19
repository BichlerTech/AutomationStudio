/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Case Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.CaseElement#getCaseList <em>Case List</em>}</li>
 *   <li>{@link com.bichler.iec.iec.CaseElement#getStatementList <em>Statement List</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getCaseElement()
 * @model
 * @generated
 */
public interface CaseElement extends EObject
{
  /**
   * Returns the value of the '<em><b>Case List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Case List</em>' containment reference.
   * @see #setCaseList(CaseList)
   * @see com.bichler.iec.iec.IecPackage#getCaseElement_CaseList()
   * @model containment="true"
   * @generated
   */
  CaseList getCaseList();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.CaseElement#getCaseList <em>Case List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Case List</em>' containment reference.
   * @see #getCaseList()
   * @generated
   */
  void setCaseList(CaseList value);

  /**
   * Returns the value of the '<em><b>Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Statement List</em>' containment reference.
   * @see #setStatementList(StatementList)
   * @see com.bichler.iec.iec.IecPackage#getCaseElement_StatementList()
   * @model containment="true"
   * @generated
   */
  StatementList getStatementList();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.CaseElement#getStatementList <em>Statement List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Statement List</em>' containment reference.
   * @see #getStatementList()
   * @generated
   */
  void setStatementList(StatementList value);

} // CaseElement
