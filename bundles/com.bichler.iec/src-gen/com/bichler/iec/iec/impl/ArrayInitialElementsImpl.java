/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ArrayInitialElements;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.InitialElement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Array Initial Elements</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ArrayInitialElementsImpl#getInitialElement <em>Initial Element</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ArrayInitialElementsImpl#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArrayInitialElementsImpl extends MinimalEObjectImpl.Container implements ArrayInitialElements
{
  /**
   * The cached value of the '{@link #getInitialElement() <em>Initial Element</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInitialElement()
   * @generated
   * @ordered
   */
  protected InitialElement initialElement;

  /**
   * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIndex()
   * @generated
   * @ordered
   */
  protected static final String INDEX_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIndex()
   * @generated
   * @ordered
   */
  protected String index = INDEX_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ArrayInitialElementsImpl()
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
    return IecPackage.Literals.ARRAY_INITIAL_ELEMENTS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InitialElement getInitialElement()
  {
    return initialElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetInitialElement(InitialElement newInitialElement, NotificationChain msgs)
  {
    InitialElement oldInitialElement = initialElement;
    initialElement = newInitialElement;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT, oldInitialElement, newInitialElement);
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
  public void setInitialElement(InitialElement newInitialElement)
  {
    if (newInitialElement != initialElement)
    {
      NotificationChain msgs = null;
      if (initialElement != null)
        msgs = ((InternalEObject)initialElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT, null, msgs);
      if (newInitialElement != null)
        msgs = ((InternalEObject)newInitialElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT, null, msgs);
      msgs = basicSetInitialElement(newInitialElement, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT, newInitialElement, newInitialElement));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getIndex()
  {
    return index;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setIndex(String newIndex)
  {
    String oldIndex = index;
    index = newIndex;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ARRAY_INITIAL_ELEMENTS__INDEX, oldIndex, index));
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
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT:
        return basicSetInitialElement(null, msgs);
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
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT:
        return getInitialElement();
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INDEX:
        return getIndex();
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
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT:
        setInitialElement((InitialElement)newValue);
        return;
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INDEX:
        setIndex((String)newValue);
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
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT:
        setInitialElement((InitialElement)null);
        return;
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INDEX:
        setIndex(INDEX_EDEFAULT);
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
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INITIAL_ELEMENT:
        return initialElement != null;
      case IecPackage.ARRAY_INITIAL_ELEMENTS__INDEX:
        return INDEX_EDEFAULT == null ? index != null : !INDEX_EDEFAULT.equals(index);
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
    result.append(" (index: ");
    result.append(index);
    result.append(')');
    return result.toString();
  }

} //ArrayInitialElementsImpl
