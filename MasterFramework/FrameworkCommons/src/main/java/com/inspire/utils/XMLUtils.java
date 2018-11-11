package com.inspire.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class XMLUtils {
	private String fileLocation;
	private String fileName;
	public static Logger log = MasterLogger.getInstance();

	public XMLUtils(String fileLocation, String fileName) {
		this.fileLocation = fileLocation;
		this.fileName = fileName;
	}

	public XMLUtils(String fileName) {
		this.fileLocation = "/";
		this.fileName = fileName;
	}

	public static NodeList getXLMNode(String tagName, String fileName) {
		NodeList nList = null;
		try {
			File inputFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			log.info("Root element :" + doc.getDocumentElement().getNodeName());
			nList = doc.getElementsByTagName(tagName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nList;
	}

	public static Node getXLMNode(String fileName, String tagName, String attribuateName, String attribuateValue) {
		NodeList nList = null;
		Node nNode = null;

		File inputFile = new File(fileName);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);

			doc.getDocumentElement().normalize();
			log.info("Root element :" + doc.getDocumentElement().getNodeName());
			nList = doc.getElementsByTagName(tagName);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		log.info("before loop");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			nNode = nList.item(temp);
			String key = nNode.getAttributes().getNamedItem("id").getNodeValue();
			log.info(key);
		}

		return null;
	}

	public static Node getNodefromNodeList(String fileName, NodeList nList, int index) {
		Node nNode = nList.item(index);
		return nNode;
	}

	public String getAttributeWithXpath(String sourceXpath) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		InputSource inputSource = new InputSource(fileName);
		String attributeValue = null;
		try {
			attributeValue = xpath.evaluate(sourceXpath, inputSource);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return attributeValue;
	}

}
