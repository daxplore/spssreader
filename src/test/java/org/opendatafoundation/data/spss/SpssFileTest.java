package org.opendatafoundation.data.spss;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.opendatafoundation.data.FileFormatInfo;

import junit.framework.Assert;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SpssFileTest {

  @Test
  public void loadExistingFileTest() {
    try {
      new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss/DatabaseTest.sav"));
    } catch(FileNotFoundException e) {
    }
  }

  @Test(expected = FileNotFoundException.class)
  public void loadNonExistingFileTest() throws FileNotFoundException {
    new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss"));
  }

  @Test
  public void testDataLoading() {
    try {
      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/DatabaseTest.sav"));
      spssFile.logFlag = false;
      spssFile.loadMetadata();
      spssFile.loadData();

      SPSSVariable variable = spssFile.getVariable(0);
      assertEquals(variable.getNumberOfObservations(), 200);
    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testIntervalMissingValues() {
    try {
      SPSSFile spssFile = new SPSSFile(
        new File("src/test/resources/org/opendatafoundation/data/spss/InervalMissing.sav"));
      spssFile.logFlag = false;
      spssFile.loadMetadata();
      SPSSVariable variable0 = spssFile.getVariable(0);
      assertEquals(variable0.categoryMap.size(), 6);
      assertEquals(variable0.categoryMap.get("-2").isMissing, true);
      assertEquals(variable0.categoryMap.get("-1").isMissing, true);
      assertEquals(variable0.categoryMap.get("1").isMissing, false);
      assertEquals(variable0.categoryMap.get("2").isMissing, false);
      assertEquals(variable0.categoryMap.get("3").isMissing, false);
      assertEquals(variable0.categoryMap.get("9").isMissing, false);

      SPSSVariable variable1 = spssFile.getVariable(1);
      assertEquals(variable1.categoryMap.size(), 4);
      variable1.categoryMap.forEach((k, cat) -> assertEquals(cat.isMissing, true));

      SPSSVariable variable2 = spssFile.getVariable(2);
      assertEquals(variable2.categoryMap.size(), 0);

    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testReadingStringVariableLabels() {
    try {
      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/StringCategories.sav"));
      spssFile.loadMetadata();
      SPSSVariable variable = spssFile.getVariable(1);
      assertEquals(variable.getName(), "var1");
      assertEquals(variable.categoryMap.size(), 4);
      SPSSVariableCategory category = variable.getCategory("a".getBytes());
      assertEquals(category.strValue, "a");
      assertEquals(category.label, "Label A");
      SPSSVariable variable1 = spssFile.getVariable(2);
      assertEquals(variable1.getName(), "var2");
      assertEquals(variable1.categoryMap.size(), 2);
      SPSSVariableCategory category1 = variable1.getCategory("string string1".getBytes());
      assertEquals(category1.strValue, "string string1");
      assertEquals(category1.label, "This is a long string 1");

    } catch(Exception e) {
      Assert.fail();
    }

  }

  @Test
  public void testReadingLongStringVariableCount() {
    try {
      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
      assertThat(spssFile.getVariableCount(), is(11));
    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testReadingShortStringVariableExactValue() {
    try {
      final String exactValue
          = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis,";

      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
      SPSSVariable variable = spssFile.getVariable(1);
      SPSSVariable.VariableType type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.STRING));
      assertThat(variable.getShortName(), is("SVAR001"));
      SPSSStringVariable stringVariable = (SPSSStringVariable) variable;
      String variableValue = stringVariable.getValueAsString(1, new FileFormatInfo(FileFormatInfo.Format.ASCII));
      assertThat(variableValue, is(exactValue));

    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testReadingShortStringVariableLargerValue() {
    try {
      final String exactValue
          = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis,";
      final String largerValue
          = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.";

      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
      SPSSVariable variable = spssFile.getVariable(2);
      SPSSVariable.VariableType type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.STRING));
      assertThat(variable.getName(), is("STheVariable002"));
      SPSSStringVariable stringVariable = (SPSSStringVariable) variable;
      String variableValue = stringVariable.getValueAsString(1, new FileFormatInfo(FileFormatInfo.Format.ASCII));
      assertThat(variableValue, not(is(largerValue)));
      assertThat(variableValue, is(exactValue));

    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testReadingShortStringVariableSmallerValue() {
    try {
      final String smallerValue
          = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean m";

      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
      SPSSVariable variable = spssFile.getVariable(3);
      SPSSVariable.VariableType type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.STRING));
      assertThat(variable.getName(), is("SVar003"));
      SPSSStringVariable stringVariable = (SPSSStringVariable) variable;
      String variableValue = stringVariable.data.get(0); // getValueAsString() pads value based on ASCII format
      assertThat(variableValue, is(smallerValue));
    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testReadingLongStringVariableExactValue() {
    try {
      final String exactValue
          = "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure? On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free";

      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
      SPSSVariable variable = spssFile.getVariable(6);
      SPSSVariable.VariableType type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.STRING));
      assertThat(variable.getName(), is("STheVariable006"));
      SPSSStringVariable stringVariable = (SPSSStringVariable) variable;
      String variableValue = stringVariable.getValueAsString(1, new FileFormatInfo(FileFormatInfo.Format.ASCII));
      assertThat(variableValue, is(exactValue));
    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testReadingLongStringVariableLargerValue() {
    try {
      final String exactValue
          = "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure? On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free";
      final String largerValue
          = "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness. No one rejects, dislikes, or avoids pleasure itself, because it is pleasure, but because those who do not know how to pursue pleasure rationally encounter consequences that are extremely painful. Nor again is there anyone who loves or pursues or desires to obtain pain of itself, because it is pain, but because occasionally circumstances occur in which toil and pain can procure him some great pleasure. To take a trivial example, which of us ever undertakes laborious physical exercise, except to obtain some advantage from it? But who has any right to find fault with a man who chooses to enjoy a pleasure that has no annoying consequences, or one who avoids a pain that produces no resultant pleasure? On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammelled and when nothing preve";

      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
      SPSSVariable variable = spssFile.getVariable(7);
      SPSSVariable.VariableType type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.STRING));
      assertThat(variable.getName(), is("SVar007"));
      SPSSStringVariable stringVariable = (SPSSStringVariable) variable;
      String variableValue = stringVariable.getValueAsString(1, new FileFormatInfo(FileFormatInfo.Format.ASCII));
      assertThat(variableValue, is(not(largerValue)));
      assertThat(variableValue, is(exactValue));
    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testReadingLongStringVariableSmallerValue() {
    try {
      final String smallerValue
          = "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the s";

      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
      SPSSVariable variable = spssFile.getVariable(9);
      SPSSVariable.VariableType type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.STRING));
      assertThat(variable.getName(), is("STheVariable009"));
      SPSSStringVariable stringVariable = (SPSSStringVariable) variable;
      String variableValue = stringVariable.data.get(0);
      assertThat(variableValue, is(smallerValue));
    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testReadingLongStringVariableOtherTypesInBetween() {
    try {
      final String smallerValue
          = "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the s";

      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();

      SPSSVariable variable = spssFile.getVariable(4);
      SPSSVariable.VariableType type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.NUMERIC));
      assertThat(variable.getName(), is("NVar004"));
      SPSSNumericVariable numericVariable = (SPSSNumericVariable) variable;
      assertThat(numericVariable.data.get(0).doubleValue(), is(12.12));

      // TODO test date later
      variable = spssFile.getVariable(5);
      type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.NUMERIC));
      assertThat(variable.getName(), is("DVar005"));
      numericVariable = (SPSSNumericVariable) variable;
      assertThat(numericVariable.getValueAsString(1, new FileFormatInfo(FileFormatInfo.Format.ASCII)),
          is("10/10/2013"));

      variable = spssFile.getVariable(8);
      type = variable.type;
      assertThat(type, is(SPSSVariable.VariableType.NUMERIC));
      assertThat(variable.getName(), is("IVar008"));
      numericVariable = (SPSSNumericVariable) variable;
      assertThat(numericVariable.data.get(0).intValue(), is(1000));

    } catch(Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void testVariableLengths() {
    try {
      final int[] expectedLengths = { 6, 255, 255, 255, 8, 10, 1430, 1430, 6, 1430, 30 };

      SPSSFile spssFile = new SPSSFile(
          new File("src/test/resources/org/opendatafoundation/data/spss/VeryLongStringVariables.sav"));
      spssFile.loadMetadata();
      for(int i = 0; i < spssFile.getVariableCount(); i++) {
        SPSSVariable variable = spssFile.getVariable(i);
        assertThat(variable.getLength(), is(expectedLengths[i]));
      }
    } catch(Exception e) {
      Assert.fail();
    }

  }

  @Test
  public void testSYSMISS() {
    SPSSFile spssFile = null;
    try {
      spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss/OV103V2OVERALLv5b.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
    } catch(Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  @Test
  public void testNumericValueFormat() {
    SPSSFile spssFile = null;
    try {
      spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss/TestNumber.sav"));
      spssFile.loadMetadata();
      spssFile.loadData();
      SPSSVariable variable = spssFile.getVariable(1);
      assertEquals(variable.getName(), "HEIGHT");
      assertThat(variable.getValueAsString(1, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is("137.34"));
      assertThat(variable.getValueAsString(2, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is("9999.99"));
      assertThat(variable.getValueAsString(3, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is("99.99"));
      assertThat(variable.getValueAsString(4, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is(""));
      assertThat(variable.getValueAsString(5, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is("12.10"));
      assertThat(variable.getValueAsString(6, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is("12.47"));
      assertThat(variable.getValueAsString(7, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is("123456789.34"));

      SPSSVariable variable1 = spssFile.getVariable(2);
      assertEquals(variable1.getName(), "WEIGHT");
      assertThat(variable1.getValueAsString(1, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is("1,232.00"));
      assertThat(variable1.getValueAsString(2, new FileFormatInfo(FileFormatInfo.Format.ASCII)), is("12.00"));
    } catch(Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

}
