package org.opendatafoundation.data;

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

/**
 * Class to describe file format options for import/export
 *  
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class FileFormatInfo {
    /** Export compatibility mode */
    public static enum Compatibility {NATIVE, GENERIC};
    /** Export format */
    public static enum Format {ASCII, SPSS, SAS, STATA};
    /** Ascii format */
    public static enum ASCIIFormat {FIXED,DELIMITED,CSV};
    
    public Compatibility compatibility    = Compatibility.NATIVE;
    public Format        format           = Format.ASCII;
    public ASCIIFormat   asciiFormat      = ASCIIFormat.FIXED;
    public char          asciiDelimiter   ='\t';
    public boolean       namesOnFirstLine = true;
    
    public FileFormatInfo() {
    }
    
    public FileFormatInfo(Format format) {
    	this.format = format;
    }
    
    public String toString() {
        String str;
        str = format.name();
        if(format==Format.ASCII) {
            str += "_"+asciiFormat.toString();
            /*
            if(asciiFormat==ASCIIFormat.DELIMITED) {
                switch(asciiDelimiter) {
                case '\t': str += ".TAB";
                default: str += "."+ (int) asciiDelimiter;
                }
            }
            */
            if(compatibility!=Compatibility.GENERIC) str += "_"+compatibility.toString();
        }
        return(str);
    }
}
