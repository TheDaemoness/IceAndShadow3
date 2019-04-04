# .compat

The following package contains various wrappers+adapters for keeping Minecraft's codebase at arm's length away from the rest of the mod.

Breakages between versions are most likely (ideally, only) going to happen here.

## Type Name Prefix "A" - Adapter
Takes IaS3 objects (or their data) and exposes a Minecraft-compatible interface to them.

Used for Blocks, Items, Entities, and virtually everything that needs to go in a Registry.

## Type Name Prefix "C" - Condom
Takes Minecraft objects (or their data) and exposes an IaS3-controlled interface to them.

Typically used inside adapters to pass on to IaS objects.