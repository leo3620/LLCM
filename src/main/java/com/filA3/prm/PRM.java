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

import java.io.FileReader;
import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
  * Main class for calculating Health Care Homes hospitalisation probability.
  *
  * Users should call the main method with appropriate arguments.
  * <pre>
  *
  * usage: prm -d &lt;FILE&gt; [-h] [-v]
  *
  * Calculates Health Care Homes hospitalisation probability.
  *
  * Options:
  * -d,--data &lt;FILE&gt;   Data input file.
  * -h,--help          Print this help message.
  * -v,--verbose       Displays summary information about inputs.
  * </pre>
  * The data file should be a file in JSON format, that looks as follows:
  * <pre>
  *
  * {
  *    "Age": "44",
  *    "IsFemale": "true",
  *    "Ethnicity": "NON_ATSI",
  *    "BMICategory": "BMI_7_TO_25",
  *    "SmokingStatus": "NON_SMOKER",
  *    "AlcoholStatus": "NON_DRINKER",
  *    "IRSADDecile": "TEN",
  *    "MedicationGroups": [
  *        "STATINS",
  *        "ANTI_COAGULANTS",
  *        "ANTI_DEPRESSANTS",
  *        "ANTI_PSYCHOTICS",
  *        "ANTI_INFLAMMATORY",
  *        "STEROIDS"
  *    ],
  *    "Diseases": [
  *        "ASTHMA",
  *        "COPD",
  *        "CHRONIC_KIDNEY",
  *        "CORONARY_HEART",
  *        "STROKE",
  *        "TRANSIENT_ISCHAEMIC_ATTACK",
  *        "ATRIAL_FIBR",
  *        "CONG_HEART_FAILURE",
  *        "DIABETES_TYPE_1",
  *        "DIABETES_TYPE_2",
  *        "VENOUS_THROMBO",
  *        "OSTEOARTHRITIS",
  *        "DEPRESSION",
  *        "ANXIETY",
  *        "BIPOLAR",
  *        "SCHIZOPHRENIA",
  *        "CANCER",
  *        "DEMENTIA",
  *        "EPILEPSY",
  *        "CROHNS",
  *        "ULCER_COLITIS",
  *        "COELIAC",
  *        "STEATORRHEA",
  *        "MALABSORP_SYNDR",
  *        "CHRONIC_LIVER",
  *        "PANCREATITIS",
  *        "HYPERTENSION",
  *        "OSTEOPOROSIS",
  *        "RHEUMATOID",
  *        "HYPERLIPIDAEMIA",
  *        "HYPERCHOLESTEROLAEMIA",
  *        "HYPERTRIGLYCERIDAEMIA",
  *        "RHEUMATIC_HEART"
  *    ],
  *    "Pathology": {
  *        "HaemoglobinCategory": "LOW",
  *        "PlateletsCategory": "LOW",
  *        "AlanineAminotransferaseCategory": "LOW",
  *        "GammaGTCategory": "LOW",
  *        "HbA1cCategory": "LOW",
  *        "BilirubinCategory": "LOW",
  *        "CholesterolCategory": "MED",
  *        "CreatinineCategory": "LOW",
  *        "TriglyceridesCategory": "LOW",
  *        "AlbuminCreatinineRatioCategory": "NO_TEST_HISTORY",
  *        "LDLCategory": "HIGH",
  *        "EGFRCategory": "LOW",
  *        "BloodPressureCategory": "LOW"
  *    }
  * }
  * </pre>
  * <code>Age</code> should be a floating point number &gt; 0.
  * <p>
  * <code>IsFemale</code> should true or false.
  * <p>
  * <code>Ehnicity</code> should be one of the
  *           values in {@link com.filA3.prm.HCHRiskStratificationPRMData.Ethnicity}.
  * <p>
  * <code>BMICategory</code> should be one of the
  *           values in {@link com.filA3.prm.HCHRiskStratificationPRMData.BMICategory}.
  * <p>
  * <code>SmokingStatus</code> should be one of the
  *           values in {@link com.filA3.prm.HCHRiskStratificationPRMData.SmokingStatus}.
  * <p>
  * <code>AlcoholStatus</code> should be one of the
  *           values in {@link com.filA3.prm.HCHRiskStratificationPRMData.AlcoholStatus}.
  * <p>
  * <code>IRSADDecile</code> should be one of the
  *           values in {@link com.filA3.prm.HCHRiskStratificationPRMData.IRSADDecile}.
  * <p>
  * <code>MedicationGroups</code> should be an array of the
  *         values in {@link com.filA3.prm.HCHRiskStratificationPRMData.MedicationGroup}.
  * The array may be empty.
  * <p>
  * <code>Diseases</code> should be an array of the
  *           values in {@link com.filA3.prm.HCHRiskStratificationPRMData.Disease}.
  * The array may be empty.
  * <p>
  * <code>Pathology</code> should be a list of type/category pairs. The type
  * is one of the values in:
  * <pre>
  * HaemoglobinCategory,
  * PlateletsCategory,
  * AlanineAminotransferaseCategory,
  * GammaGTCategory,
  * HbA1cCategory",
  * BilirubinCategory,
  * CholesterolCategory,
  * CreatinineCategory,
  * TriglyceridesCategory,
  * AlbuminCreatinineRatioCategory,
  * LDLCategory,
  * EGFRCategory,
  * BloodPressureCategory</pre>
  * And the category is one of the
  *     values in {@link com.filA3.prm.HCHRiskStratificationPRMData.PathologyCategory}.
  *
  * @author Precedence Health Care Pty Ltd
  */
public class PRM
{
    // Constants
    private static final String PROGRAM_NAME = "prm";
    private static final String PROGRAM_DESC
	= "Calculates Health Care Homes hospitalisation probability.";

    // Variables
    private static String filename = null;
    private static boolean verbose = false;
    private static Options options = null;

    /**
      * Private constructor, to prevent construction of this class.
      */
    private PRM()
    {
    }

    /**
      * Convenience function for help without error text.
      *
      * Takes an exitValue, so after displaying help, exits with that value.
      *
      * @param exitValue value to exit with
      */
    private static void help(int exitValue)
    {
	help(exitValue, null);
    }

    /**
      * Displays command line help. If errorText is not null, errorText
      * is shown before the help.
      *
      * @param exitValue value to exit with
      * @param errorText error text to display before help.
      *                  Use null if no error text
      *
      */
    private static void help(int exitValue, String errorText)
    {
	HelpFormatter formatter = new HelpFormatter();
	if (errorText != null) {
	    formatter.setSyntaxPrefix(errorText + "\n"
		    + formatter.getSyntaxPrefix());
	}
	formatter.printHelp(PROGRAM_NAME, "\n" + PROGRAM_DESC + "\n\n"
	    + "Options:", options, "", true);
	System.exit(exitValue);
    }

    // Main entry point
    /**
      * The main entry point.
      * Command line options are required.  See class description.
      * <p>
      * Displays the calculated value.
      *
      * @param args command line arguments
      */
    public static void main(String[] args)
    {
	// Create the command line parser
	CommandLineParser parser = new DefaultParser();

	// Create the Options
	options = new Options();
	// Help
	options.addOption( "h", "help", false, "Print this help message.");
	// Verbose
	options.addOption( "v", "verbose", false,
			    "Displays summary information about inputs.");
	// Main data file (required)
	Option inputFile = new Option("d", "data", true, "Data input file.");
	inputFile.setRequired(true);
	inputFile.setArgName("FILE");
	options.addOption(inputFile);

	// Parse the command line
	try {
	    CommandLine line = parser.parse(options, args);
	    if (line.hasOption("h")) { // Show help
		help(0);
	    }
	    if (line.hasOption("v")) { //Verbose info display
		verbose = true;
	    }
	    if (line.hasOption("d")) { // Main data file (required)
		filename = line.getOptionValue("d");
	    }
	}
	catch (ParseException exp) // Show help
	{
	    help(1);
	}

	// Now do the calculation
	if (filename != null) {
	    try { // Parse the JSON file
		JSONParser jsonparser = new JSONParser();
		Object obj = jsonparser.parse(new FileReader(filename));
		JSONObject jsonObject = (JSONObject) obj;
		PRMCalculator calculator = new PRMCalculator(jsonObject);
		calculator.parse();
		double score = calculator.calculate();
		if (verbose) { // Additional info before calculation
		    System.out.println(calculator.displayData());
		}
		System.out.println(score);
	    } catch (FileNotFoundException e) { // File not found
		help(1, "File '" + filename + "' not found.");
	    } catch (Exception e) { // Some error so display and exit
		System.err.println("Error: " + e);
		System.exit(1);
	    }
	}
    }
}
