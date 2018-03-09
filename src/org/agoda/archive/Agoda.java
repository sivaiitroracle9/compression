package org.agoda.archive;

public class Agoda {

	public static void main(String[] args) {
		AComp comp = CompressorFactory
				.getCompressor(CompressionAlgorithm.JAVAZIP);
		// comp.compress("C:\\Users\\edupuganti_s-pc\\Desktop\\test-dir", "C:\\Users\\edupuganti_s-pc\\Desktop\\zip-test", 1048576);
		comp.decompress("C:\\Users\\edupuganti_s-pc\\Desktop\\zip-test\\test-dir.zip", "C:\\Users\\edupuganti_s-pc\\Desktop\\out-test-dir");
/*		if (args.length < 3 || args.length > 4) {
			help();
		}

		AComp comp = CompressorFactory
				.getCompressor(CompressionAlgorithm.JAVAZIP);
		
		String command = args[0];
		if (command.toLowerCase().equals("compress") && args.length == 3) {
			comp.compress(args[1], args[2], Integer.parseInt(args[3]));
		} else if (command.toLowerCase().equals("decompress")
				&& args.length == 2) {
			comp.decompress(args[1], args[2]);
		} else if (command.toLowerCase().equals("help")) {
			help();
		} else {
			System.out.println("ERROR :: Invalid command ...........");
			help();
		}*/
	}

	private static void help() {

	}
}
