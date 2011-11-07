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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * SPSS Record Type 7 Subtype 13 - Long variable names
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSRecordType7Subtype13 extends SPSSAbstractRecordType {
    // type 7
    int     recordTypeCode;
    int     recordSubtypeCode;
    int     dataElementLength;
    int     numberOfDataElements;
    Map<String,String> nameMap;
    
    // subtype 13
    String longNamesStr;

    public void read(SPSSFile is) throws IOException, SPSSFileException {
        // position in file
        fileLocation = is.getFilePointer();

        // record type
        recordTypeCode = is.readSPSSInt();
        if(recordTypeCode!=7) throw new SPSSFileException("Error reading record type 7 subtype 11: bad record type ["+recordTypeCode+"]. Expecting Record Type 7.");

        // subtype
        recordSubtypeCode = is.readSPSSInt();
        if(recordSubtypeCode!=13) throw new SPSSFileException("Error reading record type 7 subtype 13: bad subrecord type ["+recordSubtypeCode+"]. Expecting Record Subtype 13.");

        // data elements
        dataElementLength = is.readSPSSInt();
        if(dataElementLength!=1) throw new SPSSFileException("Error reading record type 7 subtype 11: bad data element length ["+dataElementLength+"]. Expecting 1.");
        numberOfDataElements = is.readSPSSInt();

        // read the long names String
        longNamesStr = is.readSPSSString(numberOfDataElements);

        // load names (separated by tabs)
        nameMap = new LinkedHashMap<String,String>();
        StringTokenizer st1 = new StringTokenizer(longNamesStr,"\t");
        while (st1.hasMoreTokens()) {
            StringTokenizer st2 = new StringTokenizer(st1.nextToken(),"=");
            if(st2.countTokens()>=2) {
                nameMap.put(st2.nextToken(), st2.nextToken());
            }
        }
    }

    public String toString() {
        String str="";
        str += "\nRECORD TYPE 7 SUBTYPE 13 - LONG VARIABLE NAMES";
        str += "\nLocation        : "+fileLocation;
        str += "\nRecord Type     : "+recordTypeCode;
        str += "\nRecord Subtype  : "+recordSubtypeCode;
        str += "\nData elements   : "+numberOfDataElements;
        str += "\nElement length  : "+dataElementLength;
        str += "\nLong Names      : "+longNamesStr;
        Iterator it = nameMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            str += "\n"+(entry.getKey() + " = " + entry.getValue());
        }
        return(str);
    }

    public class VariableDisplayParams {
        int measure;
        int width;
        int alignment;
    }
}
