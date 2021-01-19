/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.FunctionBody;
import com.bichler.iec.iec.FunctionDeclaration;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.IoVarDeclarations;
import com.bichler.iec.iec.NonGenericType;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.FunctionDeclarationImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.FunctionDeclarationImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.FunctionDeclarationImpl#getIoVarDeclarations <em>Io Var Declarations</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.FunctionDeclarationImpl#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FunctionDeclarationImpl extends LibraryElementImpl implements FunctionDeclaration
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
   * The cached value of the '{@link #getType() <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected NonGenericType type;

  /**
   * The cached value of the '{@link #getIoVarDeclarations() <em>Io Var Declarations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIoVarDeclarations()
   * @generated
   * @ordered
   */
  protected EList<IoVarDeclarations> ioVarDeclarations;

  /**
   * The cached value of the '{@link #getBody() <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBody()
   * @generated
   * @ordered
   */
  protected FunctionBody body;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FunctionDeclarationImpl()
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
    return IecPackage.Literals.FUNCTION_DECLARATION;
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
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.FUNCTION_DECLARATION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NonGenericType getType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetType(NonGenericType newType, NotificationChain msgs)
  {
    NonGenericType oldType = type;
    type = newType;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.FUNCTION_DECLARATION__TYPE, oldType, newType);
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
  public void setType(NonGenericType newType)
  {
    if (newType != type)
    {
      NotificationChain msgs = null;
      if (type != null)
        msgs = ((InternalEObject)type).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.FUNCTION_DECLARATION__TYPE, null, msgs);
      if (newType != null)
        msgs = ((InternalEObject)newType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.FUNCTION_DECLARATION__TYPE, null, msgs);
      msgs = basicSetType(newType, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.FUNCTION_DECLARATION__TYPE, newType, newType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<IoVarDeclarations> getIoVarDeclarations()
  {
    if (ioVarDeclarations == null)
    {
      ioVarDeclarations = new EObjectContainmentEList<IoVarDeclarations>(IoVarDeclarations.class, this, IecPackage.FUNCTION_DECLARATION__IO_VAR_DECLARATIONS);
    }
    return ioVarDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionBody getBody()
  {
    return body;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBody(FunctionBody newBody, NotificationChain msgs)
  {
    FunctionBody oldBody = body;
    body = newBody;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.FUNCTION_DECLARATION__BODY, oldBody, newBody);
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
  public void setBody(FunctionBody newBody)
  {
    if (newBody != body)
    {
      NotificationChain msgs = null;
      if (body != null)
        msgs = ((InternalEObject)body).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.FUNCTION_DECLARATION__BODY, null, msgs);
      if (newBody != null)
        msgs = ((InternalEObject)newBody).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.FUNCTION_DECLARATION__BODY, null, msgs);
      msgs = basicSetBody(newBody, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.FUNCTION_DECLARATION__BODY, newBody, newBody));
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
      case IecPackage.FUNCTION_DECLARATION__TYPE:
        return basicSetType(null, msgs);
      case IecPackage.FUNCTION_DECLARATION__IO_VAR_DECLARATIONS:
        return ((InternalEList<?>)getIoVarDeclarations()).basicRemove(otherEnd, msgs);
      case IecPackage.FUNCTION_DECLARATION__BODY:
        return basicSetBody(null, msgs);
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
      case IecPackage.FUNCTION_DECLARATION__NAME:
        return getName();
      case IecPackage.FUNCTION_DECLARATION__TYPE:
        return getType();
      case IecPackage.FUNCTION_DECLARATION__IO_VAR_DECLARATIONS:
        return getIoVarDeclarations();
      case IecPackage.FUNCTION_DECLARATION__BODY:
        return getBody();
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
      case IecPackage.FUNCTION_DECLARATION__NAME:
        setName((String)newValue);
        return;
      case IecPackage.FUNCTION_DECLARATION__TYPE:
        setType((NonGenericType)newValue);
        return;
      case IecPackage.FUNCTION_DECLARATION__IO_VAR_DECLARATIONS:
        getIoVarDeclarations().clear();
        getIoVarDeclarations().addAll((Collection<? extends IoVarDeclarations>)newValue);
        return;
      case IecPackage.FUNCTION_DECLARATION__BODY:
        setBody((FunctionBody)newValue);
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
      case IecPackage.FUNCTION_DECLARATION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case IecPackage.FUNCTION_DECLARATION__TYPE:
        setType((NonGenericType)null);
        return;
      case IecPackage.FUNCTION_DECLARATION__IO_VAR_DECLARATIONS:
        getIoVarDeclarations().clear();
        return;
      case IecPackage.FUNCTION_DECLARATION__BODY:
        setBody((FunctionBody)null);
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
      case IecPackage.FUNCTION_DECLARATION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case IecPackage.FUNCTION_DECLARATION__TYPE:
        return type != null;
      case IecPackage.FUNCTION_DECLARATION__IO_VAR_DECLARATIONS:
        return ioVarDeclarations != null && !ioVarDeclarations.isEmpty();
      case IecPackage.FUNCTION_DECLARATION__BODY:
        return body != null;
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

} //FunctionDeclarationImpl
