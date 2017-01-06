/*
 * Copyright (c) 2012 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opendatafoundation.data.spss;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * SPSS Record Type 7 Subtype 14 - Very Long String Value
 */
public class SPSSRecordType7Subtype14 extends SPSSAbstractRecordType {

  private static final int RECORD_TYPE = 7;

  private static final int SUB_RECORD_TYPE = 14;

  private int recordTypeCode;

  private int recordSubtypeCode;

  private int size;

  private int count;

  private Map<String, Integer> stringLengths = new LinkedHashMap<String, Integer>();

  @Override
  public void read(SPSSFile is) throws IOException, SPSSFileException {
    fileLocation = is.getFilePointer();
    recordTypeCode = is.readSPSSInt();

    if(recordTypeCode != RECORD_TYPE) {
      throw new SPSSFileException(
          "Error reading record type  " + RECORD_TYPE + " subtype " + SUB_RECORD_TYPE + ": bad record type [" +
              recordTypeCode + "]. Expecting Record Type 7.");
    }

    // subtype
    recordSubtypeCode = is.readSPSSInt();
    if(recordSubtypeCode != SUB_RECORD_TYPE) {
      throw new SPSSFileException(
          "Error reading record type" + RECORD_TYPE + "subtype " + SUB_RECORD_TYPE + ": bad sub-record type [" +
              recordSubtypeCode +
              "]. Expecting Record Subtype 21.");
    }

    size = is.readSPSSInt();
    count = is.readSPSSInt();
    String rawMap = is.readSPSSString(count);

    StringTokenizer st1 = new StringTokenizer(rawMap, "\t");
    while(st1.hasMoreTokens()) {
      StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "=");
      if(st2.countTokens() >= 2) {
        stringLengths.put(st2.nextToken(), Integer.parseInt(st2.nextToken().replaceAll("\\u0000", "")));
      }
    }

  }

  public Set<Map.Entry<String, Integer>> entries() {
    return stringLengths.entrySet();
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder() //
        .append("\nRECORD TYPE " + RECORD_TYPE + " SUBTYPE " + SUB_RECORD_TYPE + " - LONG VARIABLE LABELS") //
        .append("\nLocation              : ").append(fileLocation) //
        .append("\nRecord Type           : ").append(recordTypeCode) //
        .append("\nRecord Subtype        : ").append(recordSubtypeCode) //
        .append("\nSize                  : ").append(size) //
        .append("\nCount                 : ").append(count) //
        .append("\nString lengths:       ");

    for(Map.Entry<String, Integer> entry : stringLengths.entrySet()) {
      str.append("\t").append(entry);
    }

    return str.toString();
  }
}
