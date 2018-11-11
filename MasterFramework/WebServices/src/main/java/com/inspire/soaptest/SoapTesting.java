package com.inspire.soaptest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.XmlException;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.StandaloneSoapUICore;
import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.WsdlRequest;
import com.eviware.soapui.impl.wsdl.WsdlSubmit;
import com.eviware.soapui.impl.wsdl.WsdlSubmitContext;
import com.eviware.soapui.model.iface.Request.SubmitException;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.model.support.PropertiesMap;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.model.testsuite.TestSuite;
import com.eviware.soapui.support.SoapUIException;

public class SoapTesting {

	public static String runSoapTestSuite(String testSuiteName) {
		String reportStr = "";

		// variables for getting duration
		long startTime = 0;
		long duration = 0;

		TestRunner runner = null;

		List<TestSuite> suiteList = new ArrayList<TestSuite>();
		List<TestCase> caseList = new ArrayList<TestCase>();

		SoapUI.setSoapUICore(new StandaloneSoapUICore(true));

		// specified soapUI project
		WsdlProject project = null;
		try {
			project = new WsdlProject("CurrencyConvertor-soapui-project.xml");
			suiteList = project.getTestSuiteList();
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SoapUIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get a list of all test suites on the project

		// you can use for each loop
		for (int i = 0; i < suiteList.size(); i++) {

			// get name of the "i" element in the list of a test suites
			testSuiteName = suiteList.get(i).getName();
			reportStr = reportStr + "\nTest Suite: " + testSuiteName;

			// get a list of all test cases on the "i"-test suite
			caseList = suiteList.get(i).getTestCaseList();

			for (int k = 0; k < caseList.size(); k++) {

				startTime = System.currentTimeMillis();

				// run "k"-test case in the "i"-test suite
				runner = project.getTestSuiteByName(testSuiteName)
						.getTestCaseByName(caseList.get(k).getName())
						.run(new PropertiesMap(), false);

				duration = System.currentTimeMillis() - startTime;

				reportStr = reportStr + "\n\tTestCase: "
						+ caseList.get(k).getName() + "\tStatus: "
						+ runner.getStatus() + "\tReason: "
						+ runner.getReason() + "\tDuration: " + duration;
			}

		}

		return reportStr;
	}

	public static Response getSoapResponse(String wsdl, String operation,
			String request) {
		WsdlProject project = null;
		Response response = null;
		try {
			project = new WsdlProject();
			WsdlInterface iface = WsdlInterfaceFactory.importWsdl(project,
					wsdl, true)[0];
			WsdlOperation ops = (WsdlOperation) iface
					.getOperationByName(operation);
			WsdlRequest req = ops.addNewRequest(request);
			req.setRequestContent(ops.createRequest(true));
			WsdlSubmit submit = (WsdlSubmit) req.submit(new WsdlSubmitContext(
					null), false);
			response = submit.getResponse();
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SoapUIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SubmitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

}
