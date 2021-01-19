/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.DerivedType;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.TypeDeclaration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.TypeDeclarationImpl#getDerivedType <em>Derived Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TypeDeclarationImpl extends MinimalEObjectImpl.Container implements TypeDeclaration
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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TypeDeclarationImpl()
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
    return IecPackage.Literals.TYPE_DECLARATION;
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.TYPE_DECLARATION__DERIVED_TYPE, oldDerivedType, newDerivedType);
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
        msgs = ((InternalEObject)derivedType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.TYPE_DECLARATION__DERIVED_TYPE, null, msgs);
      if (newDerivedType != null)
        msgs = ((InternalEObject)newDerivedType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.TYPE_DECLARATION__DERIVED_TYPE, null, msgs);
      msgs = basicSetDerivedType(newDerivedType, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.TYPE_DECLARATION__DERIVED_TYPE, newDerivedType, newDerivedType));
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
      case IecPackage.TYPE_DECLARATION__DERIVED_TYPE:
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
      case IecPackage.TYPE_DECLARATION__DERIVED_TYPE:
        return getDerivedType();
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
      case IecPackage.TYPE_DECLARATION__DERIVED_TYPE:
        setDerivedType((DerivedType)newValue);
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
      case IecPackage.TYPE_DECLARATION__DERIVED_TYPE:
        setDerivedType((DerivedType)null);
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
      case IecPackage.TYPE_DECLARATION__DERIVED_TYPE:
        return derivedType != null;
    }
    return super.eIsSet(featureID);
  }

} //TypeDeclarationImpl
