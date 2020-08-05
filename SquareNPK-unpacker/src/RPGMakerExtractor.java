import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.EndianUtils;


public class RPGMakerExtractor {
	static ByteArrayOutputStream result = new ByteArrayOutputStream();
	static ByteArrayOutputStream secret = new ByteArrayOutputStream();

	static int bufferL = 0;
	static int OUTPOS = 0;
	static List<Integer> offsetlist = new ArrayList<Integer>();
	static int filenum = 0;
	
	static Path diskPath;
	static Path outputPath;
	
	

	public static void setOutputPath(String path) {
		outputPath = Paths.get(path);
	}
	
	public static void setCDPath(String path) {
		diskPath = Paths.get(path);
	}
	
	
 	public static void main(String[] args){
 		
 		setCDPath("C:\\Hack\\rpg_packed");
 		setOutputPath("C:\\HACK\\rpg_Extracted");
 		Path TIM_TT =  diskPath.resolve("P_TIM_TT.LPK");
 		processSmallNPK(TIM_TT);
 		


	}
 	
 	
	
 	private static void processSmallNPK(Path path) {
		FileInputStream fis = null;
		ByteBuffer fileBuffer = null;
		int fileCount = 0;

		try {
			LZSDecompressor decompressor = new LZSDecompressor(/*4096*/);
			fis = new FileInputStream(path.toFile());
			fileBuffer = ByteBuffer.wrap(fis.readAllBytes());
			
			
			List<Entry<Integer,Integer>> pairList = new ArrayList<Entry<Integer,Integer>>();
			int firstByte = EndianUtils.swapInteger(fileBuffer.getInt());
			int firstblockCount = firstByte/8;
			System.out.println("firstblockCount: "+firstblockCount);
			fileBuffer.position(0);
			
			for (int i = 0; i < firstblockCount; i++) {
				pairList.add(new AbstractMap.SimpleEntry<Integer, Integer>(EndianUtils.swapInteger(fileBuffer.getInt()), EndianUtils.swapInteger(fileBuffer.getInt())));

			}
			for (int i = 0; i < firstblockCount; i++) {

				fileBuffer.limit(pairList.get(i).getKey()+pairList.get(i).getValue());
				System.out.println("Buffer.capacity: "+fileBuffer.capacity());
				System.out.println("Buffer.limit: "+fileBuffer.limit());
				System.out.println("Buffer.position: "+fileBuffer.position());
				System.out.println("start: "+pairList.get(i).getKey()+" leng: "+pairList.get(i).getValue());
				while (fileBuffer.position()<pairList.get(i).getKey()+pairList.get(i).getValue()) {
					result.write(decompressor.decompressLZSBlock(fileBuffer).array());
				}
				fileBuffer.limit(fileBuffer.capacity());
				dumpFile(result, path.getFileName().toString(),fileCount);
				fileCount++;
				decompressor.reset();
			}

			fileBuffer.limit(fileBuffer.capacity());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
			System.out.println("fileBuffer.position(): 0x"+Integer.toHexString(fileBuffer.position()));
		}
	}
 	
	private static void dumpFile(ByteArrayOutputStream byteArrayStream, String folderName, int fileCount, String fileName, String extension) {
		try {
			File file = new File(outputPath.resolve(folderName).toFile(), fileName+"_"+String.format("%03d", fileCount)+"."+extension);
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
	
	private static void dumpFile(ByteArrayOutputStream byteArrayStream, String folderName, int fileCount) {
		dumpFile(byteArrayStream, folderName, fileCount, "picture", "tim");
	}

	

}
