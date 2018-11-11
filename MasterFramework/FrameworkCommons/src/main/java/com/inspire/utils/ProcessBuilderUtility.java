package com.inspire.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.log4j.Logger;

import com.inspire.abstestbase.AbstractTestBase;
import com.inspire.abstestbase.MasterLogger;

/**
 * @author sachi
 *
 */
public class ProcessBuilderUtility extends AbstractTestBase {
	protected ProcessBuilder pbuilder = new ProcessBuilder();
	public static Logger log = MasterLogger.getInstance();

	public static int executeComand(String cmd) {
		log.info("executing command " + cmd);
		CommandLine cmdLine = CommandLine.parse(cmd);
		DefaultExecutor executor = new DefaultExecutor();
		int exitValue = -1;
		try {
			exitValue = executor.execute(cmdLine);
		} catch (ExecuteException e1) {
			log.info(e1.getMessage());
		} catch (IOException e1) {
			log.info(e1.getMessage());
		}
		return exitValue;
	}

	public static Process executeCommandTimeout5Minutes(String cmd) {
		Process p = null;
		if (System.getProperty("os.name").startsWith("Windows")) {
			cmd = "cmd /c " + cmd;
		}

		try {
			log.info("running cmd " + cmd);
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor(360, TimeUnit.SECONDS);
		} catch (IOException | InterruptedException e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return p;
	}

	public static Process executeComandNoWait(String cmd) {
		Process p = null;
		if (System.getProperty("os.name").startsWith("Windows")) {
			cmd = "cmd /c " + cmd;
		}

		try {
			p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			log.info(e.getMessage());
			e.printStackTrace();
		}
		return p;
	}

	public static Process executeComand(String[] cmd) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			log.info(e.getMessage());
		}
		return p;
	}

	public static String executeCommandNGetResults(String cmd) {
		Process p = null;
		String stdout = "";
		String stderr = "";
		String s = null;
		if (System.getProperty("os.name").startsWith("Windows")) {
			cmd = "cmd /c " + cmd;
		}

		try {
			log.info("executing " + cmd);
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor(360, TimeUnit.SECONDS);
			if (p.exitValue()==0 || p.exitValue()==1) {
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while ((s = stdInput.readLine()) != null) {
					log.info(s);
					stdout = stdout + s;
				}
				while ((s = stdError.readLine()) != null) {
					log.info(s);
					stderr = stderr + s;
				}
			}else {
				log.info("exit code is not zero "+p.exitValue());
				return null;
			}
			
		} catch (IOException | InterruptedException e) {
			log.info(e.getMessage());
		}
		log.info(stdout);
		return stdout;
	}

	public int executeProcess(String cmd) {
		pbuilder.command(cmd);
		Process process = null;
		try {
			process = pbuilder.start();
			int errcode = process.waitFor();
		} catch (InterruptedException | IOException e) {
			log.info("error while executing the process " + e.getMessage());
		}
		return process.exitValue();
	}

}
