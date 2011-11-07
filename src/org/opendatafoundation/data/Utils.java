package org.opendatafoundation.data;

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

import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Collection of utility functions
 * 
 * @author Pascal Heus (pheus@opendatafoundation.org)
 *
 */
public class Utils {

	
    /**
     * Converts an w3c.dom.Node into a String
     * @param node
     * @return The DOM as a String
     * @throws TransformerFactoryConfigurationError 
     * @throws TransformerException 
     * @throws TransformerConfigurationException 
     */
    public static String DOM2String(Node node) throws TransformerException, TransformerFactoryConfigurationError {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        StringWriter writer = new StringWriter();
        transFactory.newTransformer().transform(new DOMSource(node),new StreamResult(writer));
        String result = writer.getBuffer().toString();
        return(result);
    }

    /**
     * Converts an w3c.dom.Node into a color syntax HTML
     * @param node
     * @return The HTML as a String
     * @throws TransformerException 
     */
    public static String DOM2HTML(Node node) throws TransformerException {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        StringWriter writer = new StringWriter();
        Source xsltSource = new StreamSource(Utils.class.getResourceAsStream("xml2html.xslt")); // use this.getClass() instead for non static methods
        Transformer transformer = transFactory.newTransformer(xsltSource);
        StreamResult result = new StreamResult(System.out);
        transformer.transform(new DOMSource(node), result);
        return(writer.getBuffer().toString());
    }
    
    /**
     * Pads a string left with spaces
     * @param str
     * @return the padded string
     */
    public static String leftPad(String str, int length) {
        return(leftPad(str,length,' '));
    }

    /**
     * Pads a string left with specified character 
     * @param str
     * @return the padded string
     */
    public static String leftPad(String str, int length, char ch) {
        int padding = length-str.length();
        if(padding>0) {
            char [] buf = new char[padding];
            for (int i = 0; i < padding; i++) buf[i] = ch;
            return(new String(buf) + str);
        }
        else return(str);
    }

    /**
     * Writes and org.w3c.dom.Document to a file
     * 
     * @param doc
     * @param filename
     * @throws TransformerFactoryConfigurationError 
     * @throws TransformerException 
     */
    public static void writeXmlFile(Document doc, String filename) throws TransformerFactoryConfigurationError, TransformerException {
        // Prepare the DOM document for writing
        Source source = new DOMSource(doc);

        // Prepare the output file
        File file = new File(filename);
        Result result = new StreamResult(file);

        // Write the DOM document to the file
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);
    }

    /**
     * Writes and org.w3c.dom.Document to a file in a colored syntax HTML file
     * 
     * @param doc
     * @param filename
     * @throws TransformerFactoryConfigurationError 
     * @throws TransformerException 
     */
    public static void writeXmlFileAsHtml(Document doc, String filename) throws TransformerFactoryConfigurationError, TransformerException {
        // Prepare the DOM document for writing
        Source source = new DOMSource(doc);

        // Prepare the output file
        File file = new File(filename);
        Result result = new StreamResult(file);

        // Get transform 
        Source xsltSource = new StreamSource(Utils.class.getResourceAsStream("xml2html.xslt")); // use this.getClass() instead for non static methods

        // Write the DOM document to the file
        Transformer xformer = TransformerFactory.newInstance().newTransformer(xsltSource);
        xformer.transform(source, result);
    }
    
    /**
     * Adds id related attributes to DDI Identifiable element
     * @param e
     * @param id
     */
    public static void setDDIIdentifiableId(Element e, String id) {
    	e.setAttribute("id", id);
    	e.setAttribute("isIdentifiable", "true");
    }

    /**
     * Adds id related attributes to DDI Maintainable element
     * @param e
     * @param id
     */
    public static void setDDIMaintainableId(Element e, String id) {
    	e.setAttribute("id", id);
    	e.setAttribute("isMaintainable", "true");
    }

    /**
     * Adds id related attributes to DDI Versionable element
     * @param e
     * @param id
     */
    public static void setDDIVersionableId(Element e, String id) {
    	e.setAttribute("id", id);
    	e.setAttribute("isVersionable", "true");
    }
}
