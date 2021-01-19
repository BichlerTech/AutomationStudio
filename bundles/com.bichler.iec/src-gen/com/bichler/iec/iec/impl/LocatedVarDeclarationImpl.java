/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.LocatedVarDeclaration;
import com.bichler.iec.iec.Location;
import com.bichler.iec.iec.SpecInit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Located Var Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.LocatedVarDeclarationImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.LocatedVarDeclarationImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.LocatedVarDeclarationImpl#getSpecInit <em>Spec Init</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocatedVarDeclarationImpl extends MinimalEObjectImpl.Container implements LocatedVarDeclaration
{
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
   * The cached value of the '{@link #getLocation() <em>Location</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected Location location;

  /**
   * The cached value of the '{@link #getSpecInit() <em>Spec Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSpecInit()
   * @generated
   * @ordered
   */
  protected SpecInit specInit;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LocatedVarDeclarationImpl()
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
    return IecPackage.Literals.LOCATED_VAR_DECLARATION;
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
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.LOCATED_VAR_DECLARATION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Location getLocation()
  {
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetLocation(Location newLocation, NotificationChain msgs)
  {
    Location oldLocation = location;
    location = newLocation;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.LOCATED_VAR_DECLARATION__LOCATION, oldLocation, newLocation);
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
  public void setLocation(Location newLocation)
  {
    if (newLocation != location)
    {
      NotificationChain msgs = null;
      if (location != null)
        msgs = ((InternalEObject)location).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.LOCATED_VAR_DECLARATION__LOCATION, null, msgs);
      if (newLocation != null)
        msgs = ((InternalEObject)newLocation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.LOCATED_VAR_DECLARATION__LOCATION, null, msgs);
      msgs = basicSetLocation(newLocation, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.LOCATED_VAR_DECLARATION__LOCATION, newLocation, newLocation));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SpecInit getSpecInit()
  {
    return specInit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSpecInit(SpecInit newSpecInit, NotificationChain msgs)
  {
    SpecInit oldSpecInit = specInit;
    specInit = newSpecInit;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT, oldSpecInit, newSpecInit);
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
  public void setSpecInit(SpecInit newSpecInit)
  {
    if (newSpecInit != specInit)
    {
      NotificationChain msgs = null;
      if (specInit != null)
        msgs = ((InternalEObject)specInit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT, null, msgs);
      if (newSpecInit != null)
        msgs = ((InternalEObject)newSpecInit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT, null, msgs);
      msgs = basicSetSpecInit(newSpecInit, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT, newSpecInit, newSpecInit));
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
      case IecPackage.LOCATED_VAR_DECLARATION__LOCATION:
        return basicSetLocation(null, msgs);
      case IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT:
        return basicSetSpecInit(null, msgs);
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
      case IecPackage.LOCATED_VAR_DECLARATION__NAME:
        return getName();
      case IecPackage.LOCATED_VAR_DECLARATION__LOCATION:
        return getLocation();
      case IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT:
        return getSpecInit();
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
      case IecPackage.LOCATED_VAR_DECLARATION__NAME:
        setName((String)newValue);
        return;
      case IecPackage.LOCATED_VAR_DECLARATION__LOCATION:
        setLocation((Location)newValue);
        return;
      case IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT:
        setSpecInit((SpecInit)newValue);
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
      case IecPackage.LOCATED_VAR_DECLARATION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case IecPackage.LOCATED_VAR_DECLARATION__LOCATION:
        setLocation((Location)null);
        return;
      case IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT:
        setSpecInit((SpecInit)null);
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
      case IecPackage.LOCATED_VAR_DECLARATION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case IecPackage.LOCATED_VAR_DECLARATION__LOCATION:
        return location != null;
      case IecPackage.LOCATED_VAR_DECLARATION__SPEC_INIT:
        return specInit != null;
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

} //LocatedVarDeclarationImpl
