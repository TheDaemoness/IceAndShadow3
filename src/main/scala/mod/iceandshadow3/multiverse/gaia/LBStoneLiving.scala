package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.BLogicBlockSimple
import mod.iceandshadow3.multiverse.DomainGaia

class LBStoneLiving extends BLogicBlockSimple(DomainGaia, "livingstone", MatStone) {
	override protected def getVariantName(variant: Int) = ELivingstoneTypes.values()(variant).name
	override def countVariants = ELivingstoneTypes.values().length
}
