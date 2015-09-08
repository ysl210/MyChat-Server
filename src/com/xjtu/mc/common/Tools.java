package com.xjtu.mc.common;

public class Tools {
	public static byte[] encrypt(byte[] plain,String account){
		for(int i=0;i<plain.length;i++){
			plain[i]=(byte) (plain[i]&0x01);
		}
		return plain;
	}
}
