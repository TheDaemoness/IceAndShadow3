package mod.iceandshadow3.multiverse.gaia

import mod.iceandshadow3.lib.LogicItemMulti
import mod.iceandshadow3.multiverse.DomainGaia

class LIShale extends LogicItemMulti(
	DomainGaia,
	"livingshale",
	ELivingstoneTypes.values().map(t => (t.name, 1)).toIndexedSeq:_*
) {

}
