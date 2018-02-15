/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju Meka
* File:    ExpectParser.java
* Created: Dec 06, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.util.expect;


import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chtr.tmoauto.util.expect.Token.Type;


public class ExpectParser
{
   private static Logger log = LoggerFactory.getLogger(ExpectParser.class);
   private final String expectationString;
   private final Deque<Object> values;
   private String state = "read";
   private char closingCharacter;
   private final List<Token> tokens = new LinkedList<>();
   private StringBuilder buf = new StringBuilder();


   public static List<Comparison> parse(String expectationString, Object... values)
   {
      ExpectParser parser = new ExpectParser(expectationString, values);
      return parser.parse();
   }


   public ExpectParser(String expectationString, Object... values)
   {
      this.expectationString = expectationString;
      this.values = new LinkedList<>(Arrays.asList(values));
   }


   public List<Comparison> parse()
   {
      // parse and fill the tokens
      for (int i = 0; i < expectationString.length(); i++)
      {
         char c = expectationString.charAt(i);
         switch (state)
         {
            case "read":
               read(c);
               break;
            case "read-operator":
               readOperator(c);
               break;
            case "read-escaped":
               readUntil(c, closingCharacter);
               break;
         }
      }
      for (Token token : tokens)
      {
         if (token.getType() == Type.VAR)
         {
            if (values.isEmpty())
            {
               throw new RuntimeException("missing passed in value for expect: " + expectationString);
            }
            token.setValue(values.pop());
         }
      }
      log.debug("tokenized expectation string: {}", tokens);
      //
      // segment the token list into their comparison groups
      List<List<Token>> tokenSubLists = new LinkedList<>();
      List<Token> sub = new LinkedList<>();
      for (Token token : tokens)
      {
         if (token.getType() == Type.AND)
         {
            if ( !sub.isEmpty())
            {
               tokenSubLists.add(sub);
            }
            sub = new LinkedList<>();
         }
         else if (token.getType() != Type.CRUFF)
         {
            sub.add(token);
         }
      }
      if ( !sub.isEmpty())
      {
         tokenSubLists.add(sub);
      }
      log.debug("segmented tokens: {}", tokenSubLists);
      //
      // convert each group of tokens into their equivalent comparisons
      List<Comparison> comparisons = new LinkedList<>();
      for (List<Token> t : tokenSubLists)
      {
         Comparison comparison = Comparison.fromTokenList(t);
         if ( !comparison.isLeftSet() && !comparisons.isEmpty())
         {
            comparison.setLeft(comparisons.get(comparisons.size() - 1).getLeft());
         }
         comparison.validate();
         comparisons.add(comparison);
      }
      log.debug("comparisons: {}", comparisons);
      return comparisons;
   }


   public void read(char c)
   {
      if (Character.isWhitespace(c))
      {
         stateChange("read");
      }
      else if (isCompSymbol(c))
      {
         stateChange("read-operator");
         buf.append(c);
      }
      else
      {
         switch (c)
         {
            case '{':
               closingCharacter = '}';
               stateChange("read-escaped");
               break;
            case '[':
               closingCharacter = ']';
               stateChange("read-escaped");
               break;
            case '"':
               closingCharacter = '"';
               stateChange("read-escaped");
               break;
            case '\'':
               closingCharacter = '\'';
               stateChange("read-escaped");
               break;
         }
         buf.append(c);
      }
   }


   public void readOperator(char c)
   {
      if (isCompSymbol(c))
      {
         buf.append(c);
      }
      else
      {
         read(c);
      }
   }


   public void readUntil(char c, char stopOn)
   {
      buf.append(c);
      if (c == stopOn)
      {
         stateChange("read");
      }
   }


   public void stateChange(String newState)
   {
      state = newState;
      String segment = buf.toString().trim();
      if ( !segment.isEmpty())
      {
         Token token = new Token(segment);
         tokens.add(token);
      }
      buf = new StringBuilder();
   }


   public boolean isCompSymbol(char c)
   {
      return c == '>' || c == '<' || c == '=' || c == '!';
   }
}