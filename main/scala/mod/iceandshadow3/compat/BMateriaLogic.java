package mod.iceandshadow3.compat;

import mod.iceandshadow3.BDomain;
import mod.iceandshadow3.basics.IMateria;

public abstract class BMateriaLogic extends BLogic {
	final BMateria materia;
	protected BMateriaLogic(BDomain b, String name, BMateria mat) {
		super(b, name);
		materia = mat;
	}
	
	protected IMateria getMateria() {return materia;}
	public boolean isOfMateria(IMateria b) {return b == materia;}
}
