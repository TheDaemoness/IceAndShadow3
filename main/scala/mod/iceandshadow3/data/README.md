# .data

Contains stuff useful for serialization/deserialization, especially to Minecraft's NBT.

Classes in this package may reference types in `net.minecraft.nbt`.

Many of the classes in here implement `ITextLineRW`, used by the config system.
Remember that after changing them, there may need to be a major version bump for all config files that use that class.
If this change is radical enough that the name `ITextLineRW` is no longer accurate,
`IaS3.VER_CFG_FMT` should be bumped instead.