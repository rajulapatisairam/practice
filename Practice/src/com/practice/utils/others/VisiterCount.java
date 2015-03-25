package com.practice.utils.others;

import java.io.IOException;

public class VisiterCount {

	private static final String URL="http://www.javanotepad.com/2015/03/localhostchange.html";
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int count=100;
		while(count-->0)
		{
		Process p = Runtime.getRuntime().exec(
				"C:\\Program Files\\Internet Explorer\\iexplore.exe \""
						+ URL + "\"");
		Thread.sleep(5000);
		p.destroy();
		System.out.println("executed! " + p.waitFor());
		}
	}

}
