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
import java.util.ArrayList;
import java.util.List;

/**
 * SPSS Record Type 7 Subtype 11 - Variable display parameters
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSRecordType7Subtype11 extends SPSSAbstractRecordType {
    // type 7
    int     recordTypeCode;
    int     recordSubtypeCode;
    int     dataElementLength;
    int     numberOfDataElements;
    
    // subtype 11
    List<VariableDisplayParams> variableDisplayParams; /** a collection of VariableDisplayParams (one for each variable) */

    
    public void read(SPSSFile is) throws IOException, SPSSFileException {
        // position in file
        fileLocation = is.getFilePointer();

        // record type
        recordTypeCode = is.readSPSSInt();
        if(recordTypeCode!=7) throw new SPSSFileException("Error reading record type 7 subtype 11: bad record type ["+recordTypeCode+"]. Expecting Record Type 7.");

        // subtype
        recordSubtypeCode = is.readSPSSInt();
        if(recordSubtypeCode!=11) throw new SPSSFileException("Error reading record type 7 subtype 11: bad subrecord type ["+recordSubtypeCode+"]. Expecting Record Subtype 11.");

        // data elements
        dataElementLength = is.readSPSSInt();
        if(dataElementLength!=4) throw new SPSSFileException("Error reading record type 7 subtype 11: bad data element length ["+dataElementLength+"]. Expecting 4.");

        numberOfDataElements = is.readSPSSInt();
        int n_variables = numberOfDataElements / 3;
        if( (numberOfDataElements % 3)!=0 ) throw new SPSSFileException("Error reading record type 7 subtype 11: number of data elements ["+dataElementLength+"] is not a multiple of 3.");
        
        // read display parameters for eeach variable
        variableDisplayParams = new ArrayList<VariableDisplayParams>();
        for(int i=0 ; i<n_variables; i++) {
            VariableDisplayParams params = new VariableDisplayParams();
            params.measure = is.readSPSSInt();
            params.width= is.readSPSSInt();
            params.alignment = is.readSPSSInt();
            variableDisplayParams.add(params);
        }
    }

    public String toString() {
        String str="";
        str += "\nRECORD TYPE 7 SUBTYPE 11 - VARIABLE DISPLAY PARAMETERS";
        str += "\nLocation        : "+fileLocation;
        str += "\nRecord Type     : "+recordTypeCode;
        str += "\nRecord Subtype  : "+recordSubtypeCode;
        str += "\nData elements   : "+numberOfDataElements;
        str += "\nElement length  : "+dataElementLength;
        int var_index=0;
        for (VariableDisplayParams params: variableDisplayParams) {
            var_index++;
            str += "\nDisplay params  : Var "+var_index+" Measure="+params.measure+" Width="+params.width+" Alignment="+params.alignment;
        }
        return(str);
    }

    public class VariableDisplayParams {
        int measure;
        int width;
        int alignment;
    }
}
