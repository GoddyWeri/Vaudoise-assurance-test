package com.example.clientapi.utils;

public class CustomUtils {
	public static final String PERSON_TEXT = "Person";
	public static final String PERSON_COMPANY_ID_ERROR = "The client of type Person cannot be provided with a company identifier.";
	public static final String COMPANY_TEXT = "Company";
	public static final String COMPANY_BDAY_ERROR = "The client of type Company cannot be provided with a birth date.";
	public static final String CLIENT_DATA_CONFLICT_ERROR = "Client with exact same data already exists";
	public static final String CLIENT_ABSENT_ERROR = "Client with provided id does not exist in database.";
	public static final String EMPTY_STRING = "";
	public static final String CLIENT_FOR_CONTRACTT_ABSENT_ERROR = "The client with the associated id for whom we want to create contract was not found";
	public static final String START_DATE_EARLIER_ERROR = "Start date cannot be earlier than today";
	public static final String END_DATE_EARLIER_ERROR = "End date cannot be earlier than today";
	public static final String END_DATE_EARLIER_TODAY_ERROR = "End date cannot be earlier or same as the start date";
	public static final String CONTRACT_ABSENT_ERROR = "contract of associated id is not found in database";
	public static final String CREATING_CONTRACT_TEXT = "Creating client with data: {}";
	public static final String CONTRACT_REQUEST_SUCCESSFULLY_CHECKED = "Contract with data: {} successfully checked";
	public static final String SUCCESS_CREATED_CONTRACT_TEXT = "Contract succesfully created";
	public static final String SUCCESS_UPDATED_CONTRACT_TEXT = "Contract succesfully created with new info from: {}";
	public static final String CONTRACT_ABSENT_ERROR_LOG = "Contract with ID {} not found in database";
	public static final String SUCCESS_FINDING_ALL_CONTRACTS_TEXT = "Successfully found all contracts associated to client ID: {}";
	public static final String CLIENT_ABSENT_ERROR_LOG = "client with id: {} not found";
	public static final String SUM_SUCCESS_TEXT = "All clients contracts successfully summed";
	public static final String UPDATEBEFORE_AFTER_UPDATEAFTER_ERROR_TEXT = "updatedBefore must be after or equal to updatedAfter";
	public static final String UPDATEBEFORE_IN_FUTURE_ERROR_TEXT = "updatedBefore cannot be in the future";
	public static final String UPDATEAFTER_IN_FUTURE_ERROR_TEXT = "updatedAfter cannot be in the future";
}
