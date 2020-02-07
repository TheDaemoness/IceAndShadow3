# Testing IaS3

IaS3 uses JUnit 5 for testing (despite the fact that ScalaTest exists).

The `test` task in the gradle project will compile and run the tests.
They will be run in the `build/resources` directory,
which is where resources get copied to after processing.

By default all logging to the standard error stream (but not standard output) is shown.
Additionally, partial stack traces of any thrown exceptions (including assertion failures) is also shown.
