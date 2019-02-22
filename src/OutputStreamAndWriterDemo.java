import java.io.*;
import java.util.Scanner;

public class OutputStreamAndWriterDemo {

	public static void main(String[] args) {

		String hello = "hello word!";
//		boolean flush = true;
		boolean flush = false;
		Writer os = null;
		try {
			os = writeCharToFile(hello, flush);
			writeByteToFile(hello);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!flush && os!=null) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter anything below to flush and close the writer stream.");
			scanner.hasNext();
			try {
				os.flush();
				os.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	private static FileWriter writeCharToFile(String hello, Boolean flush) throws IOException {
		File file = new File("src/writer.txt");
		Writer os = new FileWriter(file);
		os.write(hello);
		if (!flush) return (FileWriter) os; // dangerous!
		os.flush();
		os.close();
		return null;
	}

	private static void writeByteToFile(String hello) throws IOException{
		byte[] byteArray = hello.getBytes();
		File file = new File("src/outputStream.txt");
		OutputStream os = new FileOutputStream(file);
		os.write(byteArray);
		os.flush();
		os.close();
	}

}
