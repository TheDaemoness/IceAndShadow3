# .gen

Contains the (mostly) Minecraft agnostic parts of the IaS3 worldgen system.

One `BWorldSource` exists per IaS3 dimension.
Its methods are called `compat.dimension.AChunkGenerator`, specifically `getChunkSource`.
Most terrain gen (and an initial simple decoration pass) happens in the returned `BChunkSource`.

WARNING: Some stuff in here has thread-safety requirements.
