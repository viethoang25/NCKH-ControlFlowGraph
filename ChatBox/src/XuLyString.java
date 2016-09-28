import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class XuLyString {
	private int [] a [];
	private XuLyString(){}
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int i = 100;
		double d = 100.1;
		boolean b = (i==d);
		System.out.println(b);
		
		/*while (true) {
			String nhap = scanner.nextLine();

			//String regex = "(/@|/.)";
			System.out.println(xuly(nhap));
		}*/
	}
	
	public static boolean xuly(String str){
		char[] c = str.toCharArray();
		int hl = c.length/2;
		boolean kt = true;
		for(int i = 0; i < hl; i++){
			if(c[i] != c[c.length-1-i])
				kt = false;
		}
		return kt;
	}
}