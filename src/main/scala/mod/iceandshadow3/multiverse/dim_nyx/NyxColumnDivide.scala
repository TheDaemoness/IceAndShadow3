package mod.iceandshadow3.multiverse.dim_nyx

class NyxColumnDivide extends BNyxColumn {
	override def bedrock() = WorldGenNyx.bedrock
	override def apply(i: Int) = blockDefault(i)
}
