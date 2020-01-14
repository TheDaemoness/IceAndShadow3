package mod.iceandshadow3.lib.spatial;

//NOTE: There are general algorithms for most of the things that are hard-coded with if/else statements.
//They're not worth it in this narrow implementation.

import net.minecraft.util.Direction;

public enum EAxis {
	DOWN_UP( "y", Direction.Axis.Y) {
		@Override
		protected EAxis getRotated(EAxis b) {
			return (b == EAxis.WEST_EAST) ? NORTH_SOUTH : WEST_EAST;
		}
		@Override
		boolean shouldInvert(EAxis to) {
			return to == WEST_EAST;
		}
		@Override
		public EFacing facing(boolean positive) {
			return positive ? EFacing.UP : EFacing.DOWN;
		}
	},
	WEST_EAST("x", Direction.Axis.X) {
		@Override
		protected EAxis getRotated(EAxis b) {
			return (b == EAxis.DOWN_UP) ? NORTH_SOUTH : DOWN_UP;
		}
		@Override
		boolean shouldInvert(EAxis to) {
			return to == NORTH_SOUTH;
		}
		@Override
		public EFacing facing(boolean positive) {
			return positive ? EFacing.EAST : EFacing.WEST;
		}
	},
	NORTH_SOUTH("z", Direction.Axis.Z) {
		@Override
		protected EAxis getRotated(EAxis b) {
			return (b == EAxis.DOWN_UP) ? WEST_EAST : DOWN_UP;
		}
		@Override
		boolean shouldInvert(EAxis to) {
			return to == DOWN_UP;
		}
		@Override
		public EFacing facing(boolean positive) {
			return positive ? EFacing.SOUTH : EFacing.NORTH;
		}
	};

	private final String name;
	private final Direction.Axis vanilla;
	EAxis(String name, Direction.Axis axis) {
		this.name = name;
		this.vanilla = axis;
	}

	public static EAxis fromVanilla(Direction.Axis axis) {
		switch(axis) {
			case X: return WEST_EAST;
			case Y: return DOWN_UP;
			case Z: return NORTH_SOUTH;
			default: return null;
		}
	}
	public Direction.Axis toVanilla() {
		return vanilla;
	}

	@Override
	public String toString() {
		return name;
	}

	/** Return true if the "positive" flag on EFacing should be flipped
	 * when rotating counterclockwise to the given around an axis-aligned vector pointing toward posinf.
	 * Return false if this == to.
	 */
	abstract boolean shouldInvert(EAxis to);
	protected abstract EAxis getRotated(EAxis b);
	public EAxis rotated(EAxis b) {
		if(b == this) return this;
		else return getRotated(b);
	}
	public abstract EFacing facing(boolean positive);
}
