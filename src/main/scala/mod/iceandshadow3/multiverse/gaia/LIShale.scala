package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.multiverse.DomainGaia

class LIShale(variant: ELivingstoneTypes)
extends LogicItemMulti(DomainGaia, "livingshale_"+variant.name, variant.rarity)
