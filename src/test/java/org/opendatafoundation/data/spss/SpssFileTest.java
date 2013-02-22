package org.opendatafoundation.data.spss;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import junit.framework.Assert;

public class SpssFileTest
{

  @Test
  public void loadExistingFileTest()
  {
    try {
      SPSSFile spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss/TestDatabase.sav"));
    } catch(FileNotFoundException e) {
    }
  }

  public void loadNonExistingFileTest()
  {
    try {
      SPSSFile spssFile = new SPSSFile(new File("src/test/resources/org/opendatafoundation/data/spss"));
      Assert.fail();
    } catch(FileNotFoundException e) {
      Assert.assertTrue(true);
    }
  }

}
