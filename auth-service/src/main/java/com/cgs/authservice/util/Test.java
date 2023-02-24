package com.cgs.authservice.util;

public class Test {
	
	public static void main(String[] args) {
		String expdate = "202908";
        String year = expdate.substring(2, 4);
        String month = expdate.substring(4, 6);
        System.out.println(year+month);
        System.out.println(month+"/"+year);
	}

}