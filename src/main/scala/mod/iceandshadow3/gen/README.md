# .gen

Contains the (mostly) Minecraft agnostic parts of the IaS3 worldgen system.
The type restriction is in place, but some idosyncracies of Minecraft's worldgen (i.e. chunk shapes) do bleed through.

One `BWorldSource` exists per IaS3 dimension.
Its methods are called `compat.world.impl.AChunkGenerator`, specifically `getChunkSource`.
Most terrain gen (and an initial simple decoration pass) happens in the returned `BChunkSource`.

**WARNINGS**:
* Some stuff in here has thread-safety requirements.
* Under no circumstances is any of this to cache data indefinitely, ESPECIALLY noise maps.
* World generation needs to be reasonably performant.
Information used in world generation should be generated at most once per chunk, preferably just once.
