/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Sink</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.DataSink#getGlobvar <em>Globvar</em>}</li>
 *   <li>{@link com.bichler.iec.iec.DataSink#getDirvar <em>Dirvar</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getDataSink()
 * @model
 * @generated
 */
public interface DataSink extends EObject
{
  /**
   * Returns the value of the '<em><b>Globvar</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Globvar</em>' containment reference.
   * @see #setGlobvar(GlobalVar)
   * @see com.bichler.iec.iec.IecPackage#getDataSink_Globvar()
   * @model containment="true"
   * @generated
   */
  GlobalVar getGlobvar();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.DataSink#getGlobvar <em>Globvar</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Globvar</em>' containment reference.
   * @see #getGlobvar()
   * @generated
   */
  void setGlobvar(GlobalVar value);

  /**
   * Returns the value of the '<em><b>Dirvar</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Dirvar</em>' containment reference.
   * @see #setDirvar(DirectVariable)
   * @see com.bichler.iec.iec.IecPackage#getDataSink_Dirvar()
   * @model containment="true"
   * @generated
   */
  DirectVariable getDirvar();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.DataSink#getDirvar <em>Dirvar</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Dirvar</em>' containment reference.
   * @see #getDirvar()
   * @generated
   */
  void setDirvar(DirectVariable value);

} // DataSink
