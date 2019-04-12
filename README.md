_Here be Navistra-scaled dragons. This mod is heavily WIP and should be considered entirely unfit for purpose until version 0.1.0.0, and mostly incomplete until version 1.0.0.0_

# Ice and Shadow III
__A brutal post-endgame dimension mod for Minecraft 1.12 + Forge.__

This is a rewrite of the rewrite of the original Ice and Shadow mod to both rework its content and be a bit more future-proof for a change.

Adds a new cold, dark, and highly hostile dimension to Minecraft.
Its entry requirements and offerings make it a potential long-term destination after finishing most of vanilla Minecraft's progression.
Indefinite survival is possible, though survival on Nyx comes with unique challenges.

## Getting Started

__NYI.__

## Building/Editing

The contents of this repository should be placed in the `src` folder in your Forge MDK.

IaS3 is written partially in Java 8, partially in Scala 2.11.
To build properly, you will need to add `apply plugin: 'scala'` to your `build.gradle` and `s/compileJava/compileScala/`.

## Versioning

IaS3 uses a four-number semantic versioning scheme. Each field of the version marks changes that are one of the following:

1. Revision
	* Result in major incompatibilities with older save files.
	* Are API changes which are NOT backward compatible (i.e. removal of deprecated APIs, Minecraft-related breakage).
2. Major Version
	* Result in new clients not being compatible with older servers (e.g. content removals/replacements).
	* Affect world generation.
	* Result in save file incompatibilities that do not entirely prevent loading.
3. Minor Version
	* Result in new servers not being compatible with older clients (e.g. content additions).
	* Are backward-incompatible configuration file changes.
	* Are API changes which are backward compatible.
4. Patch Version
	* Are backward-compatible configuration file changes.
	* Do not affect compatibility (e.g., balance changes, most bugfixes).

If the major or revision numbers are zero, that particular build of IaS3 should be considered unstable, and major version breakages may happen on minor versions.