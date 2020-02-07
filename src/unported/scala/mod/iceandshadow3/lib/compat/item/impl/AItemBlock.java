package mod.iceandshadow3.lib.compat.item.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.LogicBlock;
import mod.iceandshadow3.lib.base.ProviderLogic;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.id.WId;
import mod.iceandshadow3.lib.compat.block.impl.IABlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AItemBlock<BlockType extends Block & IABlock> extends BlockItem implements ProviderLogic.Block {
	private final LogicBlock logic;
	public AItemBlock(LogicBlock logic, BlockType ab) {
		super(ab, LogicToProperties$.MODULE$.toPropertiesPartial(logic));
		this.logic = logic;
	}

	@Nullable
	@Override
	public BLogicBlock getLogic() {
		return logic;
	}

	@Nonnull
	@Override
	public WId id() {
		return logic.id();
	}
}
