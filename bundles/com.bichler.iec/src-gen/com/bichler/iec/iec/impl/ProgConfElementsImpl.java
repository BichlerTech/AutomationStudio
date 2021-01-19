/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ProgConfElement;
import com.bichler.iec.iec.ProgConfElements;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Prog Conf Elements</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ProgConfElementsImpl#getProgconf <em>Progconf</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProgConfElementsImpl extends MinimalEObjectImpl.Container implements ProgConfElements
{
  /**
   * The cached value of the '{@link #getProgconf() <em>Progconf</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProgconf()
   * @generated
   * @ordered
   */
  protected EList<ProgConfElement> progconf;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProgConfElementsImpl()
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
    return IecPackage.Literals.PROG_CONF_ELEMENTS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ProgConfElement> getProgconf()
  {
    if (progconf == null)
    {
      progconf = new EObjectContainmentEList<ProgConfElement>(ProgConfElement.class, this, IecPackage.PROG_CONF_ELEMENTS__PROGCONF);
    }
    return progconf;
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
      case IecPackage.PROG_CONF_ELEMENTS__PROGCONF:
        return ((InternalEList<?>)getProgconf()).basicRemove(otherEnd, msgs);
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
      case IecPackage.PROG_CONF_ELEMENTS__PROGCONF:
        return getProgconf();
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
      case IecPackage.PROG_CONF_ELEMENTS__PROGCONF:
        getProgconf().clear();
        getProgconf().addAll((Collection<? extends ProgConfElement>)newValue);
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
      case IecPackage.PROG_CONF_ELEMENTS__PROGCONF:
        getProgconf().clear();
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
      case IecPackage.PROG_CONF_ELEMENTS__PROGCONF:
        return progconf != null && !progconf.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ProgConfElementsImpl
