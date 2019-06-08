package mod.iceandshadow3.compat.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.util.EnumFacing;

import static net.minecraft.block.state.BlockFaceShape.*;

public enum ESideType {
	OTHER(UNDEFINED),
	THIN(CENTER_SMALL, MIDDLE_POLE_THIN),
	MEDIUM(CENTER, MIDDLE_POLE),
	THICK(CENTER_BIG, MIDDLE_POLE_THICK),
	CURVED(BOWL),
	FULL(SOLID);

	private final BlockFaceShape nonpole, pole;
	ESideType(BlockFaceShape nonpole, BlockFaceShape pole) {
		this.nonpole = nonpole;
		this.pole = pole;
	}
	ESideType(BlockFaceShape nonpole) {
		this(nonpole, nonpole);
	}

	public BlockFaceShape expose(EnumFacing face, boolean isPole) {
		return (isPole && face != EnumFacing.DOWN && face != EnumFacing.UP) ? pole : nonpole;
	}
}
