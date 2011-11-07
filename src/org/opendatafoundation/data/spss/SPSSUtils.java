package org.opendatafoundation.data.spss;

/*
 * Author(s): Pascal Heus (pheus@opendatafoundation.org)
 *  
 * This product has been developed with the financial and 
 * technical support of the UK Data Archive Data Exchange Tools 
 * project (http://www.data-archive.ac.uk/dext/) and the 
 * Open Data Foundation (http://www.opendatafoundation.org) 
 * 
 * Copyright 2007 University of Essex (http://www.esds.ac.uk) 
 * 
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, 
 * Boston, MA  02110-1301  USA
 * The full text of the license is also available on the Internet at 
 * http://www.gnu.org/copyleft/lesser.html
 * 
 */

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Various utility functions
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSUtils {

    /**
     * Converts a 8-byte value into a double
     * 
     * @param buffer
     * @return converted value as double
     */
    public static double byte8ToDouble(byte[] buffer) {
        long lvalue = ( 
                ((long)(buffer[7])       << 56)  +
                ((long)(buffer[6]&0xFF)  << 48)  +
                ((long)(buffer[5]&0xFF)  << 40)  +
                ((long)(buffer[4]&0xFF)  << 32)  +
                ((long)(buffer[3]&0xFF)  << 24)  +
                ((long)(buffer[2]&0xFF)  << 16)  +
                ((long)(buffer[1]&0xFF)  << 8)   +
                 (long)(buffer[0]&0xFF));
        return(Double.longBitsToDouble(lvalue));
    }

    /**
     * Converts a 8-byte value into a String
     * 
     * @param buffer
     * @return converted value as String
     */
    public static String byte8ToString(byte[] buffer) {
        String str = new String(buffer).replaceAll("\\s+$", "");
        return(str);
    }
    
    /**
     * Converts a 4-byte value into an integer
     * 
     * @param buffer
     * @return converted value as integer
     */public static int byte4ToInt(byte[] buffer) {
        int n=0;
        for (int i = 0; i < 4; i++) {
            n |= (buffer[i] & 0xFF) << ( i * 8);
        } 
        return(n);
    }

    /**
     * Converts a numeric value representing a date/time in SPSS into a Java GregorianCalendar.
     * Date/time in SPSS are stored as the number of seconds elapsed since midnight 
     * on October 14th 1582 (start of the Gergorian calendar)
     *   
     * @param value the numeric representation of the date/time to convert 
     * @return A GregorianCalendar object 
     */
    public static GregorianCalendar numericToCalendar(double value) {
        GregorianCalendar calendar = new GregorianCalendar();

        //calendar.set(1582,Calendar.OCTOBER,14,0,0,0); // SPSS Start date
        value -= 12219379200.0; // Change date baseline from midnight on October 14th 1582 to January 1st 1970   
        calendar.set(1970,Calendar.JANUARY,1,0,0,0); // SPSS Start date
        
        // break down into days, hours, minutes, seconds, hundreds
        int daysOffset = (int) (value / 86400.0); //
        value -=  daysOffset*86400.0;
        int hoursOffset = (int) (value / 3600.0);
        value -=  hoursOffset * 3600.0;
        int minutesOffset = (int) (value / 60.0);
        value -= minutesOffset * 60.0; 
        int secondsOffset = (int) (value);
        int hundredsOffset = (int) (value - secondsOffset)*100;

        //System.out.println(value+" | "+(value / 86400.0)+"="+daysOffset+"["+hoursOffset+":"+minutesOffset+":"+secondsOffset+"."+hundredsOffset+"]\n");
        
        // add offset to calendar
        calendar.add(Calendar.DAY_OF_YEAR,daysOffset);
        calendar.add(Calendar.HOUR_OF_DAY,  hoursOffset);
        calendar.add(Calendar.MINUTE,minutesOffset );
        calendar.add(Calendar.SECOND,secondsOffset);
        calendar.add(Calendar.MILLISECOND,hundredsOffset*10);

        return(calendar);
    }
}
