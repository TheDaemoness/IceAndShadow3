package mod.iceandshadow3.multiverse.polis

import mod.iceandshadow3.lib.LogicBlock
import mod.iceandshadow3.multiverse.DomainPolis
import mod.iceandshadow3.multiverse.gaia.ELivingstoneType

class LBStone(variant: ELivingstoneType)
extends LogicBlock(DomainPolis, "stone_"+variant.name, Materias.stone)
