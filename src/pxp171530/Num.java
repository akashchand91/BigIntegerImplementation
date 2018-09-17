package pxp171530;

public class Num implements Comparable<Num> {

	static long defaultBase = 10; // Change as needed
	long base = defaultBase; // Change as needed
	long[] arr; // array to store arbitrarily large integers
	boolean isNegative; // boolean flag to represent negative numbers
	int len; // actual number of elements of array that are used; number is stored in
				// arr[0..len-1]

	public Num(String s) {
		/*
		 * this.len = s.length(); arr = new long[len]; for (int i = len - 1; i > 0; i--)
		 * { arr[len - 1 - i] = s.charAt(i); } if(s.charAt(0) == '-') { this.isNegative
		 * = true; len--; } else { arr[len - 1] = s.charAt(0); }
		 */

		// second try
		if (s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
		}
		this.len = s.length();
		arr = new long[this.len];
		for (int i = len - 1; i > 0; i--) {
			arr[len - 1 - i] = s.charAt(i) - '0';
		}
	}

	public Num(long x) {
		this(x + "");
	}

	public static Num add(Num a, Num b) throws Exception {

		// Define positive/negative
		if (a.isNegative && !b.isNegative && a.compareTo(b) > 0) {
			return subtractHelper(b, a);
		}
		if (!a.isNegative && b.isNegative && a.compareTo(b) < 0) {
			return subtractHelper(b, a);
		}

		// perform add and return
		Num result = addHelper(a, b);
		if (a.isNegative && b.isNegative) {
			result.isNegative = a.isNegative;
		}
		return result;

	}

	public static Num addHelper(Num a, Num b) throws Exception {

		String sumStr = "";
		if (a.base != b.base) {
			throw new Exception();
		}
		// perform addition
		int i = 0, j = 0;
		long carry = 0, sum = 0;
		while (i < a.len && j < b.len) {
			sum = a.arr[i] + b.arr[j] + carry;
			sumStr += sum % a.base;
			carry = sum / a.base;
			i++;
			j++;
		}
		while (i < a.len) {
			sum = a.arr[i] + carry;
			sumStr += sum % a.base;
			carry = sum / a.base;
			i++;
		}
		while (j < b.len) {
			sum = b.arr[j] + carry;
			sumStr += sum % a.base;
			carry = sum / a.base;
			j++;
		}
		if (carry != 0) {
			sumStr += carry;
		}
		return new Num(sumStr);
	}

	public static Num subtract(Num a, Num b) throws Exception {
		String subStr = "";
		if (a.isNegative != b.isNegative) {
			Num result = addHelper(a, b);
			result.isNegative = a.isNegative;
			return result;
		}

		Num result = subtractHelper(a, b);
		return result;
	}

	public static Num subtractHelper(Num a, Num b) {
		String subStr = "";
		if (a.compareTo(b) > 0) {

		}

		return new Num(subStr);
	}

	public static Num product(Num a, Num b) {
		return null;
	}

	// Use divide and conquer
	public static Num power(Num a, long n) {
		return null;
	}

	// Use binary search to calculate a/b
	public static Num divide(Num a, Num b) {
		return null;
	}

	// return a%b
	public static Num mod(Num a, Num b) {
		return null;
	}

	// Use binary search
	public static Num squareRoot(Num a) {
		return null;
	}

	// Utility functions
	// compare "this" to "other": return +1 if this is greater, 0 if equal, -1
	// otherwise
	public int compareTo(Num other) {
		return 0;
	}

	// Output using the format "base: elements of list ..."
	// For example, if base=100, and the number stored corresponds to 10965,
	// then the output is "100: 65 9 1"
	public void printList() {
	}

	// Return number to a string in base 10
	public String toString() {
		Num base10;
		String result = "";

		try {
			base10 = this.convertToBase10();
			for (int i = 0; i < base10.len - 1; i++) {
				result = base10.arr[i] + result;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public long base() {
		return base;
	}

	public Num convertToBase10() throws Exception {
		// convert to decimal - horner's method
		Num inBase10 = new Num(0);
		for (int i = 0; i <= this.len - 1; i++) {
			Num part = new Num((long) (this.arr[i] * Math.pow(10, i))); // in base 10 so we can call the constructor
			add(inBase10, part);
		}
		inBase10.base = 10;
		return inBase10;
	}

	// Return number equal to "this" number, in base=newBase
	public Num convertBase(int newBase) throws Exception {
		Num inNewBase = this;
		// convert to decimal
		if (this.base != 10) {
			inNewBase = this.convertToBase10();
		}
		if (newBase != 10) {
			// convert to new base from decimal
			Num newbase = new Num(newBase);
			int size = (int) Math.ceil(((inNewBase.len + 1) / Math.log10(newBase)) + 1);
			long[] newNumArr = new long[size];
			int i = 0;
			while (inNewBase.len == 1 && inNewBase.arr[0] == 0) {
				newNumArr[i] = mod(inNewBase, newbase).arr[0];
				inNewBase = divide(inNewBase, newbase);
				i++;
			}
		}
		inNewBase.base = newBase;
		return inNewBase; // call truncate here
	}

	// Divide by 2, for using in binary search
	public Num by2() {
		long[] newNum;
		if (this.base == 2) {
			newNum = new long[this.len - 1];
			for (int i = 1; i < len; i++) {
				newNum[i - 1] = this.arr[i];
			}

		} else {
			newNum = new long[this.len];
			long carry = 0;
			for (int i = 0; i < len; i++) {
				long num = (carry + this.arr[this.len - 1 - i]);
				carry = num % 2;
				newNum[i] = num / 2;
			}
		}
		return new Num(newNum, this.base);
	}

	// Evaluate an expression in postfix and return resulting number
	// Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
	// a number: [1-9][0-9]*. There is no unary minus operator.
	public static Num evaluatePostfix(String[] expr) {
		return null;
	}

	// Evaluate an expression in infix and return resulting number
	// Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
	// a number: [1-9][0-9]*. There is no unary minus operator.
	public static Num evaluateInfix(String[] expr) {
		return null;
	}

	public static void main(String[] args) throws Exception {
		Num x = new Num(999);
		Num y = new Num("8");
		Num z = Num.add(x, y);
		System.out.println(z);
		Num a = Num.power(x, 8);
		System.out.println(a);
		if (z != null)
			z.printList();
	}
}