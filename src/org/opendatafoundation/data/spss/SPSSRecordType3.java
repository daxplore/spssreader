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

/**
 * SPSS Record Type 3 - Value labels
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSRecordType3 extends SPSSAbstractRecordType {
    int     recordTypeCode;
    int     numberOfLabels;
    Map<byte[],String> valueLabel = new LinkedHashMap<byte[],String>();
    
    public void read(SPSSFile is) throws IOException, SPSSFileException {
        // position in file
        fileLocation = is.getFilePointer();

        // record type
        recordTypeCode = is.readSPSSInt();
        if(recordTypeCode!=3) throw new SPSSFileException("Error reading variableRecord: bad record type ["+recordTypeCode+"]. Expecting Record Type 3.");

        // number of labels
        numberOfLabels = is.readSPSSInt();

        // labels
        for(int i=0; i<numberOfLabels; i++) {
            // read the label value
            byte[] value = new byte[8];
            is.read(value);
            
            if(is.isBigEndian) { 
            	// flip value
            	// TODO: don't do this for string variables (but we don't know the type here....)
            	for(int j=0; j<3; j++) {
            		byte tmp;
            		tmp=value[j];
            		value[j]=value[7-j];
            		value[7-j]=tmp;
            	}
            }
            
            // the following byte in an unsigned integer (max value is 60)
            int labelLength = is.read();

            // read the label
            String label = is.readSPSSString(labelLength);
            // value labels are stored in chunks of 8-bytes with space allocated for length+1 characters
            // --> we need to skip unused bytes in the last chunk
            if( ((labelLength+1) % 8) != 0) is.skipBytes( 8 - ((labelLength+1) % 8) );

            // Store in map
            valueLabel.put(value, label);
        }
    }

    public String toString() {
        String str="";
        str += "\nRECORD TYPE 3 - VALUE LABEL RECORD";
        str += "\nLocation        : "+fileLocation;
        str += "\nRecord Type     : "+recordTypeCode;
        str += "\nNumber labels   : "+numberOfLabels;
        Iterator iter = valueLabel.entrySet().iterator();
        while(iter.hasNext()) {
           Map.Entry entry = (Map.Entry)iter.next();
           byte[] value = (byte[]) entry.getKey();
           String label = (String) entry.getValue();
           str += "\n "+SPSSUtils.byte8ToDouble(value)+"="+label;
        }
        return(str);
    }
}
