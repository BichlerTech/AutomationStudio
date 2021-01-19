/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.InputOutputDeclarations;
import com.bichler.iec.iec.VarInitDecl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input Output Declarations</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.InputOutputDeclarationsImpl#getInitDecls <em>Init Decls</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InputOutputDeclarationsImpl extends IoVarDeclarationsImpl implements InputOutputDeclarations
{
  /**
   * The cached value of the '{@link #getInitDecls() <em>Init Decls</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInitDecls()
   * @generated
   * @ordered
   */
  protected EList<VarInitDecl> initDecls;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InputOutputDeclarationsImpl()
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
    return IecPackage.Literals.INPUT_OUTPUT_DECLARATIONS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<VarInitDecl> getInitDecls()
  {
    if (initDecls == null)
    {
      initDecls = new EObjectContainmentEList<VarInitDecl>(VarInitDecl.class, this, IecPackage.INPUT_OUTPUT_DECLARATIONS__INIT_DECLS);
    }
    return initDecls;
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
      case IecPackage.INPUT_OUTPUT_DECLARATIONS__INIT_DECLS:
        return ((InternalEList<?>)getInitDecls()).basicRemove(otherEnd, msgs);
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
      case IecPackage.INPUT_OUTPUT_DECLARATIONS__INIT_DECLS:
        return getInitDecls();
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
      case IecPackage.INPUT_OUTPUT_DECLARATIONS__INIT_DECLS:
        getInitDecls().clear();
        getInitDecls().addAll((Collection<? extends VarInitDecl>)newValue);
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
      case IecPackage.INPUT_OUTPUT_DECLARATIONS__INIT_DECLS:
        getInitDecls().clear();
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
      case IecPackage.INPUT_OUTPUT_DECLARATIONS__INIT_DECLS:
        return initDecls != null && !initDecls.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //InputOutputDeclarationsImpl
