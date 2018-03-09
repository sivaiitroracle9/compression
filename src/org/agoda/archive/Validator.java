package org.agoda.archive;

import java.io.File;

public class Validator {

	public static void compression(String source, String destination,
			int size) throws IllegalAccessException {

		if (source == null) {
			throw new IllegalArgumentException("Invalid source path " + source);
		}
		File sourceFile = new File(source);
		// read access for all files in the directory path.
		if (!sourceFile.canRead()) {
			throw new IllegalAccessException("Cannot read " + source);
		}

		if (sourceFile.isDirectory()) {
			File[] files = sourceFile.listFiles();
			if (files.length == 0) {
				throw new IllegalArgumentException("Empty directory ... ");
			}
		}
		// source size exceptions.
		// TODO
		if (destination == null) {
			throw new IllegalArgumentException("Invalid destination path "
					+ destination);
		}

		File destFile = new File(destination);
		if (!destFile.isDirectory()) {
			throw new IllegalArgumentException(destination
					+ " is not a directory");
		}
		if (!destFile.canWrite()) {
			throw new IllegalAccessException("Cannot write to " + destination);
		}

		// destination size exceptions
		// TODO
	}

	public static void decompression() throws IllegalAccessException {
		// TODO
	}
}
