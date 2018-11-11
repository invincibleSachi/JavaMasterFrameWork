package com.inspire.utils;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.inspire.abstestbase.MasterLogger;


/**
 * This method allows you to send and execute *nix command through SSH to
 * specified removed host and returns command output, in case incorrect command
 * will return command output error
 */

/**
 * @author sachi
 *
 */
public class JshUtility {
	private String userName;
	private String password;
	private String host;
	private int port;
	JSch jsch;
	Session session;
	public static Logger log = MasterLogger.getInstance();

	public JshUtility(String userName, String password, String host, int port) {
		this.userName = userName;
		this.password = password;
		this.port = port;
		this.host = host;
		if (port == 0) {
			this.port = 22;
		} else {
			this.port = port;
		}

		connect();
	}

	private void connect() {
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(userName, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			log.info("Establishing Connection...");
			session.connect();
			log.info("Establed Connection...");
		} catch (JSchException e) {
			e.printStackTrace();
		}

	}

	public String execCommand(String command) {

		int port = 22;
		String rez = "+!";

		try {
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command); // setting command

			channel.setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					// System.out.print(new String(tmp, 0, i));
					rez = new String(tmp, 0, i);
				}
				if (channel.isClosed()) {
					log.info("exit-status: " + channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					rez = e.toString();
				}
			}
			channel.disconnect();
			session.disconnect();
		}

		catch (Exception e) {
			rez = e.toString();
		}
		return rez;
	}
}
