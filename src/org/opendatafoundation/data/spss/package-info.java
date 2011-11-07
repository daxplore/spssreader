/**
 * <p>
 * Provides the classes necessary to read SPSS .sav data files to (1) produce DDI 2.0 and DDI 3.0 XML metadata and (2) export the data into ASCII format.
 * </p>
 * 
 * <h2>Usage</h2>
 * 
 * <h4>Initialization</h4>
 * <p>
 * <div>- open an SPSS file by instantiating a new SPSSFile object.</div>
 * <div>- call the SPSSFile.loadMetadata() method to read thed dictionary and other SPSS specific information</div>
 * </p>
 * 
 * <h4>Retrieving DDI metadata</h4>
 * <p>
 * <div>- Use the getDDI2() method to retrieve a DDI 2 compliant XML.</div>
 * <div>- Use the getDDI3LogicalProduct(), getDDI3PjysicalDataProduct() and getDDI3PhysicalInstance() methods to retrieve a DDI 3.0-CR XML.</div>
 * </p>
 * <p>
 * The above methods need at one argument to specify which physical format this DDI should be produce for (SPSS, ASCII).
 * Use the SPSSFile.DataFormat enumeration to choose a format.
 * </p>
 * <p>For DDI 3, element identifiers are generated based in the uniqueID property of the SPSSFile object. 
 * A random value is generated automatically using the java.util.UUID class. This value can be changed by calling the SPSSFile.setUniqueID methos
 * </p>
 * <p style="color:#a00000;">
 * Note that the DDI 3.0 is currently in candidate release status and the specfication is subject to frequent changes. 
 * There is no guarentee that the XMl produced by this package is in compliance with the latest version.
 * </p>
 * 
 * <h4>Exporting Data to a file</h4>
 * <div>- Create a new FileFormatInfo object</div>
 * <div>- Call the Utils.exportFile(...) or Utils.exportFileAsHtml(...) static method.</div>
 * </p>
 * <p>
 * NOTE: 
 * The exported ASCII data is currently always in NATIVE format. 
 * This mainly means that the date and time variable type remain in their original SPSS formatting which may not be compatible with other software.
 * A GENERIC format is under development.
 * </p>
 * 
 * <h2>Know Issues / Todo</h2>
 * <div>- DateTime formats that include hundreds of seconds do not read properly (there is a few hundreds difference)</div>
 * <div>- SPSS files produced with SPSS 4 or earlier on non Intel system or with the open source SPSS Writer package do not export data.</div>
 * <div>- GENERIC ASCII export is not yet implemented.</div>
 * 
 * <h2>Example</h2>
 * <div>Load an SPSS file, export to FIXED ASCII and generate DDI2 / DDI3 metadata</div> 
 * <pre>
 *      SPSSFile spss = new SPSSFile(new File(this.getClass().getResource("testdata/SPSSTest.sav").toString().substring(5)));
 *      FileFormatInfo format = new FileFormatInfo();
 *      // FIXED ASCII
 *      spss.exportData(new File("c:/temp/test.fixed.dat"), format);
 *      // DDI 2 XML
 *      Utils.writeXmlFile(spss.getDDI2(),"c:/temp/test.ddi2.xml");
 *      // DDI 3 Logical Product
 *      Utils.writeXmlFile(spss.getDDI3LogicalProduct(),"c:/temp/test.ddi3.lp.xml");
 *      // DDI 3 Physical Data Product
 *      Utils.writeXmlFile(spss.getDDI3PhysicalDataProduct(format),"c:/temp/test.ddi3.pdp.xml");
 *      // DDI 3 Physical Instance
 *      Utils.writeXmlFile(spss.getDDI3PhysicalInstance(format),"c:/temp/test.ddi3.pi.xml");
 * </pre>
 * 
 *
 * <h2>Contact/Feedback</h2>
 * <p>Contact pheus@opendatafoundation for comments, feedback or questions on this package.
 * </p>
 * 
 * <h2>Acknowledgements</h2>
 * <p>
 * This product has been developed with the financial and  
 * technical support of the UK Data Archive Data Exchange Tools 
 * project (http://www.data-archive.ac.uk/dext/) and the 
 * Open Data Foundation (http://www.opendatafoundation.org) 
 * </p>
 * 
 * <h2>License</h2>
 * <p>
 * Copyright 2007-2008 University of Essex (http://www.esds.ac.uk) 
 * </p>
 * <p>
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 * </p>
 * <p>
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, 
 * Boston, MA  02110-1301  USA
 * The full text of the license is also available on the Internet at 
 * http://www.gnu.org/copyleft/lesser.html
 * </p>
 */
package org.opendatafoundation.data.spss;
