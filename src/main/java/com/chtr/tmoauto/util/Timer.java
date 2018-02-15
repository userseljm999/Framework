/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  P2193524
* File:    Timer.java
* Created: Nov 16, 2016
*
* Description:
*
*-------------------------------------------------------------
* BITBUCKET
*
* Last Revision: $Change: 304670 $
* Last Checkin:  $DateTime: 2016/11/15 14:58:47 $
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/


package com.chtr.tmoauto.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;



/**
 * 
 * @author P2193524
 *
 */
public class Timer 
{


//	protected static final Logger log = LoggerFactory.getLogger(Timer.class);
	
	public static final long SECOND = 1000;
	public static final long MINUTE = 60000;
	public static final long HOUR = 3600000;
	
	private long timeStarted = 0;
	private long timeStopped = 0;
	private boolean isStopped = false;
	
	
	
	
	// @author Gaurav Kumar
	private transient int iStartTime = 0;
	private transient String strTotalTime;	
	private transient long lnInTime;
	private transient long lnOutTime;
	private transient long lnTotalTime;
	private transient long lnHours;
	private transient long lnMins;
	private transient long lnSecs;
	private transient String strTimeIn;
	private transient String strTimeOut;

	
	
	/**
	 * Timer is running.
	 */
	public Timer()
	{
		start();
	}
	
	
	/**
	 * Start Timer
	 */
	public void start()
	{
		timeStarted = System.currentTimeMillis();
		isStopped = false;
	}
	
	
	
	/**
	 * Stop Timer
	 */
	public void stop()
	{
		timeStopped = System.currentTimeMillis();
		isStopped = true;
	}
	
	
	/**
	 * Return the time between when start() was called to when stop() was called.
	 * 
	 * 
	 * @return time in milliseconds
	 * @author P2193524
	 * @since 11/16/16
	 * 
	 */
	public long getElapsed()
	{
		long endTime = isStopped ? timeStopped : System.currentTimeMillis();
		
		return endTime - timeStarted;
	}
	
	

	
	
	
	/**
	 * This method returns TRUE if the elapsed time has exceeded the expected duration
	 * 
	 * @param durationInMilliseconds
	 * @return TRUE if the timer is past expected duration
	 */
	public boolean isOverdue(long durationInMilliseconds)
	{
		boolean result = getElapsed() > durationInMilliseconds;
		
		if (isStopped)
		{
			result = (timeStopped > timeStarted) && result;
		}
		
		return result;
	}
	
	
	/**
	 * Method that returns the total time.
	 * 
	 * @return the total time elapsed as a string
	 */
	public String getStrTotalTime() 
	{
		return strTotalTime;
	}

	
	/**
	 * Method that sets the total time
	 * 
	 * @param strTotalTime
	 * @author Gaurav Kumar
	 */
	public void fw_setStrTotalTime(final String strTotalTime) 
	{
		this.strTotalTime = strTotalTime;
	}
	
	
	/**
	 * Method that gets the start time
	 * 
	 * @return start time in milliseconds
	 * @author Gaurav Kumar
	 */
	public int fw_getStartTime() {
		return iStartTime;
	}

	
	/**
	 * Method that sets the start time
	 * @param startTime
	 * @author Gaurav Kumar
	 */
	public void fw_setStartTime(final int startTime) {
		this.iStartTime = startTime;
	}
	
	/**
	 * 
	 * @author Gaurav Kumar
	 */
	public void fw_inTime() {
		final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		final Date time1 = new Date();
		strTimeIn = dateFormat.format(time1);
		lnInTime = System.currentTimeMillis();

		System.out.println("Entry time: " + strTimeIn);
//		logmanager.info(log, "Entry time: " + strTimeIn);
	}
	
	
	/**
	 * 
	 * 	@author Gaurav Kumar
	 */
	public void fw_outTime() {
		final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		final Date time1 = new Date();
		strTimeOut = dateFormat.format(time1);
		lnOutTime = System.currentTimeMillis();
		lnTotalTime = (lnOutTime - lnInTime);
		lnSecs = lnTotalTime / 1000;
		lnMins = lnSecs / 60;
		lnHours = lnMins / 60;
		lnSecs = lnSecs - lnMins * 60;
		lnMins = lnMins - lnHours * 60;

		strTotalTime = Long.toString(lnHours).concat("HRS")
				.concat(Long.toString(lnMins)).concat("Min")
				.concat(Long.toString(lnSecs)).concat("sec");

		System.out.println("Exit time: " + strTimeOut);
//		logmanager.info(log, "Exit time: " + strTimeOut);
//		logmanager.debug(log, "Total time: " + strTotalTime);
	}
	
	
}
