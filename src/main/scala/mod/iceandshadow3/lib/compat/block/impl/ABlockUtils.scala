package mod.iceandshadow3.lib.compat.block.impl

import java.util.Random
import java.util.function.Consumer

import javax.annotation.Nonnull
import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.loot.{LootBuilder, WLootContextBlock}
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.storage.loot.{LootContext, LootParameterSets, LootParameters}
import net.minecraftforge.api.distmarker.{Dist, OnlyIn}

private[impl] object ABlockUtils {
	@Nonnull def getDrops(logic: BLogicBlock, state: BlockState, cb: LootContext.Builder) = {
		val lb = new LootBuilder[WLootContextBlock](
			new WLootContextBlock(cb.withParameter(LootParameters.BLOCK_STATE, state).build(LootParameterSets.BLOCK))
		)
		logic.addDrops(lb)
		lb.results
	}

	def animateTick(
		logic: BLogicBlock, stateIn: BlockState, worldIn: World, pos: BlockPos, rand: Random
	): Unit = {
		val handler = logic.handlerClientTick
		if (handler != null) handler.accept(new WBlockRef(worldIn, pos, stateIn) {
			override def rng() = rand
		})
	}
}
