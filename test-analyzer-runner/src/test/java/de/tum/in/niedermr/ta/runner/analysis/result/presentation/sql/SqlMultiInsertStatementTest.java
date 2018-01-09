package de.tum.in.niedermr.ta.runner.analysis.result.presentation.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Test {@link SqlMultiInsertStatement}. */
public class SqlMultiInsertStatementTest {

	/** Test. */
	@Test
	public void testToSql() {
		SqlMultiInsertStatement statement = new SqlMultiInsertStatement("INSERT INTO Person (id, name) VALUES\r\n%s;");

		assertTrue(statement.toSql().isEmpty());

		statement.addValuesStatementPart("(3, 'Tom')");
		statement.addValuesStatementPart("(4, 'Susi')");

		assertEquals("INSERT INTO Person (id, name) VALUES\r\n(3, 'Tom'),\r\n(4, 'Susi');", statement.toSql());
	}
}
