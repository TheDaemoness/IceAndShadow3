package mod.iceandshadow3.compat;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class Vec3M extends Vec3 {

	public Vec3M(int x, int y, int z) {
		super(x, y, z);
	}
	public Vec3M(Vec3 v) {
		super(v.x, v.y, v.z, false);
	}
	public Vec3M(BlockPos p) {
		super(p.getX(), p.getY(), p.getZ(), true);
	}
	public Vec3M(Entity e) {
		super(toLong(e.posX), (int)toLong(e.posY), toLong(e.posZ), false);
	}
	
	public Vec3M add(int x, int y, int z) {
		x += x << SUB_BITS;
		y += ((short)y) << SUB_BITS;
		z += z << SUB_BITS;
		return this;
	}
	public Vec3M add(double x, double y, double z) {
		x += toLong(x);
		y += (int)toLong(y);
		z += toLong(z);
		return this;
	}
	public Vec3M add(Vec3 b) {
		x += b.x;
		y += b.y;
		z += b.z;
		return this;
	}
	public Vec3M sub(Vec3 b) {
		x -= b.x;
		y -= b.y;
		z -= b.z;
		return this;
	}
	public Vec3M mult(double multiplier) {
		x *= multiplier;
		y *= multiplier;
		z *= multiplier;
		return this;
	}
	/// Converts the vector to its normal equivalent (magnitude = 1).
	public Vec3M norm() {
		return mult(1/mag());
	}
	public Vec3M inv() {
		return mult(-1);
	}
}
