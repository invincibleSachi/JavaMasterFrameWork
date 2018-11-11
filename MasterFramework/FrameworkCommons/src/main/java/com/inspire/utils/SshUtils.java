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
 * @author sachi
 *
 */
public class SshUtils {
	public static Logger log = MasterLogger.getInstance();

	public static int runcommand(String host, String user, String privateKey, String command) {
		log.info(host);
		log.info(user);
		log.info(command);
		JSch jsch = new JSch();
		String rez = "+!";
		int port = 22;
		int exitstatus = -1;
		try {
			jsch.addIdentity(privateKey);
			log.info("ssh identity added ");

			Session session = jsch.getSession(user, host, port);
			log.info("ssh session created.");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("HostKeyAlgorithms", "ssh-rsa");
			session.setConfig(config);
			session.connect();
			log.info("session connected.....");
			log.info("shell channel connected....");

			try {
				Channel channel = session.openChannel("exec");
				log.info("running command " + command);
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
						rez = new String(tmp, 0, i);
					}
					if (channel.isClosed()) {
						log.info("exit-status: " + channel.getExitStatus());
						exitstatus = channel.getExitStatus();
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

		} catch (JSchException e) {
			log.info("JSCH Exception " + e.getMessage());
		}
		log.info(rez);
		return exitstatus;

	}
}
