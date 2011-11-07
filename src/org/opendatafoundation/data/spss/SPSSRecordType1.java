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

/**
 * SPSS Record Type 1 - General information
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSRecordType1 extends SPSSAbstractRecordType {
    String  recordTypeCode;
    String  productIdentification;
    int     layoutCode;;
    int     OBSperObservation;
    int     compressionSwitch;
    int     weightVariableIndex;
    int     numberOfCases;
    double  compressionBias;
    String  creationDate;
    String  creationTime;
    String  fileLabel;
    
    public void read(SPSSFile is) throws IOException, SPSSFileException {
        // position in file
        fileLocation = is.getFilePointer();
        // signature
        recordTypeCode = is.readSPSSString(4);
        if(recordTypeCode.compareTo("$FL2")!=0) throw new SPSSFileException("Read header error: this is not a valid SPSS file. Does not start with $FL2.");
        // identification
        productIdentification = is.readSPSSString(60).replaceAll("\\s+$", "");

        
        // layout code
        long filePointer = is.getFilePointer(); // save position for endianness check below
        layoutCode = is.readSPSSInt();
        
        // --> layoutCode should be 2 or 3. 
        // --> If not swap bytes and check again which would then indicate big-endian
        // See PSPP or R's foreign package sfm-read.c file
        if(layoutCode!=2 && layoutCode!=3) {
        	// try to flip to big-endian mode and read again
        	is.isBigEndian=true;
        	is.seek(filePointer);
            layoutCode = is.readSPSSInt();
        }
            
        // check layout type
        /* 20080303-PH: Removed this check as received valid files with layout=3.
        if(layoutCode==3) {
        	throw new SPSSFileException("Read header: SPSS Portable files not supported (layout Code ["+layoutCode+"]).");
        }
        if(layoutCode!=2) {
        	throw new SPSSFileException("Read header error: invalid Layout Code ["+layoutCode+"]. Value should be 2.");
        }
        */
        // OBS
        OBSperObservation = is.readSPSSInt();
        // compression
        compressionSwitch = is.readSPSSInt();
        // weight
        weightVariableIndex = is.readSPSSInt();
        // #cases
        numberOfCases = is.readSPSSInt();
        // compression bias
        compressionBias = is.readSPSSDouble();
        // time stamp
        creationDate = is.readSPSSString(9);
        creationTime = is.readSPSSString(8);
        // file label
        fileLabel = is.readSPSSString(64).replaceAll("\\s+$", "");
        // padding
        is.skipBytes(3);
    }
    
    public String toString() {
        String str="";
        str += "\nRECORD TYPE 1 - GENERAL INFO";
        str += "\nLocation        : "+fileLocation;
        str += "\nRecord Type     : "+recordTypeCode;
        str += "\nProduct ID      : "+productIdentification;
        str += "\nLayout code     : "+layoutCode;
        str += "\nOBS per obs     : "+OBSperObservation;
        str += "\nCompression     : "+compressionSwitch;
        str += "\nWeight Variable : "+weightVariableIndex;
        str += "\nNumber of cases : "+numberOfCases;
        str += "\nCompression bias: "+compressionBias;
        str += "\nCreation date   : "+creationDate;
        str += "\nCreation time   : "+creationTime;
        str += "\nFile label      : "+fileLabel;
        return(str);
    }

}
