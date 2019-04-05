# (root)

Contains `IceAndShadow3`.

Otherwise, exclusively contains pure-static classes which are immediately relevant to the overall initialization/configuration of IaS3.

## Type Name Prefixes
Two adjacent capital letters at the start of a class/file name are indicative of a type name prefix. Aside from the fairly standard "I" to mark interfaces and "E" to mark enumerations, IaS3 uses a few additional prefixes of note:

### "A" - Adapter
Takes IaS3 objects (or their data) and exposes a Minecraft-compatible interface to them.

Used for Blocks, Items, Entities, and virtually everything that needs to go in a Registry.

Should be defined exclusively in `mod.iceandshadow3.compat`.

### "B" - Base
A abstract type that is not purely abstract.

### "C" - Condom
Takes Minecraft objects (or their data) and exposes an IaS3-controlled interface to them.

Typically used inside adapters to pass on to IaS objects.

Should be defined exclusively in `mod.iceandshadow3.compat`.

### "S" - Singleton/Static
A class which is a singleton or whose main purpose is to group together some related static methods.

Not present on any of the classes in `mod.iceandshadow3` in specific, where it would be redundant.