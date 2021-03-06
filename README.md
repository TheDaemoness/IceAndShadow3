_The dust is still settling from the 1.14 -> 1.15 migration. Things are more likely to be broken than usual._

_Here be Navistra-scaled dragons.
This mod is heavily WIP and should be considered entirely unfit for purpose until version 0.1.0.0,
and incomplete until version 1.0.0.0_

_Until then,
every 0.0.x version should be considered backward-incompatible,
and every 0.0.0.x version forwards-incompatible._

# Ice and Shadow III
__A brutal post-endgame dimension mod for Minecraft 1.15.2 + Forge__

[![Gitter Link](https://badges.gitter.im/IceAndShadow3/community.svg)](https://gitter.im/IceAndShadow3/community)

This is a rewrite of the rewrite of the original Ice and Shadow mod to both rework its content and be a bit more future-proof for a change.

Adds a new cold, dark, and highly hostile dimension to Minecraft.
Its entry requirements and offerings make it a potential long-term destination after finishing most of vanilla Minecraft's progression.
Indefinite survival is possible, though survival on Nyx comes with unique challenges.

## Getting Started (As A Player)

IaS3 is explorable, but nowhere close to its design goals.
If you want to play around with it anyway, follow the added advancements.

## Getting Started (As A Developer)
[![Last GitHub Workflow Run](https://github.com/TheDaemoness/IceAndShadow3/workflows/CI/badge.svg)](https://actions-badge.atrox.dev/TheDaemoness/IceAndShadow3/goto)

IaS3 is written in Scala 2.13 and Java 8.

To get a usable development environment:
1. Ensure you are using Java **8 or later, up to and excluding 11**.
Otherwise, you will run into problems with the Forge Gradle plugin.
2. Clone/extract this repository into an empty directory.
3. Use the Gradle wrapper as usual with a compatible version of Java.
	1. You may generate Intellij IDEA project files using the `idea` task, and Eclipse project files with `eclipse`.
	Note that Eclipse's Scala plugin has been abandoned and is known to be unstable on Eclipse 4.11.
	2. You may run the client and/or dedicated server with the `runClient` and `runServer` tasks respectively.
	3. `runData` will generate resource files in `src/generated/resources` from mod content.
	`clean` will NOT delete these files, as they are tracked by version control.
	4. Remember to run `check` frequently!

Refer to the READMEs under the `src` directory for more information.

## License

Ice and Shadow III's original files
(as in, assets not derived from Minecraft assets and that do not directly reference any net.minecraft classes)
are licensed under the Mozilla Public License version 2, where possible.
