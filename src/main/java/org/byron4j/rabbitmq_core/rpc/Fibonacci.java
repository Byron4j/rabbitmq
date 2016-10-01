package org.byron4j.rabbitmq_core.rpc;

/**
 *  
 *	@author  	Byron.Y.Y
 *  @optDate 	2016年9月28日
 *  This comment is ...
 */
public class Fibonacci {

	/**
	 * 生产斐波那契数列
	 * @param n
	 * @return
	 */
	public static int generate(int n){
		if(n <= 0) return 0;
		if(n == 1) return 1;
		if(n > 30) {System.err.println("基数过大,计算失败,return a negtive value: "); return -1;};
		return generate(n - 1) + generate(n - 2);
	}

	
	public static void main(String[] args) {
//		System.out.println(generate(0));
//		System.out.println(generate(1));
//		System.out.println(generate(2));
//		System.out.println(generate(3));
//		System.out.println(generate(4));
//		System.out.println(generate(5));
//		System.out.println(generate(10));
		System.out.println(generate(50));
	}
}
