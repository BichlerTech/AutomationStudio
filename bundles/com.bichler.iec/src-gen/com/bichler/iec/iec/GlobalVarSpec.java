/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Global Var Spec</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.GlobalVarSpec#getVariable <em>Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.GlobalVarSpec#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getGlobalVarSpec()
 * @model
 * @generated
 */
public interface GlobalVarSpec extends EObject
{
  /**
   * Returns the value of the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variable</em>' containment reference.
   * @see #setVariable(GlobalVar)
   * @see com.bichler.iec.iec.IecPackage#getGlobalVarSpec_Variable()
   * @model containment="true"
   * @generated
   */
  GlobalVar getVariable();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.GlobalVarSpec#getVariable <em>Variable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variable</em>' containment reference.
   * @see #getVariable()
   * @generated
   */
  void setVariable(GlobalVar value);

  /**
   * Returns the value of the '<em><b>Location</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location</em>' containment reference.
   * @see #setLocation(Location)
   * @see com.bichler.iec.iec.IecPackage#getGlobalVarSpec_Location()
   * @model containment="true"
   * @generated
   */
  Location getLocation();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.GlobalVarSpec#getLocation <em>Location</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location</em>' containment reference.
   * @see #getLocation()
   * @generated
   */
  void setLocation(Location value);

} // GlobalVarSpec
