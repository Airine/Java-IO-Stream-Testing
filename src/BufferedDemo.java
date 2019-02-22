import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Arrays;

public class BufferedDemo {

	public static void main(String[] args) {
		String fileName = "src/流浪地球_utf8.txt";
		boolean buffered = false;
//		boolean buffered = true;
		long startTime = System.currentTimeMillis();
		try{
			copyFileByByte(fileName, buffered);
//			copyFileByChar(fileName, buffered);
		} catch (IOException e){
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.println("-----------------------------------------------------------");
		System.out.println("Time cost: " + time + "ms");
		printMemoryInfo();
	}

	private static void copyFileByByte(String fileName, boolean buffered) throws IOException {
		// Copy file byte by byte using byte IO stream.
		File file = new File(fileName);
		File destFile = new File("src/copyByte.txt");
		InputStream is;
		OutputStream os;
		if (!buffered) {
			is = new FileInputStream(file);
			os = new FileOutputStream(destFile);
		} else {
			is = new BufferedInputStream(new FileInputStream(file));
			os = new BufferedOutputStream(new FileOutputStream(destFile));
		}
		int c;
		while((c = is.read()) != -1) {
			os.write(c);
		}
		is.close();
		os.close();
	}

	private static void copyFileByChar(String fileName, boolean buffered) throws IOException {
		// Copy file char by char using char IO stream.
		File file = new File(fileName);
		File destFile = new File("src/copyChar.txt");
		Reader is;
		Writer os;
		if (!buffered) {
			is = new FileReader(file);
			os = new FileWriter(destFile);
		} else {
			is = new BufferedReader(new FileReader(file));
			os = new BufferedWriter(new FileWriter(destFile));
		}
		if (!buffered) {
			int c;
			while ((c = is.read()) != -1) {
				os.write(c);
			}
		} else {
			String s;
			BufferedReader bufferedReader = (BufferedReader) is;
			while ((s = bufferedReader.readLine()) != null){
				os.write(s);
			}
		}
		is.close();
		os.close();
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
