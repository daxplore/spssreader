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
 * SPSS Record Type 2 - Variable information
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSRecordType2 extends SPSSAbstractRecordType {
    int     recordTypeCode;
    int     variableTypeCode;
    int     hasLabel;
    int     missingValueFormatCode;
    int     printFormatCode;
        int     printFormatDecimals;
        int     printFormatWidth;
        int     printFormatType;
        int     printFormatZero; // always 0 
    int     writeFormatCode;
        int     writeFormatDecimals;
        int     writeFormatWidth;
        int     writeFormatType;
        int     writeFormatZero; // always 0
    String  name;
    int     labelLength;
    String  label;
    //double  missingValue2[] = new double[3];
    byte    missingValue[][] = new byte[3][8];
    

    /* The value label set associated with this variableRecord */
    SPSSRecordType3 valueLabelSet;
    
    /**
     * @return The code matching the format type 
     */
    public String getFormatTypeCode(int value) {
        String label = "UNK";
        switch(value) {
            case  0: label = ""; break;
            case  1: label = "A"; break;
            case  2: label = "AHEX"; break;
            case  3: label = "COMMA"; break;
            case  4: label = "DOLLAR"; break;
            case  5: label = "F"; break;
            case  6: label = "IB"; break;
            case  7: label = "PIBHEX"; break;
            case  8: label = "P"; break;
            case  9: label = "PIB"; break;
            case 10: label = "PK"; break;
            case 11: label = "RB"; break;
            case 12: label = "RBHEX"; break;
            case 15: label = "Z"; break;
            case 16: label = "N"; break;
            case 17: label = "E"; break;
            case 20: label = "DATE"; break;
            case 21: label = "TIME"; break;
            case 22: label = "DATETIME"; break;
            case 23: label = "ADATE"; break;
            case 24: label = "JDATE"; break;
            case 25: label = "DTIME"; break;
            case 26: label = "WKDAY"; break;
            case 27: label = "MONTH"; break;
            case 28: label = "MOYR"; break;
            case 29: label = "QYR"; break;
            case 30: label = "WKYR"; break;
            case 31: label = "PCT"; break;
            case 32: label = "DOT"; break;
            case 33: label = "CCA"; break; 
            case 34: label = "CCB"; break;
            case 35: label = "CCC"; break;
            case 36: label = "CCD"; break;
            case 37: label = "CCE"; break;
            case 38: label = "EDATE"; break;
            case 39: label = "SDATE"; break;
        }
        return(label);
    }

    /**
     * @return The label matching the format type 
     */
    public String getFormatTypeLabel(int value) {
        String label = "Unknown";
        switch(value) {
            case  0: label = "Continuation of string variable"; break;
            case  1: label = "Alphanumeric"; break;
            case  2: label = "Alphanumeric hexadecimal"; break;
            case  3: label = "F format with comma"; break;
            case  4: label = "Integer binary"; break;
            case  5: label = "F (default numeric) format"; break;
            case  6: label = "Integer binary"; break;
            case  7: label = "Positive integer binary - hexadecimal"; break;
            case  8: label = "Packed decimal"; break;
            case  9: label = "Positive integer binary (unsigned)"; break;
            case 10: label = "Positive packed decimal (unsigned)"; break;
            case 11: label = "Floating point binary"; break;
            case 12: label = "Floating point binary - hex"; break;
            case 15: label = "Zoned decimal"; break;
            case 16: label = "N format - unsigned with leading zeroes"; break;
            case 17: label = "E fromat - with explicit power of 10"; break;
            case 20: label = "Date format dd-mmm-yyyy"; break;
            case 21: label = "Time format hh:mm:ss.s"; break;
            case 22: label = "Date and time"; break;
            case 23: label = "Date in mm/dd/yyyy form"; break;
            case 24: label = "Julian date - yyyyddd"; break;
            case 25: label = "Date-time dd hh:mm:ss.s"; break;
            case 26: label = "Day of the week"; break;
            case 27: label = "Month"; break;
            case 28: label = "mmm yyyy"; break;
            case 29: label = "q Q yyyy"; break;
            case 30: label = "ww WK yyyy"; break;
            case 31: label = "Percent - F followed by '%'"; break;
            case 32: label = "Like COMMA, swicthing dot for comma"; break;
            case 33: label = "User-programmable currency format (1)"; break; 
            case 34: label = "User-programmable currency format (2)"; break;
            case 35: label = "User-programmable currency format (3)"; break;
            case 36: label = "User-programmable currency format (4)"; break;
            case 37: label = "User-programmable currency format (5)"; break;
            case 38: label = "Date in dd.mm.yyyy style"; break;
            case 39: label = "Date in yyyy/mm/dd style"; break;
        }
        return(label);
    }

    /**
     * Read the record in the SPSS file 
     */
    public void read(SPSSFile is) throws IOException, SPSSFileException {
        // position in file
        fileLocation = is.getFilePointer();
        // record type
        recordTypeCode = is.readSPSSInt();
        if(recordTypeCode!=2) throw new SPSSFileException("Error reading variable Record: bad record type ["+recordTypeCode+"]. Expecting Record Type 2.");
        // variableRecord type
        variableTypeCode = is.readSPSSInt();
        // has label
        hasLabel = is.readSPSSInt();
        // missing value format
        missingValueFormatCode = is.readSPSSInt();
        if(Math.abs(missingValueFormatCode)>3) throw new SPSSFileException("Error reading variable Record: invalid missing value format code ["+missingValueFormatCode+"]. Range is -3 to 3.");
        // format codes (reversed on non-intel machines)
        printFormatCode = is.readSPSSInt();
            printFormatDecimals = (printFormatCode >>  0) & 0xFF; // byte 1
            printFormatWidth    = (printFormatCode >>  8) & 0xFF; // byte 2
            printFormatType     = (printFormatCode >> 16) & 0xFF; // byte 3
            printFormatZero     = (printFormatCode >> 24) & 0xFF; // byte 4
        writeFormatCode = is.readSPSSInt();
            writeFormatDecimals = (writeFormatCode >>  0) & 0xFF; // byte 1
            writeFormatWidth    = (writeFormatCode >>  8) & 0xFF; // byte 2
            writeFormatType     = (writeFormatCode >> 16) & 0xFF; // byte 3
            writeFormatZero     = (writeFormatCode >> 24) & 0xFF; // byte 4
        // name
        name = is.readSPSSString(8).replaceAll("\\s+$", "");
        // label
        if(hasLabel==1) {
            labelLength = is.readSPSSInt();
            label = is.readSPSSString(labelLength);
            // variableRecord labels are stored in chunks of 4-bytes
            // --> we need to skip unused bytes in the last chunk
            if( (labelLength % 4) != 0) is.skipBytes( 4 - (labelLength % 4));
        }
        // missing values
        for(int i=0; i < Math.abs(missingValueFormatCode) ; i++) {
            //missingValue[i] = is.readSPSSDouble();
             is.read(missingValue[i]);
        }
    }

    public String toString() {
        String str="";
        str += "\nRECORD TYPE 2 - VARIABLE";
        str += "\nLocation        : "+fileLocation;
        str += "\nRecord Type     : "+recordTypeCode;
        str += "\nVariable Type   : "+variableTypeCode;
        str += "\nHas Label       : "+hasLabel;
        str += "\nMissing Format  : "+missingValueFormatCode;
        str += "\nPrint Format    : "+printFormatCode;
        str += "\n- Decimals      : "+printFormatDecimals;
        str += "\n- Width         : "+printFormatWidth;
        str += "\n- Type          : "+printFormatType + " ["+getFormatTypeCode(printFormatType)+"/"+getFormatTypeLabel(printFormatType)+"]";;
        str += "\n- Zero          : "+printFormatZero;
        str += "\nWrite Format    : "+writeFormatCode;
        str += "\n- Decimals      : "+writeFormatDecimals;
        str += "\n- Width         : "+writeFormatWidth;
        str += "\n- Type          : "+writeFormatType + " ["+getFormatTypeCode(writeFormatType)+"/"+getFormatTypeLabel(writeFormatType)+"]";;
        str += "\n- Zero          : "+writeFormatZero;
        str += "\nName            : "+name;
        if(hasLabel==1) {
            str += "\nLabel length    : "+labelLength;
            str += "\nLabel           : "+label;
        }
        // missing values
        if(this.missingValueFormatCode>0) {
            // 1-3 --> discrete missing value codes
            for(int i=0; i < this.missingValueFormatCode ; i++) {
                str += "\nMissing "+i+"       :";
                if(this.variableTypeCode==0) str += ""+SPSSUtils.byte8ToDouble(missingValue[i]);
                else str += SPSSUtils.byte8ToString(missingValue[i]);
            }
        }
        else if(this.missingValueFormatCode <= -2) {
            // -2 --> range of missing value codes
            str += "\nMissing range   : ";
            if(this.variableTypeCode==0) str += ""+SPSSUtils.byte8ToDouble(missingValue[0]);
            else str += SPSSUtils.byte8ToString(missingValue[0]);
            str += " - ";
            if(this.variableTypeCode==0) str += ""+SPSSUtils.byte8ToDouble(missingValue[1]);
            else str += SPSSUtils.byte8ToString(missingValue[1]);
            if(this.missingValueFormatCode==-3) {
                str += "\nMissing 3       : ";
                if(this.variableTypeCode==0) str += ""+SPSSUtils.byte8ToDouble(missingValue[2]);
                else str += SPSSUtils.byte8ToString(missingValue[2]);
            }
        }
        return(str);
    }

}
