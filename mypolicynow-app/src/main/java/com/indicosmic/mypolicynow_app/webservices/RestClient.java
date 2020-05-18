package com.indicosmic.mypolicynow_app.webservices;

import com.indicosmic.mypolicynow_app.utils.CommonMethods;

public class RestClient
	{
		private static API REST_CLIENT;

		//public static String ROOT_URL = "https://www.mypolicynow.com/Api/";

		public static String ROOT_URL = "http://uat.mypolicynow.com/ags/";
		public static String ROOT_URL2 = "http://uat.mypolicynow.com/ags/";
		public static String ROOT_IC_IMAGE_URL = ROOT_URL2+"assets/images/client-logos/200x120/";
		public static String URLProd = "https://www.mypolicynow.com/Api/";
		public  static String ROOT_URL_BREAKIN = "http://uat.mypolicynow.com/Api/";
		public static String URLUAT = "http://uat.mypolicynow.com/";

		public static String GenerateQrCode = "https://pierre2106j-qrcode.p.rapidapi.com/api?backcolor=ffffff&pixel=10&ecl=L+%7C+M%7C+Q+%7C+H&forecolor=000000&type=json+%7C+url+%7C+tel+%7C+sms+%7C+email&";


		public static String x_api_key = "ags_87674599009";
		public static String api_user_name = "admin";
		public static String api_password = "1234";
		public static String Basic_auth = CommonMethods.Base64_Encode(api_user_name + ":" + api_password);


		public static String Development = "http://www.mymfnow.com/api";
		/*static
			{
				setupRestClient();
			}*/


		private RestClient()
			{
			}

		public static API get()
			{
				return REST_CLIENT;
			}

		/*private static void setupRestClient()
			{
				RestAdapter.Builder builder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(ROOT).setClient(new OkClient(new OkHttpClient()));
				RestAdapter restAdapter = builder.build();
				REST_CLIENT = restAdapter.create(API.class);
			}*/
	}
