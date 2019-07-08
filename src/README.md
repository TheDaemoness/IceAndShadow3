# More Developer Information

IaS3 uses tabs for indentation everywhere, and spaces (never tabs) for code alignment between lines.

## Relaxed JSON

The `.json` and `.mcmeta` files in IaS3's source tree get minified using
[a lax parser](http://docs.groovy-lang.org/docs/latest/html/api/groovy/json/JsonParserType.html#LAX)
during resource processing. In practice, this means:
* These files should be formatted helpfully.
* Comments may be used (prefer `#`-style).
* Trailing commas are permissible.
* Unquoted or single-quoted keys can be used (but should be avoided).
* Unquoted or single-quoted strings can be used (but should also be avoided).

## ../sandbox

The .gitignore includes a directory named `sandbox` (no relation to the security concept),
which has no specified purpose and isn't used by anything.
You can use this directory for storing convenience scripts or any WIP assets that shouldn't make it into the repository.

## Bloop

Bloop is temporarily unsupported until
its developers publish an updated Gradle plugin (for Scala 2.13) to Maven central.
