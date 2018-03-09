package org.agoda.archive;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class JavaZipImpl extends AbstractAComp {

	@Override
	public void compress(String source, String destination, int maxFileSize) {

		try {
			Validator.compression(source, destination, maxFileSize);

			File input = new File(source);
			List<File> iFiles = getSourceFiles(input);

			String zipname = source.substring(source
					.lastIndexOf(File.separator));

			File df = new File(destination + File.separator + zipname + ".zip");

			FileOutputStream fos = new FileOutputStream(df);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (File f : iFiles) {
				System.out.println("Zipping " + f.getAbsolutePath());
				String fName = f.getName();
				FileInputStream fis = new FileInputStream(f);
				byte[] buffer = new byte[maxFileSize];
				int len, i = 0;
				while ((len = fis.read(buffer)) > 0) {
					ZipEntry ze = new ZipEntry(f.getAbsolutePath().substring(
							source.length() + 1)
							+ "#" + i);
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

			File sourceFile = new File(source);
			String zipPath = source.substring(0, source.length() - 4);
			File temp = new File(zipPath);
			temp.mkdir();
			ZipFile zipFile = new ZipFile(sourceFile);

			Enumeration<? extends ZipEntry> e = zipFile.entries();
			Comparator<ZipEntry> zipEntryComparator = new Comparator<ZipEntry>() {

				@Override
				public int compare(ZipEntry o1, ZipEntry o2) {
					int i1 = Integer.parseInt(o1.getName().substring(o1.getName().lastIndexOf("#")) + 1);
					int i2 = Integer.parseInt(o2.getName().substring(o1.getName().lastIndexOf("#")) + 1);
					return i1 - i2;
				}
			};
			Map<String, TreeSet<ZipEntry>> entries = new HashMap<String, TreeSet<ZipEntry>>();
			while (e.hasMoreElements()) {
				ZipEntry entry = e.nextElement();

				if (entry.isDirectory()) {
					continue;
				}
				else {
					System.out.println("Extracting file: " + entry.getName());
					String decompFile = getRealativePath(entry.getName(), "", true);
					if(!entries.containsKey(decompFile)) {
						entries.put(decompFile, new TreeSet<ZipEntry>(zipEntryComparator));
					}
					entries.get(decompFile).add(entry);
				}
			}
			
			for (String ename : entries.keySet()) {
				File destinationPath = new File(zipPath, ename);
				
				// create parent directories
				destinationPath.getParentFile().mkdirs();
				
				System.out.println("Merging related split files to " + destinationPath);

				FileOutputStream fos = new FileOutputStream(destinationPath);
				for(ZipEntry entry : entries.get(ename)) {
					BufferedInputStream bis = new BufferedInputStream(
							zipFile.getInputStream(entry));	
					int b;
					byte buffer[] = new byte[1024];
					BufferedOutputStream bos = new BufferedOutputStream(fos,
							1024);
					while ((b = bis.read(buffer, 0, 1024)) != -1) {
						bos.write(buffer, 0, b);
						bos.flush();
					}

					bos.close();
					bis.close();
				}
			}

			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private String getRealativePath(String fullPath, String soruceDir,
			boolean skipPathExtention) {
		if (!skipPathExtention)
			return fullPath.substring(soruceDir.length() + 1);
		return fullPath.substring(soruceDir.length() + 1,
				fullPath.lastIndexOf('#'));
	}

}
