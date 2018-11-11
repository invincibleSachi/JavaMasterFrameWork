package com.inspire.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;

/**
 * @author sachi
 *
 */
public class MongoUtility {
	Logger log = Logger.getLogger(MongoUtility.class);
	MongoClient mongoClient;
	MongoDatabase mongodb;

	public MongoUtility(int portNumber, String dbName) {
		try {
			mongoClient = new MongoClient("localhost", portNumber);
			mongodb = mongoClient.getDatabase(dbName);
		} catch (Exception e) {
			System.out.println("mongo db not configured at port " + portNumber);
			log.info("Exception occurred while initializing mongodb " + e.getMessage());
		}

	}

	public MongoUtility(String server, int portNumber, String dbName, String userName, String password) {
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		seeds.add(new ServerAddress(server, portNumber));
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		credentials.add(MongoCredential.createScramSha1Credential(userName, dbName, password.toCharArray()));
		mongoClient = new MongoClient(seeds, credentials);
		mongodb = mongoClient.getDatabase(dbName);
	}

	public MongoDatabase getDatabase(String dbName) {
		mongodb = mongoClient.getDatabase(dbName);
		return mongodb;
	}

	public void createCollections(String collection) {
		mongodb.createCollection(collection);
	}

	public MongoDatabase createCollections(String collection, boolean autoIndex, boolean capped, long maxDocuments,
			long sizeInBytes) {
		if (mongodb.getCollection(collection).count() == 0) {

			CreateCollectionOptions option = new CreateCollectionOptions();
			option.autoIndex(autoIndex);
			option.capped(capped);
			option.maxDocuments(maxDocuments);
			option.sizeInBytes(sizeInBytes);
			// collection Options example { capped : true, autoIndexID : true,
			// size : 6142800, max : 10000 }
			mongodb.createCollection(collection, option);
		}
		return mongodb;
	}

	public void dropDb(String databaseName) {
		mongoClient.dropDatabase(databaseName);
	}

	public Document runCommand(Bson mongoCommand) {
		return mongodb.runCommand(mongoCommand);
	}

	public MongoCollection<Document> getCollections(String collectionName) {
		return mongodb.getCollection(collectionName);
	}

	public FindIterable<Document> find(String collectionName, List<String> key, List<String> value) {
		BasicDBObject whereQuery = new BasicDBObject();
		Iterator<String> keyIt = key.iterator();
		Iterator<String> valueIt = value.iterator();
		while (keyIt.hasNext() && valueIt.hasNext()) {
			whereQuery.put(keyIt.next(), valueIt.next());
		}

		MongoCollection<Document> collection = mongodb.getCollection(collectionName);
		FindIterable<Document> cursor = collection.find(whereQuery);
		return cursor;
	}

	public FindIterable<Document> find(String collectionName) {
		// https://docs.mongodb.com/getting-started/java/query/
		MongoCollection<Document> collection = mongodb.getCollection(collectionName);
		FindIterable<Document> cursor = collection.find();
		return cursor;
	}

	public FindIterable<Document> find(String collectionName, Bson bson) {
		// https://docs.mongodb.com/getting-started/java/query/
		MongoCollection<Document> collection = mongodb.getCollection(collectionName);
		FindIterable<Document> cursor = collection.find(bson);
		return cursor;
	}

	public AggregateIterable<Document> aggregate(String collectionName, List<? extends Bson> document) {
		// https://docs.mongodb.com/getting-started/java/aggregation/
		MongoCollection<Document> collection = mongodb.getCollection(collectionName);
		AggregateIterable<Document> iterable = collection.aggregate(document);
		return iterable;
	}

}
