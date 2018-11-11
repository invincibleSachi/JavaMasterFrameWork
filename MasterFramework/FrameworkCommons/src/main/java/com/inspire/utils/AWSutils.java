package com.inspire.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.VersionListing;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.inspire.abstestbase.MasterLogger;
import com.inspire.constants.UtilConstants;

/**
 * @author sachi
 *
 */
public class AWSutils {
	// static AWSCredentials credentials = new
	// ProfileCredentialsProvider().getCredentials();
	static Logger log = MasterLogger.getInstance();
	static AWSCredentials credentials = new BasicAWSCredentials(
			PropertyFileUtils.getProperty(UtilConstants.S3_CONFIG, "ACCESS_KEY"),
			PropertyFileUtils.getProperty(UtilConstants.S3_CONFIG, "SECRET_ACCESS_KEY"));
	static AmazonS3 s3client = new AmazonS3Client(credentials);
	static String S3folderName = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "S3snapshotPath");
	static String bucketName = PropertyFileUtils.getProperty(UtilConstants.CONFIG_FILE, "S3BucketName");
	static Region usWest1 = Region.getRegion(Regions.US_WEST_1);
	static AmazonSQS sqs = new AmazonSQSClient(credentials);

	public static void uploadFileS3(String subfolderName, String filePath) {
		File file = new File(filePath);
		InputStream filestream = null;
		try {
			filestream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (subfolderName != null || !subfolderName.equals("")) {
			subfolderName = subfolderName + "/";
		}
		String filename = S3folderName + subfolderName + "/" + file.getName();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/json");
		log.info(filename);
		s3client.putObject(new PutObjectRequest(bucketName, filename, filestream, metadata));
		log.info(filename + " upload successfully at S3 server");

	}

	public static void uploadFileS3(String subfolderName, File file) {
		String filename = S3folderName + file.getName();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/json");
		if (subfolderName != null || !subfolderName.equals("")) {
			subfolderName = subfolderName + "/";
		}
		log.info(filename);
		InputStream filestream = null;
		try {
			filestream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		s3client.putObject(new PutObjectRequest(bucketName, filename, filestream, metadata));
		log.info(filename + " upload successfully at S3 server");

	}

	public static void uploadFileS3(String bucketName, String S3folderName, File file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/json");
		S3folderName = S3folderName + "/";
		String filename = S3folderName + file.getName();
		log.info(filename);
		InputStream filestream = null;
		try {
			filestream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			log.info(e.getMessage());
			log.info("creating file stream failed before S3 upload");
			return;
		}
		s3client.putObject(new PutObjectRequest(bucketName, filename, filestream, metadata));
		log.info(filename + " upload successfully at S3 server in bucket " + bucketName);

	}

	public static void uploadAllFiles(String folderName, String strDateFolder) {
		folderName = folderName + strDateFolder;
		File folder = new File(folderName);
		if (folder.exists() && folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				String filename = S3folderName + strDateFolder + "/" + file.getName();
				InputStream filestream = null;
				try {
					filestream = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType("application/json");
				s3client.putObject(new PutObjectRequest(bucketName, filename, filestream, metadata));
				log.info(filename + " upload successfully at S3 server");
			}
		} else {
			log.info(folderName + " either does not exists or is not a directory");
		}

	}
	
	public static void uploadFileToS3(String folderName, String filePath) {
		File file = new File(filePath);
		InputStream filestream = null;
		try {
			filestream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			log.info(e.getMessage());
			log.info("creating file stream failed before S3 upload");
			return;
		}
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType("application/json");
		String filename = folderName + "/" + file.getName();
		log.info(filename);
		s3client.putObject(new PutObjectRequest(bucketName, filename, filestream, metadata));
		log.info(filename + " upload successfully at S3 server");
	}

	public static void createFolderS3(String folderName, String subfolderName) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + subfolderName, emptyContent,
				metadata);
		// send request to S3 to create folder
		s3client.putObject(putObjectRequest);
		log.info(subfolderName + " folder Created successfully");
	}

	public static void createSubFolderInSnapshot(String subfolderName) {
		createFolderS3(S3folderName, subfolderName);
	}

	public static void getFileFromS3(String folderName, String fileName, String targetFolderName) {
		String existingBucketName = bucketName;
		String keyName = folderName + "/" + fileName;
		File file = new File(fileName);
		if(file.exists()) {
			file.delete();			
		}
		log.info("Downloading file " + fileName + " from S3 server ...");
		GetObjectRequest request = new GetObjectRequest(existingBucketName, keyName);
		S3Object object = s3client.getObject(request);
		S3ObjectInputStream objectContent = object.getObjectContent();
		try {
			IOUtils.copy(objectContent, new FileOutputStream(targetFolderName + "/" + fileName));
			log.info(fileName + " downloaded successfully from bucket " + bucketName);
		} catch (FileNotFoundException e) {
			log.info(fileName + " file not present in S3 " + e.getMessage());
		} catch (IOException e) {
			log.info(fileName + " IOException occurred" + e.getMessage());
		}
		
	}
	
	/**
     * @param bucketName
     * @param fileKey
     * @return
     */
    public static VersionListing getVersions(String bucketName, String fileKey) {
        AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials(PropertyFileUtils.getProperty
        		(UtilConstants.S3_CONFIG, "ACCESS_KEY"),
    			PropertyFileUtils.getProperty(UtilConstants.S3_CONFIG, "SECRET_ACCESS_KEY")));
        ListVersionsRequest listVersionsRequest = new ListVersionsRequest();
        listVersionsRequest.setBucketName(bucketName);
        listVersionsRequest.setPrefix(fileKey);
        return s3Client.listVersions(listVersionsRequest);
    }

    /**
     * @param bucketName
     * @param fileKey
     * @return
     */
    public static String getOldestVersion(String s3Path) {
    	String version = null;
        if (null != s3Path) {
        	log.info(s3Path);
            AmazonS3URI s3VideoPath = new AmazonS3URI(s3Path);
            VersionListing listOfVersions = getVersions(s3VideoPath.getBucket(), s3VideoPath.getKey());
            if (null != listOfVersions) {
                List<S3VersionSummary> s3VersionSummaries = listOfVersions.getVersionSummaries();
                if (s3VersionSummaries.size() == 1) {
                	version = (s3VersionSummaries.get(0).getVersionId());
                	log.info(version);
                    return s3VersionSummaries.get(0).getVersionId();
                }else {
                Map<Date, String> versionMap = new TreeMap<>();
                for (S3VersionSummary s3VersionSummary : s3VersionSummaries) {
                    log.info(versionMap.put(s3VersionSummary.getLastModified(), s3VersionSummary.getVersionId()));
                }
                return versionMap.get(versionMap.keySet().stream().sorted().collect(Collectors.toList()).get(0));
            }}
        }
        return version;
    }

	public static List<String> getS3FileListInFolder(String bucketName, String folderKey) {
		List<String> fileList = new ArrayList<String>();
		String key=null;
		ObjectListing objectListing = s3client
				.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderKey + "/"));

		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			key=objectSummary.getKey().replaceAll(folderKey+"/", "");
			fileList.add(key);
		}
		fileList.remove(0);
		return fileList;
	}

	public static long getS3FileSize(String bucketName, String folderName, String fileName) {
		String existingBucketName = bucketName;
		String keyName = folderName + "/" + fileName;
		log.info("getting S3 file Object " + fileName + " from S3 server ...");
		GetObjectRequest request = new GetObjectRequest(existingBucketName, keyName);
		S3Object object = s3client.getObject(request);
		return object.getObjectMetadata().getContentLength();
	}

	public static void postSQSMsg() {
		AmazonSQS sqs = new AmazonSQSClient(credentials);
		sqs.setRegion(usWest1);
	}

	public static String createSQSQueue(String queueName) {

		sqs.setRegion(usWest1);
		CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
		return sqs.createQueue(createQueueRequest).getQueueUrl();
	}

	public static void sendQMsg(String myQueueUrl, String msg) {
		log.info("sending msg to " + myQueueUrl + " " + msg);
		sqs.setRegion(usWest1);
		sqs.sendMessage(new SendMessageRequest(myQueueUrl, msg));
	}

	public static List<Message> receiveQMsg(String myQueueUrl) {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
		receiveMessageRequest.setMaxNumberOfMessages(10);
		return sqs.receiveMessage(receiveMessageRequest).getMessages();
	}

	public static void deleteQMsg(String myQueueUrl, String messageReceiptHandle) {
		log.info("deleting msg " + messageReceiptHandle + " from " + myQueueUrl);
		sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));
	}

	public static void deleteQ(String myQueueUrl) {
		log.info("deleting SQS Q " + myQueueUrl);
		sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
	}

	public static boolean checkObjectExists(String s3BucketName, String path) {
		boolean isValidFile = true;
		try {
			ObjectMetadata objectMetadata = s3client.getObjectMetadata(s3BucketName, path);
		} catch (AmazonS3Exception s3e) {
			if (s3e.getStatusCode() == 404) {
				// i.e. 404: NoSuchKey - The specified key does not exist
				isValidFile = false;
			} else {
				throw s3e; // rethrow all S3 exceptions other than 404
			}
		}

		return isValidFile;
	}
	

}
