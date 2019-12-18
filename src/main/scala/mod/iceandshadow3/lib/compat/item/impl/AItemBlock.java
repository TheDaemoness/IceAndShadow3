package mod.iceandshadow3.lib.compat.item.impl;

import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.LogicBlock;
import mod.iceandshadow3.lib.base.LogicProvider;
import mod.iceandshadow3.lib.compat.LogicToProperties$;
import mod.iceandshadow3.lib.compat.block.impl.ABlock;
import net.minecraft.item.BlockItem;

import javax.annotation.Nullable;

public class AItemBlock extends BlockItem implements LogicProvider.Block {
	private final LogicBlock logic;
	public AItemBlock(LogicBlock logic, ABlock ab) {
		super(ab, LogicToProperties$.MODULE$.toPropertiesPartial(logic));
		this.logic = logic;
	}

	@Nullable
	@Override
	public BLogicBlock getLogic() {
		return logic;
	}
}
