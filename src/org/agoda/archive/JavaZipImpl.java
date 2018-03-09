package org.agoda.archive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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

		try {

			// create output directory is not exists
			File folder = new File(destination);
			if (!folder.exists()) {
				folder.mkdir();
			}

			Map<String, Integer> numberFiles = new HashMap<String, Integer>();
			Map<String, SortedSet<String>> zipEntryFiles = new HashMap<String, SortedSet<String>>();
			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(source));

			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			int maxSize = 0;
			while (ze != null) {
				if (!ze.isDirectory()) {
					String fName = ze.getName();
					String actualName = fName.substring(0,
							fName.lastIndexOf("#"));
					if (!zipEntryFiles.containsKey(actualName))
						zipEntryFiles.put(actualName, new TreeSet<String>());
					zipEntryFiles.get(actualName).add(fName);
					int x = Integer.parseInt(fName.substring(fName
							.lastIndexOf("#")));
					int n = Math.max(numberFiles.get(fName), x + 1);
					numberFiles.put(fName, n);
					maxSize = Math.max(maxSize,
							(int) (new File(fName)).length());
				}
				ze = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();

			byte[] buffer = new byte[maxSize];
			zis = new ZipInputStream(new FileInputStream(source));
			ze = zis.getNextEntry();
			String previous = "";
			while (ze != null) {
				if (ze.isDirectory()) {
					ze = zis.getNextEntry();
					continue;
				}

				String fileName = ze.getName();
				String actualName = fileName.substring(
						fileName.lastIndexOf(File.separator),
						fileName.lastIndexOf("#"));

				File newFile = new File(destination + File.separator
						+ actualName);
				System.out.println("file unzip : " + newFile.getAbsoluteFile());
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);
				while (ze != null
						&& !previous.equals(ze.getName().substring(
								fileName.lastIndexOf(File.separator),
								fileName.lastIndexOf("#")))) {
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.flush();
					ze = zis.getNextEntry();
				}
				fos.close();
				previous = actualName;
			}

			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
