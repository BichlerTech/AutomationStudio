/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.DerivedType;
import com.bichler.iec.iec.EnumeratedValue;
import com.bichler.iec.iec.IecPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enumerated Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.EnumeratedValueImpl#getDerivedType <em>Derived Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.EnumeratedValueImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnumeratedValueImpl extends InitialElementImpl implements EnumeratedValue
{
  /**
   * The cached value of the '{@link #getDerivedType() <em>Derived Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDerivedType()
   * @generated
   * @ordered
   */
  protected DerivedType derivedType;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EnumeratedValueImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return IecPackage.Literals.ENUMERATED_VALUE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DerivedType getDerivedType()
  {
    return derivedType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDerivedType(DerivedType newDerivedType, NotificationChain msgs)
  {
    DerivedType oldDerivedType = derivedType;
    derivedType = newDerivedType;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.ENUMERATED_VALUE__DERIVED_TYPE, oldDerivedType, newDerivedType);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setDerivedType(DerivedType newDerivedType)
  {
    if (newDerivedType != derivedType)
    {
      NotificationChain msgs = null;
      if (derivedType != null)
        msgs = ((InternalEObject)derivedType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.ENUMERATED_VALUE__DERIVED_TYPE, null, msgs);
      if (newDerivedType != null)
        msgs = ((InternalEObject)newDerivedType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.ENUMERATED_VALUE__DERIVED_TYPE, null, msgs);
      msgs = basicSetDerivedType(newDerivedType, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ENUMERATED_VALUE__DERIVED_TYPE, newDerivedType, newDerivedType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ENUMERATED_VALUE__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case IecPackage.ENUMERATED_VALUE__DERIVED_TYPE:
        return basicSetDerivedType(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case IecPackage.ENUMERATED_VALUE__DERIVED_TYPE:
        return getDerivedType();
      case IecPackage.ENUMERATED_VALUE__NAME:
        return getName();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case IecPackage.ENUMERATED_VALUE__DERIVED_TYPE:
        setDerivedType((DerivedType)newValue);
        return;
      case IecPackage.ENUMERATED_VALUE__NAME:
        setName((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case IecPackage.ENUMERATED_VALUE__DERIVED_TYPE:
        setDerivedType((DerivedType)null);
        return;
      case IecPackage.ENUMERATED_VALUE__NAME:
        setName(NAME_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case IecPackage.ENUMERATED_VALUE__DERIVED_TYPE:
        return derivedType != null;
      case IecPackage.ENUMERATED_VALUE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //EnumeratedValueImpl
