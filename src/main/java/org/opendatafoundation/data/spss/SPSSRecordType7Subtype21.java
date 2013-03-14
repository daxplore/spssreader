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
import java.util.ArrayList;
import java.util.Collection;

public class SPSSRecordType7Subtype21 extends SPSSAbstractRecordType {

  private static final int RECORD_TYPE = 7;

  private static final int SUB_RECORD_TYPE = 21;

  private static final int DATA_ELEMENT_LENGTH = 1;

  private int recordTypeCode;

  private int recordSubtypeCode;

  private int dataElementLength;

  private int numberOfDataElements;

  public final Collection<Variable> variables = new ArrayList<Variable>();

  @Override
  public void read(SPSSFile is) throws IOException, SPSSFileException {
    fileLocation = is.getFilePointer();
    recordTypeCode = is.readSPSSInt();

    if(recordTypeCode != RECORD_TYPE) {
      throw new SPSSFileException(
          "Error reading record type  " + RECORD_TYPE + " subtype " + SUB_RECORD_TYPE + ": bad record type [" + recordTypeCode + "]. Expecting Record Type 7.");
    }

    // subtype
    recordSubtypeCode = is.readSPSSInt();
    if(recordSubtypeCode != SUB_RECORD_TYPE) {
      throw new SPSSFileException("Error reading record type" + RECORD_TYPE + "subtype " + SUB_RECORD_TYPE + ": bad sub-record type [" + recordSubtypeCode +
          "]. Expecting Record Subtype 21.");
    }

    // data elements
    dataElementLength = is.readSPSSInt();
    if(dataElementLength != DATA_ELEMENT_LENGTH) throw new SPSSFileException(
        "Error reading record type" + RECORD_TYPE + "subtype " + SUB_RECORD_TYPE + ": bad data element length [" + dataElementLength + "]. Expecting 1.");

    numberOfDataElements = is.readSPSSInt();
    long filePointer = is.getFilePointer();
    long bytesRead = 0;

    while(bytesRead < numberOfDataElements) {
      Variable variable = new Variable();
      variable.read(is);
      variables.add(variable);

      bytesRead = is.getFilePointer() - filePointer;
    }
  }

  public Collection<Variable> getVariables() {
    return variables;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder() //
        .append("\nRECORD TYPE " + RECORD_TYPE + " SUBTYPE " + SUB_RECORD_TYPE + " - LONG VARIABLE LABELS") //
        .append("\nLocation              : ").append(fileLocation) //
        .append("\nRecord Type           : ").append(recordTypeCode) //
        .append("\nRecord Subtype        : ").append(recordSubtypeCode) //
        .append("\nData elements         : ").append(numberOfDataElements) //
        .append("\nElement length        : ").append(dataElementLength);

    for(Variable variable : variables) {
      str.append(variable.toString());
    }

    return str.toString();
  }

  //
  // Inner classes
  //

  static public class Variable {
    private int variableNameLength;

    private String name;

    private int nameWidth;

    private int numberOfLabels;

    private final Collection<Label> labels = new ArrayList<Label>();

    public void read(SPSSFile is) throws IOException {
      variableNameLength = is.readSPSSInt();
      name = is.readSPSSString(variableNameLength);
      nameWidth = is.readSPSSInt();
      numberOfLabels = is.readSPSSInt();

      for(int i = 0; i < numberOfLabels; i++) {
        Label longStringLabel = new Label();
        longStringLabel.read(is);
        labels.add(longStringLabel);
      }
    }

    public Collection<Label> getLabels() {
      return labels;
    }

    public String getName() {
      return name;
    }

    public String toString() {
      StringBuilder str = new StringBuilder() //
        .append("\nVariable")
        .append("\n- Variable name length  : ").append(variableNameLength) //
        .append("\n- Variable name         : ").append(name) //
        .append("\n- Variable width        : ").append(nameWidth) //
        .append("\n- Number of labels      : ").append(numberOfLabels);

      for(Label label : labels) {
        str.append(label.toString());
      }
      return str.toString();
    }

  }

  static public class Label {
    private int valueLength;

    private String value;

    private int labelLength;

    private String label;

    public void read(SPSSFile is) throws IOException {
      valueLength = is.readSPSSInt();
      value = is.readSPSSString(valueLength);
      labelLength = is.readSPSSInt();
      label = is.readSPSSString(labelLength);
    }

    public String getLabel() {
      return label;
    }

    public String getValue() {
      return value;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public String toString() {
      return new StringBuilder() //
        .append("\nLabels") //
        .append("\n- Label length  : ").append(labelLength) //
        .append("\n- Label         : ").append(label) //
        .append("\n- Value length  : ").append(valueLength) //
        .append("\n- Value         : ").append(value).toString();
    }
  }
}
