package com.mallkvs.bulk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BulkInterfaceMain {

	public static void main(String[] args) {
		SpringApplication.run(BulkInterfaceMain.class, args);
	}
	/*
	test request example:

	curl --location --request POST 'http://localhost:8080/' \
	--header 'Content-Type: application/json' \
	--header 'Authorization: Basic dXNlcjpwYXNzd29yZA==' \
	--data-raw '{
	   "request":[
		  {
			 "shopId":"233264",
			 "manageNumber":"2692349328233"
		  },
		  {
			 "shopId":"233264",
			 "manageNumber":"2692349328233"
		  }
	   ]
	}'
	 */

}
