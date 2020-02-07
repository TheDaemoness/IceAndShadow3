package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.multiverse.DomainGaia

class LICortra(dust: Boolean) extends LogicItemMulti(DomainGaia, if(dust) "cortra_dust" else "cortra", 2)