package mod.iceandshadow3.multiverse.dim_nyx

import mod.iceandshadow3.lib.compat.misc.ResourceMap

object UnusualFreezeMap {
	private val unusualFreezeMap = new ResourceMap[String]
	def add(id: String, to: String): Unit = {
		//TODO: Check that to is valid.
		unusualFreezeMap += Tuple2(id, to)
	}

	//TODO: Allow specifying these in recipes.
	add("minecraft:blaze_powder", "minecraft:air")
	add("minecraft:fire_charge", "minecraft:air")
	add("minecraft:magma_cream", "minecraft:air")
	add("minecraft:magma_block", "minecraft:netherrack")
	add("minecraft:lava_bucket", "minecraft:obsidian")
	add("minecraft:water_bucket", "minecraft:ice")
	add("minecraft:cod_bucket", "minecraft:ice")
	add("minecraft:salmon_bucket", "minecraft:ice")
	add("minecraft:pufferfish_bucket", "minecraft:ice")
	add("minecraft:tropical_fish_bucket", "minecraft:ice")
	add("minecraft:sticky_piston", "minecraft:piston")

	def apply(what: String) = unusualFreezeMap.get(what)
}
