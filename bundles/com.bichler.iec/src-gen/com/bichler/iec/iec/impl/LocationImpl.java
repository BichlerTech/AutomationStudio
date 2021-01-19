/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.DirectVariable;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.Location;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.LocationImpl#getDirectVariable <em>Direct Variable</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocationImpl extends MinimalEObjectImpl.Container implements Location
{
  /**
   * The cached value of the '{@link #getDirectVariable() <em>Direct Variable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDirectVariable()
   * @generated
   * @ordered
   */
  protected DirectVariable directVariable;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LocationImpl()
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
    return IecPackage.Literals.LOCATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DirectVariable getDirectVariable()
  {
    return directVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetDirectVariable(DirectVariable newDirectVariable, NotificationChain msgs)
  {
    DirectVariable oldDirectVariable = directVariable;
    directVariable = newDirectVariable;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.LOCATION__DIRECT_VARIABLE, oldDirectVariable, newDirectVariable);
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
  public void setDirectVariable(DirectVariable newDirectVariable)
  {
    if (newDirectVariable != directVariable)
    {
      NotificationChain msgs = null;
      if (directVariable != null)
        msgs = ((InternalEObject)directVariable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.LOCATION__DIRECT_VARIABLE, null, msgs);
      if (newDirectVariable != null)
        msgs = ((InternalEObject)newDirectVariable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.LOCATION__DIRECT_VARIABLE, null, msgs);
      msgs = basicSetDirectVariable(newDirectVariable, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.LOCATION__DIRECT_VARIABLE, newDirectVariable, newDirectVariable));
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
      case IecPackage.LOCATION__DIRECT_VARIABLE:
        return basicSetDirectVariable(null, msgs);
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
      case IecPackage.LOCATION__DIRECT_VARIABLE:
        return getDirectVariable();
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
      case IecPackage.LOCATION__DIRECT_VARIABLE:
        setDirectVariable((DirectVariable)newValue);
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
      case IecPackage.LOCATION__DIRECT_VARIABLE:
        setDirectVariable((DirectVariable)null);
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
      case IecPackage.LOCATION__DIRECT_VARIABLE:
        return directVariable != null;
    }
    return super.eIsSet(featureID);
  }

} //LocationImpl
