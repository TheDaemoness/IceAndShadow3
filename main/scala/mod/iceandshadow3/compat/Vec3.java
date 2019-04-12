package mod.iceandshadow3.compat;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

/**
 * An immutable fixed-precision 3d vector class.
 */
public class Vec3 implements Cloneable, Comparable<Vec3> {
	protected static final long SUB_BITS = 16;
	protected static final long CHUNK_BITS = 4;
	
	protected static final long SUB_MULT = 1 << SUB_BITS;
	protected static final long CHUNK_MULT = 1 << CHUNK_BITS;
	protected static final long SUB_MASK = SUB_MULT-1;
	protected static final long CHUNK_MASK = CHUNK_MULT-1;
	protected static long toLong(double value) {
		long whole = (long)value;
		return (whole << SUB_BITS) + (long)((value - whole) * SUB_MULT);
	}
	protected static long toBlockCoord(long value) {
		return value >> SUB_BITS;
	}
	protected static int toSubCoord(long value) {
		return (int)(value & SUB_MASK);
	}
	protected static double toDouble(long value) {
		return toBlockCoord(value) + ((double)toSubCoord(value))/SUB_MULT;
	}
	
	// WARNING: Do not make these final. However, also do not modify them.
	// For in-place modifying operations, see Vec3M.
	protected long x, z;
	protected int y;
	
	/// Unit vectors.
	public static final Vec3
		U = new Vec3(0, 1, 0),
		D = new Vec3(0, -1, 0),
		S = new Vec3(0, 0, 1),
		N = new Vec3(0, 0, -1),
		E = new Vec3(1, 0, 0),
		W = new Vec3(-1, 0, 0);
		
	public long xBlock() {return toBlockCoord(x);}
	public int yBlock() {return (int)toBlockCoord(y);}
	public long zBlock() {return toBlockCoord(z);}
	public int xChunk() {return (int)(toBlockCoord(x) >> CHUNK_BITS);}
	public int zChunk() {return (int)(toBlockCoord(z) >> CHUNK_BITS);}
	public byte xSubChunk() {return (byte)(toBlockCoord(x) & CHUNK_MASK);}
	public byte zSubChunk() {return (byte)(toBlockCoord(z) & CHUNK_MASK);}
	public int xSubBlock() {return toSubCoord(x);}
	public int ySubBlock() {return toSubCoord(y);}
	public int zSubBlock() {return toSubCoord(z);}
	public double xDouble() {return toDouble(x);}
	public double yDouble() {return toDouble(y);}
	public double zDouble() {return toDouble(z);}
	
	public Vec3(int x, int y, int z) {
		this(x, y, z, true);
	}
	protected Vec3(long x, int y, long z, boolean needsshift) {
		long shift = needsshift ? SUB_BITS : 0;
		this.x = x << shift;
		this.y = y << shift;
		this.z = z << shift;
	}
	/// Returns a mutable copy of this vector.
	Vec3M copy() {
		return new Vec3M(this);
	}
	
	public double mag() {
		return Math.sqrt(x*x + (long)y*y + z*z);
	}
	public double magH() {
		return Math.sqrt(x*x + z*z);
	}
	public double dot(Vec3 b) {
		return toDouble(x*b.x + (long)y*b.y + z*b.z);
	}
	
	public BlockPos toBlockPos() {
		return new BlockPos(xBlock(), yBlock(), zBlock());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Vec3) {
			Vec3 b = (Vec3)o;
			return y==b.y && x==b.x && z==b.z;
		}
		return false;
	}
	@Override
	public String toString() {
		return "<"+xDouble()+','+yDouble()+','+zDouble()+'>';
	}
	@Override
	public int compareTo(Vec3 b) {
		return Double.compare(mag(), b.mag());
	}
	
}
