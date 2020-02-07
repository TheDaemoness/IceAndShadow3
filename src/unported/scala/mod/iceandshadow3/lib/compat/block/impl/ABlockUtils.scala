package mod.iceandshadow3.lib.compat.block.impl

import java.util.Random
import java.util.function.Consumer

import javax.annotation.Nonnull
import mod.iceandshadow3.lib.BLogicBlock
import mod.iceandshadow3.lib.compat.block.WBlockRef
import mod.iceandshadow3.lib.compat.loot.{LootBuilder, WLootContextBlock}
import mod.iceandshadow3.lib.util.E3vl
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.{IBlockReader, World}
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

	def canHarvestBlock(
		logic: BLogicBlock, state: BlockState, world: IBlockReader, pos: BlockPos, player: PlayerEntity
	): E3vl = {
		if (logic.materia.harvestLevel < 0) E3vl.TRUE
		else E3vl.NEUTRAL
	}
}
