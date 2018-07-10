# test-analyzer [![Build Status](https://travis-ci.org/cqse/test-analyzer.svg?branch=master)](https://travis-ci.org/cqse/test-analyzer)

A mutation testing tool to detect pseudo-tested methods and to collect information about the test execution (e.g., the minimal stack distance between a test case and a method).

# Eclipse Setup

```
mvn clean install
mvn eclipse:eclipse
```

# Main Class
`de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart`

# Sample Configuration
`/test-analyzer-runner/sample/sample-configuration.config`

(See also: configurations of the integration tests in: `/test-analyzer-test-int/src/test/data/integrationtest*/configuration`)

# Troubleshooting
If the project `test-analyzer-sdist-maven` cannot be built, the used Maven version is too old. Maven 3.5.2 is working.

# Data Post-Processing
SQL schema and procedures: `test-analyzer-runner/sql`

(Required database: MySQL >= 5.7)

## Setup
1. Use the files in `1_setup/1_schema` to create the schema
2. Use the files in `1_setup/2_procedures` to create the needed functions and procedures

## Data Import
1. Import `execution_information.sql.txt` and subsequently all other result files
2. Check the raw data
3. Transfer the raw data into structures optimized for analyses using the script `2_processing/transfer_script.sql`. It is necessary to specify the value of `@executionId`.
4. Execute `CALL MaterializeViews()` to create / refresh materialized views (optional).

### Other
* Use `CALL RevertTransfer([executionId])` to revert a data transfer.
* Use `CALL RemoveRawImport([executionId])` to remove the raw data after a transfer.