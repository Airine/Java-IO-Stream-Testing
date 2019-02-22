import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Arrays;

public class InputStreamAndReaderDemo {

	public static void main(String[] args) {
		String fileName = "src/流浪地球_utf8.txt";
		long startTime = System.currentTimeMillis();
		try{
			readByteFromFile(fileName);
//			readCharFromFile(fileName);
		} catch (IOException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.println("-----------------------------------------------------------");
		System.out.println("Time cost: " + time + "ms");
		printMemoryInfo();
	}

	private static void readByteFromFile(String fileName) throws IOException {
		// Read from file byte by byte.

		File file= new File(fileName);
		byte[] byteArray= new byte[(int) file.length()];
		InputStream is = new FileInputStream(file);

		int size = is.read(byteArray);

		System.out.print( "File size:\t" + size + " bytes\n" +
							"Content:\t" + new String(byteArray).substring(0, 5) + "...\n" +
							"Raw:\t\t");

		for (byte b: Arrays.copyOfRange(byteArray, 0, 5)) {
			System.out.printf("%X", b); // hex value of a byte
			System.out.print(",");
		}
		System.out.print("...\n");

		System.out.println("Raw->char:\t" + new String(Arrays.copyOfRange(byteArray, 0, 3)));
		// Pick up first three bytes and convert them to String.
		// PS: A UTF-8 Chinese Character takes three bytes.

		is.close();
		System.out.println();
	}

	private static void readCharFromFile(String fileName) throws IOException{
		// Read from file char by char

		File file= new File(fileName);
		Reader reader = new FileReader(file);

		char [] charArray = new char[(int) file.length()];
		int size = reader.read(charArray);
		System.out.print( "File size:\t" + size + " chars\n" +
							"Content:\t" + new String(charArray).substring(0, 5) + "...\n" +
							"Raw content:\t");
		System.out.print(Arrays.copyOfRange(charArray, 0, 5));

		System.out.print("...");
		reader.close();
		System.out.println("\n");
	}

	private static void printMemoryInfo(){
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		System.out.println("-----------------------------------------------------------");
		System.out.println("Memory cost:");
		System.out.println("Heap memory used:" + memoryMXBean.getHeapMemoryUsage().getUsed()/1024/1024 + " MB");
		System.out.println("Non Heap memory used:" + memoryMXBean.getNonHeapMemoryUsage().getUsed()/1024/1024 + " MB");
		System.out.println("-----------------------------------------------------------");
	}

}
