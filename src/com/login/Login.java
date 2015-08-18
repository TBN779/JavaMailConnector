package com.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.lombardisoftware.core.xml.WPSDataValue.Base64;

public class Login {

	public static void main(String[] args) throws NoSuchAlgorithmException,
			KeyManagementException, MalformedURLException, IOException {

		String host = "bpm856pc.corp.coutureconsulting.com";
		String port = "9443";
		String protocol = "https://";
		String link = protocol.concat(host);
		String link1 = link.concat(":");
		String link2 = link1.concat(port);
		String finallink = link2.concat("/rest/bpm/wle/v1/task/7157?parts=all");
		String username = "deadmin";
		String password = "teamworks";

		SSLContext sc = SSLContext.getInstance("SSL");

		TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
			public X509Certificate[] getAcceptedIssuers() { return null; }
		};

		sc.init(null, new TrustManager[] { tm }, new SecureRandom());

		SSLSocketFactory sf = sc.getSocketFactory();

		HttpsURLConnection urlCon = (HttpsURLConnection) new URL(finallink).openConnection();
		((HttpsURLConnection) urlCon).setSSLSocketFactory(sf);

		// LOGIN
		String authString = username + ":" + password;
		String authEncBytes = Base64.encode(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		urlCon.setRequestProperty("Authorization", "Basic " + authStringEnc);

		// get data
		final InputStream input = urlCon.getInputStream();
		String result = getStringFromInputStream(input);
		System.out.println(result);
		System.out.println("Done");
		
		
		
	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

}
