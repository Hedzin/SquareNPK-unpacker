import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.EndianUtils;


public class RPGMakerPacker {
	static ByteArrayOutputStream result = new ByteArrayOutputStream();
	static Path folderWithFiles2Pack;
	static Path outputPath;
		
	public static void setOutputPath(String path) {
		outputPath = Paths.get(path);
	}
	
	public static void setFolderWithFiles2Pack(String path) {
		folderWithFiles2Pack = Paths.get(path);
	}
	
	
 	public static void main(String[] args) throws IOException, Exception {
 		
 		setFolderWithFiles2Pack("C:\\Hack\\TEST");
 		setOutputPath("C:\\HACK\\rpg_packed");
 		String fileName = "P_TIM_TT.LPK";
 		
 		Path TIM_TT =  outputPath.resolve(fileName);
 		createLPK(TIM_TT);
	}
 	
 	private static void createLPK(Path filename) throws IOException {
  		List<File> fileList = Arrays.asList(folderWithFiles2Pack.toFile().listFiles()); 
 		int filecount = fileList.size();
 		System.out.println(filecount);
 		
		
		int offset = filecount*8;
 		for (File file : fileList) {
 			int fileL = Math.toIntExact(file.length());
 			EndianUtils.writeSwappedInteger(result, offset);
 			EndianUtils.writeSwappedInteger(result, fileL);
 			offset +=  fileL;
		}
 		for (File file : fileList) {
 			FileInputStream fis = null;
 			fis = new FileInputStream(file);
 			result.write(fis.readAllBytes());
 			fis.close();
		}
 		
 		dumpFile(result, filename);
 		

	}
 	
	private static void dumpFile(ByteArrayOutputStream byteArrayStream, Path filename) {
		try {
			File file = new File(filename.toString());
			file.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(file); 
			byteArrayStream.writeTo(fos);
			byteArrayStream.flush();
			fos.flush();
			byteArrayStream.close();
			fos.close();
			byteArrayStream.reset();
			//result = new ByteArrayOutputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}
