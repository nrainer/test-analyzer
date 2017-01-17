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
1. Import all result files
2. Check the raw data
3. Call the `Transfer` procedure with the execution id (procedure is specified in `perform_transfer.sql`)