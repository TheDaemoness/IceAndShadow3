# More Developer Information

## Bloop

IaS3 includes the bloop plugin in `build.gradle` by default.
This means that by running the `bloopInstall` task:
* IaS3 can be compiled using the [Bloop build server](https://scalacenter.github.io/bloop/).
* The [Metals language server](https://scalameta.org/metals/) can be used.

If, however, you want to test, reobfuscate, or package the mod with Bloop, you're on your own.

## ./sandbox

The .gitignore includes a directory named `sandbox` (no relation to the security concept), which has no specified purpose and isn't used by anything.
You can use this directory for storing convenience scripts or any WIP assets that shouldn't make it into the repository.