package com.inspire.utils;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;

/**
 * @author sachi
 *
 */
public class AeroSpikeUtil {
	AerospikeClient client;
	String seedHost;
	int port;

	public AeroSpikeUtil(String seedHost, int port, String user, String password) {
		ClientPolicy cPolicy = new ClientPolicy();
		cPolicy.timeout = 500;
		cPolicy.user = user;
		cPolicy.password = password;
		this.seedHost = seedHost;
		this.port = port;
		this.client = new AerospikeClient(cPolicy, this.seedHost, this.port);

	}

	public Record getRecord(Key key) {
		Policy policy = new Policy();
		Record record = client.get(policy, key);
		return record;
	}

	public Record getRecordSet(Statement stmt) {
		RecordSet rs = client.query(null, stmt);
		Record record = null;

		try {
			while (rs.next()) {
				record = rs.getRecord();
			}
		} finally {
			rs.close();
		}
		return record;
	}

	public Key getKey(Statement stmt) {
		RecordSet rs = client.query(null, stmt);
		Key key = null;

		try {
			while (rs.next()) {
				key = rs.getKey();
			}
		} finally {
			rs.close();
		}
		return key;
	}

}
