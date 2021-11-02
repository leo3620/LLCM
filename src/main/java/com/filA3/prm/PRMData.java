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

import java.util.EnumSet;
import java.util.Set;
import java.util.Iterator;
import java.lang.reflect.Method;


import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.filA3.prm.HCHRiskStratificationPRMData;

/**
  * The data structure used by the calculator. This class extends
  * the HCHRiskStratificationPRMData class to provide concenience
  * functions for setting data objects – mainly through reflection.
  *
  * @author Precedence Health Care Pty Ltd
  */
public class PRMData extends HCHRiskStratificationPRMData
{
    /**
      * Given enum type and value, sets the appropriate variable
      * with method name based on the type. For example, with enumType
      * "SmokingStatus" and enumValue "NON_SMOKER", calls the internal
      * method setSmokingStatus with argument SmokingStatus.NON_SMOKER.
      *
      * @param enumType the enum type name
      * @param enumValue the value to set
      */
    @SuppressWarnings("unchecked")
    public void setEnum(String enumType, String enumValue)
    {
        try { // Use reflection
            Class<?> c = Class.forName(getClass().getSuperclass().getName()
					+ "$" + enumType);
            Class<Enum> cc = (Class<Enum>) c;
            Method method = getClass().getMethod("set" + enumType, cc);
            method.invoke(this, Enum.valueOf(cc, enumValue));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
      * Given enum type and an array of enum values, sets the
      * appropriate variable based on the type. For example, with
      * enumType "MedicationGroups" and an array of enums, calls
      * the setMedicationGroups method with a set of MedicationGroup enums.
      *
      * @param enumType the enum type name
      * @param enumValues the array of values to set
      */
    @SuppressWarnings("unchecked")
    public void setGroupEnum(String enumType, String[] enumValues)
    {
	String type = enumType;
	if (enumType.endsWith("s")) { // Make a simple singular from plural
	    type = StringUtils.chop(enumType);
	}
	try { // Use reflection
	    Class<?> c = Class.forName(getClass().getSuperclass()
		    .getName() + "$" + type);
	    Class<Enum> cc = (Class<Enum>) c;
	    Set es = EnumSet.noneOf(cc);
	    for (String val : enumValues) { // Build a set
		es.add(Enum.valueOf(cc, val));
	    }
            Method method = getClass().getMethod("set" + enumType, Set.class);
            method.invoke(this, es);
	} catch (Exception e) {
	    System.err.println(e);
	}
    }

    /**
      * Given enum type and a JSONArray of enum values, sets the
      * appropriate variable based on the type. For example, with
      * enumType "MedicationGroups" and an array of enums, calls
      * the setMedicationGroups method with a set of MedicationGroup enums.
      *
      * @param enumType the enum type name
      * @param enumValues the JSONArray of values to set */
    public void setGroupEnum(String enumType, JSONArray enumValues)
    {
	String[] groupArray = new String[enumValues.size()];
	for (int i = 0; i < enumValues.size(); i++)
	{
	    groupArray[i] = (String) enumValues.get(i);
	}
	setGroupEnum((String) enumType, groupArray);
    }

    /**
      * Given a JSONObject with name/value pairs of pathology categories,
      * applies appropriate methods based on the type.
      * An example may be something like:
      * <pre>
      * {
      *     "TriglyceridesCategory": "LOW",
      *     "AlbuminCreatinineRatioCategory": "NO_TEST_HISTORY"
      * }
      * </pre>
      * @param pathologyCategories the JSONObject
      */
    @SuppressWarnings("unchecked")
    public void setPathology(JSONObject pathologyCategories)
    {
	for(Iterator iterator = pathologyCategories.keySet().iterator();
							iterator.hasNext();)
	{
	    String key = (String) iterator.next();
	    try {
		Class<?> c = Class.forName(getClass().getSuperclass()
			.getName() + "$" + "PathologyCategory");
		Class<Enum> cc = (Class<Enum>) c;
		Method method = getClass().getMethod("set" + key, cc);
		method.invoke(this, Enum.valueOf(cc,
				    (String) pathologyCategories.get(key)));
	    } catch (Exception e) {
		System.err.println(e);
	    }
	}
    }
}
