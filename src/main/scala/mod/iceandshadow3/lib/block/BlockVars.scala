package mod.iceandshadow3.lib.block

import mod.iceandshadow3.lib.spatial.EAxis

class VarBlockBool(name: String) extends VarBlock[Boolean](name, false, true)
class VarBlockOrd(name: String, max: Int) extends VarBlock[Int](name, Array.tabulate(max)(idx => idx).toIndexedSeq:_*)
class VarBlockAxis(name: String) extends VarBlock[EAxis](name, EAxis.values().toIndexedSeq:_*) {}