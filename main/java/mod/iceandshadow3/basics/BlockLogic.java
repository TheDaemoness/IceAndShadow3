package mod.iceandshadow3.basics;

import java.util.List;

import mod.iceandshadow3.compat.BMateria;
import mod.iceandshadow3.compat.CRefItem;
import mod.iceandshadow3.compat.HarvestMethod;

public class BlockLogic {
	public interface IProvider {
		public BlockLogic getBlockLogic();
	}
	protected final BMateria mat;
	public BlockLogic(BMateria m) {
		mat = m;
	}
	
	public BMateria getMateria() {return mat;}
}
