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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.opendatafoundation.data.FileFormatInfo;
import org.opendatafoundation.data.Utils;

/**
 * SPSS numeric variable
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 */
public class SPSSNumericVariable extends SPSSVariable {
    /** a list of data values used to load the file into memory */ 
    public List<Double> data; 
    /** a single data value used when reading data from disk */ 
    public double value;
    
    // summary statistics
    public double min = Double.MAX_VALUE; 
    public double max = Double.MIN_VALUE;
    public double mean = 0.0;
    public double min_wgt = Double.MAX_VALUE; 
    public double max_wgt = Double.MIN_VALUE;
    public double mean_wgt = 0.0; 
    
    /**
     * Class constructor 
     */
    public SPSSNumericVariable(SPSSFile file) {
        super(file);
        data = new ArrayList<Double>();
        type=VariableType.NUMERIC;
    }

    /**
     * Adds a category to the variable based on a byte[8] value  
     * @throws SPSSFileException 
     */
    public SPSSVariableCategory addCategory(byte[] byteValue, String label) throws SPSSFileException {
        double value = SPSSUtils.byte8ToDouble(byteValue);
        return(addCategory(value,label));
    }

    /**
     * Adds a category to the variable based on a double value  
     * @throws SPSSFileException 
     */
    public SPSSVariableCategory addCategory(double value, String label) throws SPSSFileException {
        SPSSVariableCategory cat;
        String strValue = valueToString(value).trim();
        cat = categoryMap.get(strValue);
        if(cat==null) {
            // create and to the map
            cat = new SPSSVariableCategory();
            categoryMap.put(strValue, cat);
        }
        cat.value = value;
        cat.strValue = strValue;
        cat.label = label;
        return(cat);
    }
    
    /**
     * Gets a category for this variable based on a byte[8] value
     * @throws SPSSFileException 
     */
    public SPSSVariableCategory getCategory(byte[] byteValue) throws SPSSFileException {
        double value = SPSSUtils.byte8ToDouble(byteValue);
        return(getCategory(value));
    }

    /**
     * Gets a category for this variable based on a double value
     * @throws SPSSFileException 
     */
    public SPSSVariableCategory getCategory(double value) throws SPSSFileException {
        return(categoryMap.get(valueToString(value)));
    }
    
    /**
     * @return A string representing variable in SPSS syntax  
     */
    public String getSPSSFormat() {
        String formatStr = "";
        switch(this.variableRecord.writeFormatType) {
        case 3: // comma
            formatStr="Comma"+getLength()+"."+getDecimals();
            break;
        case 4: // dollar
            formatStr="Dollar"+getLength()+"."+getDecimals();
            break;
        case 5: // fixed format (default)
            formatStr="F"+getLength()+"."+getDecimals();
            break;
        case 17: // scientific notation
            formatStr="E"+getLength()+"."+getDecimals();
            break;
        case 20: // Date dd-mmm-yyyy or dd-mmm-yy
            formatStr="Date"+getLength();
            break;
        case 21: // Time in hh:mm, hh:mm:ss or hh:mm:ss.ss
            formatStr="Time"+getLength()+"."+getDecimals();
            break;
        case 22: // DateTime in dd-mmm-yyyy hh:mm, dd-mmm-yyyy hh:mm:ss or dd-mmm-yyyy hh:mm:ss.ss 
            formatStr="DateTime"+getLength()+"."+getDecimals();
            break;
        case 23: // Date in mm/dd/yy or mm/dd/yyyy 
            formatStr="ADate"+getLength();
            break;
        case 24: // Date in yyyyddd or yyddd 
            formatStr="JDate"+getLength();
            break;
        case 25: // DateTime in ddd:hh:mm, ddd:hh:mm:ss or ddd:hh:mm:ss.ss 
            formatStr="DTime"+getLength()+"."+getDecimals();
            break;
        case 26: // Date as day of the week, full name or 3-letter 
            formatStr="Wkday"+getLength();
            break;
        case 27: // Date 3-letter month 
            formatStr="Month"+getLength();
            break;
        case 28: // Date in mmm yyyy or mmm yy
            formatStr="Moyr"+getLength();
            break;
        case 29: // Date in q Q yyyy or q Q yy 
            formatStr="QYr"+getLength();
            break;
        case 30: // Date in wk WK yyyy or wk WK yy
            formatStr="Wkyr"+getLength();
            break;
        case 32: // dot
            formatStr="Dot"+getLength()+"."+getDecimals();
            break;
        case 33: // Custom currency A
            formatStr="Cca"+getLength()+"."+getDecimals();
            break;
        case 34: // Custom currency B
            formatStr="Ccb"+getLength()+"."+getDecimals();
            break;
        case 35: // Custom currency C
            formatStr="Ccc"+getLength()+"."+getDecimals();
            break;
        case 36: // Custom currency D
            formatStr="Ccd"+getLength()+"."+getDecimals();
            break;
        case 37: // Custom currency E
            formatStr="Cce"+getLength()+"."+getDecimals();
            break;
        case 38: // Date in dd.mm.yy or dd.mm.yyyy 
            formatStr="EDate"+getLength();
            break;
        case 39: // Date in yyyy/mm/dd or yy/mm/dd (?) 
            formatStr="SDate"+getLength();
            break;
        default:
            formatStr="other";
        }
        return(formatStr);
        
    }

    /**
     * Returns an observation value as a string based on the specified data ad variable format.
     * The specified record number is used to determine which value is read. 
     * If a nthe observation umber is between 1 and the number of observation in the file is specifed 
     * and asusming the data has been loaded in memory, the relevant record number value is returned. 
     * If the observation number is 0, the variable value is retrned instead.    
     * 
     * @param obsNumber the record. Either 0 or between 1 and the nukber of observations 
     * @param dataFormat the file format 
     * @throws SPSSFileException 
     */
    public String getValueAsString(int obsNumber, FileFormatInfo dataFormat) throws SPSSFileException {
        String strValue;
        double val;

        // check range
        if(obsNumber < 0 || obsNumber > this.data.size()) {
            throw new SPSSFileException("Invalid observation number ["+obsNumber+". Range is 1 to "+this.data.size()+"] or 0.");
        }
        // init value to convert
        if(obsNumber == 0) val = this.value;
        else if(obsNumber > 0 && this.data.size()==0 )throw new SPSSFileException("No data availble");
        else val = data.get(obsNumber-1);

        // convert
        strValue = valueToString(val);

        // format output
        
        // length
        if(dataFormat.asciiFormat==FileFormatInfo.ASCIIFormat.FIXED) {
            // fixed length formats
            if(strValue.equals(".")) strValue = Utils.leftPad("", this.getLength()); // replace missing values with spaces
            else if(strValue.length() < getLength()) strValue = Utils.leftPad(strValue,this.getLength()); // left pad 
            else if(strValue.length() > getLength()) { // this value is too long to fit in the allocate space
                // for fixed format, see if we can truncate the decimals (this is the same for SPSS fixed export)
                if(this.variableRecord.writeFormatType==5 && this.getDecimals()>0) {
                    int dotPosition = strValue.lastIndexOf(".");
                    // TODO: when a value is less between 1 and -1 (0.1234), SPSS also removes the leading zero 
                    if(dotPosition+2 <= getLength()) { // we can fit at least one decimal
                        strValue = String.format(Locale.US,"%"+this.getLength()+"."+ (this.getLength()-dotPosition-1)+"f",value);
                    }
                    else if(dotPosition <= getLength()) { // we can fit the non-decimal protion
                        strValue = Utils.leftPad(strValue.substring(1,dotPosition-1),this.getLength());
                    }
                    else strValue = Utils.leftPad("",getLength(),'*'); 
                }
                else strValue = Utils.leftPad("",getLength(),'*'); // this overflows the allocated width, return a string of '*'
            }
        }
        else {
            // variable length formats
            strValue = strValue.trim();
        }
        
        // some number formats may contain a comma
        if(dataFormat.format==FileFormatInfo.Format.ASCII) {
            if(dataFormat.asciiFormat==FileFormatInfo.ASCIIFormat.CSV) {
                if(strValue.contains(",") || strValue.contains("\"") || strValue.contains("\n")) {
                    strValue = "\""+strValue+"\"";
                }
            }
        }
        
        return(strValue);
    }

    
    /**
     * Converts a numeric value (float) into a string representation based on the variable formnatting.
     * 
     * @param  value the value to format
     * @return the string containing the formatted value
     * @throws SPSSFileException if an unknown write fromat type is found
     */
    public String valueToString(double value) throws SPSSFileException {
        String strFormat = "";
        String strValue;
        int    nDecimals;
        GregorianCalendar calendar;
        
        if(new Double(value).isNaN()) {
            strValue = ".";
        }
        else {
            switch(this.variableRecord.writeFormatType) {
            case 3: // Comma
                strFormat += "%,."+this.getDecimals()+"f"; 
                strValue = String.format(Locale.US,strFormat,value);
                break;
            case 4: // dollar
                strFormat += "$%."+this.getDecimals()+"f"; 
                strValue = String.format(Locale.US,strFormat,value);
                break;
            case 5: // fixed format (default)
                strFormat += "%"+this.getLength()+"."+this.getDecimals()+"f"; 
                strValue = String.format(Locale.US,strFormat,value);
                break;
            case 17: // scientific notation
                nDecimals = this.getDecimals();
                if(nDecimals>0) nDecimals--; // remove one decimal for the sign
                strFormat += "% "+this.getLength()+"."+nDecimals+"E"; 
                strValue = String.format(Locale.US,strFormat,value);
                break;
            case 20: // Date dd-mmm-yyyy or dd-mmm-yy
                calendar = SPSSUtils.numericToCalendar(value);
                if(this.getLength()==11) strFormat += "%1$td-%1$tb-%1$tY";
                else strFormat += "%1$td-%1$tb-%1$ty";
                strValue = String.format(Locale.US,strFormat,calendar).toUpperCase();
                break;
            case 21: // Time in hh:mm, hh:mm:ss or hh:mm:ss.ss
                calendar = SPSSUtils.numericToCalendar(value);
                strFormat += "%1$tH:%1$tM";
                if(this.getLength()>=8)strFormat += ":%1$tS";
                if(this.getLength()==11) strFormat += ".%2$2d"; // we add the 2-digit for 1/100 sec as extra parameter (Formatter and Calendar use 3 digits milliseconds)
                // TODO: add .ss for width=11
                strValue = String.format(Locale.US,strFormat, calendar, calendar.get(Calendar.MILLISECOND)/10 ).toUpperCase();
                break;
            case 22: // DateTime in dd-mmm-yyyy hh:mm, dd-mmm-yyyy hh:mm:ss or dd-mmm-yyyy hh:mm:ss.ss 
                calendar = SPSSUtils.numericToCalendar(value);
                strFormat += "%1$td-%1$tb-%1$tY %1$tH:%1$tM";
                if(this.getLength()>=20) strFormat += ":%1$tS";
                if(this.getLength()==23) strFormat += ".%2$2d";
                strValue = String.format(Locale.US,strFormat, calendar,calendar.get(Calendar.MILLISECOND)/10).toUpperCase();
                break;
            case 23: // Date in mm/dd/yy or mm/dd/yyyy 
                calendar = SPSSUtils.numericToCalendar(value);
                if(this.getLength()==10) strFormat += "%1$tm/%1$td/%1$tY"; 
                else strFormat += "%1$tm/%1$td/%1$ty";
                strValue = String.format(Locale.US,strFormat, calendar);
                break;
            case 24: // Date in yyyyddd or yyddd 
                calendar = SPSSUtils.numericToCalendar(value);
                if(this.getLength()==7) strFormat += "%1$tY%1$tj";
                else strFormat += "%1$ty%1$tj";
                strValue = String.format(Locale.US,strFormat, calendar);
                break;
            case 25: // DateTime in ddd:hh:mm, ddd:hh:mm:ss or ddd:hh:mm:ss.ss 
                calendar = SPSSUtils.numericToCalendar(value);
                strFormat += "%1$tj:%1$tH:%1$tM";
                if(this.getLength()>=12) strFormat += ":%1$tS";
                if(this.getLength()==15) strFormat += ".%2$2d";
                strValue = String.format(Locale.US,strFormat, calendar,calendar.get(Calendar.MILLISECOND)/10);
                break;
            case 26: // Date as day of the week, full name or 3-letter 
                calendar = new GregorianCalendar();
                calendar.set(Calendar.DAY_OF_WEEK, (int) value);
                if(this.getLength()==9) strFormat += "%1$tA";
                else strFormat += "%1$ta";
                strValue = String.format(Locale.US,strFormat, calendar).toUpperCase(); // upper case to match SPSS export
                break;
            case 27: // Date 3-letter month 
                calendar = new GregorianCalendar();
                calendar.set(Calendar.MONTH, (int) value-1); // January is 0 in Java
                strFormat += "%1$tb";
                strValue = String.format(Locale.US,strFormat, calendar).toUpperCase(); // upper case to match SPSS export
                break;
            case 28: // Date in mmm yyyy or mmm yy
                calendar = SPSSUtils.numericToCalendar(value);
                if(this.getLength()==8) strFormat += "%1$tb %1$tY"; 
                else strFormat += "%1$tb %1$ty";
                strValue = String.format(Locale.US,strFormat, calendar).toUpperCase();
                break;
            case 29: // Date in q Q yyyy or q Q yy 
                calendar = SPSSUtils.numericToCalendar(value);
                if(calendar.get(Calendar.MONTH)<=3) strFormat += "1 Q ";
                else if(calendar.get(Calendar.MONTH)<=6) strFormat += "2 Q ";
                else if(calendar.get(Calendar.MONTH)<=9) strFormat += "3 Q "; 
                else strFormat += "4 Q"; 
                if(this.getLength()==8) strFormat += "%1$tY";
                else strFormat += "%1$ty";
                strValue = String.format(Locale.US,strFormat, calendar);
                break;
            case 30: // Date in wk WK yyyy or wk WK yy
                calendar = SPSSUtils.numericToCalendar(value);
                if(this.getLength()==10) strFormat += "%1$2d WK %2$tY"; 
                else strFormat += "%1$2d WK %2$ty";
                strValue = String.format(Locale.US,strFormat, calendar.get(Calendar.WEEK_OF_YEAR), calendar);
                break;
            case 32: // Dot (use Germany locale, for some reasonm french does not display the dot thousand separator)
                strFormat += "%,."+this.getDecimals()+"f"; 
                strValue = String.format(Locale.GERMANY,strFormat,value);
                break;
            case 33: // custom currency A
            case 34: // custom currency B
            case 35: // custom currency C
            case 36: // custom currency D
            case 37: // custom currency E
                strFormat += "%"+this.getLength()+"."+this.getDecimals()+"f"; 
                strValue = String.format(Locale.US,strFormat,value);
                break;
            case 38: // Date in dd.mm.yy or dd.mm.yyyy 
                calendar = SPSSUtils.numericToCalendar(value);
                if(this.getLength()==10) strFormat += "%1$td.%1$tm.%1$tY"; 
                else strFormat += "%1$td.%1$tm.%1$ty";
                strValue = String.format(Locale.US,strFormat, calendar);
                break;
            case 39: // Date in yy/mm/dd or yyyy/mm/dd 
                calendar = SPSSUtils.numericToCalendar(value);
                if(this.getLength()==10) strFormat += "%1$tY/%1$tm.%1$td"; 
                else strFormat += "%1$ty/%1$tm/%1$td";
                strValue = String.format(Locale.US,strFormat, calendar);
                break;
            default:
                throw new SPSSFileException("Unknown write format type ["+this.variableRecord.writeFormatType+"]");
            }
        }
        return(strValue);
    }
}
