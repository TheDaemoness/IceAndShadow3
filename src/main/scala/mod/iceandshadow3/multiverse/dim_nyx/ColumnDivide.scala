package mod.iceandshadow3.multiverse.dim_nyx

class ColumnDivide extends BColumn {
	override def bedrock() = WorldGenNyx.bedrock
	override def apply(i: Int) = blockDefault(i)
}
