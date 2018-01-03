import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.EndianUtils;


public class Unpacker {
	static CircularFifoQueue<Byte> circularBuffer = new CircularFifoQueue<>(4096);
	static ByteArrayOutputStream result = new ByteArrayOutputStream();
	static int bufferL = 0;
	static int OUTPOS = 0;
	static List<Integer> offsetlist = new ArrayList<Integer>();
	static int filenum = 0;
 	public static void main(String[] args) throws IOException, Exception {
 		
 		//processLZS();
 		processNPK();
	}
	
 	
 	
 	@SuppressWarnings("resource")
	private static void processLZS() throws IOException {
 		while(!circularBuffer.isAtFullCapacity())circularBuffer.add(Byte.valueOf((byte)0));
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
					readLZSBlock(b1kb);
				}
				dumpFile(result);


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
		}	}



	private static void processNPK() {
		while(!circularBuffer.isAtFullCapacity())circularBuffer.add(Byte.valueOf((byte)0));
		
		//System.out.println("tezst");	
		File file = new File("NPK_WEP.NPK");
		FileInputStream fis = null;
		ByteBuffer b1kb = ByteBuffer.allocate(1024);
		try {
			fis = new FileInputStream(file);
			while (fis.read(b1kb.array()) != -1) {
				int blocksInMegaBlockCount = EndianUtils.swapInteger(b1kb.getInt());
				System.out.println("Blocks in megablock: "+blocksInMegaBlockCount);
				int compressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				System.out.println("Size of commpressed data: "+compressedDataLenght);
				int decompressedDataLenght = EndianUtils.swapShort(b1kb.getShort());
				System.out.println("Size of decompressed data: "+decompressedDataLenght);
				while (b1kb.position()<compressedDataLenght) {
					System.out.println("current position: "+b1kb.position());
					readLZSBlock(b1kb);
				}
				b1kb.clear();
				b1kb.position(0);
				if (blocksInMegaBlockCount == 1) {dumpFile(result);}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("oops!");
		}
        //Collections.sort(offsetlist);
/*
		for (Integer offset : offsetlist) {
			System.out.println(offset);
		}
		
		/*
		while(fis.re) {
			
		}
		System.out.println(Hex.encodeHex(readBlock(fis)));
		*/
 	}
 	
	private static void dumpFile(ByteArrayOutputStream byteArrayStream) throws IOException {
		//byteArrayStream
		FileOutputStream fos = new FileOutputStream("picture_"+(filenum++)+".TIM"); 
		byteArrayStream.writeTo(fos);
		result.flush();
		fos.flush();
		result.close();
		fos.close();
		result = new ByteArrayOutputStream();
		
		circularBuffer.clear();
		while(!circularBuffer.isAtFullCapacity())circularBuffer.add(Byte.valueOf((byte)0));
		bufferL = 0;
		OUTPOS = 0;

	}

	private static void readLZSBlock(ByteBuffer bb) throws IOException, Exception {
		//read control byte
		byte controlByte = 0;
		controlByte = bb.get();

		System.out.println("control byte: "+Byte.toUnsignedInt(controlByte));
		System.out.println("control byte bits: "+Integer.toBinaryString(Byte.toUnsignedInt(controlByte)));
		for (int i = 0; i < 8; i++) {
			if (isLiteral(controlByte,i)) {
				byte b = bb.get();
				circularBuffer.add(b);
				//debugPrintBuffer();
				bufferL++;
				result.write(b);
				OUTPOS++;
			}
			else result.write(getFromCBuffer(bb));
		}

	}
	


	private static byte[] getFromCBuffer(ByteBuffer bb) throws IOException {
		byte first = bb.get();
		byte second = bb.get();
		Integer lowNibble = Integer.valueOf(second & 0x0f); 
		Integer highNibble = Integer.valueOf(((second & 0xf0) >> 4));
		//System.out.println("1: "+String.format("%02X ", first));
		//System.out.println("2: "+String.format("%02X ", second));
		System.out.print(bufferL+" bytes to copy from buffer: "+(lowNibble+3));
		//System.out.println("4: "+highNibble);
		//System.out.println("5: "+String.format("%02X ", highNibble));
		int offset = EndianUtils.readSwappedShort(new byte[] {first, highNibble.byteValue()}, 0);
		int realoffset = OUTPOS - ((OUTPOS + 0xfee - offset) & 0xfff);
		int realoffset2 = 4096 - OUTPOS + realoffset;
		offsetlist.add(offset);
		System.out.println(" offset to copy from buffer: "+offset);
	
		byte[] bytesFromBuffer = new byte[lowNibble+3];
		// some hacks
		boolean hack2 =  false;
		if (offset+lowNibble+3 > 4095) hack2 =  true; 
		try {
			/*
			for (int i = 0; i < (lowNibble+3); i++) {
				if(hack2 && (offset+i)==4611) offset = offset-i;// some hacks
				int realOffset = offset+i;
				
				bytesFromBuffer[i]=circularBuffer.get(realOffset);
			}*/
			for (int i = 0; i < (lowNibble+3); i++) {
				byte b = circularBuffer.get(realoffset2);
				circularBuffer.add(b);
				bytesFromBuffer[i]=b;
			}
			
		} catch (Exception e) {


			debugPrintBuffer();
			throw e;
		}

		
		OUTPOS = OUTPOS+bytesFromBuffer.length;
		return bytesFromBuffer;
	}

	private static boolean isLiteral(byte controlByte, int position) throws Exception {
		if (((controlByte >> position) & 1) == 1) return true;
		else if  (((controlByte >> position) & 1) == 0) return false;
		throw new Exception("something went wrong during controlByte parsing");
		
	}
	
	private static void debugPrintBuffer() {
		System.out.println("*****buffer print start*****");
		for (int i = 0; i < circularBuffer.size(); i++) {
			System.out.print(String.format("%02X ", circularBuffer.get(i)));
			if((i+1)%64==0) System.out.println();
		}
		System.out.println("*****buffer print end******");

	}

}
