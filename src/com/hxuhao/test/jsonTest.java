package com.hxuhao.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hxuhao.utils.JsonUtils;

public class jsonTest {
	
	@Test
	public void test(){
		Map<String,String> myMap = new HashMap<String,String>();
		myMap.put("name", "hxuhao");
		myMap.put("age", "21");
		String jsonString = JsonUtils.encode(myMap);
		System.out.println(jsonString);
		for(String key:myMap.keySet()){
			System.out.println(key + " : " + myMap.get(key));
		}
		HashMap<String,String> jsonMap = JsonUtils.decode(jsonString, HashMap.class);

		for(String key:jsonMap.keySet()){
			System.out.println(key + " : " + jsonMap.get(key));
		}
	}
}
