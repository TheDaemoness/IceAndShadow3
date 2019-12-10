# .multiverse

Contains elements of the world(s) of Ice and Shadow III.

Items, blocks, mobs, and more all go into subpackages here.

`BDomain` objects go into this package specifically, not their respective subpackage.
`BDimension` objects also go here.

Remember to add both to `iceandshadow3.InitCommon`!

## Type Name Prefixes

As there are a few common categories of types here, here are some more type name prefixes.

* `Mat` - Scala object that extends BMateria or one of its children.
* `LB` - Scala class that extends BLogicBlock.
* `LM` - Scala class that extends BLogicMob.
* `LI` - Scala class that extends BLogicItem.
* `S*` - BStateData paired with the applicable logic.

## Nyx Item Freezing

The fixed list of vanilla items that freeze unusually can be found in `dim_nyx.UnusualFreezeMap`.
Otherwise, see the `antifreeze` and `freezes` item tags.