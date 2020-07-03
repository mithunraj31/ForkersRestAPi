package com.mbel.constants;

public class Constants {

	//Constants for Authentication login

	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;

	public static final String SIGNING_KEY = "devglan123r";

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String HEADER_STRING = "Authorization";

	public static final String AUTHORITIES_KEY = "scopes";

	//Constants for service strings

	public static final String STRING_FALSE = "false";

	public static final String STRING_TRUE = "true";

	public static final String PRODUCT_COMPONENT_ID="product_component_id";

	public static final String QTY = "qty";

	public static final String PRODUCT_ID="product_id";

	public static final String PRICE ="price";

	public static final String MESSAGE ="message";

	public static final String USER_NAME ="userName";

	public static final String COUNT = "count";
	
	public static final String INCOMING_SHIPMENT_ID = "incomingShipmentId";

	public static final String QUANTITY_UPDATED = "Incoming Quantity Updated";

	public static final String QUANTITY_NOT_FIXED = "Incoming Quantity is not fixed";
	
	public static final String UNFULFILLED="unfulfilled";
	
	public static final String FULFILLED="fulfilled";
	
	public static final String REVERTED = "reverted";


	//Constants for reading query parameter

	public static final String FCST="fcst";

	public static final String WAIT="?wait";

	public static final String WITH_KITTING="?withKitting";

	public static final String WITHOUT_KITTING="?withoutKitting";

	public static final String NOT_CONFIRMED="notConfirmed";

	public static final String NOT_IN_STOCK="?notInStock";

	public static final String ARRIVED="?arrived";

	//Constants for Edit reason 

	public static final String DELETED = "1";

	public static final String ORDER_CONFIRMED = "2";

	public static final String ORDER_NOT_CONFIRMED = "3";
	
	public static final String ORDER_FULFILLED="4";

	public static final String ORDER_REVERTED="5";
	
	public static final String INCOMING_ARRIVED="8";
	
	public static final String INCOMING_REVERTED="9";
	
	public static final String EDITED = "10";
	
}