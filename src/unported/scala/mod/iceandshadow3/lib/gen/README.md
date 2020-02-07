# .gen

Contains the (mostly) Minecraft agnostic parts of the IaS3 worldgen system.
The type restriction is in place, but some idosyncracies of Minecraft's worldgen (i.e. chunk shapes) do bleed through.

One `BWorldGen` exists per IaS3 dimension, which provides several `BWorldGenLayer`s, which are executed in sequence.

## CONTRIBUTOR WARNINGS

* Some stuff in here has thread-safety requirements.
* Under no circumstances is any of this to cache data indefinitely, ESPECIALLY noise maps.
* World generation needs to be reasonably performant. Prefer to avoid foreach (including for-comprehensions).
* Information used in world generation should be generated at most once per chunk, preferably just once.
* Soft/weak cache values save lives.

## Worldgen Feature Options

Listed below are the various BWorldGenFeatureType subtypes and the differences between them.

* BWorldGenFeatureTypeSimple: A virtual 2x2 grid of world gen column functions.
    * Useful for structures that can be easily expressed in terms of a few simple functions.
* CanvasFeature: A volume which can be painted with TBlockStateSources.
    * Useful for semi-static but complex structures that don't need to be rotated (rotation TBI).
* BWorldGenFeatureTypeBuilt: Construct CanvasFeatures for each instance of a feature.
    * For when features need to be more dynamic than what a CanvasFeature affords.
