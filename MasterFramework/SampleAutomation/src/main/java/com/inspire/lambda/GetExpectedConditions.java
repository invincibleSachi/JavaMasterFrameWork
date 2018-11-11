package com.inspire.lambda;

import com.inspire.utils.PostgresqlUtil;

/**
  for future implementation
 *
 */
public class GetExpectedConditions {

	interface ExpectedDBConditions {
		boolean getDBConditions(PostgresqlUtil pgutil, String query);
	}

	interface ExpectedNumericCondition {
		boolean expectedNumericConditions(int a);
	}

	ExpectedDBConditions getAlert = (PostgresqlUtil pgutil, String query) -> {
		String result = pgutil.executeSelectQuery(query);
		return !result.isEmpty();
	};

	ExpectedNumericCondition numCond = (int a) -> {
		return (100 % a == 0);
	};

	public boolean getDBConditions(ExpectedDBConditions conditions, PostgresqlUtil pgutil, String query) {
		return conditions.getDBConditions(pgutil, query);
	}

	public boolean getNumericCondition(ExpectedNumericCondition num, int a) {
		return num.expectedNumericConditions(a);
	}

}
