package org.opendatafoundation.data.spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.opendatafoundation.data.FileFormatInfo;

import junit.framework.Assert;

public class SpssFileTest
{

  @Test
  public void loadExistingFileTest()
  {
    try {
      SPSSFile spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss/DatabaseTest.sav"));
    } catch(FileNotFoundException e) {
    }
  }

  @Test
  public void loadNonExistingFileTest()
  {
    try {
      SPSSFile spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss"));
      Assert.fail();
    } catch(FileNotFoundException e) {
      Assert.assertTrue(true);
    }
  }

  @Test
  public void testDataLoading() {
    try {
      SPSSFile spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss/DatabaseTest.sav"));
      spssFile.logFlag = false;

      spssFile.loadMetadata();
      spssFile.loadData();


      SPSSVariable variable = spssFile.getVariable(0);
      for (int i = 1; i <= variable.getNumberOfObservation(); i++) {
        System.out.print("SPSS:  " + variable.getValueAsString(i, new FileFormatInfo(FileFormatInfo.Format.SPSS)));
        System.out.print("\tASCII: " + variable.getValueAsString(i, new FileFormatInfo(FileFormatInfo.Format.ASCII)));
        System.out.println("\tSTATA: " + variable.getValueAsString(i, new FileFormatInfo(FileFormatInfo.Format.STATA)));
      }

    } catch(SPSSFileException e) {
      e.printStackTrace();
    } catch(FileNotFoundException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    } catch(IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

}
