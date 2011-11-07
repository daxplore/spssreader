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
import java.util.List;

/**
 * SPSS Record Type 7 Subtype 4 - Release and machine specific "float" type information.
 * Added in SPSS release 4.0
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSRecordType7Subtype4 extends SPSSAbstractRecordType {
    // type 7
    int     recordTypeCode;
    int     recordSubtypeCode;
    int     dataElementLength;
    int     numberOfDataElements;
    // subtype 4
    double  sysmiss; /** system missing value */
    double  highest; /** value for HIGHEST in missing values and recode */
    double  lowest;  /** value for LOWEST in missing values and recode */

    List<byte[]> dataElement;
    
    public void read(SPSSFile is) throws IOException, SPSSFileException {
        // position in file
        fileLocation = is.getFilePointer();

        // record type
        recordTypeCode = is.readSPSSInt();
        if(recordTypeCode!=7) throw new SPSSFileException("Error reading record type 7 subtype 4: bad record type ["+recordTypeCode+"]. Expecting Record Type 7.");

        // subtype
        recordSubtypeCode = is.readSPSSInt();
        if(recordSubtypeCode!=4) throw new SPSSFileException("Error reading record type 7 subtype 4: bad subrecord type ["+recordSubtypeCode+"]. Expecting Record Subtype 4.");

        // data elements
        dataElementLength = is.readSPSSInt();
        if(dataElementLength!=8) throw new SPSSFileException("Error reading record type 7 subtype 3: bad data element length ["+dataElementLength+"]. Expecting 8.");

        numberOfDataElements = is.readSPSSInt();
        if(numberOfDataElements!=3) throw new SPSSFileException("Error reading record type 7 subtype 3: bad number of data elements ["+dataElementLength+"]. Expecting 3.");

        sysmiss = is.readSPSSDouble();
        highest = is.readSPSSDouble();
        lowest = is.readSPSSDouble();
    }

    public String toString() {
        String str="";
        str += "\nRECORD TYPE 7 SUBTYPE 4 - RELEASE AND MACHINE SPECIFIC FLOAT INFORMATION";
        str += "\nLocation        : "+fileLocation;
        str += "\nRecord Type     : "+recordTypeCode;
        str += "\nRecord Subtype  : "+recordSubtypeCode;
        str += "\nData elements   : "+numberOfDataElements;
        str += "\nElement length  : "+dataElementLength;
        str += "\nSysmiss         : "+sysmiss;
        str += "\nHighest         : "+highest;
        str += "\nLowest          : "+lowest;
        return(str);
    }

}
