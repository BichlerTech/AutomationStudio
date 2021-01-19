/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Task Initialization</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.TaskInitialization#getSingle <em>Single</em>}</li>
 *   <li>{@link com.bichler.iec.iec.TaskInitialization#getInterval <em>Interval</em>}</li>
 *   <li>{@link com.bichler.iec.iec.TaskInitialization#getPrior <em>Prior</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getTaskInitialization()
 * @model
 * @generated
 */
public interface TaskInitialization extends EObject
{
  /**
   * Returns the value of the '<em><b>Single</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Single</em>' containment reference.
   * @see #setSingle(DataSource)
   * @see com.bichler.iec.iec.IecPackage#getTaskInitialization_Single()
   * @model containment="true"
   * @generated
   */
  DataSource getSingle();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TaskInitialization#getSingle <em>Single</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Single</em>' containment reference.
   * @see #getSingle()
   * @generated
   */
  void setSingle(DataSource value);

  /**
   * Returns the value of the '<em><b>Interval</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Interval</em>' containment reference.
   * @see #setInterval(DataSource)
   * @see com.bichler.iec.iec.IecPackage#getTaskInitialization_Interval()
   * @model containment="true"
   * @generated
   */
  DataSource getInterval();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TaskInitialization#getInterval <em>Interval</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Interval</em>' containment reference.
   * @see #getInterval()
   * @generated
   */
  void setInterval(DataSource value);

  /**
   * Returns the value of the '<em><b>Prior</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prior</em>' attribute.
   * @see #setPrior(int)
   * @see com.bichler.iec.iec.IecPackage#getTaskInitialization_Prior()
   * @model
   * @generated
   */
  int getPrior();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TaskInitialization#getPrior <em>Prior</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prior</em>' attribute.
   * @see #getPrior()
   * @generated
   */
  void setPrior(int value);

} // TaskInitialization
