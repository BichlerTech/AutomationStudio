/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Located Var Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.LocatedVarDeclaration#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.LocatedVarDeclaration#getLocation <em>Location</em>}</li>
 *   <li>{@link com.bichler.iec.iec.LocatedVarDeclaration#getSpecInit <em>Spec Init</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getLocatedVarDeclaration()
 * @model
 * @generated
 */
public interface LocatedVarDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.bichler.iec.iec.IecPackage#getLocatedVarDeclaration_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.LocatedVarDeclaration#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Location</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location</em>' containment reference.
   * @see #setLocation(Location)
   * @see com.bichler.iec.iec.IecPackage#getLocatedVarDeclaration_Location()
   * @model containment="true"
   * @generated
   */
  Location getLocation();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.LocatedVarDeclaration#getLocation <em>Location</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location</em>' containment reference.
   * @see #getLocation()
   * @generated
   */
  void setLocation(Location value);

  /**
   * Returns the value of the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Spec Init</em>' containment reference.
   * @see #setSpecInit(SpecInit)
   * @see com.bichler.iec.iec.IecPackage#getLocatedVarDeclaration_SpecInit()
   * @model containment="true"
   * @generated
   */
  SpecInit getSpecInit();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.LocatedVarDeclaration#getSpecInit <em>Spec Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Spec Init</em>' containment reference.
   * @see #getSpecInit()
   * @generated
   */
  void setSpecInit(SpecInit value);

} // LocatedVarDeclaration
