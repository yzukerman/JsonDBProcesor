package com.enavigo.doleloader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.enavigo.doleloader.mapper.DoleSaladsCaProductMapper;
import com.enavigo.doleloader.mapper.JsonMapper;
import com.enavigo.doleloader.pojo.Product;

public class DBLoader {
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Guess who's trying to be back?...");
		
		Product recipe = new Product();
		ObjectMapper mapper = new ObjectMapper();
		Connection connection = null;
		List<Product> recipes = new ArrayList<Product>();
		
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations
			connection = DriverManager.getConnection("jdbc:mysql://localhost/dole_db?" +
				                                   "user=root&password=ima711");
			System.out.println("DB Connection established...");
			
			JsonNode tree = mapper.readTree(new File("/Users/yuvalzukerman/Dropbox/Enavigo/Clients/Dole/Extracts/dolesalads.ca/dole-salads-ca-products.json"));
//			Class c = Class.forName("com.enavigo.doleloader.mapper.DoleSaladsCaJsonMapper");
//			JsonMapper jsonMapper = new DoleSaladsCaJsonMapper();
//			jsonMapper = (JsonMapper) c.cast(jsonMapper);
			
			

			JsonMapper jsonMapper;
			jsonMapper =
			  (JsonMapper) Class.forName("com.enavigo.doleloader.mapper.DoleSaladsCaProductMapper").newInstance();
			jsonMapper.mapJson(tree);
//			System.out.println(tree.size());
//			Iterator<String> fieldNames = tree.fieldNames();
//			while (fieldNames.hasNext())
//			{
//				System.out.println(fieldNames.next());
//			}
//			Iterator<JsonNode> iterator = tree.elements();
//			while(iterator.hasNext())
//			{
//				JsonNode currentNode = iterator.next();
//				System.out.println("Node size: " + currentNode.size());
//				Iterator<JsonNode> productIterator = currentNode.elements();
//				while(productIterator.hasNext())
//				{
//					JsonNode productNode = productIterator.next();
//					System.out.println(productNode.get("title"));
//				}
//			}
			
			
        } catch (Exception ex) {
            // handle the error
        	System.out.println("Error: " + ex.getMessage());
        }
		finally
		{
			try
			{
				if (connection != null)
				{
					connection.close();
					System.out.println("DB Connection closed");
				}
			}
			catch(Exception e)
			{
				System.out.println("Issue closing DB connection: " + e.getMessage());
			}
				
		}
	}

}
