import java.io.*;
import java.util.Arrays;

public class InputStreamAndReaderDemo {

	public static void readByteFromFile() throws IOException {
		File file= new File( "流浪地球_utf8.txt");
		byte[] byteArray= new byte[(int) file.length()];
		// 因为是用字节流来读媒介，所以对应的是InputStream
		// 又因为媒介对象是文件，所以用到子类是FileInputStream
		InputStream is = new FileInputStream(file);
		int size = is.read(byteArray);
		System.out.println( "File size: " + size + "\n" +
							"Content(head 5):" + new String(byteArray).substring(0, 5) + "\n" +
							"Raw content(head 5)" + Arrays.copyOfRange(byteArray, 0, 5)
		);
		is.close();
	}

	public static void readCharFromFile() throws IOException{
		File file= new File( "流浪地球_utf8.txt");
		// 因为是用字符流来读媒介，所以对应的是Reader
		// 又因为媒介对象是文件，所以用到子类是FileReader
		Reader reader = new FileReader(file);
		char [] charArray = new char[(int) file.length()];
		int size = reader.read(charArray);
		System.out.println( "File size: " + size + "\n" +
							"Content(head 5):" + new String(charArray).substring(0, 5) + "\n" +
							"Raw content(head 5)" + Arrays.copyOfRange(charArray, 0, 5)
		);
		reader.close();
	}

	public static void main(String[] args) {

	}
}
