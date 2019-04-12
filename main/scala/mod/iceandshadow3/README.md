# (root)

Contains `IceAndShadow3`.

Otherwise, exclusively contains pure-static classes which are immediately relevant to the overall initialization/configuration of IaS3.

Despite Scala offering the option to do otherwise, the Scala files mostly follow the one-public-class-per-file rule.

## Type Name Prefixes
Two adjacent capital letters at the start of a class/file name are indicative of a type name prefix.

### "A" - Adapter
Always written in Java. Takes IaS3 objects (or their data) and exposes a Minecraft-compatible interface to them.

Used for Blocks, Items, Entities, and virtually everything that needs to go in a Registry.

Should be defined exclusively in `mod.iceandshadow3.compat`.

### "B" - Base
Java: an `abstract` class.

Scala: a non-`sealed` `abstract` class.

### "C" - Compatibility/Condom
Always written in Scala. Takes Minecraft objects (or their data) and exposes an IaS3-controlled interface to them.

Exposes the objects it's hiding with `private[compat]` visibility.

Typically used inside adapters to pass on to IaS objects.

Should be defined exclusively in `mod.iceandshadow3.compat`.

### 'E' - Enumeration
Java: a `public enum`. Woop.

Scala: a sealed class with a companion object that contains case objects that subtype the sealed class.

### 'I' - Interface
Java: an `interface`.

Scala: a `trait` that contains only `def`s.

### "S" - Singleton/Static
If a Java file, a class whose only purpose is to group together static methods.

If a Scala file, an `object`.

Not present on any of the classes in `mod.iceandshadow3` in specific, where it would be redundant.