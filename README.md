# test-analyzer [![Teamscale Project](https://img.shields.io/badge/teamscale-test--analyzer-brightgreen.svg)](https://demo.teamscale.com/activity.html#/test-analyzer)
A mutation testing tool to detect pseudo-tested methods.

# Eclipse Setup

```
mvn clean install
mvn eclipse:eclipse -DdownloadSources=true
```

# Main class
`de.tum.in.niedermr.ta.runner.start.AnalyzerRunnerStart`

# Sample configuration
`/test-analyzer-runner/sample/sample-configuration.config`

(See also: configurations of the integration tests in: `/test-analyzer-test-int/src/test/data/integrationtest*/configuration`)

# Data postprocessing
SQL schema and procedures: `test-analyzer-runner/sql`

(Required database: MySQL >= 5.7)

## Setup
1. Use the files in `1_setup/1_schema` to create the schema
2. Use the files in `1_setup/2_procedures` to create the needed functions and procedures

## Data import
1. Import `execution_information.sql.txt" and subsequently all other result files
2. Check the raw data
3. Transfer the raw data into structures optimized for analyses using the script `2_processing/transfer_script.sql`. It is necessary to specify the value of `@executionId`.
4. Execute `CALL MaterializeViews()` to create / refresh materialized views (optional).

### Other
* Use `CALL RevertTransfer([executionId])` to revert a data transfer.
* Use `CALL RemoveRawImport([executionId])` to remove the raw data after a transfer.