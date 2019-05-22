_Here be Navistra-scaled dragons. This mod is heavily WIP and should be considered entirely unfit for purpose until version 0.1.0.0, and mostly incomplete until version 1.0.0.0_

_Until then, this every 0.0.x version should be considered backward-incompatible, and every 0.0.0.x version forwards-incompatible._

# Ice and Shadow III
__A brutal post-endgame dimension mod for Minecraft 1.13 + Forge.__

This is a rewrite of the rewrite of the original Ice and Shadow mod to both rework its content and be a bit more future-proof for a change.

Adds a new cold, dark, and highly hostile dimension to Minecraft.
Its entry requirements and offerings make it a potential long-term destination after finishing most of vanilla Minecraft's progression.
Indefinite survival is possible, though survival on Nyx comes with unique challenges.

## Getting Started (As A Player)

__NYI.__

## Getting Started (As A Developer)

To get a usable development environment:
1. Clone/extract this repository into an empty directory.
2. Extract a copy of the Forge MDK (version 25 or later) into the same directory *without overwriting any existing files*.
3. Use the Forge-provided gradle wrapper as usual.
	1. You may need to set the `JAVA_HOME` environment variable before running the gradle wrapper if you're using Java 11.
	2. Tasks of special interest include `clean`, `build`, `runClient`, `runServer`.

You may generate Intellij IDEA project files using the `idea` task. Generating an Eclipse project with `eclipse` is left in as an option but is not recommended, as the main Scala plugin is outdated to the point of instability on recent versions of Eclipse.

Afterward, refer to the READMEs under the `src/main/scala` directory for more information.

### Bloop

As building a Scala project with Gradle without the daemon is slower than it needs to be, IaS3 can also be tested using the Bloop build server. No changes should need to be made to `build.gradle`. Just run the `bloopInstall` task to export a Bloop configuration.

This also enables use of the [Metals language server](https://scalameta.org/metals/).

### ./sandbox

The .gitignore includes a directory named `sandbox`, which has no specified purpose and isn't used by anything.
You can use this directory for storing convenience scripts or any WIP assets that shouldn't make it into the repository.

## License

Ice and Shadow III's original files (as in, assets not derived from Minecraft assets and that do not reference any net.minecraft classes) are licensed under the Mozilla Public License version 2, where possible.
