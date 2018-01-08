import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.commons.io.EndianUtils;

public class LZSDecompressor {
	//private int bufferSize = 4096; 
	//private CircularFifoQueue<Byte> circularBuffer;
	private int realOutputPosition  = 0;
	private ByteRingBuffer circularBuffer =  new ByteRingBuffer();


	public LZSDecompressor(/*int bufferSize*/) {
		//this.bufferSize = bufferSize; // set buffer size
		//circularBuffer = new CircularFifoQueue<>(this.bufferSize); // create ring buffer
 		//while(!circularBuffer.isAtFullCapacity())circularBuffer.add(Byte.valueOf((byte)0)); //fill buffer with zeros
	}
	
	public ByteBuffer decompressLZSBlock(ByteBuffer compressedData) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		byte controlByte = 0;
		controlByte = compressedData.get();
		//if(controlByte == 0) System.out.println("controlbyte == 0 , position: "+compressedData.position());
		//System.out.println("control byte: "+Byte.toUnsignedInt(controlByte));
		//System.out.println("control byte bits: "+Integer.toBinaryString(Byte.toUnsignedInt(controlByte)));
		for (int i = 0; i < 8; i++) {
			
			try {
				if (isLiteral(controlByte,i)) {
					byte b = compressedData.get();
					circularBuffer.add(b);
					realOutputPosition++;
					//System.out.println(Hex.encodeHex(new byte[] {b}));
					output.write(b);;
				}
				else output.write(getFromCBuffer(compressedData));
				
			} catch (BufferUnderflowException e) {
				if (compressedData.limit() == compressedData.capacity()) {
					System.err.println("buffer-end unexpectebelly reached");
				}
				break;
			}

		}
		return ByteBuffer.wrap(output.toByteArray());
	}
	
	private boolean isLiteral(byte controlByte, int position) {
		if (((controlByte >> position) & 1) == 1) return true;
		else /*if  (((controlByte >> position) & 1) == 0)*/ return false;
		//throw new Exception("something went wrong during controlByte parsing");
		
	}
	
	private void debugPrintBuffer() {
		System.out.println("*****buffer print start*****");
		for (int i = 0; i < circularBuffer.rBufSize; i++) {
			System.out.print(String.format("%02X ", circularBuffer.get(i)));
			if((i+1)%64==0) System.out.println();
		}
		System.out.println("*****buffer print end******");

	}
	
	private byte[] getFromCBuffer(ByteBuffer bb) {
		byte first = bb.get();
		byte second = bb.get();
		Integer lowNibble = Integer.valueOf(second & 0x0f); 
		Integer highNibble = Integer.valueOf(((second & 0xf0) >> 4));
		int offset = EndianUtils.readSwappedShort(new byte[] {first, highNibble.byteValue()}, 0);
		int realoffset = realOutputPosition - ((realOutputPosition + 0xfee - offset) & 0xfff);
		int realoffset2 = 4096 - realOutputPosition + realoffset;
		
		/*if (realoffset2 == 4096) {
			System.out.println("realoffset2 == 4096, offset: "+offset);
			realoffset2 = 0;
		}
		*/

		byte[] bytesFromBuffer = new byte[lowNibble];
		// some hacks
		//boolean hack2 =  false;
		//if (offset+lowNibble+3 > 4095) hack2 =  true; 
		try {
			/*
			for (int i = 0; i < (lowNibble+3); i++) {
				if(hack2 && (offset+i)==4611) offset = offset-i;// some hacks
				int realOffset = offset+i;
				
				bytesFromBuffer[i]=circularBuffer.get(realOffset);
			}*/
			/*for (int i = 0; i < (lowNibble+3); i++) {
				byte b = circularBuffer.get(realoffset2);
				circularBuffer.add(b);
				bytesFromBuffer[i]=b;
			}*/
			return circularBuffer.readAndWrite(offset, lowNibble);
			
			
			
		} catch (Exception e) {


			debugPrintBuffer();
			throw e;
		}

		
		//realOutputPosition = realOutputPosition+bytesFromBuffer.length;
		//return bytesFromBuffer;
	}
	
	
	public void reset() {
		realOutputPosition = 0;
		circularBuffer.clear();
	}

	
}
