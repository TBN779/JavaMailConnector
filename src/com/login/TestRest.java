package com.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

/**
 * Main.java
 *
 * @author ibpmcoing.blogspot.com
 */
public class TestRest {

	/**
	 * Extends the size of an array.
	 */
	static final String username = "pradee"; // your account name
	static final String password = "pradeep"; // your password for the account

	static class MyAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			System.err.println("Feeding username and password for "+ getRequestingScheme() + " authentication");
			return (new PasswordAuthentication(username, password.toCharArray()));
		}
	}

	public void sendPostRequest() {

		// Build parameter string
		String data = "action=start&params={'a':'1','b':'2'}&createTask=false&parts=all";
		try {

			// Send the request
			URL url = new URL(
					"http://bpmserver:9081/rest/bpm/wle/v1/service/POC@GetSumOfNumbers");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Accept", "application/json");

			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream());

			// write parameters
			writer.write(data);
			writer.flush();

			// Get the response
			StringBuffer answer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				answer.append(line);
			}
			writer.close();
			reader.close();

			// Output the response
			System.out.println(answer.toString());

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Starts the program
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		Authenticator.setDefault(new MyAuthenticator()); // provides basic
															// authentication
		new TestRest().sendPostRequest();
	}
}