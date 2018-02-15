package com.chtr.tmoauto.webservices;
/**
 * This class is used to set maskable nodes so that those node are not considered while XMLcomparator.
 * @since: 11/02/2016
 * @author: Sangram Pisal
 */

public class Constants
{

	public static final String[] MASKABLENODES =
	{ "ns2:EchoBack", "ns3:MessageID", "ns3:CorrelationID" };
	public static String[] maskableAttribute = new String[]
	{ "attribute", "masked" };;

}
