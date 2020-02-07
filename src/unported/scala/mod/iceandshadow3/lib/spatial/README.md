# .lib.spatial

Classes and objects whose main purpose is to store or manipulate coordinates, volumes, regions, etc.

Most of this package's contents are unfit for `util` as their behaviors are defined in terms of Minecraft units.
If a type could go in `util` or `spatial` (e.g. `TupleXZ`), it should go here.

References to Minecraft's own spatial types (e.g. `BlockPos`) are allowed.

## *XZ/*XYZ

These classes use discrete coordinates, but have no defined units.
Additionally, `IPos` coordinate objects are usually not used to store offsets.
This is the key distinction between them and the `IPos` traits, despite otherwise sharing many similarities.

When in doubt, use the `XZ`/`XYZ` classes.