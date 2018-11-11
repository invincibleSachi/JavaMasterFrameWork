package com.inspire.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.apache.log4j.Logger;

import com.inspire.abstestbase.MasterLogger;


/**
 * @author sachi
 *
 */
public class EmailUtility {
	Logger log = MasterLogger.getInstance();
	private String host, port;
	private String verificationCode;
	Properties properties;
	final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	public EmailUtility(String host, String port) {
		this.host = host;
		this.port = port;
		// Get system properties
		properties = System.getProperties();
		/*
		 * Host and port configurations For Gmail host="smtp.gmail.com"
		 * port="465"
		 *
		 * For Yahoo Mail host="smtp.mail.yahoo.com" port="587"
		 */
		// Gmail setup
		/*
		 * properties.setProperty("mail.store.protocol", "imaps");
		 * properties.put("mail.smtp.host", host);
		 * properties.put("mail.smtp.port", port);
		 *
		 * properties.put("mail.smtp.socketFactory.port", port);
		 * properties.put("mail.smtp.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory"); properties.put("mail.smtp.auth",
		 * "true"); properties.setProperty("mail.smtp.ssl.trust", "smtpserver");
		 */

		// office 365
		// host=imap-mail.outlook.com
		// port=993

		properties.setProperty("mail.imaps.socketFactory.class", SSL_FACTORY);
		properties.setProperty("mail.imaps.socketFactory.fallback", "false");
		properties.setProperty("mail.store.protocol", "imaps");
		properties.setProperty("mail.imaps.port", port);
		properties.setProperty("mail.imaps.socketFactory.port", port);
		properties.put("mail.imaps.host", host);
	}

	public void SendEmail(final String username, final String password, String to, String SubjectLine,
			String textBody) {

		// Get the default Session object.
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(username));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject(SubjectLine);

			// Now set the actual message
			message.setText(textBody);

			// Send message
			Transport.send(message);

			log.info("Sent message successfully....");
			log.info("Email Sent From: " + username);
			log.info("Email Sent to: " + to);
		} catch (MessagingException mex) {
			log.info("Email not sent "+mex.getMessage());
		}
	}

	public Message[] ReadEmail(String username, String password, String folder, boolean setMsgFlagSeen) {
		Message[] messages = null;
		Message msg = null;

		try {
			Session session = Session.getDefaultInstance(properties, null);
			Store store = session.getStore();
			store.connect(host, username, password);
			System.out.println(store);

			Folder inbox = store.getFolder(folder);
			inbox.open(Folder.READ_WRITE);
			FlagTerm term = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			messages = inbox.search(term);
			if (setMsgFlagSeen == true) {
				for (int i = 0; i < messages.length; i++) {
					msg = messages[i];
					msg.setFlag(Flags.Flag.SEEN, true);
					System.out.println("---------------------------------");
					System.out.println("Email Number " + (i));
					System.out.println("Subject: " + msg.getSubject());
					System.out.println("From: " + msg.getFrom()[0]);
				}

			}

			log.info("Email Read is successful");

		} catch (NoSuchProviderException e) {
			log.info("Email provider not found");
		} catch (MessagingException e) {
			log.info("Message exception occurred");
		}

		return messages;
	}

	public Message[] ReadEmailfromSenderAndafter(String username, String password, String folder, String sendermailId,
			int sentAfter, String subject) {
		Message[] messages = null;
		// create properties field

		Calendar cal = Calendar.getInstance();
		sentAfter = 0 - sentAfter;
		cal.add(Calendar.MINUTE, sentAfter);
		Date dte = cal.getTime();

		try {
			Session session = Session.getDefaultInstance(properties, null);
			Store store = session.getStore("imaps");
			store.connect(host, username, password);
			log.info("Connection to email account successful");

			Folder EmailFolder = store.getFolder(folder);
			EmailFolder.open(Folder.READ_WRITE);

			SearchTerm sender = new FromTerm(new InternetAddress(sendermailId));
			FlagTerm UnSeenterm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			log.info("date send after " + dte);
			ReceivedDateTerm beforeNowterm = new ReceivedDateTerm(ComparisonTerm.GE, dte);

			SearchTerm searchTerms = new AndTerm(beforeNowterm, UnSeenterm);
			SearchTerm fromAndUnSeemTerm = new AndTerm(searchTerms, sender);
			SearchTerm subjectTerm = new SubjectTerm(subject);
			SearchTerm fromAndUnSeemAndSubjectTerm = new AndTerm(fromAndUnSeemTerm, subjectTerm);
			messages = EmailFolder.search(fromAndUnSeemAndSubjectTerm);
			log.info("number of filtered Emails" + messages.length);
			log.info("Email Read is successful");

		} catch (NoSuchProviderException e) {
			log.info("Email provider not found");
			System.exit(1);
			e.printStackTrace();
		} catch (MessagingException e) {
			log.info("Message exception occurred");
			e.printStackTrace();
			System.exit(2);
		}

		return messages;
	}

	public Message[] ReadEmailfromSenderAndafter(String username, String password, String folder, String sendermailId,
			int sentAfter) {
		Message[] messages = null;
		// create properties field

		Calendar cal = Calendar.getInstance();
		sentAfter = 0 - sentAfter;
		cal.add(Calendar.MINUTE, sentAfter);
		Date dte = cal.getTime();

		try {
			Session session = Session.getDefaultInstance(properties, null);
			Store store = session.getStore("imaps");
			store.connect(host, username, password);
			log.info("Connection to email account successful");

			Folder EmailFolder = store.getFolder(folder);
			EmailFolder.open(Folder.READ_WRITE);

			SearchTerm sender = new FromTerm(new InternetAddress(sendermailId));
			FlagTerm UnSeenterm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			System.out.println("date send after " + dte);
			ReceivedDateTerm beforeNowterm = new ReceivedDateTerm(ComparisonTerm.GE, dte);

			SearchTerm searchTerms = new AndTerm(beforeNowterm, UnSeenterm);
			SearchTerm fromAndUnSeemTerm = new AndTerm(searchTerms, sender);
			messages = EmailFolder.search(fromAndUnSeemTerm);
			log.info("number of filtered Emails" + messages.length);
			log.info("Email Read is successful");

		} catch (NoSuchProviderException e) {
			log.info("Email provider not found");
			System.exit(1);
			e.printStackTrace();
		} catch (MessagingException e) {
			log.info("Message exception occurred");
			e.printStackTrace();
			System.exit(2);
		}

		return messages;
	}

	public String returnOTPCodeFromEMail(Message[] msges) throws Exception {

		String content = "";
		try {
			if (msges.length > 0) {
				Object msgContent = msges[msges.length - 1].getContent();
				if (msgContent instanceof MimeMultipart) {

					Multipart multipart = (MimeMultipart) msgContent;

					BodyPart bodyPart = multipart.getBodyPart(multipart.getCount() - 1);

					String disposition = bodyPart.getDisposition();

					if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
						System.out.println("Mail have some attachment");

						DataHandler handler = bodyPart.getDataHandler();
						System.out.println("file name : " + handler.getName());
					} else if (bodyPart.getContent() instanceof MimeMultipart) {
						MimeMultipart innerMulti = (MimeMultipart) bodyPart.getContent();
						BodyPart internal = innerMulti.getBodyPart(innerMulti.getCount() - 1);
						content = internal.getContent().toString();
					} else {
						content = bodyPart.getContent().toString();
					}
				} else
					content = msges[msges.length - 1].getContent().toString();

				if (content.contains("is")) {
					String[] messageList = content.split("pin is");
					content = messageList[1];
					System.out.print(content);
					if (content.contains("<img"))
						content = content.substring(0, content.indexOf("<img")).trim();

				}
			}
		} catch (MessagingException e1) {
			log.info(e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e2) {
			log.info(e2.getMessage());
			e2.printStackTrace();
		}
		log.info("Value of OTP Code " + content);
		return content.trim();
	}

	private void browseURL(String url) {
		String charset = "UTF-8";

		URLConnection connection;
		try {
			connection = new URL(url).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException ex) {
			String message = ex.getMessage();
			String[] messageList = message.split("vc=");
			verificationCode = messageList[1];

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
