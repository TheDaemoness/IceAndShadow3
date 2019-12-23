package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.file.BJsonAssetGen
import mod.iceandshadow3.lib.compat.recipe.ECraftingType
import mod.iceandshadow3.multiverse.{DomainGaia, DomainPolis}
import mod.iceandshadow3.multiverse.gaia.ELivingstoneTypes


class LBStone(variant: ELivingstoneTypes)
extends LogicBlock(DomainPolis, "stone_"+variant.name, Materias.stone) {
	override def getBlockModelGen = Some(BJsonAssetGen.blockCube)
}
