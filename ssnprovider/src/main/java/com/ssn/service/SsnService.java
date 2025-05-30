package com.ssn.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class SsnService {
	 private static final Map<Integer, String> ssnPrefixToStateMap = new HashMap<>();

	    static {
	        // Dummy prefixes for learning/practice (not real SSN area numbers)
	        ssnPrefixToStateMap.put(10, "Alabama");
	        ssnPrefixToStateMap.put(11, "Alaska");
	        ssnPrefixToStateMap.put(37, "Nevada");
	        ssnPrefixToStateMap.put(38, "New Hampshire");
	        ssnPrefixToStateMap.put(39, "New Jersey");

	      
	    }

	    public String getStateFromSsn(String ssn) {
	        if (ssn == null || ssn.length() < 2) {
	            return "Invalid SSN";
	        }

	        try {
	            int prefix = Integer.parseInt(ssn.substring(0, 2));
	            return ssnPrefixToStateMap.getOrDefault(prefix, "Unknown State");
	        } catch (NumberFormatException e) {
	            return "Invalid SSN format";
	        }
	    }
	}
