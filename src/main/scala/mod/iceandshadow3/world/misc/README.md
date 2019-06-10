# .world.misc

Implementations of new status effects (which are disassociated from any particular domain) and particle effects.

Note that currently, the displayed texture varies with the order of initialization due to a static counter variable.
The textures themselves should be 18x18 and placed in `textures/status.png`.

*FUTURE WARNING*: If all of the vanilla entries in the objects here are removed,
make sure there's something else that will trigger lazy evaluation besides the relevant binders' populate methods.