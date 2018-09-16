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
		this.len = (int) Math.ceil(((s.length() + 1) / Math.log10(this.base)) + 1);
		arr = new long[this.len];
		for (int i = len - 1; i > 0; i--) {
			arr[len - 1 - i] = s.charAt(i);
		}
	}

	public Num(long x) {
		this(x + "");
	}

	public static Num add(Num a, Num b) {

		String sumStr = "";

		// Define positive/negative
		if (a.isNegative && !b.isNegative && a.compareTo(b) > 0) {
			return subtract(b, a, sumStr);
		}
		if (!a.isNegative && b.isNegative && a.compareTo(b) < 0) {
			return subtract(b, a, sumStr);
		}
		if (a.isNegative && b.isNegative) {
			sumStr += "-";
		}
		// perform add and return
		return add(a, b, sumStr);

	}

	public static Num add(Num a, Num b, String sumStr) {

		// perform addition
		int i = 0, j = 0;
		long carry = 0, sum = 0;
		while (i < a.len && j < b.len) {
			sum = a.arr[i] + b.arr[j] + carry;
			sumStr += sum % base;
			carry = sum / base;
			i++;
			j++;
		}
		while (i < a.len) {
			sum = a.arr[i] + carry;
			sumStr += sum % base;
			carry = sum / base;
			i++;
		}
		while (j < b.len) {
			sum = b.arr[j] + carry;
			sumStr += sum % base;
			carry = sum / base;
			j++;
		}
		if (carry != 0) {
			sumStr += carry;
		}
		return new Num(sumStr);
	}

	public static Num subtract(Num a, Num b) {
		String subStr = "";
		if (a.isNegative != b.isNegative) {
			Num result = add(a, b, subStr);
			result.isNegative = a.isNegative;
			return result;
		}

		return subtract(a, b, subStr);

	}

	public static Num subtract(Num a, Num b, String subStr) {
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
		return null;
	}

	public long base() {
		return base;
	}

	// Return number equal to "this" number, in base=newBase
	public Num convertBase(int newBase) {
		return null;
	}

	// Divide by 2, for using in binary search
	public Num by2() {
		return null;
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

	public static void main(String[] args) {
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