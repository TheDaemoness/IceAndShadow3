# Understanding IaS3's Codebase

## Type Usage Restrictions
Unless a package's README specifies otherwise, anything originating from one of the following packages (or subpackages thereof):
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

This restriction is the cause for a lot of added complexity in IaS3's codebase, however it's also necessary to avoid breakage (which DOES happen between Minecraft versions) spreading uncontrollably to the rest of IaS3's codebase.

Exceptions are on a package-by-package basis.

## Type Name Prefixes
Two or more adjacent capital letters at the start of a class/file name are indicative of a type name prefix.

### "A" - Adapter
Always written in Java. Takes IaS3 objects (or their data) and exposes a Minecraft-compatible (or Forge-compatible) interface to them.

Used for Blocks, Items, Entities, and virtually everything that needs to go in a Registry.

Should be defined exclusively in `mod.iceandshadow3.compat`.

### "B" - Base
Java: an abstract class.

Scala: a non-sealed abstract class.

### "C" - Compatibility/Condom
Always written in Scala. Takes Minecraft objects (or their data) and exposes an IaS3-controlled interface to them.

Usually exposes the objects it's hiding with `protected[compat]` or `private[compat]` visibility, sometimes narrower.

Typically constructed inside adapters to pass on to IaS objects.

Should be defined exclusively in `mod.iceandshadow3.compat`.

### 'E' - Enumeration
Java: a `public enum`. Woop.

Scala: a sealed class with a companion object that contains case objects that subtype the sealed class.

### 'I' - Interface
Java: an `interface`.

Scala: a `trait` that contains only public non-final `def`s.

### "S" - Singleton/Static
Java: a singleton OR a class whose only purpose is to group together static methods.

Scala: a non-package non-companion `object`.

Not present on any of the classes in `mod.iceandshadow3` in specific, where it would be redundant.

### "T" - Trait
Always written in Scala. A trait that does NOT fit the criteria for the `I` prefix.