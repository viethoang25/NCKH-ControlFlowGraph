package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class FileManager {

	private static FileManager instance = new FileManager();
	private String rootPath;
	private File file;
	private String data;

	private FileManager() {
		
	}
	
	public static FileManager getInstance() {
		return instance;
	}

	public void init(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getData() {
		return data;
	}

	public File openFile(String fileName) {
		StringBuilder filePath = new StringBuilder(rootPath);
		filePath.
		append(fileName);
		this.file = new File(filePath.toString());
		if (!file.exists()) {
			System.out.println(filePath.toString() + " is not exist");
			return null;
		}
		return this.file;
	}

	public void readFile() {
		try {
			if (file != null) {
				FileInputStream inputStream = new FileInputStream(this.file);
				byte[] buffer = new byte[(int) this.file.length()];
				inputStream.read(buffer);
				this.data = new String(buffer);
				inputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readFile(String fileName) {
		openFile(fileName);
		readFile();
	}

	public void writeFile(String data) {
		try {
			if (file != null) {
				byte[] buffer = toBytes(data.toCharArray());
				FileOutputStream outputStream = new FileOutputStream(this.file);
				outputStream.write(buffer);
				outputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private byte[] toBytes(char[] chars) {
	    CharBuffer charBuffer = CharBuffer.wrap(chars);
	    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
	    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
	            byteBuffer.position(), byteBuffer.limit());
	    Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
	    Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
	    return bytes;
	}
	
}
