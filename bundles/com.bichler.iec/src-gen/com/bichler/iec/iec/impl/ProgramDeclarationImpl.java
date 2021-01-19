/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.FunctionBlockBody;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ProgramDeclaration;
import com.bichler.iec.iec.ProgramVarDeclarations;

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
 * An implementation of the model object '<em><b>Program Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramDeclarationImpl#getVarDeclarations <em>Var Declarations</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramDeclarationImpl#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProgramDeclarationImpl extends LibraryElementDeclarationImpl implements ProgramDeclaration
{
  /**
   * The cached value of the '{@link #getVarDeclarations() <em>Var Declarations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVarDeclarations()
   * @generated
   * @ordered
   */
  protected EList<ProgramVarDeclarations> varDeclarations;

  /**
   * The cached value of the '{@link #getBody() <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBody()
   * @generated
   * @ordered
   */
  protected FunctionBlockBody body;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProgramDeclarationImpl()
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
    return IecPackage.Literals.PROGRAM_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ProgramVarDeclarations> getVarDeclarations()
  {
    if (varDeclarations == null)
    {
      varDeclarations = new EObjectContainmentEList<ProgramVarDeclarations>(ProgramVarDeclarations.class, this, IecPackage.PROGRAM_DECLARATION__VAR_DECLARATIONS);
    }
    return varDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public FunctionBlockBody getBody()
  {
    return body;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBody(FunctionBlockBody newBody, NotificationChain msgs)
  {
    FunctionBlockBody oldBody = body;
    body = newBody;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_DECLARATION__BODY, oldBody, newBody);
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
  public void setBody(FunctionBlockBody newBody)
  {
    if (newBody != body)
    {
      NotificationChain msgs = null;
      if (body != null)
        msgs = ((InternalEObject)body).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_DECLARATION__BODY, null, msgs);
      if (newBody != null)
        msgs = ((InternalEObject)newBody).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_DECLARATION__BODY, null, msgs);
      msgs = basicSetBody(newBody, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_DECLARATION__BODY, newBody, newBody));
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
      case IecPackage.PROGRAM_DECLARATION__VAR_DECLARATIONS:
        return ((InternalEList<?>)getVarDeclarations()).basicRemove(otherEnd, msgs);
      case IecPackage.PROGRAM_DECLARATION__BODY:
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
      case IecPackage.PROGRAM_DECLARATION__VAR_DECLARATIONS:
        return getVarDeclarations();
      case IecPackage.PROGRAM_DECLARATION__BODY:
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
      case IecPackage.PROGRAM_DECLARATION__VAR_DECLARATIONS:
        getVarDeclarations().clear();
        getVarDeclarations().addAll((Collection<? extends ProgramVarDeclarations>)newValue);
        return;
      case IecPackage.PROGRAM_DECLARATION__BODY:
        setBody((FunctionBlockBody)newValue);
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
      case IecPackage.PROGRAM_DECLARATION__VAR_DECLARATIONS:
        getVarDeclarations().clear();
        return;
      case IecPackage.PROGRAM_DECLARATION__BODY:
        setBody((FunctionBlockBody)null);
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
      case IecPackage.PROGRAM_DECLARATION__VAR_DECLARATIONS:
        return varDeclarations != null && !varDeclarations.isEmpty();
      case IecPackage.PROGRAM_DECLARATION__BODY:
        return body != null;
    }
    return super.eIsSet(featureID);
  }

} //ProgramDeclarationImpl
