package com.enavigo.doleloader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.enavigo.doleloader.mapper.DoleSaladsCaProductMapper;
import com.enavigo.doleloader.mapper.JsonMapper;
import com.enavigo.doleloader.persistence.DoleJsonPersistor;
import com.enavigo.doleloader.pojo.Product;

public class DBLoader {
	

	public static void main(String[] args) {
			
		
		ObjectMapper mapper = new ObjectMapper();
		Connection connection = null;
		List<HashMap<String, String>> tasks = null;
		
		// load tasks from config file
		try
		{
			tasks = loadTasks();
		}
		catch (JsonProcessingException jpe)
		{
			System.out.println("Failed to open config file." + jpe.getLocalizedMessage());
		}
		catch (IOException ioe)
		{
			System.out.println("Failed to open config file." + ioe.getMessage());
		}
		
		
		try {
            // The newInstance() call is a work around for some
            // broken Java implementations
			connection = DriverManager.getConnection("jdbc:mysql://localhost/dole_db?" +
				                                   "user=root&password=ima711");
			System.out.println("DB Connection established...");
			
			for(HashMap<String, String> task : tasks)
			{
				JsonNode tree = mapper.readTree(new File(task.get("source-file")));
				JsonMapper jsonMapper;
				jsonMapper =
				  (JsonMapper) Class.forName(task.get("mapper")).newInstance();
				System.out.println("Step: " + task.get("name") + "... ");
				char siteCode = task.get("source-site").charAt(0);
				Object mapResult = jsonMapper.mapJson(tree);
				
				DoleJsonPersistor jsonPersistor = 
						(DoleJsonPersistor) Class.forName(task.get("persistor")).newInstance();
				jsonPersistor.persist(connection, mapResult, siteCode);
				
				System.out.println("Step: " + task.get("name") + " DONE. ");
			}
			
			
        } catch (Exception ex) {
            // handle the error
        	System.out.println("Error: " + ex.getMessage());
        	ex.printStackTrace();
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
	
	private static List<HashMap<String, String>> loadTasks() throws JsonProcessingException, IOException
	{
		List<HashMap<String, String>> tasks = new ArrayList<HashMap<String, String>>();
		ObjectMapper mapper = new ObjectMapper();
		
		
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			//JsonNode taskTree = mapper.readTree(new File("./config/dole-config.json"));
			JsonNode taskTree = mapper.readTree(new File("./config/dole-salads.json"));
			
			JsonNode taskList = taskTree.get("tasks");
			Iterator<JsonNode> taskIterator = taskList.elements();
			while(taskIterator.hasNext())
			{
				JsonNode taskNode = taskIterator.next();
				HashMap<String, String> task = new HashMap<String, String>();
				task.put("name", taskNode.get("name").asText());
				task.put("source-file", taskNode.get("source-file").asText());
				task.put("mapper", taskNode.get("mapper").asText());
				task.put("source-site", taskNode.get("source-site").asText());
				task.put("persistor", taskNode.get("persistor").asText());
				tasks.add(task);
			}
			
			return tasks;
	}

}
