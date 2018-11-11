package com.inspire.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;


/**
 * @author sachi
 *
 */
public class NetworkUtils {
	static Logger log = MasterLogger.getInstance();

	public static boolean verifyMachineReachable(String ip) {
		if (ip != null) {
			try {
				return InetAddress.getByName(ip).isReachable(20000);
			} catch (UnknownHostException e) {
				log.info(ip + " not reachable throws Unknown host exception");
				log.info(e.getMessage());
			} catch (IOException e) {
				log.info(ip + " not reachable throws Unknown host exception");
				log.info(e.getMessage());
			}
		}
		return false;
	}

	public static String getIpByDomainName(String domainName) {
		if (domainName != null) {
			try {
				InetAddress host = InetAddress.getByName(domainName);
				return host.getHostAddress();
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			}

		}
		return null;

	}

}
