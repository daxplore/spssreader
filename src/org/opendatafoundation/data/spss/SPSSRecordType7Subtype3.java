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
 * SPSS Record Type 7 Subtype 3 - Release and machine specific "integer" type information.
 * Added in SPSS release 4.0
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSRecordType7Subtype3 extends SPSSAbstractRecordType {
    // type 7
    int     recordTypeCode;
    int     recordSubtypeCode;
    int     dataElementLength;
    int     numberOfDataElements;
    // subtype 3
    int     releaseMajor;
    int     releaseMinor;
    int     releaseSpecial;
    int     machineCode;
    int     floatRepresentation; // 1=IEEE, 2=IBM370, 3=DEC BVAX E
    int     compressionScheme;
    int     endianness; // 1=big-endian, 2=little-endian
    int     characterRepresentation; // 1=EBCDIC, 2=7-bit ASCII, 3=8-bit ASCII, 4=DEC Kanji

    List<byte[]> dataElement;
    
    public void read(SPSSFile is) throws IOException, SPSSFileException {
        // position in file
        fileLocation = is.getFilePointer();

        // record type
        recordTypeCode = is.readSPSSInt();
        if(recordTypeCode!=7) throw new SPSSFileException("Error reading record type 7 subtype 3: bad record type ["+recordTypeCode+"]. Expecting Record Type 7.");

        // subtype
        recordSubtypeCode = is.readSPSSInt();
        if(recordSubtypeCode!=3) throw new SPSSFileException("Error reading record type 7 subtype 3: bad subrecord type ["+recordSubtypeCode+"]. Expecting Record Subtype 3.");

        // data elements
        dataElementLength = is.readSPSSInt();
        if(dataElementLength!=4) throw new SPSSFileException("Error reading record type 7 subtype 3: bad data element length ["+dataElementLength+"]. Expecting 4.");

        numberOfDataElements = is.readSPSSInt();
        if(numberOfDataElements!=8) throw new SPSSFileException("Error reading record type 7 subtype 3: bad number of data elements ["+dataElementLength+"]. Expecting 8.");

        releaseMajor = is.readSPSSInt();
        releaseMinor = is.readSPSSInt();
        releaseSpecial = is.readSPSSInt();
        machineCode = is.readSPSSInt();
        floatRepresentation = is.readSPSSInt();
        compressionScheme = is.readSPSSInt();
        endianness = is.readSPSSInt();
        characterRepresentation = is.readSPSSInt();
    }

    public String toString() {
        String str="";
        str += "\nRECORD TYPE 7 SUBTYPE 3 - RELEASE AND MACHINE SPECIFIC INTEGER INFORMATION";
        str += "\nLocation        : "+fileLocation;
        str += "\nRecord Type     : "+recordTypeCode;
        str += "\nRecord Subtype  : "+recordSubtypeCode;
        str += "\nData elements   : "+numberOfDataElements;
        str += "\nElement length  : "+dataElementLength;
        str += "\nRelease major   : "+releaseMajor;
        str += "\nRelease minor   : "+releaseMinor;
        str += "\nRelease special : "+releaseSpecial;
        str += "\nMachine code    : "+machineCode;
        str += "\nFloating point  : "+floatRepresentation+" ["+getFloatRepresentationLabel()+"]";
        str += "\nEndianness      : "+endianness+" ["+getEndiannessLabel()+"]";;
        str += "\nCharacter set   : "+characterRepresentation+" ["+getCharacterRepresentationLabel()+"]";;
        return(str);
    }
    
    /**
     * @return The label macthing the floating point representation 
     */
    public String getFloatRepresentationLabel() {
        String label = "Unknown"; 
        switch(floatRepresentation) {
            case 1: label="IEEE"; break;
            case 2: label="IBM 370"; break;
            case 3: label="DEX VAX E"; break;
        }
        return(label);
    }

    /**
     * @return The label macthing the floating point representation 
     */
    public String getEndiannessLabel() {
        String label = "Unknown"; 
        switch(floatRepresentation) {
            case 1: label="Big endian"; break;
            case 2: label="Little endian"; break;
        }
        return(label);
    }

    /**
     * @return The label macthing the character representation 
     */
    public String getCharacterRepresentationLabel() {
        String label = "Unknown"; 
        switch(characterRepresentation) {
            case 1: label="EBCDIC"; break;
            case 2: label="7-bit ASCII"; break;
            case 3: label="8-bit ASCII"; break;
            case 4: label="DEC Kanji"; break;
        }
        return(label);
    }
}
