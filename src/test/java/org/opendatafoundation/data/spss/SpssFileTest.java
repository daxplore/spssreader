package org.opendatafoundation.data.spss;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import junit.framework.Assert;

import static org.junit.Assert.assertEquals;

public class SpssFileTest
{

  @Test
  public void loadExistingFileTest()
  {
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
      SPSSFile spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss/DatabaseTest.sav"));
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
  public void testReadingStringVariableLabels() {
    try {
      SPSSFile spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss/StringCategories.sav"));
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


}
