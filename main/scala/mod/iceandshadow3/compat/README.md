# .compat

The following package contains various wrappers+adapters for keeping Minecraft's codebase at arm's length away from the rest of the mod.

This is where most "A" and "C" classes are declared.

## Block Requirements
It's very important that all blocks have a `BlockLogic, int` constructor, as this gets called in `SBlockFactory`.