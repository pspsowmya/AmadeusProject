package com.amadeustest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.Response;
import com.amadeus.exceptions.ResponseException;

public class FlightAvailabilityMultipleCallsAUHtoHYD {
	static List durationArray = new ArrayList();
	static List numberArray = new ArrayList();
	static List carrierCodeArray = new ArrayList();
	static List arrivalCodeArray = new ArrayList();
	static List arrivalTimeArray = new ArrayList();
	static List departureCodeArray = new ArrayList();
	static List departureTimeArray = new ArrayList();
	static List operatorCarrierCodeArray = new ArrayList();
	static List aircraftCodeArray = new ArrayList();
	static List totalSeatsArray = new ArrayList();
	

	// Amadeus authorization creds to generate access token
	//Amadeus amadeus = Amadeus.builder("emcOTJUDtdaLuXc5euAHaLNAKHDNFknX", "2aEMOAogWDuGj55C").build();
	Amadeus amadeus=Amadeus.builder(AllAPICalls.getApiKey(), AllAPICalls.getApiSecret()).build(); 
	static String body;

	public void apiCall() throws ResponseException {
		// Creation of Request body
		for(int i=0;i<AllAPICalls.datesArray.size();i++) {
			body = "{\"originDestinations\":[{\"id\":\"1\",\"originLocationCode\":\"AUH\","
					+ "\"destinationLocationCode\":\"HYD\",\"departureDateTime\":{\"date\":\""
					+ AllAPICalls.datesArray.get(i)+"\"}}],\"travelers\":[{\"id\":\"1\",\"travelerType\":\"ADULT\"}],"
					+ "\"searchCriteria\" : \r\n" + "          {\r\n" + "              \"flightFilters\": {\r\n"
					+ "                 \"carrierRestrictions\" : {\r\n"
					+ "                            \"includedCarrierCodes\" : [\"KL\",\"AF\",\"UA\",\"EK\",\"BA\",\"EY\",\"AA\",\"WN\",\"U2\",\"AC\",\"6E\"]\r\n"
					+ "                                         },\r\n" + "                \"connectionRestriction\": {\r\n"
					+ "                            \"maxNumberOfConnections\" : \"0\"\r\n"
					+ "                                          }\r\n" + "                                }\r\n"
					+ "          }\r\n" + "  ,\r\n" + "\"sources\":[\"GDS\"]}";
			
			addaDatatoArrayList();
		}

	}

	public void addaDatatoArrayList() throws ResponseException {
		// API call to get response
		
		Response response = amadeus.post("/v1/shopping/availability/flight-availabilities", body);
		
		if(response.getStatusCode()!=200)
		{
			System.out.println("An error occurred..!! Please try again Later.");
		}
		else
		{
			String jsonResponse = response.getResult().toString();
			// Printing Data json
			System.out.println(jsonResponse);
			// Parsing JSON response
			JSONObject obj = new JSONObject(jsonResponse);
			if(obj.has("data")) {
				JSONArray data = obj.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {

					String duration = (String) data.getJSONObject(i).get("duration");

					JSONArray segments = data.getJSONObject(i).getJSONArray("segments");
					for (int j = 0; j < segments.length(); j++) {
						String number = (String) segments.getJSONObject(j).get("number");

						String carrierCode = (String) segments.getJSONObject(j).get("carrierCode");

						JSONObject arrival = segments.getJSONObject(j).getJSONObject("arrival");
						String iataCode = arrival.getString("iataCode");

						String arrivalTime = arrival.getString("at");

						JSONObject departure = segments.getJSONObject(j).getJSONObject("departure");
						String depiataCode = departure.getString("iataCode");

						String departureTime = departure.getString("at");
						String opcarrierCode;
						if(segments.getJSONObject(j).has("operating")) {
							JSONObject operating = segments.getJSONObject(j).getJSONObject("operating");
							opcarrierCode = operating.getString("carrierCode");
						}
						else
						{
							opcarrierCode = "opcarrier code not present";
						}

						JSONObject aircraft = segments.getJSONObject(j).getJSONObject("aircraft");
						String aircraftCode = aircraft.getString("code");

						int totalSeats = 0;
						JSONArray availabilityClasses = segments.getJSONObject(j).getJSONArray("availabilityClasses");
						for (int k = 0; k < availabilityClasses.length(); k++) {
							int numberOfBookableSeats = (int) availabilityClasses.getJSONObject(k).get("numberOfBookableSeats");
							totalSeats = totalSeats + numberOfBookableSeats;
						}
						//Adding all the data to arraylists
						durationArray.add(duration);
						numberArray.add(number);
						carrierCodeArray.add(carrierCode);
						arrivalCodeArray.add(iataCode);
						arrivalTimeArray.add(arrivalTime);
						departureCodeArray.add(depiataCode);
						departureTimeArray.add(departureTime);
						operatorCarrierCodeArray.add(opcarrierCode);
						aircraftCodeArray.add(aircraftCode);
						totalSeatsArray.add(totalSeats);
					}
				}
			}
			else {
				System.out.println("No data present");
			}
		}
	}


	public void allAPICalls() throws ResponseException, IOException, EncryptedDocumentException, InvalidFormatException {
		// Amadeus authorization creds to generate access token
		//Amadeus amadeus = Amadeus.builder("emcOTJUDtdaLuXc5euAHaLNAKHDNFknX", "2aEMOAogWDuGj55C").build();
		apiCall();

		//Writing data to excel
		FileInputStream inputStream = new FileInputStream("D://FlightAvailabilty.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet =  workbook.createSheet("AUHtoHYDFlightAvailability");
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Timestamp");
		header.createCell(1).setCellValue("Duration");
		header.createCell(2).setCellValue("Departure Iata Code");
		header.createCell(3).setCellValue("Departure Time");
		header.createCell(4).setCellValue("Arrival Iata Code");
		header.createCell(5).setCellValue("Arrival Time");
		header.createCell(6).setCellValue("Carrier Code");
		header.createCell(7).setCellValue("Aircraft Number");
		header.createCell(8).setCellValue("Aircraft Code");
		header.createCell(9).setCellValue("No of Bookable Seats");
		header.createCell(10).setCellValue("Operator Carrier Code");
		//Checking if all the arraylists are of same size 
		if(durationArray.size()!=departureCodeArray.size() || durationArray.size()!= departureTimeArray.size()
				|| durationArray.size()!=arrivalCodeArray.size() || durationArray.size() != arrivalTimeArray.size()
				|| durationArray.size() != carrierCodeArray.size() || durationArray.size() != numberArray.size()
				|| durationArray.size() != aircraftCodeArray.size() || durationArray.size() != totalSeatsArray.size()
				|| durationArray.size() != operatorCarrierCodeArray.size()) {

			throw new IllegalStateException("Some data Missing..!! Please try Again..!!");
		}
		//Looping through the arrayLists 
		for(int m=0;m<durationArray.size();m++) {
			Row row = sheet.createRow((short) m+1);
			row.createCell(0).setCellValue(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
			row.createCell(1).setCellValue((String) durationArray.get(m));
			row.createCell(2).setCellValue((String)departureCodeArray.get(m));
			row.createCell(3).setCellValue((String)departureTimeArray.get(m));
			row.createCell(4).setCellValue((String)arrivalCodeArray.get(m));
			row.createCell(5).setCellValue((String)arrivalTimeArray.get(m));
			row.createCell(6).setCellValue((String)carrierCodeArray.get(m));
			row.createCell(7).setCellValue((String)numberArray.get(m));
			row.createCell(8).setCellValue((String)aircraftCodeArray.get(m));
			row.createCell(9).setCellValue((Integer)totalSeatsArray.get(m));
			row.createCell(10).setCellValue((String)operatorCarrierCodeArray.get(m));	
		}
		try {
			FileOutputStream outputStream = new FileOutputStream("D://FlightAvailabilty.xlsx");
			workbook.write(outputStream); 
		}
		finally {
			workbook.close();
		}
		System.out.println("Data inserted Successfully");	
	}

}




