package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.lib.compat.Registrar
import mod.iceandshadow3.lib.compat.item.WItemStack
import mod.iceandshadow3.lib.compat.recipe.ECraftingType
import mod.iceandshadow3.multiverse.DomainGaia

class LIShale(variant: ELivingstoneTypes)
extends LogicItemMulti(DomainGaia, "livingshale_"+variant.name, variant.rarity) {
	Registrar.addRecipeCallback(s"smelting.$name", name => {
		ECraftingType.COOK_SMELT(name,
			ECraftingType.About(WItemStack.make(s"minecraft:${variant.name}_dye")),
			this.toWItemType
		)
	})
}
