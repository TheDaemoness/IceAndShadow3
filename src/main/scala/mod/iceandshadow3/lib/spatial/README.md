# .lib.spatial

Classes and objects whose main purpose is to store or manipulate coordinates, volumes, regions, etc.

Most of this package's contents are unfit for `util` as their behaviors are defined in terms of Minecraft units.
If a type could go in `util` or `spatial` (e.g. `PairXZ`), it should go here.

References to Minecraft's own spatial types (e.g. `BlockPos`) are allowed.