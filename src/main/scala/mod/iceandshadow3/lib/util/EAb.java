package mod.iceandshadow3.lib.util;

public enum EAb {
	A(true, false) {
		@Override
		public <T> T remapTo(T a, T b, T ab) {
			return a;
		}
	},
	B(false, true) {
		@Override
		public <T> T remapTo(T a, T b, T ab) {
			return b;
		}
	},
	AB(true, true) {
		@Override
		public <T> T remapTo(T a, T b, T ab) {
			return ab;
		}
	};

	public final boolean a;
	public final boolean b;
	public final boolean ab;

	EAb(boolean a, boolean b) {
		this.a = a;
		this.b = b;
		this.ab = a && b;
	}

	public abstract <T> T remapTo(T a, T b, T ab);
}
