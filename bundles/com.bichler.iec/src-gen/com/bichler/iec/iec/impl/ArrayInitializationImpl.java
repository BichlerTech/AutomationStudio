/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ArrayInitialElements;
import com.bichler.iec.iec.ArrayInitialization;
import com.bichler.iec.iec.IecPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Array Initialization</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ArrayInitializationImpl#getInitialElements <em>Initial Elements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArrayInitializationImpl extends InitialElementImpl implements ArrayInitialization
{
  /**
   * The cached value of the '{@link #getInitialElements() <em>Initial Elements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInitialElements()
   * @generated
   * @ordered
   */
  protected EList<ArrayInitialElements> initialElements;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ArrayInitializationImpl()
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
    return IecPackage.Literals.ARRAY_INITIALIZATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ArrayInitialElements> getInitialElements()
  {
    if (initialElements == null)
    {
      initialElements = new EObjectContainmentEList<ArrayInitialElements>(ArrayInitialElements.class, this, IecPackage.ARRAY_INITIALIZATION__INITIAL_ELEMENTS);
    }
    return initialElements;
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
      case IecPackage.ARRAY_INITIALIZATION__INITIAL_ELEMENTS:
        return ((InternalEList<?>)getInitialElements()).basicRemove(otherEnd, msgs);
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
      case IecPackage.ARRAY_INITIALIZATION__INITIAL_ELEMENTS:
        return getInitialElements();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case IecPackage.ARRAY_INITIALIZATION__INITIAL_ELEMENTS:
        getInitialElements().clear();
        getInitialElements().addAll((Collection<? extends ArrayInitialElements>)newValue);
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
      case IecPackage.ARRAY_INITIALIZATION__INITIAL_ELEMENTS:
        getInitialElements().clear();
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
      case IecPackage.ARRAY_INITIALIZATION__INITIAL_ELEMENTS:
        return initialElements != null && !initialElements.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ArrayInitializationImpl
