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

/**
 * Class to read SPSS comrepssed/uncompressedf data record 
 *  
 * @author Pascal Heus (pheus@opendatafoundation)
 */
public class SPSSDataRecord {
    SPSSFile file;
    long fileLocation;
    
    static byte[] cluster = new byte[8]; // 8-byte cluster for compressed files (this value is retained between calls)
    static byte   clusterIndex=8; // for compressed files (once initialized, this value is retained between calls) 
    
    /**
     * Reads the values for the current observation into memory. 
     * This assumes that the file pointer is properly positionned.
     *  
     * @throws IOException
     * @throws SPSSFileException
     */
    public void read(SPSSFile is) throws IOException, SPSSFileException {
        read(is,false);
    }

    /**
     * Reads the values for the current observation into memory. 
     * If fromDisk is set to true, the record values are loaded 
     * into the variable single values instead of the data list
     * This assumes that the file pointer is properly positionned.
     *  
     * @throws IOException
     * @throws SPSSFileException
     */
    public void read(SPSSFile is, boolean fromDisk) throws IOException, SPSSFileException {
        SPSSNumericVariable numVar = null;
        double numData=Double.NaN;
        SPSSStringVariable strVar = null;
        String strData="";

        // init
        file = is;
        fileLocation = file.getFilePointer();
        
        // read data for each variable
        Iterator varIterator = file.variableMap.keySet().iterator();
        while(varIterator.hasNext()) {
            SPSSVariable var = file.variableMap.get(varIterator.next());

            //file.log("\nVARIABLE "+var.variableRecord.name+" pointer "+file.getFilePointer());
            
            // compute number of blocks used by this variable
            int blocksToRead=0; /** Number of data storage blocks used by the current variable */ 
            int charactersToRead=0; /** Number of chacarters to read for a string variable */
            int dataIndex = 0;

            // init 
            if(var.type==SPSSVariable.VariableType.NUMERIC) {
                numData = Double.NaN;
                blocksToRead=1;
            }
            else {
                strData="";
                // string: depends on string length but always in blocks of 8 bytes
                charactersToRead = var.variableRecord.variableTypeCode;
                blocksToRead = ( (charactersToRead-1) / 8) + 1;
            }
                
            // read the variable from the file 
            while(blocksToRead > 0) {
                //file.log("REMAINING #blocks ="+blocksToRead);
                if(file.isCompressed()) {
                    /* COMPRESSED DATA FILE */
                    //file.log("cluster index "+clusterIndex);
                    if(clusterIndex>7) {
                        //file.log("READ CLUSTER");
                        // need to read a new compression cluster of up to 8 variables
                        file.read(cluster);
                        clusterIndex=0;
                    }
                    // convert byte to an unsigned byte in an int 
                    int byteValue = (0x000000FF & (int)cluster[clusterIndex]);
                    //file.log("Variable "+var.variableRecord.name+" cluster byte"+(clusterIndex)+"="+byteValue);
                    clusterIndex++;

                    switch(byteValue) {
                    case 0: // skip this code
                        break;
                    case 252: // end of file, no more data to follow. This should not happen.
                        throw new SPSSFileException("Error reading data: unexpected end of compressed data file (cluster code 252)"); 
                    case 253: // data cannot be compressed, the value follows the cluster
                        if(var.type==SPSSVariable.VariableType.NUMERIC) {
                            numData =file.readSPSSDouble();
                        }
                        else {  // STRING
                            // read a maximum of 8 characters but could be less if this is the last block
                            int blockStringLength = Math.min(8,charactersToRead);
                            // append to existing value
                            strData += file.readSPSSString(blockStringLength);
                            // if this is the last block, skip the remaining dummy byte(s) (in the block of 8 bytes)
                            if(charactersToRead<8) {
                                file.skipBytes(8-charactersToRead);
                            }
                            // update the characters counter
                            charactersToRead -= blockStringLength; 
                        }
                        break;
                    case 254: // all blanks
                        if(var.type==SPSSVariable.VariableType.NUMERIC) {
                            // note: not sure this is used for numeric values (?)
                            numData =0.0;
                        }
                        else {
                            // append 8 spaces to existing value
                            strData  += "        ";
                        }
                        break;
                    case 255: // system missing value
                        if(var.type==SPSSVariable.VariableType.NUMERIC) {
                            // numeric variable
                            numData = Double.NaN;
                        }
                        else {
                            // string variable
                            throw new SPSSFileException("Error reading data: unexpected SYSMISS for string variable"); 
                        }
                        break;
                    default: // 1-251 value is code minus the compression BIAS (normally always equal to 100)
                        if(var.type==SPSSVariable.VariableType.NUMERIC) {
                            // numeric variable
                            numData = byteValue - file.infoRecord.compressionBias;
                            //file.log(""+numVar.data.get(numVar.data.size()-1));
                        }
                        else {
                            // string variable
                            throw new SPSSFileException("Error reading data: unexpected compression code for string variable"); 
                        }
                        break;
                    }
                }
                else {
                    /* UNCOMPRESSED DATA */
                    if(var.type==SPSSVariable.VariableType.NUMERIC) {
                        numData = file.readSPSSDouble();
                    }
                    else {
                        // read a maximum of 8 characters but could be less if this is the last block
                        int blockStringLength = Math.min(8,charactersToRead);
                        // append to existing value
                        strData += file.readSPSSString(blockStringLength);
                        // if this is the last block, skip the remaining dummy byte(s) (in block of 8 bytes)
                        if(charactersToRead<8) {
                            //file.log("SKIP "+file.skipBytes(8-charactersToRead)+"/"+(8-charactersToRead));    
                        }
                        // update counter
                        charactersToRead -= blockStringLength; 
                    }
                }
                blocksToRead--;
            }
            // Post=processing for string variables
            if(var.type==SPSSVariable.VariableType.STRING) {
                // If the variable is a string and all the blocks where blank (254), make it an empty string
                if(strData.trim().length()==0) {
                    strData = "";
                }
                else {
                    // right trim only
                    strData = strData.replaceAll("\\s+$", "");
                }
            }

            // Store in variable 
            if(var.type==SPSSVariable.VariableType.NUMERIC) {
                numVar = (SPSSNumericVariable) var;
                //  numeric: always uses 1 block of 8 bytes
                if(fromDisk) numVar.value = numData;
                else {
                    numVar.data.add(Double.NaN);
                    dataIndex = numVar.data.size()-1;
                    numVar.data.set(dataIndex,numData); 
                }
            }
            else { // STRING
                strVar = (SPSSStringVariable) var;
                if(fromDisk) strVar.value = strData;
                else {
                    strVar.data.add("");
                    dataIndex = strVar.data.size()-1;
                    strVar.data.set(dataIndex,strData); 
                }
                //file.log("chars "+charactersToRead+" blocks "+blocksToRead);
            }
            
            // debug trace
            /*
            if(var.variableRecord.variableTypeCode==0) {
                file.log("Numeric variable "+var.variableRecord.name+" value "+numVar.data.get( numVar.data.size()-1) ) ;
            }
            else {
                file.log("String variable "+var.variableRecord.name+" value ["+strVar.data.get( strVar.data.size()-1) +"]") ;
            }
            */
        } // next variable        
    }
}
