/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.DerivedType;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.InitializedStructure;
import com.bichler.iec.iec.StructureInitialization;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Initialized Structure</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.InitializedStructureImpl#getDerivedType <em>Derived Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.InitializedStructureImpl#getInitialization <em>Initialization</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InitializedStructureImpl extends MinimalEObjectImpl.Container implements InitializedStructure
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
   * The cached value of the '{@link #getInitialization() <em>Initialization</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInitialization()
   * @generated
   * @ordered
   */
  protected StructureInitialization initialization;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InitializedStructureImpl()
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
    return IecPackage.Literals.INITIALIZED_STRUCTURE;
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE, oldDerivedType, newDerivedType);
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
        msgs = ((InternalEObject)derivedType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE, null, msgs);
      if (newDerivedType != null)
        msgs = ((InternalEObject)newDerivedType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE, null, msgs);
      msgs = basicSetDerivedType(newDerivedType, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE, newDerivedType, newDerivedType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StructureInitialization getInitialization()
  {
    return initialization;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetInitialization(StructureInitialization newInitialization, NotificationChain msgs)
  {
    StructureInitialization oldInitialization = initialization;
    initialization = newInitialization;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION, oldInitialization, newInitialization);
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
  public void setInitialization(StructureInitialization newInitialization)
  {
    if (newInitialization != initialization)
    {
      NotificationChain msgs = null;
      if (initialization != null)
        msgs = ((InternalEObject)initialization).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION, null, msgs);
      if (newInitialization != null)
        msgs = ((InternalEObject)newInitialization).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION, null, msgs);
      msgs = basicSetInitialization(newInitialization, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION, newInitialization, newInitialization));
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
      case IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE:
        return basicSetDerivedType(null, msgs);
      case IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION:
        return basicSetInitialization(null, msgs);
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
      case IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE:
        return getDerivedType();
      case IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION:
        return getInitialization();
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
      case IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE:
        setDerivedType((DerivedType)newValue);
        return;
      case IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION:
        setInitialization((StructureInitialization)newValue);
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
      case IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE:
        setDerivedType((DerivedType)null);
        return;
      case IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION:
        setInitialization((StructureInitialization)null);
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
      case IecPackage.INITIALIZED_STRUCTURE__DERIVED_TYPE:
        return derivedType != null;
      case IecPackage.INITIALIZED_STRUCTURE__INITIALIZATION:
        return initialization != null;
    }
    return super.eIsSet(featureID);
  }

} //InitializedStructureImpl
