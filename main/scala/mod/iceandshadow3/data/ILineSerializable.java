package mod.iceandshadow3.data;

public interface ILineSerializable {
	public void fromLine(String line) throws IllegalArgumentException;
	public String toLine();
}
