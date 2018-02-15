/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju Meka
* File:    Expect.java
* Created: Dec 06, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/


package com.chtr.tmoauto.util.expect;


import static com.chtr.tmoauto.util.expect.Utils.fill;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Static class providing expectation evaluation. An expectation is a string (hopefully in a fluent language) and a list
 * of arguments for that string, which are parsed together and evaluated for truthiness.<br>
 * General Syntax:<br>
 * <br>
 * VAR|LITERAL [OPERATOR] VAR|LITERAL AND [VAR] [OPERATOR] VAR|LITERAL ... END<br>
 *
 * <ul>
 * <li>VAR (or variable) looks like: {any text}, and will be filled in using values from the passed in arguments.</li>
 * <li>OPERATOR is one of "=", "!=", ">", "<", ">=", "<=", "contains", "!contains", additionally each operator has
 * alternate representations (i.e. '==' for '='), more details can be found in the Operator enum</li>
 * <li>LITERAL has the format [ - literal value - ], and is parsed and used as a literal value for comparison (i.e. it
 * isn't a passed in value), literal values can be numbers (1, 2, 3.4), booleans (true, false), the keyword 'null', any
 * string, or a comma separated list of any of these things. If necessary a string can be specified with quotes
 * (" or ') to prevent conversion to a non-string type e.g. the string true can be written as ["true"]</li>
 * <li>AND is a conjunction joining expect terms together and is one of: 'and', ',', '&', '&&'. <strong>THERE IS NO
 * 'OR'</strong></li>
 * </ul>
 * When multiple comparisons are used and joined together using an AND token, it is not necessary to have a VAR or
 * LITERAL on the left side of the operator in any but the first clause. In this case the left hand value from the
 * previous clause will be used. For example: <br>
 * <code>expect("{my str} to contain [s] and be greater-than [q]", myStr);</code><br>
 * Will evaluate as (myStr.contains("s") && myStr.compareTo("q") > 0) <br>
 * <br>
 * <strong>Examples:</strong><br>
 * <code>
 * Integer i = 3;<br>
 * String str = "str";<br>
 * expect("{the number} = [3]", i); // PASSES<br>
 * expect("{the number} is > [0]", i); // PASSES<br>
 * expect("{the number} <= [3] and > [0]", i); // PASSES<br>
 * expect("{str} contains [st]", str); // PASSES<br>
 * expect("{str} contains {the number}", str, i); // FAILS<br>
 * </code>
 * <br>
 */
public class Expect
{
   private static Logger log = LoggerFactory.getLogger(Expect.class);


   /**
    * The primary expect method. Evaluates the expectation string using the given values. Throws an AssertionFailed
    * exception if the expectation fails.
    *
    * @param expectationString The expectation string
    * @param values The values to fill the expectation string with
    */
   
public static void expect(String expectationString, Object... values)
   {
      evaluate(false, true, expectationString, values);
   }


   /**
    * Identical to normal expect method, but with additional logging
    *
    * @param expectationString The expectation string
    * @param values The values to fill the expectation string with
    */

   public static void expectVerbose(String expectationString, Object... values)
   {
      evaluate(true, true, expectationString, values);
   }


   /**
    * Evaluates the expectation string using the given values. Does NOT throw an AssertionFailed
    * exception if the expectation fails, the returned boolean will have the result of evaluating the expectation.
    *
    * @param expectationString The expectation string
    * @param values The values to fill the expectation string with
    */
   public static boolean expectNoAssert(String expectationString, Object... values)
   {
      return evaluate(false, false, expectationString, values);
   }


   /**
    * Inverted evaluation for expectations. Evaluates the expectation string using the given values. Throws an
    * AssertionFailed exception if the expectation passes. Not recommended for use, inverting logic gets confusing.
    *
    * @param expectationString The expectation string
    * @param values The values to fill the expectation string with
    */
   public static void expectFalse(String expectationString, Object... values)
   {
      List<Comparison> comparisons = ExpectParser.parse(expectationString, values);
      if (evaluate(comparisons, false))
      {
         Assert.fail("expect: " + expectationString + "  -->  " + fill(expectationString, values));
      }
      else
      {
         log.info("passed: expect {}  -->  {}", expectationString, fill(expectationString, values));
      }
   }


   /**
    * 
    * @param verbose
    * @param doAssert
    * @param expectationString
    * @param values
    * @return
    */
   
   private static boolean evaluate(boolean verbose, boolean doAssert, String expectationString, Object... values)
   {
      List<Comparison> comparisons = ExpectParser.parse(expectationString, values);
      boolean pass = evaluate(comparisons, verbose);
      if ( !pass)
      {
         if (doAssert)
         {
            Assert.fail("expect: " + expectationString + "  -->  " + fill(expectationString, values));
         }
         else
         {
            log.error("expect: {} --> {}", expectationString, fill(expectationString, values));
         }
      }
      else
      {
         log.info("passed: expect {}  -->  {}", expectationString, fill(expectationString, values));
      }
      return pass;
   }


   /**
    * 
    * @param comparisons
    * @param verbose
    * @return
    */
   private static boolean evaluate(List<Comparison> comparisons, boolean verbose)
   {
      if (comparisons.isEmpty())
      {
         throw new RuntimeException("No comparisons to evaluate");
      }
      AtomicBoolean success = new AtomicBoolean(true);
      StringJoiner sj = new StringJoiner(", ");
      comparisons.stream().forEach(c -> {
         if ( !c.evaluate())
         {
            success.set(false);
         }
         sj.add(c.toString());
      });
      if (verbose)
      {
         log.info("comparisons: {}", sj.toString());
      }
      else
      {
         log.debug("comparisons: {}", sj.toString());
      }
      return success.get();
   }
}