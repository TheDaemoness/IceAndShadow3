_Here be Navistra-scaled dragons. This mod is heavily WIP and should be considered entirely unfit for purpose until version 0.1.0.0, and mostly incomplete until version 1.0.0.0_

_Until then, this every 0.0.x version should be considered backward-incompatible, and every 0.0.0.x version forwards-incompatible._

_Abandon hope, all ye who try to run this on the dedicated server._

# Ice and Shadow III
__A brutal post-endgame dimension mod for Minecraft 1.13 + Forge.__

This is a rewrite of the rewrite of the original Ice and Shadow mod to both rework its content and be a bit more future-proof for a change.

Adds a new cold, dark, and highly hostile dimension to Minecraft.
Its entry requirements and offerings make it a potential long-term destination after finishing most of vanilla Minecraft's progression.
Indefinite survival is possible, though survival on Nyx comes with unique challenges.

## Getting Started (As A Player)

__NYI.__

## Getting Started (As A Developer)
[![Travis CI Status](https://travis-ci.org/TheDaemoness/IceAndShadow3.svg?branch=master "Travis CI status (branch: master)")](https://travis-ci.org/TheDaemoness/IceAndShadow3)

IaS3 uses Gradle, but does not include a gradle wrapper in VCS for largely philosophical reasons.

To get a usable development environment:
1. Ensure you are using Java **8 or later, up to and excluding 11**.
Otherwise, you will run into problems when you try to run the gradle wrapper.
2. Clone/extract this repository into an empty directory.
3. Get a compatible copy of the gradle wrapper (4.9 up to and excluding 5.0). You can either:
	* (Recommended) If you have gradle installed, run `gradle wrapper` in the project directory.
	* Extract a copy of the Forge MDK (version 25.0 up to and excluding 26.0) into the same directory *without overwriting any existing files*.
4. Use the gradle wrapper as usual with a compatible version of Java.
    1. You may generate Intellij IDEA project files using the `idea` task. Generating an Eclipse project with `eclipse` is left in as an option but is not recommended,
    as the main Scala plugin is outdated to the point of instability on recent versions of Eclipse.
    2. You may run the client and/or dedicated server with the `runClient` and `runServer` tasks respectively.
    3. Remember to run `check` frequently!

Refer to the READMEs under the `src` directory for more information.

## License

Ice and Shadow III's original files (as in, assets not derived from Minecraft assets and that do not reference any net.minecraft classes) are licensed under the Mozilla Public License version 2, where possible.
