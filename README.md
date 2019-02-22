 # Java-IO-Stream-Testing

 ## Java IO stream classification

 ### Classify by IO type
 1. Input: 
    - InputStream
    - Reader
    
 2. Output:
    - OutputStream
    - Writer

 ### Classify by stream type
 1. Byte stream
    - InputStream
    - OutputStream
    
 2. Char stream
    - Reader
    - Writer

 ---

 `Input/OuputStream` and `Reader/Writer` are the basic superclass of Java IO streams which are all abstract.

 ![](https://img-blog.csdn.net/20171121180314999)
 
 ## Basic Usages
 
 ### Input (InputStream vs. Reader)
 
 1. InputStream
 
    ```java
    private static void readByteFromFile(String fileName) throws IOException {
        // Read from file byte by byte.
    
        File file= new File(fileName);
        byte[] byteArray= new byte[(int) file.length()];
        InputStream is = new FileInputStream(file);
    
        int size = is.read(byteArray);
    
        System.out.print(   "File size:\t" + size + " bytes\n" +
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
    ```
    
 2. Reader
 
    ```java
    private static void readCharFromFile(String fileName) throws IOException{
        // Read from file char by char

        File file= new File(fileName);
        Reader reader = new FileReader(file);

        char [] charArray = new char[(int) file.length()];
        int size = reader.read(charArray);
        System.out.print(   "File size:\t" + size + " chars\n" +
                            "Content:\t" + new String(charArray).substring(0, 5) + "...\n" +
                            "Raw content:\t");
        System.out.print(Arrays.copyOfRange(charArray, 0, 5));

        System.out.print("...");
        reader.close();
        System.out.println("\n");
    }
    ```
 
 3. main(String args[])
 
    ```java
    public static void main(String[] args) {
        String fileName = "src/流浪地球_utf8.txt";
        long startTime = System.currentTimeMillis();
        try{
            readByteFromFile(fileName);
            // readCharFromFile(fileName);
        } catch (IOException e){
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("-----------------------------------------------------------");
        System.out.println("Time cost: " + time + "ms");
        printMemoryInfo();
    }
    
    private static void printMemoryInfo(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.println("-----------------------------------------------------------");
        System.out.println("Memory cost:");
        System.out.println("Heap memory used:" + memoryMXBean.getHeapMemoryUsage().getUsed()/1024/1024 + " MB");
        System.out.println("Non Heap memory used:" + memoryMXBean.getNonHeapMemoryUsage().getUsed()/1024/1024 + " MB");
        System.out.println("-----------------------------------------------------------");
    }
    ```
 
 In upper situation, InputStream and Reader use an array with enough size to read file content at once. The performance 
 gap between them is not obvious.
 
 Test file: https://github.com/Airine/Java-IO-Stream-Testing/blob/master/src/流浪地球_utf8.txt
 
 ### Output (OutputStream vs. Writer)
 
 1. OutputStream
 
    ```java
    private static void writeByteToFile(String hello) throws IOException{
        byte[] byteArray = hello.getBytes();
        File file = new File("src/outputStream.txt");
        OutputStream os = new FileOutputStream(file);
        os.write(byteArray);
        os.flush();
        os.close();
    }
    ```
 
 2. Writer
 
    ```java
    private static FileWriter writeCharToFile(String hello, Boolean flush) throws IOException {
        File file = new File("src/writer.txt");
        Writer os = new FileWriter(file);
        os.write(hello);
        if (!flush) return (FileWriter) os; // dangerous!
        os.flush();
        os.close();
        return null;
    }
    ```
    
 3. main(String args[])
 
    ```java
    public static void main(String args[]){
        String hello = "hello word!";
        //boolean flush = true;
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
    ```
 
 From this example, if we assign `false` to variable `flush` and run the program, before enter anything in the console,
 we will find two different files in `src`. One is 'outputStream.txt' with file content "hello word!", the other is
 'writer.txt' with empty content.
 
 It is because that the `Writer` need to convert bytes it read from a file and buffer them char by char into the memory
 in specified codeset to implement the function 'stream by character'. And the `Writer` would flush the buffer to 
 output destination only when the buffer is full or the flush function is invoked.
 <br>
 The default size of java stream buffer is 8192 bytes.
 
 ---
 
 ## Advanced
 
 ### Buffered Stream 
 
 Use wrapper stream to improve the performance.
 
 To compare the performance between buffered and non-buffered stream, below is a copy file applet.
 
 1. Buffered byte stream
 
    ```java
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
    ```
 
 2. Buffered char stream
 
    ```java
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
    ```
    
 3. main(String args[])
 
    ```java
    public static void main(String[] args) {
        String fileName = "src/流浪地球_utf8.txt";
        boolean buffered = false;
        // boolean buffered = true;
        long startTime = System.currentTimeMillis();
        try{
            copyFileByByte(fileName, buffered);
            // copyFileByChar(fileName, buffered);
        } catch (IOException e){
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        System.out.println("-----------------------------------------------------------");
        System.out.println("Time cost: " + time + "ms");
        printMemoryInfo();
    }
	
	private static void printMemoryInfo(){
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.println("-----------------------------------------------------------");
        System.out.println("Memory cost:");
        System.out.println("Heap memory used:" + memoryMXBean.getHeapMemoryUsage().getUsed()/1024/1024 + " MB");
        System.out.println("Non Heap memory used:" + memoryMXBean.getNonHeapMemoryUsage().getUsed()/1024/1024 + " MB");
        System.out.println("-----------------------------------------------------------");
    }
    ```
    
 Output:
    
    Non-buffered:
    1. byte stream
        -----------------------------------------------------------
        Time cost: 4124ms
        -----------------------------------------------------------
        Memory cost:
        Heap memory used:5 MB
        Non Heap memory used:4 MB
        -----------------------------------------------------------
    2. char stream
        -----------------------------------------------------------
        Time cost: 88ms
        -----------------------------------------------------------
        Memory cost:
        Heap memory used:30 MB
        Non Heap memory used:5 MB
        -----------------------------------------------------------
    
    Buffered:
    1. byte stream
        -----------------------------------------------------------
        Time cost: 28ms
        -----------------------------------------------------------
        Memory cost:
        Heap memory used:5 MB
        Non Heap memory used:4 MB
        -----------------------------------------------------------
    2. char stream
        -----------------------------------------------------------
        Time cost: 31ms
        -----------------------------------------------------------
        Memory cost:
        Heap memory used:5 MB
        Non Heap memory used:4 MB
        -----------------------------------------------------------
        
 ### Stream convert
 
 Additionally, the `InputStream` and `OutStream` can be converted to `Reader` and `Writer` by codes below.
 
 ```java
 // Input
 InputStream is = new FileInputStream(file);
 Reader reader = new InputStreamReader(is);
 
 //Output
 OutputStream os = new OutputStream(file);
 Writer writer = new OutputStreamWriter(os);
 ```
 
 Demo
 
 ```java
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
 ```
 
 Question: Can `Reader` and `Writer` be converted to `InputStream` and `OutputStream`?
 
 Answer: No. <br>Why? Let's analyze the source code definition of these four classes.
 
 ```java
 // Input
 public abstract class InputStream implements Closeable {}
 public abstract class Reader implements Readable, Closeable {}
 // Output
 public abstract class OutputStream implements Closeable, Flushable {}
 public abstract class Writer implements Appendable, Closeable, Flushable {}
 ```
 
 As can be seen from the upper code, to be converted to `Reader` or `Writer`, the `InputStream` and `OutputStream`
 implement a more interface `Readable` or `Appendable`. This process is an addition. But for `Reader` and `Writer` to
 become `Input/OutputStream`, it needs interface subtraction which is impossible.
 
 ---
 ## Reference
 1. https://blog.csdn.net/wangzhongshun/article/details/78595228