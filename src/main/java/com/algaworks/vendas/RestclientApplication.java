package com.algaworks.vendas;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.algaworks.vendas.springsample.hello.Quote;

/***
 *
 * https://stackoverflow.com/questions/1725863/why-cant-i-find-the-truststore-for-an-ssl-handshake
 * http://forum.spring.io/forum/spring-projects/web-services/52629-disabling-hostname-verification
 * 
 */
@SpringBootApplication
public class RestclientApplication {

	private static final Logger log = LoggerFactory.getLogger(RestclientApplication.class);

	private static final String URL = "https://gturnquist-quoters.cfapps.io/api/random";

	public static void main(String[] args) {
		SpringApplication.run(RestclientApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});

		return builder.build();

	}

	private void testDisableSSL() {
		try {
			String keystoreType = "JKS";
			InputStream keystoreLocation = null;
			char[] keystorePassword = null;
			char[] keyPassword = null;

			KeyStore keystore;
			keystore = KeyStore.getInstance(keystoreType);
			keystore.load(keystoreLocation, keystorePassword);
			KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmfactory.init(keystore, keyPassword);

			InputStream truststoreLocation = null;
			char[] truststorePassword = null;
			String truststoreType = "JKS";

			KeyStore truststore = KeyStore.getInstance(truststoreType);
			truststore.load(truststoreLocation, truststorePassword);
			TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

			KeyManager[] keymanagers = kmfactory.getKeyManagers();
			TrustManager[] trustmanagers = tmfactory.getTrustManagers();

			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(keymanagers, trustmanagers, new SecureRandom());
			SSLContext.setDefault(sslContext);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) {
		// CommandLineRunner runner = new CommandLineRunner() {
		//
		// @Override
		// public void run(String... args) throws Exception {
		// // TODO Auto-generated method stub
		//
		// }
		// };
		// return runner;
		return args -> {
			Quote quote = restTemplate.getForObject(URL, Quote.class);
			log.info(quote.toString());
		};
	}
}
