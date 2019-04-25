package mod.iceandshadow3.util;

public class SMath {
	public static int bound(int min, int what, int max) {
		return Math.min(max, Math.max(min, what));
	}
	public static long bound(long min, long what, long max) {
		return Math.min(max, Math.max(min, what));
	}
}
