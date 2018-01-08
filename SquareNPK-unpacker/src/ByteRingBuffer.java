import java.util.Arrays;

/**
 * SquareSoft LZS buffer (circular) bytes.
 *
 */
public class ByteRingBuffer {

	private byte[] rBuf; // ring buffer data
	public int rBufSize = 4096; // ring buffer size
	public int writeReadOffsetCorrection = 18;
	public int lenghtCorrectionLZS = 3;
	private int rBufWrPos = rBufSize - writeReadOffsetCorrection; // position of first (oldest) data byte within the ring buffer



	public ByteRingBuffer() {
		rBuf = new byte[rBufSize];
	}

	public void clear() {
		rBufWrPos = 0;
		Arrays.fill(rBuf, (byte) 1);
		rBufWrPos = rBufSize - writeReadOffsetCorrection;

	}

	public byte write(byte bt) {
		rBuf[rBufWrPos++] = bt;
		if (rBufWrPos == rBufSize)
			rBufWrPos = 0;
		return bt;
	}

	public byte read(int offset) {
		return rBuf[offset & 0xfff];
	}

	public byte[] readAndWrite(int offset, int lenght) {
		int realLenght = lenght+lenghtCorrectionLZS;
		byte[] data = new byte[realLenght];
		for (int i = 0; i < (realLenght); i++) {
			data[i]=read(offset+i);
			write(data[i]);
		} 
		return data;

	}
	
	public byte add(byte bt) {
		return write(bt);

	}
	
	public byte get(int offset) {
		return read(offset);

	}
	
}
