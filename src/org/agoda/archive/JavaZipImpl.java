package org.agoda.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JavaZipImpl extends AbstractAComp {

	public JavaZipImpl() {
		
	}

	@Override
	public void compress(String source, String destination, int maxFileSize) {


		try {
			Validator.compressionValidation(source, destination, maxFileSize);
			
			File input = new File(source);
			List<File> iFiles = getSourceFiles(input);
			
			FileOutputStream fos = new FileOutputStream(destination);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (File f : iFiles) {
				System.out.println("Zipping " + f.getAbsolutePath());

				String fName = f.getName();
				FileInputStream fis = new FileInputStream(f);
				byte[] buffer = new byte[maxFileSize];
				int len, i = 0;
				while ((len = fis.read(buffer)) > 0) {
					ZipEntry ze = new ZipEntry(fName + "#" + i);
					zos.putNextEntry(ze);

					zos.write(buffer, 0, len);

					zos.closeEntry();
					i++;
				}

				fis.close();
			}
			zos.close();
			fos.close();
		} catch (IOException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void decompress(String source, String destination) {

	}

}
