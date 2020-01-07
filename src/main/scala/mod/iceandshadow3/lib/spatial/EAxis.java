package mod.iceandshadow3.lib.spatial;

//NOTE: There are general algorithms for most of the things that are hard-coded with if/else statements.
//They're not worth it in this narrow implementation.

public enum EAxis {
	DOWN_UP( "y") {
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
	WEST_EAST("x") {
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
	NORTH_SOUTH("z") {
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
	EAxis(String name) {
		this.name = name;
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
