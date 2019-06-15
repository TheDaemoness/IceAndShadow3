# .compat

Package with classes that make heavy references to Minecraft's types,
or that need to access/provide package-visible fields of Minecraft types.

Files in this package (including subpackages) may use any otherwise restricted type,
though there is a ban on directly inheriting from anything in `net.minecraftforge`
(implementing interfaces is fine).

Subpackages may in turn contain `impl` subpackages,
which contain classes and objects primarily related to the implementation of IaS3 logic.
They generally shouldn't be referenced wherever their associated Minecraft types shouldn't be referenced.

Files in this package specifically are not meant to be used outside of the general `compat` package tree.
