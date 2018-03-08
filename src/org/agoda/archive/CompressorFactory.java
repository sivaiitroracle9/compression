package org.agoda.archive;


public class CompressorFactory {

	private static final CompressorFactory instance = new CompressorFactory();
	
	public CompressorFactory getFactory() {
		return instance;
	}
	
	public static AComp getCompressor(CompressionAlgorithm algorithm) {
		
		if(CompressionAlgorithm.JAVAZIP == algorithm) {
			return new JavaZipImpl();
		} else {
			throw new IllegalArgumentException("Invalid compression Algorithm.");
		}
		
	}
}
