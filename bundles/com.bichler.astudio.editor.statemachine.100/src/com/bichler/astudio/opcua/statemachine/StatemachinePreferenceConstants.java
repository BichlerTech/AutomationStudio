package com.bichler.astudio.opcua.statemachine;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import com.google.gson.Gson;

public class StatemachinePreferenceConstants {

	public static final String CREATE_STATEMACHINE_PAGE_UML_PATH = "createstatemachineumlpath";
	public static final String CREATE_STATEMACHINE_PAGE_EXPORT_PATH = "createstatemachineexportpath";
	
	public static final String REVERSE_NS_STATEMACHINE_PAGE_EXPORT_PATH = "reversestatemachineexportpath";
	
	public static final String REVERSE_STATEMACHINE_PAGE_EXPORT_PATH = "reversestatemachineexportpath";
	
	/**
	 * Adds a value to the preference store.
	 * 
	 * @param PreferenceId Preference Id to store
	 * @param valueToAdd   Value to add
	 */
	public static void addValueToStore(String preferenceId, String valueToAdd) {
		// get value from store
		IPreferenceStore preferenceStore = StatemachineActivator.getDefault().getPreferenceStore();
		String value = preferenceStore.getString(preferenceId);
		String[] arrayValues = convertStringToArray(value);
		// add value
		List<String> list = new ArrayList<>();
		for (String arrayValue : arrayValues) {
			list.add(arrayValue);
		}
		// do not add value if exists
		if (list.contains(valueToAdd)) {
			return;
		}
		list.add(valueToAdd);
		// save value to store
		preferenceStore.setValue(preferenceId, convertArrayToString(list.toArray(new String[0])));
	}

	/**
	 * Converts a string to a string array.
	 * 
	 * @param value String to convert
	 * @return Array values from the string.
	 */
	public static String[] convertStringToArray(String value) {
		// empty value
		if (value == null || value.isEmpty()) {
			return new String[0];
		}
		// Gson converter
		Gson gson = new Gson();
		return gson.fromJson(value, String[].class);
	}

	/**
	 * Converts a string array to a string.
	 * 
	 * @param value String array to convert
	 * @return String from the array.
	 */
	public static String convertArrayToString(String[] value) {
		// Gson converter
		Gson gson = new Gson();
		// converts to Json string
		String jsonString = gson.toJson(value);
		return jsonString;
	}
}
