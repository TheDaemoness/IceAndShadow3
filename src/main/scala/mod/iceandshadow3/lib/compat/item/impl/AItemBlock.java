package mod.iceandshadow3.lib.compat.item.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.base.LogicPair;
import mod.iceandshadow3.lib.base.LogicProvider;
import mod.iceandshadow3.lib.compat.block.impl.ABlock;
import net.minecraft.item.BlockItem;

import javax.annotation.Nullable;

public class AItemBlock extends BlockItem implements LogicProvider.Block {
	private final BLogicBlock logic;
	private final int variant;
	public AItemBlock(BLogicBlock logic, int variant, ABlock ab) {
		super(ab, logic.toItemProperties(variant));
		this.logic = logic;
		this.variant = variant;
	}

	@Nullable
	@Override
	public LogicPair<BLogicBlock> getLogicPair() {
		return LogicPair.apply(logic, variant);
	}
}
