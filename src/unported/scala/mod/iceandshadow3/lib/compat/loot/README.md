# lib.compat.loot

Contains IaS3's homebrew loot generation system, used as a more pleasant alternative to JSON loot providers.

Most of the generation of actual item stacks is done by `Loot` objects, each one generating one item stack.

These are usually contained by a mutable `LootBuilder` object, which also stores a wrapped `LootContext`
and provides it to the `Loot` objects at generation time.
