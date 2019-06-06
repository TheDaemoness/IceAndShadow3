# Understanding IaS3's Codebase

IaS3's codebase can be difficult to understand. This can be attributed to a few things:
* The mod is written in two languages, one of which has a reputation for being complex.
* The mod isn't written in the most idiomatic Java or Scala.
* The mod is a library mod and content mod rolled into one.
* The mod has some fairly involved inheritance trees but no class diagram.
* ~~The lead developer is nuts.~~

If you just want to see how X block/item/entity works,
then what you're looking for is almost certainly in a subpackage of `mod.iceandshadow3.world`.
It should be easy to find from there.

For everyone else, it's recommended to have a look a the READMEs in each package.

## Type Usage Restrictions
This is important to understand why parts of the codebase are fairly convoluted
(e.g. all extensions of `mod.iceandshadow3.basics.util.TLogic`).

Unless a package's README specifies otherwise, anything originating from one of the following packages
(or subpackages thereof):
* `net.minecraft`
* `net.minecraftforge`
* `com.mojang`

...may not be directly used by IaS3 in any way. This means:
* Classes may not be imported, inherited from, instantiated, or otherwise referenced in code.
* Interfaces may not be imported, implemented, extended, or otherwise referenced in code.
* Instances of those types may not be passed to any methods or constructors, nor have any of their methods called/fields accessed.
* Methods that return instances of the relevant types may not be called.
* Static members/enum constants belonging to a prohibited class may not be called/accessed.
* Reflection may not be used to bypass the above restrictions.

Where possible (and where it'd be remotely sane to do),
these restrictions should be enforced using package-level access.

This restriction is the cause for a lot of added complexity in IaS3's codebase,
however it's also necessary to avoid breakage (which as any seasoned modder will tell you happens BIGTIME between Minecraft versions) spreading uncontrollably to the rest of IaS3's codebase.

Exceptions are on a package-by-package basis.
Subpackages of `mod.iceandshadow3` where you will definitely NOT find such exceptions are `basics`, `util`, and `world`.

## Type Name Prefixes
Two or more adjacent capital letters at the start of a class/file name are indicative of a type name prefix.
If necessary, abbreviations that would otherwise be capitalized will be camelcased to ensure this (e.g. NBT -> Nbt).

Note that these are more guidelines than hard rules.

### "A" - Adapter
Always written in Java.
Takes IaS3 objects (or their data) and exposes a Minecraft-compatible (or Forge-compatible) interface to them.

Used for Blocks, Items, Entities, and virtually everything that needs to go in a Registry.

### "B" - Base
A class intended to be subtyped. Usually abstract, *never* a leaf in the inheritance tree
(`sealed` without any other classes in the same file extending it, or `final`).

### "CNV" - Conversions
Always written in Scala.
A non-package object that contains implicit conversions.

### 'E' - Enumeration
Java: a `public enum`. Woop.

Scala: a sealed class with a companion object that contains `case object`s that subtype the sealed class.

### 'I' - Interface
Java: an `interface`. Note that IaS3's Java portions are in Java 8.

Scala: an unsealed `trait` that could be losslessly translated to a Java 8 interface.

### "T" - Trait
Always written in Scala. A trait that does NOT fit the criteria for the `I` prefix. Often a mixin.

### "W" - Wrapper
Formerly prefixed with "C". Always written in Scala.
Takes Minecraft objects (or their data) and exposes an IaS3-controlled interface to them.

Usually exposes the objects it's hiding with `protected[compat]` or `private[compat]` visibility, sometimes narrower.

Typically constructed inside adapters to pass on to IaS objects.
These should be mostly safe to use throughout IaS3's codebase, barring a handful of forbidden methods.