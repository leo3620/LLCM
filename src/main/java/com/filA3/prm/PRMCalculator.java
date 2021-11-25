// Copyright (C) 2017-2018
// Precedence Health Care Pty Ltd
// Melbourne, Victoria, 3000, AUSTRALIA
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not
// use this file except in compliance with the License. You may obtain a copy of
// the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations
// under the License.

package com.filA3.prm;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.LinkedHashMap;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import com.filA3.prm.HCHRiskStratificationPRMScoreCalculator;
import com.filA3.prm.PRMData;

/**
  * The PRM calculator.  Takes a JSONObject with the many parameters used
  * in the PRM calculation.
  * <p>
  * The user calls parse() on the created object in order to parse
  * the JSON object. The user can then call displayData() to display the
  * data fields after application of the JSON parameters, and calculate()
  * to calculate and return the score.
  *
  * @author Precedence Health Care Pty Ltd
  */
public class PRMCalculator extends HCHRiskStratificationPRMScoreCalculator
{
    /** The data object **/
    private PRMData data;
    /** The json object with parameter structure */
    private JSONObject json;

    /**
      * Constructor. Takes a JSON object with paramters used in the calculation.
      *
      * @param obj JSON object with parameters
      */
    public PRMCalculator(JSONObject obj)
    {
	data = new PRMData();
	json = obj;
    }

    /**
      * Parses the JSON parameters supplied in construction.
      */
    public void parse()
    {
	for(Iterator iterator = json.keySet().iterator(); iterator.hasNext();)
	{
	    String key = (String) iterator.next();
	    switch(key) {
		case "Age":
			double age = ((double)(int)json.get(key));
		    data.setAge(age);
		    break;
		case "IsFemale":
		    boolean isFemale = (boolean) json.get(key);
		    data.setIsFemale(isFemale);
		    break;
		case "MedicationGroups":
				JSONArray temp = new JSONArray();
				temp.addAll((ArrayList) json.get(key));
		    data.setGroupEnum(key, temp);
		    break;
		case "Diseases":
				temp = new JSONArray();
				temp.addAll((ArrayList) json.get(key));
				data.setGroupEnum(key, temp);
		    break;
		case "Pathology":
		    data.setPathology(new JSONObject( (LinkedHashMap) json.get(key)));
		    break;
		default:
 			    data.setEnum((String) key, (String) json.get(key));
		    break;
	    }
	}
    }

    /**
      * Returns all data that will be used for calculation in an appropriate
      * format for display.
      *
      * @return a string representation of the data
      */
    public String displayData()
    {
	String str =  data.toString();
	str = str.substring(str.indexOf("["));
	return str;
    }

    /**
      * Calculates the Health Care Homes hospitalisation probability.
      *
      * @return the probability
      */
    public double calculate()
    {
	double score = calculatePRMScore(data);
	return score;
    }
}
