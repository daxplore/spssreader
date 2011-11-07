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

import java.util.ArrayList;
import java.util.List;

import org.opendatafoundation.data.FileFormatInfo;
import org.opendatafoundation.data.Utils;

/**
 * SPSS string variable
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSStringVariable extends SPSSVariable {
    /** a list of data values used to load the file into memory */ 
    public List<String> data;
    /** a single data value used when reading data from disk */ 
    public String value;
    
    public SPSSStringVariable(SPSSFile file) {
        super(file);
        type = VariableType.STRING;
        data = new ArrayList<String>();
    }
    /**
     * Adds a category to the variable  
     */
    public SPSSVariableCategory addCategory(byte[] byteValue, String label) {
        SPSSVariableCategory cat;
        String strValue = SPSSUtils.byte8ToString(byteValue);
        cat = categoryMap.get(strValue);
        if(cat==null) {
            // create and add to the map
            cat = new SPSSVariableCategory();
            categoryMap.put(strValue, cat);
        }
        cat.strValue = strValue;
        cat.label = label;
        return(cat);
    }

    /**
     * Gets a category for this variable based on a byte[8] value
     */
    public SPSSVariableCategory getCategory(byte[] byteValue) {
        String strValue = SPSSUtils.byte8ToString(byteValue);
        return(getCategory(strValue));
    }

    /**
     * Gets a category for this variable based on a double value
     */
    public SPSSVariableCategory getCategory(String strValue) {
        return(categoryMap.get(strValue));
    }

    /**
     * @return A string representing variable in SPSS syntax  
     */
    public String getSPSSFormat() {
        // TODO: AHEXw format?
        return("A"+this.variableRecord.writeFormatWidth);
    }

    /**
     * Returns an observation value as a string
     * @throws SPSSFileException 
     * 
     */
    public String getValueAsString(int obsNumber, FileFormatInfo dataFormat) throws SPSSFileException {
        String strValue;

        // check range
        if(obsNumber < 0 || obsNumber > this.data.size()) {
            throw new SPSSFileException("Invalid observation number ["+obsNumber+". Range is 1 to "+this.data.size()+"] or 0.");
        }
        // init value 
        if(obsNumber == 0) strValue = this.value;
        else if(obsNumber > 0 && this.data.size()==0 )throw new SPSSFileException("No data availble");
        else strValue=data.get(obsNumber-1);
        
        // format output
        if(dataFormat.format==FileFormatInfo.Format.ASCII) {
            if(dataFormat.asciiFormat==FileFormatInfo.ASCIIFormat.FIXED) { // padding
                strValue += Utils.leftPad("", this.getLength() - strValue.length());
            }
            else if(dataFormat.asciiFormat==FileFormatInfo.ASCIIFormat.CSV) { 
                // see http://en.wikipedia.org/wiki/Comma-separated_values
                // double the double-quote
                if(strValue.contains("\"")) {
                    strValue = strValue.replaceAll("\"", "\"\"");
                }
                // surround by double-quote if contains comma, double-quote, line break
                if(strValue.contains(",") || strValue.contains("\"") || strValue.contains("\n")) {
                    strValue = "\""+strValue+"\"";
                }
            }
            
        }
        return(strValue);
    }
}
