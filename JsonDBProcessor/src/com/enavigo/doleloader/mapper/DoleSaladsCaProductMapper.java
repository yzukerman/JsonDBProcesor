package com.enavigo.doleloader.mapper;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

public class DoleSaladsCaProductMapper implements JsonMapper {

	@Override
	public Object mapJson(JsonNode tree) {
		
		System.out.println("In mapJson");
		
		System.out.println(tree.size());
		Iterator<String> fieldNames = tree.fieldNames();
		while (fieldNames.hasNext())
		{
			System.out.println(fieldNames.next());
		}
		Iterator<JsonNode> iterator = tree.elements();
		while(iterator.hasNext())
		{
			JsonNode currentNode = iterator.next();
			System.out.println("Node size: " + currentNode.size());
			Iterator<JsonNode> productIterator = currentNode.elements();
			while(productIterator.hasNext())
			{
				JsonNode productNode = productIterator.next();
				System.out.println(productNode.get("title"));
			}
		}
		return null;
	}

}
