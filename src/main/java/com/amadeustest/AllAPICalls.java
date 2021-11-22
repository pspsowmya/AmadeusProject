package com.amadeustest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.amadeus.Amadeus;
import com.amadeus.Response;
import com.amadeus.exceptions.ResponseException;

public class AllAPICalls {
	
	private static String apiKey = "<<apikey>>";
	private static String apiSecret = "<<apisecret>>";
	static List datesArray = new ArrayList();
	
	public static String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public static String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public static List setDatesList() {
		datesArray.add("2021-11-22");
		datesArray.add("2021-11-23");
		datesArray.add("2021-11-24");
		datesArray.add("2021-11-25");
		datesArray.add("2021-11-26");
		datesArray.add("2021-11-27");
		datesArray.add("2021-11-28");
		datesArray.add("2021-11-29");
		datesArray.add("2021-11-30");
		datesArray.add("2021-12-01");
		datesArray.add("2021-12-02");
		datesArray.add("2021-12-03");
		datesArray.add("2021-12-04");
		datesArray.add("2021-12-05");
		datesArray.add("2021-12-06");
		return datesArray;
	}
	
	
		
	public static void main(String[] args) throws ResponseException, IOException, EncryptedDocumentException, InvalidFormatException {
		setDatesList();
		
		FlightAvailabilityMultipleCallsAMStoDEL f=new FlightAvailabilityMultipleCallsAMStoDEL();
		f.allAPICalls();
		
		FlightAvailabilityMultipleCallsAUHtoHYD f1 = new FlightAvailabilityMultipleCallsAUHtoHYD();
		f1.allAPICalls();
		
			}

}
