package mod.iceandshadow3.lib.block

class BlockVarBool(name: String) extends BBlockVar[Boolean](name, false, true)
class BlockVarOrd(name: String, max: Int) extends BBlockVar[Int](name, Array.tabulate(max)(idx => idx).toIndexedSeq:_*)
