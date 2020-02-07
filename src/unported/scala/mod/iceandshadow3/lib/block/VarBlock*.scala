package mod.iceandshadow3.lib.block

class VarBlockBool(name: String) extends VarBlock[Boolean](name, false, true)
class VarBlockOrd(name: String, max: Int) extends VarBlock[Int](name, Array.tabulate(max)(idx => idx).toIndexedSeq:_*)