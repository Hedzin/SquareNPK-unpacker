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


public class FFVIIExtractor {
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
	
	
 	public static void main(String[] args) throws IOException, Exception {
 		
 		setCDPath("D:\\HACK\\cd4");
 		setOutputPath("D:\\HACK\\cd4_Extracted");

 		
 		//standart NPK
 		Path NPK_WEP =  diskPath.resolve("NPK_WEP.NPK");
 		Path NPK_MON =  diskPath.resolve("NPK_MON.NPK");
 		Path NPK_ETC =  diskPath.resolve("NPK_ETC.NPK");
 		Path NPK_BOU =  diskPath.resolve("NPK_BOU.NPK");
 		Path NPK_ACC =  diskPath.resolve("NPK_ACC.NPK");
 		Path MAKINGCG_NPK =  diskPath.resolve("MAKINGCG\\MAKINGCG.NPK");
 		Path FF_TTOWN_NPK =  diskPath.resolve("TOWN\\FF_TTOWN.NPK"); //some short some int ????
 		
 		

 		Path NPK_DF2 =  diskPath.resolve("NPK_DF2.NPK");

 	
 		
 		
 		// FFD
 		Path CG_FFD =  diskPath.resolve("CG.FFD");
 		// DUMMY3M.DA
 		Path DUMMY3M_DA =  diskPath.resolve("DUMMY3M.DA"); ///?????
 		
 		// FFD MATER
 		Path MATER_FFD =  diskPath.resolve("ITEM\\MATER\\MATER.FFD");
 		
 		Path HERO_FFD =  diskPath.resolve("HERO\\HERO.FFD");//ok

 		Path SM000_FFD =  diskPath.resolve("SM\\SM000.FFD");//ok
 		Path SM001_FFD =  diskPath.resolve("SM\\SM001.FFD");//ok

 		
 		
 		// small NPK
 		Path NPK_WM =  diskPath.resolve("NPK_WM.NPK"); 
 		Path RIDE_0_NPK =  diskPath.resolve("R3D\\RIDE_0.NPK");////?????
 		Path NPK_DF =  diskPath.resolve("NPK_DF.NPK");
 		Path NPK_MAIN =  diskPath.resolve("NPK_MAIN.NPK");
 		Path NPK_MAKE =  diskPath.resolve("NPK_MAKE.NPK");
 		
 		
 		//LPK
 		
 		Path MLIST_LPK =  diskPath.resolve("TEXT\\MLIST.LPK");
 		Path FF_SYS_LPK =  diskPath.resolve("FF_SYS.LPK");
 		Path FF_OPN_LPK =  diskPath.resolve("FF_OPN.LPK");

 		
 		Path DATF001_LPK =  diskPath.resolve("BG\\DATF001.LPK");
 		Path DATF002_LPK =  diskPath.resolve("BG\\DATF002.LPK");
 		Path DATF003_LPK =  diskPath.resolve("BG\\DATF003.LPK");
 		Path DATF004_LPK =  diskPath.resolve("BG\\DATF004.LPK");
 		Path DATF005_LPK =  diskPath.resolve("BG\\DATF005.LPK");
 		Path DATF006_LPK =  diskPath.resolve("BG\\DATF006.LPK");
 		Path DATF007_LPK =  diskPath.resolve("BG\\DATF007.LPK");
 		Path DATF008_LPK =  diskPath.resolve("BG\\DATF008.LPK");
 		Path DATF009_LPK =  diskPath.resolve("BG\\DATF009.LPK");

 		

 		//processNPK(NPK_WEP);
 		//processNPK(NPK_MON);
 		//processNPK(NPK_ETC);
 		//processNPK(NPK_BOU);
 		//processNPK(NPK_ACC);
 		//processNPK(CG_FFD);
 		//processNPK(MATER_FFD);
 		//processNPK(MAKINGCG_NPK);
 		//processNPK(SM000_FFD);
 		//processNPK(SM001_FFD);
 		//processNPK(HERO_FFD);
 		
 		
 		//processSmallNPK(NPK_MAIN,3,0x1c800,0x92000);
 		//processSmallNPK(NPK_DF,2,0x1a800,0x8a000);
 		//processSmallNPK(NPK_MAKE,3,0x22000,0x87000);
 		//processSmallNPK(NPK_WM,5,0xa800,0x80000);
 		//processSmallNPK(NPK_DF2,0,0x000000,0x75800);
 		//processSmallNPK(FF_SYS_LPK,10,null,null);
 		//processSmallNPK(FF_OPN_LPK,1,null,null);
 		//processSmallNPK(DATF001_LPK,1,null,null);
 		//processSmallNPK(DATF002_LPK,1,null,null);
 		//processSmallNPK(DATF003_LPK,1,null,null);
 		//processSmallNPK(DATF004_LPK,1,null,null);
 		//processSmallNPK(DATF005_LPK,1,null,null);
 		//processSmallNPK(DATF006_LPK,1,null,null);
 		//processSmallNPK(DATF007_LPK,1,null,null);
 		//processSmallNPK(DATF008_LPK,1,null,null);
 		//processSmallNPK(DATF009_LPK,1,null,null);

 		//processNPK(MLIST_LPK);
 		processFFTOWNNPK(FF_TTOWN_NPK);
 		


	}
	
 	private static void processSmallNPK(Path path, int firstblockCount, Integer SecondBlockOffset, Integer thirdBlockOffset) {
		FileInputStream fis = null;
		ByteBuffer fileBuffer = null;
		int fileCount = 0;

		try {
			LZSDecompressor decompressor = new LZSDecompressor(/*4096*/);
			fis = new FileInputStream(path.toFile());
			fileBuffer = ByteBuffer.wrap(fis.readAllBytes());
			
			
			List<Entry<Integer,Integer>> pairList = new ArrayList<Entry<Integer,Integer>>();
			
			
			for (int i = 0; i < firstblockCount; i++) {
				pairList.add(new AbstractMap.SimpleEntry<Integer, Integer>(EndianUtils.swapInteger(fileBuffer.getInt()), EndianUtils.swapInteger(fileBuffer.getInt())));

			}
			for (int i = 0; i < firstblockCount; i++) {

				fileBuffer.limit(pairList.get(i).getKey()+pairList.get(i).getValue());
				//System.out.println("Buffer.capacity: "+fileBuffer.capacity());
				//System.out.println("Buffer.limit: "+fileBuffer.limit());
				//System.out.println("Buffer.position: "+fileBuffer.position());
				//System.out.println("start: "+pairList.get(i).getKey()+" leng: "+pairList.get(i).getValue());
				while (fileBuffer.position()<pairList.get(i).getKey()+pairList.get(i).getValue()) {
					result.write(decompressor.decompressLZSBlock(fileBuffer).array());
				}
				fileBuffer.limit(fileBuffer.capacity());
				dumpFile(result, path.getFileName().toString(),fileCount);
				fileCount++;
				decompressor.reset();
			}

			fileBuffer.limit(fileBuffer.capacity());

			if(SecondBlockOffset == null) return;
			//skip some zeros
			fileBuffer.position(SecondBlockOffset);
			
			while (true)  {
				System.out.println("position: "+ Long.toHexString(fileBuffer.position()));
				int limit = fileBuffer.position()+1024;
				int blocksInMegaBlockCount = EndianUtils.swapInteger(fileBuffer.getInt());
				System.out.println("Blocks in megablock: "+blocksInMegaBlockCount);
				int compressedDataLenght = EndianUtils.swapShort(fileBuffer.getShort());
				System.out.println("Size of commpressed data: "+compressedDataLenght);
				int decompressedDataLenght = EndianUtils.swapShort(fileBuffer.getShort());
				System.out.println("Size of decompressed data: "+decompressedDataLenght);
				int startPos = fileBuffer.position();
				fileBuffer.limit(startPos+compressedDataLenght);
				while (fileBuffer.position()<startPos+compressedDataLenght) {
					result.write(decompressor.decompressLZSBlock(fileBuffer).array());
					
				}
				//checkZeros(b1kb);
				fileBuffer.limit(limit);
				
				byte[] tail = Arrays.copyOfRange(fileBuffer.array(), fileBuffer.position(), limit);
				if(isNotEmpty(tail)) secret.write(tail);
				
				fileBuffer.limit(fileBuffer.capacity());
				fileBuffer.position(limit);
				System.out.println("position: "+ Long.toHexString(fileBuffer.position()));

				if (blocksInMegaBlockCount == 1) {
					//System.out.println("current position: "+b1kb.position());
					decompressor.reset();
					dumpFile(result, path.getFileName().toString(),fileCount, "music", "vab");
					fileCount++;
					break;
					
				}
			}
			
			
			
			byte[] unknown = Arrays.copyOfRange(fileBuffer.array(), fileBuffer.position(), thirdBlockOffset);
			
			result.write(unknown);
			dumpFile(result, path.getFileName().toString(),fileCount, "unknown", "hex");
			
			fileCount++;

			fileBuffer.position(thirdBlockOffset);
			
			while (true)  {
				System.out.println("position: "+ Long.toHexString(fileBuffer.position()));
				int limit = fileBuffer.position()+1024;
				int blocksInMegaBlockCount = EndianUtils.swapInteger(fileBuffer.getInt());
				System.out.println("Blocks in megablock: "+blocksInMegaBlockCount);
				int compressedDataLenght = EndianUtils.swapShort(fileBuffer.getShort());
				System.out.println("Size of commpressed data: "+compressedDataLenght);
				int decompressedDataLenght = EndianUtils.swapShort(fileBuffer.getShort());
				System.out.println("Size of decompressed data: "+decompressedDataLenght);
				int startPos = fileBuffer.position();
				fileBuffer.limit(startPos+compressedDataLenght);
				while (fileBuffer.position()<startPos+compressedDataLenght) {
					result.write(decompressor.decompressLZSBlock(fileBuffer).array());
					
				}
				//checkZeros(b1kb);
				fileBuffer.limit(limit);
				
				byte[] tail = Arrays.copyOfRange(fileBuffer.array(), fileBuffer.position(), limit);
				if(isNotEmpty(tail)) secret.write(tail);
				
				fileBuffer.limit(fileBuffer.capacity());
				fileBuffer.position(limit);
				System.out.println("position: "+ Long.toHexString(fileBuffer.position()));

				if (blocksInMegaBlockCount == 1) {
					//System.out.println("current position: "+b1kb.position());
					decompressor.reset();
					dumpFile(result, path.getFileName().toString(),fileCount, "music", "seq");
					fileCount++;
					break;
				}
			}
			dumpFile(secret, path.getFileName().toString(),999, "secret", "hex");

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
			System.out.println("fileBuffer.position(): 0x"+Integer.toHexString(fileBuffer.position()));
		}
	}
 	
	private static void processNPK(Path path) throws IOException {
		FileInputStream fis = null;
		ByteBuffer b1kb = ByteBuffer.allocate(1024);
		int fileCount = 0;
		try {
			LZSDecompressor decompressor = new LZSDecompressor(/*4096*/);

			fis = new FileInputStream(path.toFile());
			while (fis.read(b1kb.array()) != -1) {
				int blocksInMegaBlockCount = EndianUtils.swapInteger(b1kb.getInt());
				//System.out.println("Blocks in megablock: "+blocksInMegaBlockCount);
				int compressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				//System.out.println("Size of commpressed data: "+compressedDataLenght);
				int decompressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				//System.out.println("Size of decompressed data: "+decompressedDataLenght);
				b1kb.limit(compressedDataLenght);
				while (b1kb.position()<compressedDataLenght) {
					//System.out.println("current position: "+b1kb.position());
					result.write(decompressor.decompressLZSBlock(b1kb).array());
					
				}
				//checkZeros(b1kb);
				b1kb.limit(b1kb.capacity());
				
				byte[] tail = Arrays.copyOfRange(b1kb.array(), b1kb.position(), b1kb.capacity());
				if(isNotEmpty(tail)) secret.write(tail);
				
				b1kb.clear();
				b1kb.position(0);
				if (blocksInMegaBlockCount == 1) {
					//System.out.println("current position: "+b1kb.position());
					decompressor.reset();
					dumpFile(result, path.getFileName().toString(),fileCount);
					fileCount++;
					
				}
			}
			dumpFile(secret, path.getFileName().toString(),999);

			System.out.println("Current Position: "+Long.toHexString(fis.getChannel().position()));
			System.out.println("File Size: "+path.toFile().length());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
			dumpFile(result, "error",fileCount);

		}

 	}
 	
	
	private static void processFFTOWNNPK(Path path) throws IOException {
		// 0x181000
		FileInputStream fis = null;
		ByteBuffer b1kb = ByteBuffer.allocate(1024);
		int offset = 0;

		int fileCount = 0;
		try {
			LZSDecompressor decompressor = new LZSDecompressor(/*4096*/);

			fis = new FileInputStream(path.toFile());
			while (fis.read(b1kb.array()) != -1) {
				offset += 1024;
				int blocksInMegaBlockCount = EndianUtils.swapInteger(b1kb.getInt());
				//System.out.println("Blocks in megablock: "+blocksInMegaBlockCount);
				int compressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				//System.out.println("Size of commpressed data: "+compressedDataLenght);
				int decompressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				//System.out.println("Size of decompressed data: "+decompressedDataLenght);
				b1kb.limit(compressedDataLenght);
				while (b1kb.position()<compressedDataLenght) {
					//System.out.println("current position: "+b1kb.position());
					result.write(decompressor.decompressLZSBlock(b1kb).array());
					
				}
				//checkZeros(b1kb);
				b1kb.limit(b1kb.capacity());
				
				byte[] tail = Arrays.copyOfRange(b1kb.array(), b1kb.position(), b1kb.capacity());
				if(isNotEmpty(tail)) secret.write(tail);
				
				b1kb.clear();
				b1kb.position(0);
				if (blocksInMegaBlockCount == 1) {
					//System.out.println("current position: "+b1kb.position());
					decompressor.reset();
					dumpFile(result, path.getFileName().toString(),fileCount);
					fileCount++;
					
				}
				if (offset==0x181000) {System.out.println(blocksInMegaBlockCount);break;}
			}
			
			System.out.println(fileCount);
			System.out.println();
			result.reset();
			decompressor.reset();

			ByteBuffer bb2 = ByteBuffer.allocate(0x5000);
			
			fis.read(bb2.array());
			List<Entry<Integer,Integer>> pairList = new ArrayList<Entry<Integer,Integer>>();
			
			
			for (int i = 0; i < 2; i++) {
				pairList.add(new AbstractMap.SimpleEntry<Integer, Integer>(EndianUtils.swapInteger(bb2.getInt()), EndianUtils.swapInteger(bb2.getInt())));

			}
			for (int i = 0; i < 2; i++) {

				bb2.limit(pairList.get(i).getKey()+pairList.get(i).getValue());
				System.out.println("Buffer.capacity: "+bb2.capacity());
				System.out.println("Buffer.limit: "+bb2.limit());
				System.out.println("Buffer.position: "+bb2.position());
				System.out.println("start: "+pairList.get(i).getKey()+" leng: "+pairList.get(i).getValue());
				while (bb2.position()<pairList.get(i).getKey()+pairList.get(i).getValue()) {
					result.write(decompressor.decompressLZSBlock(bb2).array());
				}
				bb2.limit(bb2.capacity());
				dumpFile(result, path.getFileName().toString(),fileCount);
				fileCount++;
				decompressor.reset();
			}
			
			offset += 0x5000; 
			
			
			while (fis.read(b1kb.array()) != -1) {
				offset += 1024;
				int blocksInMegaBlockCount = EndianUtils.swapInteger(b1kb.getInt());
				//System.out.println("Blocks in megablock: "+blocksInMegaBlockCount);
				int compressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				//System.out.println("Size of commpressed data: "+compressedDataLenght);
				int decompressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				//System.out.println("Size of decompressed data: "+decompressedDataLenght);
				b1kb.limit(compressedDataLenght);
				while (b1kb.position()<compressedDataLenght) {
					//System.out.println("current position: "+b1kb.position());
					result.write(decompressor.decompressLZSBlock(b1kb).array());
					
				}
				//checkZeros(b1kb);
				b1kb.limit(b1kb.capacity());
				
				byte[] tail = Arrays.copyOfRange(b1kb.array(), b1kb.position(), b1kb.capacity());
				if(isNotEmpty(tail)) secret.write(tail);
				
				b1kb.clear();
				b1kb.position(0);
				if (blocksInMegaBlockCount == 1) {
					//System.out.println("current position: "+b1kb.position());
					decompressor.reset();
					dumpFile(result, path.getFileName().toString(),fileCount);
					fileCount++;
					
				}
				//if (offset==0x181000) break;
			}
			
			
			
			
			dumpFile(secret, path.getFileName().toString(),999);

			System.out.println("Current Position: "+Long.toHexString(fis.getChannel().position()));
			System.out.println("File Size: "+path.toFile().length());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
			dumpFile(result, "error",fileCount);

		}

 	}
	
	
	
	private static boolean isNotEmpty(byte[] tail) {

		for (byte b : tail) if (b != 0) return true;
		return false;

	}

	private static void checkZeros(ByteBuffer b1kb) {
		int lim = b1kb.limit();

		
		b1kb.limit(b1kb.capacity());
		int pos = b1kb.position();
		boolean not = false;
		while(b1kb.position() < b1kb.capacity()) {
			if (b1kb.get()!=0) {
				not =  true;

			}
		}
		if(not) {
			System.out.println("notzero");
			System.out.println("position: "+pos);
			System.out.println("*****buffer print start*****");
			System.out.println(Hex.encodeHexString(b1kb));
			System.out.println("*****buffer print end******");

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
