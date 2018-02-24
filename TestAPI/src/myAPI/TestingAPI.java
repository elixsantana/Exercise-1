package myAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject; 

public class TestingAPI {
	private static long totalExecutionTime = 0;
	
	public static void main(String[] args)  {
		try {
			TestingAPI.negativeNumberTest(); // Name as negative number 
			TestingAPI.correctInputTest(); // Good input
			TestingAPI.extraEndpointTest(); // using 2 endpoints
		//	TestingAPI.requestOverloadTest(); // Too many requests     	
		} catch (IOException exception) {
			System.out.println("Input/Output Problem ");
			System.err.println(exception.getMessage());
		}
	} // End of main
	
	private static JSONObject apiCallOut(final String url) throws IOException, JSONException {
	
		long startTime = System.currentTimeMillis();
		String genderizeAPI = url;
		URL obj = new URL(genderizeAPI);
		HttpURLConnection connectToAPI = (HttpURLConnection) obj.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connectToAPI.getInputStream()));
        String inputLine;
        StringBuffer convertion = new StringBuffer();
        while ((inputLine = in.readLine()) != null  && inputLine != "") {
        	if (inputLine!="[" || inputLine!="]") {
        			convertion.append(inputLine);
        	}	
        	
        } 
        in.close();
        
      //To JSON
        String conv = convertion.toString();
		JSONObject myResponse;
		myResponse = new JSONObject(conv);
        long endTime = System.currentTimeMillis();
		totalExecutionTime = endTime - startTime;
		
		return myResponse;
	}
	
	// this method tests beyond the edge cases.
	private static void negativeNumberTest() throws IOException{ // Test 1
		String test1 = "-1";  //CRITERIA TO TEST
		System.out.println("\nTest 1: Name as negative number");
		JSONObject resp;
		try {
			resp = (apiCallOut("https://api.genderize.io/?name="+test1));
			if ( resp.getString("name").equals(test1))
			{
				System.out.println("Name Matched: Test successful");
			}
		} catch (JSONException e) {
			System.out.println("Gender: Null");
		}
		System.out.printf("Total execution time in ms: %d \n", totalExecutionTime);
		
	
	}
	
	// this method tests using the proper api request.
	private static void correctInputTest() throws IOException { // Test 2
		String test2 = "Peter";  //CRITERIA TO TEST
		System.out.println("\nTest 2: Proper input");
		JSONObject resp;
		try {
			resp = (apiCallOut("https://api.genderize.io/?name="+test2));
			if ( resp.getString("name").equals(test2))
			{
				System.out.println("Name Matched: Test successful");
			}
		} catch (JSONException e) {
			System.out.println("Invalid");
			System.err.println(e.getMessage());
		}
		System.out.printf("Total execution time in ms: %d \n", totalExecutionTime);
		
	
	}
	
	// this method tests using 2 endpoints.
		private static void extraEndpointTest() throws IOException { // Test 3
			String test3 = "Kim";   //CRITERIA TO TEST
			String gender ="male" ; //CRITERIA TO TEST
			System.out.println("\nTest 3: Extra endpoint:  ");
			JSONObject resp;
			try {
				resp = (apiCallOut("https://api.genderize.io/?name=" +test3 + "&country_id=dk"));
				if ( resp.getString("name").equals(test3))
				{
					System.out.println("Name Matched: Test successful");
				}
				
				if ( resp.getString("gender").equals(gender))
				{
					System.out.println("Gender Matched: Test successful");
				}
			} catch (JSONException e) {
				System.out.println("Null");
			}
			System.out.printf("Total execution time in ms: %d \n", totalExecutionTime);
			
		}
	
	// this method tests if it limits your requests to 1000/day
	private static void requestOverloadTest() throws IOException //Test 4
	{
		long startTime = System.currentTimeMillis();
		URL obj;
		int responseCodeFromAPI = 0;
		HttpURLConnection connectToAPI;
		String genderizeAPI = "https://api.genderize.io/?name=elix";
			
		for (int i = 1003; i > 1; i--) {
			obj = new URL(genderizeAPI);
			connectToAPI = (HttpURLConnection) obj.openConnection();
			responseCodeFromAPI = connectToAPI.getResponseCode();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("\nTest 3: Over 1000 requests " );
		response(responseCodeFromAPI);
		System.out.printf("Total execution time in ms: %d \n", (startTime - endTime));
	}
	
	private static void response (int r)
	{
		if (r == 200) {
			System.out.println("Requests<1000 " );
		} else if (r == 400) {
			System.out.println("Bad request " );		
		} else if (r == 429) {
			System.out.println("Exceeded requests per day" );
		} else if (r == 500) {
			System.out.println("Internal Server error " );
		} else {
			System.out.println("Unknown error" );
		}
	}
	

}// End of Class