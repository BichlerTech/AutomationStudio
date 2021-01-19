/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Initial Elements</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ArrayInitialElements#getInitialElement <em>Initial Element</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ArrayInitialElements#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getArrayInitialElements()
 * @model
 * @generated
 */
public interface ArrayInitialElements extends EObject
{
  /**
   * Returns the value of the '<em><b>Initial Element</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initial Element</em>' containment reference.
   * @see #setInitialElement(InitialElement)
   * @see com.bichler.iec.iec.IecPackage#getArrayInitialElements_InitialElement()
   * @model containment="true"
   * @generated
   */
  InitialElement getInitialElement();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ArrayInitialElements#getInitialElement <em>Initial Element</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Initial Element</em>' containment reference.
   * @see #getInitialElement()
   * @generated
   */
  void setInitialElement(InitialElement value);

  /**
   * Returns the value of the '<em><b>Index</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Index</em>' attribute.
   * @see #setIndex(String)
   * @see com.bichler.iec.iec.IecPackage#getArrayInitialElements_Index()
   * @model
   * @generated
   */
  String getIndex();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ArrayInitialElements#getIndex <em>Index</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Index</em>' attribute.
   * @see #getIndex()
   * @generated
   */
  void setIndex(String value);

} // ArrayInitialElements
