package mod.iceandshadow3.lib.spatial;

import net.minecraft.util.Direction;

public enum EFacing {
	DOWN(EAxis.DOWN_UP, false, Direction.DOWN),
	UP(EAxis.DOWN_UP, true, Direction.UP),
	WEST(EAxis.WEST_EAST, false, Direction.WEST),
	EAST(EAxis.WEST_EAST, true, Direction.EAST),
	NORTH(EAxis.NORTH_SOUTH, false, Direction.NORTH),
	SOUTH(EAxis.NORTH_SOUTH, true, Direction.SOUTH);

	public final EAxis axis;
	public final boolean positive;
	private final Direction vanilla;

	EFacing(EAxis axis, boolean positive, Direction direction) {
		this.axis = axis;
		this.positive = positive;
		this.vanilla = direction;
	}

	public static EFacing fromVanilla(Direction facing) {
		switch(facing) {
			case DOWN: return DOWN;
			case UP: return UP;
			case WEST: return WEST;
			case EAST: return EAST;
			case NORTH: return NORTH;
			case SOUTH: return SOUTH;
			default: return null;
		}
	}
	public Direction toVanilla() {
		return vanilla;
	}

	public EFacing mirrored() {
		return axis.facing(!positive);
	}

	/** Return the result of rotating this direction COUNTER-CLOCKWISE. */
	public EFacing rotated(EAxis axis) {
		final EAxis newaxis = this.axis.rotated(axis);
		return newaxis.facing(positive ^ this.axis.shouldInvert(newaxis));
	}
	/** Return the result of rotating this direction COUNTER-CLOCKWISE. */
	public EFacing rotated(EFacing vec) {
		final EFacing rotated = this.rotated(vec.axis);
		return vec.positive ? rotated : rotated.mirrored();
	}
	/** Return the result of rotating this direction CLOCKWISE. */
	public EFacing rotatedCw(EFacing vec) {
		final EFacing rotated = this.rotated(vec.axis);
		return vec.positive ? rotated.mirrored() : rotated;
	}
	/** Return the result of rotating this direction CLOCKWISE. */
	public EFacing rotatedCw(EAxis axis) {
		return rotated(axis).mirrored();
	}
}
