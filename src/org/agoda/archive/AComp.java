package org.agoda.archive;

public interface AComp {

	public void compress(String source, String destination, int maxFileSize);

	public void decompress(String source, String destination);

}
