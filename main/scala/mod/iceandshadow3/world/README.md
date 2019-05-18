# .world

Contains elements of the world(s) of Ice and Shadow III. Domains, items, blocks, mobs, and more all go here.

Remember to add new domains to `mod.iceandshadow3.Domains`!

## Type Name Prefixes

The following rules apply to subpackages. In general, if something's here, it's either:
* A base class that's too specific to go in `basics` but useful to multiple domains.
* A domain.

The `S` prefix should not be used in subpackges, as almost everything will be a singleton object.

If a file name ends in a plural word, expect to find a `sealed class` inside.

* `Mat` - Scala object that extends BMateria or one of its children.
* `LB` - Scala class that extends BLogicBlock
* `LM` - Scala class that extends BLogicMob
* `LI` - Scala class that extends BLogicItem