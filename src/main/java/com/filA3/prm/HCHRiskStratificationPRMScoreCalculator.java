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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.filA3.prm.HCHRiskStratificationPRMData.Disease;
import com.filA3.prm.HCHRiskStratificationPRMData.DiseaseGroup;
import com.filA3.prm.HCHRiskStratificationPRMData.MedicationGroup;

/**
 * This class calculates PRM scores for the HCH Risk Stratification project.
 *
 * @author Precedence Health Care Pty Ltd
 */
public class HCHRiskStratificationPRMScoreCalculator
{
    /**
     * How precisely the final calculation should be done.
     */
    private static final int PRECISION = 8;

    // All the coefficients used in the calculation.
    private static final BigDecimal INTERCEPT
	= BigDecimal.valueOf(-2.7551480);

    private static final BigDecimal AGE_COEFFICIENT
	= BigDecimal.valueOf(-0.03795278d);
    private static final BigDecimal AGE_SQ_COEFFICIENT
	= BigDecimal.valueOf(0.0006159449d);
    private static final BigDecimal AGE_CU_COEFFICIENT
	= BigDecimal.valueOf(-0.000001047389d);
    private static final BigDecimal GENDER_FFEMALE_COEFFICIENT
	= BigDecimal.valueOf(0.2216384d);
    private static final BigDecimal ETHNICITY_FATSI_COEFFICIENT
	= BigDecimal.valueOf(0.4033629d);
    private static final BigDecimal ETHNICITY_FUNKNOWN_COEFFICIENT
	= BigDecimal.valueOf(-0.06624966d);
    private static final BigDecimal BMI_7CAT_25_30_COEFFICIENT
	= BigDecimal.valueOf(0.03294204d);
    private static final BigDecimal BMI_7CAT_30_35_COEFFICIENT
	= BigDecimal.valueOf(0.1939825d);
    private static final BigDecimal BMI_7CAT_35_40_COEFFICIENT
	= BigDecimal.valueOf(0.2712273d);
    private static final BigDecimal BMI_7CAT40_COEFFICIENT
	= BigDecimal.valueOf(0.4643618d);
    private static final BigDecimal BMI_7CATNOT_RECORDED_COEFFICIENT
	= BigDecimal.valueOf(0.139507d);
    private static final BigDecimal SMOKINGSTATUS_4CATEX_SMOKER_COEFFICIENT
	= BigDecimal.valueOf(0.1969316d);
    private static final BigDecimal SMOKINGSTATUS_4CATSMOKER_COEFFICIENT
	= BigDecimal.valueOf(0.4057213d);
    private static final BigDecimal SMOKINGSTATUS_4CATUNKNOWN_COEFFICIENT
	= BigDecimal.valueOf(0.199347d);
    private static final BigDecimal ANYALCOHOL_3CATDRINKER_COEFFICIENT
	= BigDecimal.valueOf(-0.2853232d);
    private static final BigDecimal ANYALCOHOL_3CATNOT_RECORDED_COEFFICIENT
	= BigDecimal.valueOf(-0.2301394d);
    private static final BigDecimal DECILE_IRSAD_F1_COEFFICIENT
	= BigDecimal.valueOf(-0.01238492d);
    private static final BigDecimal DECILE_IRSAD_F10_COEFFICIENT
	= BigDecimal.valueOf(-0.4622169d);
    private static final BigDecimal DECILE_IRSAD_F2_COEFFICIENT
	= BigDecimal.valueOf(-0.07421796d);
    private static final BigDecimal DECILE_IRSAD_F3_COEFFICIENT
	= BigDecimal.valueOf(0.004430832d);
    private static final BigDecimal DECILE_IRSAD_F4_COEFFICIENT
	= BigDecimal.valueOf(-0.1857272d);
    private static final BigDecimal DECILE_IRSAD_F6_COEFFICIENT
	= BigDecimal.valueOf(-0.2340966d);
    private static final BigDecimal DECILE_IRSAD_F7_COEFFICIENT
	= BigDecimal.valueOf(-0.2468854d);
    private static final BigDecimal DECILE_IRSAD_F8_COEFFICIENT
	= BigDecimal.valueOf(-0.2990624d);
    private static final BigDecimal DECILE_IRSAD_F9_COEFFICIENT
	= BigDecimal.valueOf(-0.1834029d);
    private static final BigDecimal DECILE_IRSAD_FUNKNOWN_COEFFICIENT
	= BigDecimal.valueOf(-0.030162d);
    private static final BigDecimal MEDS_STATINS_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.01528814d);
    private static final BigDecimal MEDS_ANTICOAGULANTS_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.2886091d);
    private static final BigDecimal MEDS_ANTIDEPRESSANTS_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.2025163d);
    private static final BigDecimal MEDS_ANTIPSYCHOTICS_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.3923084d);
    private static final BigDecimal MEDS_ANTIINFLAMMATORY_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.1301034d);
    private static final BigDecimal MEDS_STEROIDS_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.148835d);
    private static final BigDecimal NUMDISEASES_COEFFICIENT
	= BigDecimal.valueOf(0.3369661d);
    private static final BigDecimal NUMDISEASES_SQ_COEFFICIENT
	= BigDecimal.valueOf(-0.03976625d);
    private static final BigDecimal NUMDISEASES_CU_COEFFICIENT
	= BigDecimal.valueOf(0.001930389d);
    private static final BigDecimal DISEASEGRP_RESPIRATORY_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.07150371d);
    private static final BigDecimal DISEASEGRP_ATRIAL_FIBR_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.2234789d);
    private static final BigDecimal DISEASEGRP_CARDIOVASCULAR_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.4764327d);
    private static final BigDecimal DISEASEGRP_OSTEOARTHRITIS_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.2060183d);
    private static final BigDecimal DISEASEGRP_OSTEOPOROSIS_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.05950344d);
    private static final BigDecimal DISEASEGRP_RHEUMATOID_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.1149149d);
    private static final BigDecimal DISEASEGRP_MENTAL_HEALTH_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.06869549d);
    private static final BigDecimal DISEASEGRP_CANCER_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.06008254d);
    private static final BigDecimal DISEASEGRP_DIGESTIVE_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.1796635d);
    private static final BigDecimal DISEASEGRP_HYPERTENSION_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.1591489d);
    private static final BigDecimal DISEASEGRP_BLOODFATS_FLAG_COEFFICIENT =
            BigDecimal.valueOf(-0.3726723d);
    private static final BigDecimal DISEASEGRP_CHRONIC_KIDNEY_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.02682657d);
    private static final BigDecimal DISEASEGRP_DIABETES_TYPE_1_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.5844975d);
    private static final BigDecimal DISEASEGRP_DIABETES_TYPE_2_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.1332004d);
    private static final BigDecimal DISEASEGRP_VENOUS_THROMBO_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.3623621d);
    private static final BigDecimal DISEASEGRP_OTHER_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.5157983d);
    private static final BigDecimal MORB_V2_HB_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(0.4416708d);
    private static final BigDecimal MORB_V2_HB_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(0.1546069d);
    private static final BigDecimal MORB_V2_HB_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(0.04502215d);
    private static final BigDecimal MORB_V2_PLATELETS_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(0.1398411d);
    private static final BigDecimal
	MORB_V2_PLATELETS_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(-0.01470966d);
    private static final BigDecimal MORB_V2_ALT_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(-0.06195176d);
    private static final BigDecimal MORB_V2_ALT_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(0.05502668d);
    private static final BigDecimal MORB_V2_ALT_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(-0.2940664d);
    private static final BigDecimal MORB_V2_GGT_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(0.2209109d);
    private static final BigDecimal MORB_V2_GGT_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(0.1261477d);
    private static final BigDecimal MORB_V2_GGT_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(0.1979201d);
    private static final BigDecimal MORB_V2_HBA1C_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(0.1746667d);
    private static final BigDecimal MORB_V2_HBA1C_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(0.1720256d);
    private static final BigDecimal
	MORB_V2_HBA1C_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(-0.04617213d);
    private static final BigDecimal
	MORB_V2_BILIRUBIN_3CATMED_OR_HIGH_COEFFICIENT
	= BigDecimal.valueOf(0.1803894d);
    private static final BigDecimal
	MORB_V2_BILIRUBIN_3CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(0.07843205d);
    private static final BigDecimal MORB_V2_CHOLESTEROL_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(0.04530537d);
    private static final BigDecimal MORB_V2_CHOLESTEROL_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(-0.03635341d);
    private static final BigDecimal
	MORB_V2_CHOLESTEROL_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(0.1749709d);
    private static final BigDecimal
	MORB_V2_CREATININE_3CATMED_OR_HIGH_COEFFICIENT
	= BigDecimal.valueOf(1.134946d);
    private static final BigDecimal
	MORB_V2_CREATININE_3CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(-0.1704331d);
    private static final BigDecimal MORB_V2_TAG_3CATMED_OR_HIGH_COEFFICIENT
	= BigDecimal.valueOf(0.1234874d);
    private static final BigDecimal MORB_V2_TAG_3CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(-0.0963018d);
    private static final BigDecimal MORB_V2_ACR_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(0.304275d);
    private static final BigDecimal MORB_V2_ACR_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(0.1151562d);
    private static final BigDecimal MORB_V2_ACR_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(0.1059512d);
    private static final BigDecimal MORB_V2_LDL_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(-0.05859249d);
    private static final BigDecimal MORB_V2_LDL_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(-0.05395995d);
    private static final BigDecimal MORB_V2_LDL_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(0.1556545d);
    private static final BigDecimal MORB_V2_EGFR_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(0.08914862d);
    private static final BigDecimal MORB_V2_EGFR_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(0.09504744d);
    private static final BigDecimal MORB_V2_EGFR_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(0.04597902d);
    private static final BigDecimal MORB_V2_BP_4CATHIGH_COEFFICIENT
	= BigDecimal.valueOf(0.2946162d);
    private static final BigDecimal MORB_V2_BP_4CATMED_COEFFICIENT
	= BigDecimal.valueOf(0.1839175d);
    private static final BigDecimal MORB_V2_BP_4CATNO_TEST_HISTORY_COEFFICIENT
	= BigDecimal.valueOf(0.01121054d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_CARDIOVASCULAR_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.2108839d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_RESPIRATORY_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.05705658d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_DIABETES_TYPE_1_FLAG_COEFFICIENT
	= BigDecimal.valueOf(0.2492323d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_OTHER_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.172162d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_OSTEOPOROSIS_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.2475655d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_CHRONIC_KIDNEY_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.112216d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_MENTAL_HEALTH_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.06436747d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_HYPERTENSION_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.08909346d);
    private static final BigDecimal
	GENDER_FFEMALE_DISEASEGRP_DIABETES_TYPE_2_FLAG_COEFFICIENT
	= BigDecimal.valueOf(-0.0002784439d);

    /**
      * Instance of the calculator.
      */
    private static final HCHRiskStratificationPRMScoreCalculator INSTANCE
	= new HCHRiskStratificationPRMScoreCalculator();

    /**
      * returns the static instance of the calculator.
      *
      * @return the calculator
      */
    public static HCHRiskStratificationPRMScoreCalculator getInstance()
    {
        return INSTANCE;
    }

    protected HCHRiskStratificationPRMScoreCalculator()
    {
        // Protected constructor so you can't make one of these
	// (unless you're a subclass).
    }

    /**
      * Given data, calculates the hospitalisation probability.
      *
      * @param data the input data
      *
      * @return the calculation
      */
    public double calculatePRMScore(@Nonnull HCHRiskStratificationPRMData data)
    {
        BigDecimal linearPredictor = calculateLinearPredictor(data);

        BigDecimal exponent =
	    BigDecimal.valueOf(Math.exp(linearPredictor.doubleValue()));
        BigDecimal probability = exponent.divide(
		BigDecimal.valueOf(1).add(exponent), PRECISION,
		RoundingMode.HALF_UP);
        return probability.doubleValue();
    }

    /**
      * Calculates the linear predictor.
      * This does all the real work.
      *
      * @param data the input data
      *
      * @return the calculation
      */
    private BigDecimal calculateLinearPredictor(
	    HCHRiskStratificationPRMData data)
    {
        BigDecimal linearPredictor = INTERCEPT;

        BigDecimal age = BigDecimal.valueOf(data.getAge());
        linearPredictor = linearPredictor.add(age.multiply(AGE_COEFFICIENT));
        linearPredictor = linearPredictor.add(
		age.pow(2).multiply(AGE_SQ_COEFFICIENT));
        linearPredictor = linearPredictor.add(
		age.pow(3).multiply(AGE_CU_COEFFICIENT));

        if (data.getIsFemale())
        {
            linearPredictor = linearPredictor.add(GENDER_FFEMALE_COEFFICIENT);
        }

        switch (data.getEthnicity())
        {
            case ATSI:
                linearPredictor
		    = linearPredictor.add(ETHNICITY_FATSI_COEFFICIENT);
                break;
            case UNKNOWN:
                linearPredictor
		    = linearPredictor.add(ETHNICITY_FUNKNOWN_COEFFICIENT);
                break;
            // NON_ATSI is the 'reference value'.
        }

        switch (data.getBMICategory())
        {
            case BMI_25_TO_30:
                linearPredictor
		    = linearPredictor.add(BMI_7CAT_25_30_COEFFICIENT);
                break;
            case BMI_30_TO_35:
                linearPredictor
		    = linearPredictor.add(BMI_7CAT_30_35_COEFFICIENT);
                break;
            case BMI_35_TO_40:
                linearPredictor
		    = linearPredictor.add(BMI_7CAT_35_40_COEFFICIENT);
                break;
            case BMI_40_TO_200:
                linearPredictor = linearPredictor.add(BMI_7CAT40_COEFFICIENT);
                break;
            case NOT_RECORDED:
                linearPredictor
		    = linearPredictor.add(BMI_7CATNOT_RECORDED_COEFFICIENT);
                break;
            // BMI_7_TO_25 is the 'reference value'.
        }

        switch (data.getSmokingStatus())
        {
            case EX_SMOKER:
                linearPredictor = linearPredictor.add(
			SMOKINGSTATUS_4CATEX_SMOKER_COEFFICIENT);
                break;
            case SMOKER:
                linearPredictor = linearPredictor.add(
			SMOKINGSTATUS_4CATSMOKER_COEFFICIENT);
                break;
            case UNKNOWN:
                linearPredictor = linearPredictor.add(
			SMOKINGSTATUS_4CATUNKNOWN_COEFFICIENT);
                break;
            // NON_SMOKER is the 'reference value'.
        }

        switch (data.getAlcoholStatus())
        {
            case DRINKER:
                linearPredictor = linearPredictor.add(
			ANYALCOHOL_3CATDRINKER_COEFFICIENT);
                break;
            case NOT_RECORDED:
                linearPredictor = linearPredictor.add(
			ANYALCOHOL_3CATNOT_RECORDED_COEFFICIENT);
                break;
            // NON_DRINKER is the 'reference value'.
        }

        switch (data.getIRSADDecile())
        {
            case ONE:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F1_COEFFICIENT);
                break;
            case TWO:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F2_COEFFICIENT);
                break;
            case THREE:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F3_COEFFICIENT);
                break;
            case FOUR:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F4_COEFFICIENT);
                break;
            case SIX:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F6_COEFFICIENT);
                break;
            case SEVEN:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F7_COEFFICIENT);
                break;
            case EIGHT:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F8_COEFFICIENT);
                break;
            case NINE:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F9_COEFFICIENT);
                break;
            case TEN:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_F10_COEFFICIENT);
                break;
            case UNKNOWN:
                linearPredictor = linearPredictor.add(
			DECILE_IRSAD_FUNKNOWN_COEFFICIENT);
                break;
            // FIVE is the 'reference value'.
        }

        for (MedicationGroup medicationGroup : data.getMedicationGroups())
        {
            switch (medicationGroup)
            {
                case STATINS:
                    linearPredictor = linearPredictor.add(
			    MEDS_STATINS_FLAG_COEFFICIENT);
                    break;
                case ANTI_COAGULANTS:
                    linearPredictor = linearPredictor.add(
			    MEDS_ANTICOAGULANTS_FLAG_COEFFICIENT);
                    break;
                case ANTI_DEPRESSANTS:
                    linearPredictor = linearPredictor.add(
			    MEDS_ANTIDEPRESSANTS_FLAG_COEFFICIENT);
                    break;
                case ANTI_PSYCHOTICS:
                    linearPredictor = linearPredictor.add(
			    MEDS_ANTIPSYCHOTICS_FLAG_COEFFICIENT);
                    break;
                case ANTI_INFLAMMATORY:
                    linearPredictor = linearPredictor.add(
			    MEDS_ANTIINFLAMMATORY_FLAG_COEFFICIENT);
                    break;
                case STEROIDS:
                    linearPredictor = linearPredictor.add(
			    MEDS_STEROIDS_FLAG_COEFFICIENT);
                    break;
            }
        }

        BigDecimal numDiseases = BigDecimal.valueOf(data.getDiseases().size());
        linearPredictor = linearPredictor.add(
		numDiseases.multiply(NUMDISEASES_COEFFICIENT));
        linearPredictor = linearPredictor.add(numDiseases.pow(2).multiply(
			    NUMDISEASES_SQ_COEFFICIENT));
        linearPredictor = linearPredictor.add(numDiseases.pow(3).multiply(
		    NUMDISEASES_CU_COEFFICIENT));

        Set<DiseaseGroup> diseaseGroups = extractDiseaseGroups(
		data.getDiseases());
        for (DiseaseGroup diseaseGroup : diseaseGroups)
        {
            switch (diseaseGroup)
            {
                case RESPIRATORY:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_RESPIRATORY_FLAG_COEFFICIENT);
                    break;
                case ATRIAL_FIBR:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_ATRIAL_FIBR_FLAG_COEFFICIENT);
                    break;
                case CARDIOVASCULAR:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_CARDIOVASCULAR_FLAG_COEFFICIENT);
                    break;
                case OSTEOARTHRITIS:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_OSTEOARTHRITIS_FLAG_COEFFICIENT);
                    break;
                case OSTEOPOROSIS:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_OSTEOPOROSIS_FLAG_COEFFICIENT);
                    break;
                case RHEUMATOID:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_RHEUMATOID_FLAG_COEFFICIENT);
                    break;
                case MENTAL_HEALTH:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_MENTAL_HEALTH_FLAG_COEFFICIENT);
                    break;
                case CANCER:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_CANCER_FLAG_COEFFICIENT);
                    break;
                case DIGESTIVE:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_DIGESTIVE_FLAG_COEFFICIENT);
                    break;
                case HYPERTENSION:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_HYPERTENSION_FLAG_COEFFICIENT);
                    break;
                case BLOODFATS:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_BLOODFATS_FLAG_COEFFICIENT);
                    break;
                case CHRONIC_KIDNEY:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_CHRONIC_KIDNEY_FLAG_COEFFICIENT);
                    break;
                case DIABETES_TYPE_1:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_DIABETES_TYPE_1_FLAG_COEFFICIENT);
                    break;
                case DIABETES_TYPE_2:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_DIABETES_TYPE_2_FLAG_COEFFICIENT);
                    break;
                case VENOUS_THROMBO:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_VENOUS_THROMBO_FLAG_COEFFICIENT);
                    break;
                case OTHER:
                    linearPredictor = linearPredictor.add(
			    DISEASEGRP_OTHER_FLAG_COEFFICIENT);
                    break;
            }
        }

        switch (data.getHaemoglobinCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_HB_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_HB_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_HB_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getPlateletsCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_PLATELETS_4CATHIGH_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_PLATELETS_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
	    // LOW is the 'reference value', and MED is not defined
	    // for platelets.
        }

        switch (data.getAlanineAminotransferaseCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_ALT_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_ALT_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_ALT_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getGammaGTCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_GGT_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_GGT_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_GGT_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getHbA1cCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_HBA1C_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_HBA1C_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_HBA1C_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getBilirubinCategory())
        {
            case HIGH:
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_BILIRUBIN_3CATMED_OR_HIGH_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_BILIRUBIN_3CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getCholesterolCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_CHOLESTEROL_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_CHOLESTEROL_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_CHOLESTEROL_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getCreatinineCategory())
        {
            case HIGH:
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_CREATININE_3CATMED_OR_HIGH_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_CREATININE_3CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getTriglyceridesCategory())
        {
            case HIGH:
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_TAG_3CATMED_OR_HIGH_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_TAG_3CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getAlbuminCreatinineRatioCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_ACR_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_ACR_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_ACR_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getLDLCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_LDL_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_LDL_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_LDL_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getEGFRCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_EGFR_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_EGFR_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_EGFR_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        switch (data.getBloodPressureCategory())
        {
            case HIGH:
                linearPredictor = linearPredictor.add(
			MORB_V2_BP_4CATHIGH_COEFFICIENT);
                break;
            case MED:
                linearPredictor = linearPredictor.add(
			MORB_V2_BP_4CATMED_COEFFICIENT);
                break;
            case NO_TEST_HISTORY:
                linearPredictor = linearPredictor.add(
			MORB_V2_BP_4CATNO_TEST_HISTORY_COEFFICIENT);
                break;
            // LOW is the 'reference value'.
        }

        if (data.getIsFemale())
        {
            if (diseaseGroups.contains(DiseaseGroup.CARDIOVASCULAR))
            {
                linearPredictor = linearPredictor.add(
		    GENDER_FFEMALE_DISEASEGRP_CARDIOVASCULAR_FLAG_COEFFICIENT);
            }

            if (diseaseGroups.contains(DiseaseGroup.RESPIRATORY))
            {
                linearPredictor = linearPredictor.add(
			GENDER_FFEMALE_DISEASEGRP_RESPIRATORY_FLAG_COEFFICIENT);
            }

            if (diseaseGroups.contains(DiseaseGroup.DIABETES_TYPE_1))
            {
                linearPredictor = linearPredictor.add(
		    GENDER_FFEMALE_DISEASEGRP_DIABETES_TYPE_1_FLAG_COEFFICIENT);
            }

            if (diseaseGroups.contains(DiseaseGroup.OTHER))
            {
                linearPredictor = linearPredictor.add(
			GENDER_FFEMALE_DISEASEGRP_OTHER_FLAG_COEFFICIENT);
            }

            if (diseaseGroups.contains(DiseaseGroup.OSTEOPOROSIS))
            {
                linearPredictor = linearPredictor.add(
		    GENDER_FFEMALE_DISEASEGRP_OSTEOPOROSIS_FLAG_COEFFICIENT);
            }

            if (diseaseGroups.contains(DiseaseGroup.CHRONIC_KIDNEY))
            {
                linearPredictor = linearPredictor.add(
		    GENDER_FFEMALE_DISEASEGRP_CHRONIC_KIDNEY_FLAG_COEFFICIENT);
            }

            if (diseaseGroups.contains(DiseaseGroup.MENTAL_HEALTH))
            {
                linearPredictor = linearPredictor.add(
		    GENDER_FFEMALE_DISEASEGRP_MENTAL_HEALTH_FLAG_COEFFICIENT);
            }

            if (diseaseGroups.contains(DiseaseGroup.HYPERTENSION))
            {
                linearPredictor = linearPredictor.add(
		    GENDER_FFEMALE_DISEASEGRP_HYPERTENSION_FLAG_COEFFICIENT);
            }

            if (diseaseGroups.contains(DiseaseGroup.DIABETES_TYPE_2))
            {
                linearPredictor = linearPredictor.add(
		    GENDER_FFEMALE_DISEASEGRP_DIABETES_TYPE_2_FLAG_COEFFICIENT);
            }
        }

        return linearPredictor;
    }

    /**
      * Given a set of diseases, extracts the set of groups.
      *
      * @param diseases the diseases set
      *
      * @return the disease groups
      */
    private Set<DiseaseGroup> extractDiseaseGroups(Set<Disease> diseases)
    {
        return diseases.stream().map(Disease::getGroup)
				    .collect(Collectors.toSet());
    }
}
