/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju Meka
* File:    Utils.java
* Created: Dec 06, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.util.expect;


import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Utils
{
   private static final Pattern templatePattern = Pattern.compile("(\\{.*?\\})");
   private static final Pattern numberMatcher = Pattern.compile("-?\\d+(\\.\\d+)?");
   private static final Pattern commaSplitter = Pattern.compile(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
   private static int MAX_ELEMENTS = 8;


   /**
    * 
    * @param template
    * @param values
    * @return
    */
   static String fill(String template, Object... values)
   {
      if (template == null)
      {
         throw new NullPointerException("Can not fill a null template");
      }
      if (values.length == 0)
      {
         return template;
      }
      Matcher m = templatePattern.matcher(template);
      StringBuffer sb = new StringBuffer();
      int i = 0;
      while (m.find())
      {
         Object value = null;
         if (i < values.length)
         {
            value = values[i++];
         }
         if (value instanceof Collection)
         {
            m.appendReplacement(sb, collectionString((Collection< ? >)value));
         }
         else if (value instanceof String)
         {
            m.appendReplacement(sb, "\"" + String.valueOf(value) + "\"");
         }
         else
         {
            m.appendReplacement(sb, String.valueOf(value));
         }
      }
      m.appendTail(sb);
      if (sb.length() == 0)
      {
         sb.append(template);
      }
      return sb.toString();
   }


   /**
    * 
    * @param collection
    * @return
    */
   static String collectionString(Collection< ? > collection)
   {
      String s = collection.stream().limit(MAX_ELEMENTS).map(String::valueOf).collect(Collectors.joining(", ", "[", ""));
      if (collection.size() > MAX_ELEMENTS)
      {
         return s + ", ...]";
      }
      else
      {
         return s + "]";
      }
   }


   /**
    * 
    * @param str
    * @return
    */
   public static Object literalValue(String str)
   {
      Objects.requireNonNull(str);
      str = str.trim();
      if (str.isEmpty())
      {
         throw new RuntimeException("Empty literal string found");
      }
      // drop the brackets if necessary
      if ((str.startsWith("[") && str.endsWith("]")))
      {
         str = str.substring(1, str.length() - 1).trim();
      }
      // decide what kind of literal the string should be
      if (numberMatcher.matcher(str).matches())
      {
         return Double.valueOf(str);
      }
      else if (str.equalsIgnoreCase("true"))
      {
         return true;
      }
      else if (str.equalsIgnoreCase("false"))
      {
         return false;
      }
      else if (str.equalsIgnoreCase("null"))
      {
         return null;
      }
      else if (str.contains(","))
      {
         // complex regex to allow escaping strings with " or '
         String[] split = commaSplitter.split(str);
         return Stream.of(split).map(String::trim).map(Utils::literalValue).collect(Collectors.toList());
      }
      else
      {
         // un-quote the string if needed
         if ((str.startsWith("'") && str.endsWith("'")) || (str.startsWith("\"") && str.endsWith("\"")))
         {
            str = str.substring(1, str.length() - 1);
         }
         return str;
      }
   }
}