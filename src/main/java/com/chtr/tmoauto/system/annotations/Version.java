/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Danny Byers
* File:    Version.java
* Created: Dec 08, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/


package com.chtr.tmoauto.system.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The version of the product, <strong>NOT</strong> the version of the test. In practice this should only be
 * used when debugging, e.g. @Version("Sandbox"). Typically the product version should be queried dynamically
 * before the execution of a test and this annotation should be used as a fallback in case that fails.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Version
{
   String value();
}
