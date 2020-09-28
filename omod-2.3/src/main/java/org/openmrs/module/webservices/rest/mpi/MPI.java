/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.webservices.rest.mpi;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.openmrs.scheduler.tasks.TestTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;

/*
 * During registration if ADHAR is available enter column-Person Updated = PERNO  else Person Updated= NA
   During registration if ADHAR is available enter column-Patient Updated = PATNO  else Patient Updated= NA
   
   If later he brings ADHAR update Person Updated = PERNO
   								   Patient Updated = PATNO
   								   
  MPI ID = ID received from central instance on insertion. 								   
  MPI ID will be inserted into the MPI ID attribute of person by appending MPI = "MPI"+MPI retrieved.
   								   
   ### For these columns we only need to call person service.
 * 
 */

public class MPI extends AbstractTask {
	
	// Logger 
	private static final Logger log = LoggerFactory.getLogger(TestTask.class);
	
	String jsonstr = "";
	
	/**
	 * @see org.openmrs.scheduler.tasks.AbstractTask#initialize(TaskDefinition)
	 */
	@Override
	public void initialize(TaskDefinition taskDefinition) {
		log.info("Initializing task " + taskDefinition);
		
	}
	
	/**
	 * @see org.openmrs.scheduler.tasks.AbstractTask#execute()
	 */
	@Override
	public void execute() {
		try {
			
			/*
			 * String url = "http://localhost:8080/openmrs/ws/rest/v1/person?q=9509374207";
			 * String name = "admin"; String password = "Admin123"; String authString = name
			 * + ":" + password; String authStringEnc =
			 * Base64.getEncoder().encodeToString(authString.getBytes());
			 * System.out.println("Base64 encoded auth string: " + authStringEnc); Client
			 * restClient = Client.create(); WebResource webResource =
			 * restClient.resource(url); ClientResponse response =
			 * webResource.accept("application/json") .header("Authorization", "Basic " +
			 * authStringEnc) .get(ClientResponse.class);
			 * 
			 * if (response.getStatus() != 200) { throw new
			 * RuntimeException("Failed : HTTP error code : " + response.getStatus()); }
			 * 
			 * String output = response.getEntity(String.class);
			 * 
			 * System.out.println("Output from Server .... \n");
			 *System.out.println(output);
			 */
			
			List<Person> plist = new ArrayList<Person>();
			plist = personwithAdhar();
			
			for (Person person : plist) {
				System.out.println(ObjectToJson(person));
				if (person.getIsPatient()) {
					Patient pat = new Patient();
					pat = Context.getPatientService().getPatientByUuid(person.getUuid());
					
					System.out.println(ObjectToJson(person));
					
				}
				
			}
			
		}
		catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	/**
	 * @see org.openmrs.scheduler.tasks.AbstractTask#shutdown()
	 */
	@Override
	public void shutdown() {
		log.info("Shutting down task ...");
		super.shutdown();
	}
	
	public List<Person> personwithAdhar() {
		
		List<Person> plist = new ArrayList<Person>();
		
		plist = Context.getPersonService().getPeople("PERNO", false, false);
		
		return plist;
		
	}
	
	public String updatePerson() {
		return "";
	}
	
	public String updatePatient() {
		return "";
	}
	
	public String ObjectToJson(Person per) {
		jsonstr = "";
		Person pers = new Person();
		per.set
		try {
			ObjectMapper Obj = new ObjectMapper();
			jsonstr = Obj.writeValueAsString(per);
		}
		catch (Exception ex) {
			
		}
		
		return jsonstr;
	}
	
}
