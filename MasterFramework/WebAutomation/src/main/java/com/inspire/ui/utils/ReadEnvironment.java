package com.inspire.ui.utils;

import java.util.HashMap;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.inspire.utils.XMLUtils;

/**
 * @author sachi
 *
 */
public class ReadEnvironment {

	public static HashMap<String, String> environmentVariables = new HashMap<String, String>();

	private static void updateEnvMap(String env) {

		Node node = XMLUtils.getXLMNode("Environment.xml", "environment", "id", env);

		if (node != null) {
			NodeList childNode = node.getChildNodes();
			for (int temp = 0; temp < childNode.getLength(); temp++) {

				Node n = childNode.item(temp);

				updateMap(n);
			}
		} else {
			System.out.println("No such environment found");
		}

	}

	public static HashMap<String, String> getEnvironment(String env) {
		updateEnvMap(env);
		return environmentVariables;
	}

	public static void updateMap(Node n) {
		String key = "";
		String value = "";

		if (n != null) {

			if (n.getNodeName() == "parameter") {
				NodeList parameters = n.getChildNodes();
				for (int p = 0; p < parameters.getLength(); p++) {
					if (parameters.item(p).getNodeName().toLowerCase() == "name") {
						key = parameters.item(p).getTextContent();
					}

					if (parameters.item(p).getNodeName().toLowerCase() == "value") {
						value = parameters.item(p).getTextContent();
					}

					environmentVariables.put(key, value);
				}

			} else {
				NodeList childNode = n.getChildNodes();
				// System.out.println("Inside child nodes : "
				// +n.getTextContent() );
				for (int temp = 0; temp < childNode.getLength(); temp++) {

					Node n1 = childNode.item(temp);
					updateMap(n1);
				}
			}
		}
	}

}
