import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
 		Path NPK_WM =  diskPath.resolve("NPK_WM.NPK"); ////?????
 		Path RIDE_0_NPK =  diskPath.resolve("R3D\\RIDE_0.NPK");////?????

 		
 		//LPK
 		
 		Path MLIST_LPK =  diskPath.resolve("TEXT\\MLIST.LPK");////RECHECK?????


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
 		processNPK(HERO_FFD);
 		
 		


 		
 		//processNPK();
 		//processMainNPK();
 		//processOPN_LPK();
 		//processDFNPK();
 		//processDF2NPK();

	}
	
 	private static void processOPN_LPK() {
		File file = new File("D:\\HACK\\ePSXe205\\isos\\ddd\\FF_OPN.LPK");
		FileInputStream fis = null;
		ByteBuffer fileBuffer = null;
		try {
			LZSDecompressor decompressor = new LZSDecompressor(/*4096*/);
			fis = new FileInputStream(file);
			fileBuffer = ByteBuffer.wrap(fis.readAllBytes());
			//
			int firstValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("firstValue: "+firstValue);
			//
			int secondValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("secondValue: "+secondValue);
			//
			
			while (fileBuffer.position()<firstValue+secondValue) {
				result.write(decompressor.decompressLZSBlock(fileBuffer).array());
			}
			fileBuffer.limit(fileBuffer.capacity());
			//dumpFile(result);
			
			fileBuffer.clear();
			


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
			//dumpFile(result);
			System.out.println("fileBuffer.position(): 0x"+Integer.toHexString(fileBuffer.position()));
			

		}
		
	}
 	private static void processDFNPK() {
		File file = new File("D:\\HACK\\ePSXe205\\isos\\ddd\\NPK_DF.NPK");
		FileInputStream fis = null;
		ByteBuffer fileBuffer = null;
		try {
			LZSDecompressor decompressor = new LZSDecompressor(/*4096*/);
			fis = new FileInputStream(file);
			fileBuffer = ByteBuffer.wrap(fis.readAllBytes());
			//
			int firstValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("firstValue: "+firstValue);
			//
			int secondValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("secondValue: "+secondValue);
			//
			int thirdValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("thirdValue: "+thirdValue);
			//
			int fouthValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("fouthValue: "+fouthValue);
			//
			fileBuffer.limit(thirdValue);
			while (fileBuffer.position()<firstValue+secondValue) {
				result.write(decompressor.decompressLZSBlock(fileBuffer).array());
			}
			fileBuffer.limit(fileBuffer.capacity());
			//dumpFile(result);
			decompressor.reset();
			fileBuffer.limit(thirdValue+fouthValue);
			while (fileBuffer.position()<thirdValue+fouthValue) {
				result.write(decompressor.decompressLZSBlock(fileBuffer).array());
			}
		
			//dumpFile(result);
			fileBuffer.clear();
			


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
			//dumpFile(result);
			System.out.println("fileBuffer.position(): 0x"+Integer.toHexString(fileBuffer.position()));
			

		}
		
	}

	private static void processDF2NPK() {
		File file = new File("D:\\HACK\\ePSXe205\\isos\\ddd\\NPK_DF2.NPK");
		FileInputStream fis = null;
		ByteBuffer b1kb = ByteBuffer.allocate(1024);
		try {
			LZSDecompressor decompressor = new LZSDecompressor(/*4096*/);

			fis = new FileInputStream(file);
			while (fis.read(b1kb.array()) != -1) {
				int blocksInMegaBlockCount = EndianUtils.swapInteger(b1kb.getInt());
				//System.out.println("Blocks in megablock: "+blocksInMegaBlockCount);
				int compressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				//System.out.println("Size of commpressed data: "+compressedDataLenght);
				int decompressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				//System.out.println("Size of decompressed data: "+decompressedDataLenght);
				while (b1kb.position()<compressedDataLenght) {
					//System.out.println("current position: "+b1kb.position());
					result.write(decompressor.decompressLZSBlock(b1kb).array());
				}
				b1kb.clear();
				b1kb.position(0);
				if (blocksInMegaBlockCount == 1) {
					//System.out.println("current position: "+b1kb.position());
					decompressor.reset();
					//dumpFile(result);
					}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
			//dumpFile(result);
		}

 	}
		


 	private static void processMainNPK() {
		File file = new File("D:\\HACK\\ePSXe205\\isos\\ddd\\NPK_MAIN.NPK");
		FileInputStream fis = null;
		ByteBuffer fileBuffer = null;
		try {
			LZSDecompressor decompressor = new LZSDecompressor(/*4096*/);
			fis = new FileInputStream(file);
			fileBuffer = ByteBuffer.wrap(fis.readAllBytes());
			//
			int firstValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("firstValue: "+firstValue);
			//
			int secondValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("secondValue: "+secondValue);
			//
			int thirdValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("thirdValue: "+thirdValue);
			//
			int fouthValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("fouthValue: "+fouthValue);
			//
			int fifthValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("fifthValue: "+fifthValue);
			//
			int sixthValue = EndianUtils.swapInteger(fileBuffer.getInt());
			System.out.println("fifthValue: "+sixthValue);
			//
			fileBuffer.limit(thirdValue);
			while (fileBuffer.position()<firstValue+secondValue) {
				result.write(decompressor.decompressLZSBlock(fileBuffer).array());
			}
			fileBuffer.limit(fileBuffer.capacity());
			//dumpFile(result);
			decompressor.reset();
			fileBuffer.limit(fifthValue);
			while (fileBuffer.position()<thirdValue+fouthValue) {
				result.write(decompressor.decompressLZSBlock(fileBuffer).array());
			}
			fileBuffer.limit(fileBuffer.capacity());
			//dumpFile(result);
			decompressor.reset();
			fileBuffer.limit(fifthValue+sixthValue);
			while (fileBuffer.position()<fifthValue+sixthValue) {
				//System.out.println("current position: "+fileBuffer.position());
				//System.out.println(Hex.encodeHex(decompressor.decompressLZSBlock(fileBuffer).array()));
				result.write(decompressor.decompressLZSBlock(fileBuffer).array());
			}
			//dumpFile(result);
			fileBuffer.limit(fileBuffer.capacity());
			fileBuffer.position(0x01c808);
			fileBuffer.limit(0x01ce87);
			decompressor.reset();
			//System.out.println(Hex.encodeHex(decompressor.decompressLZSBlock(fileBuffer).array()));

			while (fileBuffer.position()<0x1ce87) {
				//System.out.println("current position: "+fileBuffer.position());
				//System.out.println(Hex.encodeHex(decompressor.decompressLZSBlock(fileBuffer).array()));
				result.write(decompressor.decompressLZSBlock(fileBuffer).array());
			}
			
			//dumpFile(result);
			fileBuffer.clear();
			


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
			//dumpFile(result);
			System.out.println("fileBuffer.position(): 0x"+Integer.toHexString(fileBuffer.position()));
			

		}
		
	}



	@SuppressWarnings("resource")
	private static void processLZS() throws IOException {
 		//while(!circularBuffer.isAtFullCapacity())circularBuffer.add(Byte.valueOf((byte)0));
 		File file = new File("compress1.hex");
		FileInputStream fis = null;
		fis = new FileInputStream(file);
		
		ByteBuffer b1kb = ByteBuffer.wrap(fis.readAllBytes());
		try {
			fis = new FileInputStream(file);

				int compressedDataLenght = EndianUtils.swapInteger(b1kb.getInt());
				System.out.println("Size of commpressed data: "+compressedDataLenght);
				while (b1kb.position()<compressedDataLenght) {
					System.out.println("current position: "+b1kb.position());
					//readLZSBlock(b1kb);
				}
				//dumpFile(result);


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
		}	}



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

	private static void dumpFile(ByteArrayOutputStream byteArrayStream, String fileMame, int fileCount) {
		try {
			File file = new File(outputPath.resolve(fileMame).toFile(), "picture_"+String.format("%03d", fileCount)+".tim");
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
