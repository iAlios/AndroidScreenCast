package com.greatline.alio.android.screencast.test;

public class StringFormatTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		print(String.format("hello, this is %s.", "lili"));
		print(String.format("hello, this is %d.", 1111));
		print(String.format("hello, this is %c heehe .", 'c'));
		print(String.format("hello, this is %d.", 123333333333333l));
		print(String.format("hello, this is %f.",
				23333333333322323223233121.111111111d));

		/**
		 * 对整数进行格式化：%[index$][标识][最小宽度]转换方式
		 *  '-' 在最小宽度内左对齐，不可以与“用0填充”同时使用 
		 *  '#' 只适用于8进制和16进制，8进制时在结果前面增加一个0，16进制时在结果前面增加0x 
		 *  '+' 结果总是包括一个符号（一般情况下只适用于10进制，若对象为BigInteger才可以用于8进制和16进制）
		 *  ' ' 正值前加空格，负值前加负号（一般情况下只适用于10进制，若对象为BigInteger才可以用于8进制和16进制） 
		 *  '0' 结果将用零来填充
		 *  ',' 只适用于10进制，每3位数字之间用“，”分隔 
		 *  '(' 若参数是负数，则结果中不添加负号而是用圆括号把数字括起来（同‘+’具有同样的限制）
		 */
		print(String.format("%1$,09d", -3123));
		print(String.format("%1$9d", -31));
		print(String.format("%1$-9d", -31));
		print(String.format("%1$(9d", -31));
		print(String.format("%1$#9x", 5689));
	}

	private static void print(String text) {
		System.out.println(text);
	}

}
