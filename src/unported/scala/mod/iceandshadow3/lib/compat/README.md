# .lib.compat

Package with classes that make heavy references to Minecraft's types,
or that need to access/provide package-visible fields of Minecraft types.

Files in this package (including subpackages) may use any otherwise restricted type,
though there is a ban on directly inheriting from anything in `net.minecraftforge`
(implementing interfaces is fine).

Although it might seem like `internal` would be a better name for this package,
some objects act as wrappers and are very much intended to be used in the general codebase.

## *.impl

Subpackages may in turn contain `impl` subpackages,
which contain classes and objects primarily related to the implementation of IaS3 logic,
which generally shouldn't be referenced wherever their associated Minecraft types shouldn't be referenced
(with some exceptions, like types meant to be mixed-in/inherited).

## Binders

As a way to skirt around the worst of the type restriction rules, the compat module makes heavy use of binders,
which assign integer IDs to library types that can be then used to lookup their implementations in O(1) time.
See the Binder class in the `.util.collect` for more info.

The binder singletome objects themselves are in the relevant `impl` packages.