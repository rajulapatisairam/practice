package com.practice.utils;

public class Patterns {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int outer=9;
		
		for(int outer2=outer;outer>0;outer--){
			for(int inner=1;inner<=outer;inner++)
			{
				System.out.print(inner);
			}
		
			for(int inner=outer,inner2=outer2;inner>0;)
			{
				if(inner2-->outer) if(outer<10)System.out.print("  ");else System.out.print("  ");
				else System.out.print(inner--);
			}
			
			System.out.println();
		}

	}

}
