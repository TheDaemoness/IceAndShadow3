package mod.iceandshadow3.data;

public interface ITextLineRW {
	void fromLine(String line) throws IllegalArgumentException;
	String toLine();
}
