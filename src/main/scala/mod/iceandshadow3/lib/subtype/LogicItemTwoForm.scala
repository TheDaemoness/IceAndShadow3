package mod.iceandshadow3.lib.subtype

import mod.iceandshadow3.lib.{BDomain, LogicItemMulti}

class LogicItemTwoForm(domain: BDomain, name: String, tier: Int, altform: String)
extends LogicItemMulti(domain, name, (null, tier), (altform, tier))

