import java.io.*;

public class StreamConvertDemo {

	public static void main(String[] args) {
		String fileName = "src/流浪地球_utf8.txt";

		try{
			copyFileByChar(fileName);
		} catch (IOException e){
			e.printStackTrace();
		}

	}

	private static void copyFileByChar(String fileName) throws IOException {
		// Copy file char by char using char IO stream.
		File file = new File(fileName);
		File destFile = new File("src/copyChar.txt");

		InputStream inputStream = new FileInputStream(file);
		OutputStream outputStream = new FileOutputStream(destFile);
		Reader is = new InputStreamReader(inputStream);
		Writer os = new OutputStreamWriter(outputStream);

		int c;
		while ((c = is.read()) != -1) {
			os.write(c);
		}

		is.close();
		os.close();
	}
}
