package mod.iceandshadow3.lib.block

import mod.iceandshadow3.lib.spatial.EAxis

class BlockVarBool(name: String) extends BlockVar[Boolean](name, false, true)
class BlockVarOrd(name: String, max: Int) extends BlockVar[Int](name, Array.tabulate(max)(idx => idx).toIndexedSeq:_*)
class BlockVarAxis(name: String) extends BlockVar[EAxis](name, EAxis.values().toIndexedSeq:_*) {}