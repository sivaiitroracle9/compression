package org.agoda.archive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAComp implements AComp {

	private List<File> sourceFiles;
	
	
	public AbstractAComp() {

	}
	
	protected boolean canRead(File f) {
		return f.canRead();
	}
	
	protected boolean canWrite(File f) {
		return f.canWrite();
	}

	protected boolean isDirectory(File file) {
		if (file == null) {
			throw new IllegalArgumentException("null file.");
		}
		return file.isDirectory();
	}

	protected List<File> getSourceFiles(File file) {
		if (file == null) {
			throw new IllegalArgumentException("null file.");
		}
		sourceFiles = new ArrayList<File>();
		if (file.isFile())
			sourceFiles.add(file);
		else
			listFiles(sourceFiles, file);

		return sourceFiles;
	}

	private void listFiles(List<File> source, File file) {
		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile())
				source.add(f);
			else
				listFiles(source, f);
		}
	}
}
