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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * This class represents the basic input information for the HCH Risk
 * Stratification PRM score calculation.
 *
 * @author Precedence Health Care Pty Ltd
 */
public class HCHRiskStratificationPRMData
{
    /**
      * Ethnicity values used in calculation.
      */
    public static enum Ethnicity
    {
        NON_ATSI,
        ATSI,
        UNKNOWN
    }

    /**
      * BMI Category values used in calculation.
      */
    public static enum BMICategory
    {
        BMI_7_TO_25,
        BMI_25_TO_30,
        BMI_30_TO_35,
        BMI_35_TO_40,
        BMI_40_TO_200,
        NOT_RECORDED
    }

    /**
      * Smoking Status values used in calculation.
      */
    public static enum SmokingStatus
    {
        NON_SMOKER,
        EX_SMOKER,
        SMOKER,
        UNKNOWN
    }

    /**
      * Alcohol Status values used in calculation.
      */
    public static enum AlcoholStatus
    {
        NON_DRINKER,
        DRINKER,
        NOT_RECORDED
    }

    /**
      * IRSAD Decile values used in calculation.
      * Mappings of postcodes to the decile values may be obtained
      * from the Australian Bureau of Statistics, for example.
      */
    public static enum IRSADDecile
    {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        UNKNOWN
    }

    /**
      * Medication Group values used in calculation.
      */
    public static enum MedicationGroup
    {
        STATINS,
        ANTI_COAGULANTS,
        ANTI_DEPRESSANTS,
        ANTI_PSYCHOTICS,
        ANTI_INFLAMMATORY,
        STEROIDS
    }

    /**
      * Disease Group values used in calculation.
      */
    public static enum DiseaseGroup
    {
        RESPIRATORY,
        ATRIAL_FIBR,
        CARDIOVASCULAR,
        OSTEOARTHRITIS,
        OSTEOPOROSIS,
        RHEUMATOID,
        MENTAL_HEALTH,
        CANCER,
        DIGESTIVE,
        HYPERTENSION,
        BLOODFATS,
        CHRONIC_KIDNEY,
        DIABETES_TYPE_1,
        DIABETES_TYPE_2,
        VENOUS_THROMBO,
        OTHER
    }

    /**
      * Disease values used in calculation.
      */
    public static enum Disease
    {
        ASTHMA(DiseaseGroup.RESPIRATORY),
        COPD(DiseaseGroup.RESPIRATORY),
        CHRONIC_KIDNEY(DiseaseGroup.CHRONIC_KIDNEY),
        CORONARY_HEART(DiseaseGroup.CARDIOVASCULAR),
        STROKE(DiseaseGroup.CARDIOVASCULAR),
        TRANSIENT_ISCHAEMIC_ATTACK(DiseaseGroup.CARDIOVASCULAR),
        ATRIAL_FIBR(DiseaseGroup.ATRIAL_FIBR),
        CONG_HEART_FAILURE(DiseaseGroup.CARDIOVASCULAR),
        DIABETES_TYPE_1(DiseaseGroup.DIABETES_TYPE_1),
        DIABETES_TYPE_2(DiseaseGroup.DIABETES_TYPE_2),
        VENOUS_THROMBO(DiseaseGroup.VENOUS_THROMBO),
        OSTEOARTHRITIS(DiseaseGroup.OSTEOARTHRITIS),
        DEPRESSION(DiseaseGroup.MENTAL_HEALTH),
        ANXIETY(DiseaseGroup.MENTAL_HEALTH),
        BIPOLAR(DiseaseGroup.MENTAL_HEALTH),
        SCHIZOPHRENIA(DiseaseGroup.MENTAL_HEALTH),
        CANCER(DiseaseGroup.CANCER),
        DEMENTIA(DiseaseGroup.MENTAL_HEALTH),
        EPILEPSY(DiseaseGroup.OTHER),
        CROHNS(DiseaseGroup.DIGESTIVE),
        ULCER_COLITIS(DiseaseGroup.DIGESTIVE),
        COELIAC(DiseaseGroup.DIGESTIVE),
        STEATORRHEA(DiseaseGroup.DIGESTIVE),
        MALABSORP_SYNDR(DiseaseGroup.DIGESTIVE),
        CHRONIC_LIVER(DiseaseGroup.DIGESTIVE),
        PANCREATITIS(DiseaseGroup.DIGESTIVE),
        HYPERTENSION(DiseaseGroup.HYPERTENSION),
        OSTEOPOROSIS(DiseaseGroup.OSTEOPOROSIS),
        RHEUMATOID(DiseaseGroup.RHEUMATOID),
        HYPERLIPIDAEMIA(DiseaseGroup.BLOODFATS),
        HYPERCHOLESTEROLAEMIA(DiseaseGroup.BLOODFATS),
        HYPERTRIGLYCERIDAEMIA(DiseaseGroup.BLOODFATS),
        RHEUMATIC_HEART(DiseaseGroup.CARDIOVASCULAR);

	/**
	  * The Disease group.
	  */
        private final DiseaseGroup group;

	/**
	  * Constructor. Creates a disease based on a group.
	  */
        private Disease(DiseaseGroup group)
        {
            this.group = group;
        }

	/**
	  * Returns the group of a disease.
	  *
	  * @return the group of the disease
	  */
        public DiseaseGroup getGroup()
        {
            return group;
        }
    }

    /**
      * Pathology category values used in calculation.
      */
    public static enum PathologyCategory
    {
        LOW,
        MED,
        HIGH,
        NO_TEST_HISTORY
    }

    // All fields have explicit defaults, to make it easier not to have to
    // worry about 'uninitialised'-type edge cases in code that uses
    //this object.

    // Demographics
    private double age = 0;
    private boolean isFemale = false;
    private Ethnicity ethnicity = Ethnicity.UNKNOWN;
    private BMICategory bmiCategory = BMICategory.NOT_RECORDED;
    private SmokingStatus smokingStatus = SmokingStatus.UNKNOWN;
    private AlcoholStatus alcoholStatus = AlcoholStatus.NOT_RECORDED;
    // IRSAD decile
    private IRSADDecile irsadDecile = IRSADDecile.UNKNOWN;
    // Medications
    private Set<MedicationGroup> medicationGroups = Collections.emptySet();
    // Diseases
    private Set<Disease> diseases = Collections.emptySet();
    // Pathology
    private PathologyCategory haemoglobinCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory plateletsCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory alanineAminotransferaseCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory gammaGTCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory hba1cCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory bilirubinCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory cholesterolCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory creatinineCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory triglyceridesCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory albuminCreatinineRatioCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory ldlCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory eGFRCategory
	= PathologyCategory.NO_TEST_HISTORY;
    private PathologyCategory bloodPressureCategory
	= PathologyCategory.NO_TEST_HISTORY;

    /**
      * Returns the age.
      *
      * @return the age
      */
    public double getAge()
    {
        return age;
    }

    /**
      * Sets the age.
      *
      * @param age the age
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData setAge(double age)
    {
        this.age = age;
        return this;
    }

    /**
      * Returns if is femae.
      *
      * @return true if female, false otherwise
      */
    public boolean getIsFemale()
    {
        return isFemale;
    }

    /**
      * Sets if female.
      *
      * @param isFemale if female or not
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData setIsFemale(boolean isFemale)
    {
        this.isFemale = isFemale;
        return this;
    }

    /**
      * Returns Ethnicity.
      *
      * @return Ethnicity value
      */
    @Nonnull
    public Ethnicity getEthnicity()
    {
        return ethnicity;
    }

    /**
      * Sets Ethnicity.
      *
      * @param ethnicity the Ethnicity value
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setEthnicity(@Nonnull Ethnicity ethnicity)
    {
        this.ethnicity = ethnicity;
        return this;
    }

    /**
      * Returns BMI Category.
      *
      * @return BMI Category value
      */
    @Nonnull
    public BMICategory getBMICategory()
    {
        return bmiCategory;
    }

    /**
      * Sets BMI Category.
      *
      * @param bmiCategory the BMI Category value
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setBMICategory(@Nonnull BMICategory bmiCategory)
    {
        this.bmiCategory = bmiCategory;
        return this;
    }

    /**
      * Returns Smoking Status.
      *
      * @return smoking status value
      */
    @Nonnull
    public SmokingStatus getSmokingStatus()
    {
        return smokingStatus;
    }

    /**
      * Sets Smoking Status.
      *
      * @param smokingStatus the Smoking Status value
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setSmokingStatus(@Nonnull SmokingStatus smokingStatus)
    {
        this.smokingStatus = smokingStatus;
        return this;
    }

    /**
      * Returns Alcohol Status.
      *
      * @return Alcohol status value
      */
    @Nonnull
    public AlcoholStatus getAlcoholStatus()
    {
        return alcoholStatus;
    }

    /**
      * Sets Alcohol Status.
      *
      * @param alcoholStatus the Alcohol Status value
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setAlcoholStatus(@Nonnull AlcoholStatus alcoholStatus)
    {
        this.alcoholStatus = alcoholStatus;
        return this;
    }

    /**
      * Returns IRSAD Decile.
      *
      * @return IRSAD Decile value
      */
    @Nonnull
    public IRSADDecile getIRSADDecile()
    {
        return irsadDecile;
    }

    /**
      * Sets IRSAD Decile.
      *
      * @param irsadDecile the IRSAD Decile value
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setIRSADDecile(@Nonnull IRSADDecile irsadDecile)
    {
        this.irsadDecile = irsadDecile;
        return this;
    }

    /**
      * Returns the medication groups.
      *
      * @return the set of medication groups; will never contain a null element
      */
    @Nonnull
    public Set<MedicationGroup> getMedicationGroups()
    {
        return Collections.unmodifiableSet(medicationGroups);
    }

    /**
      * Sets the medication groups.
      *
      * @param medicationGroups the new set of medication groups; must not
      * contain a null element
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
            setMedicationGroups(@Nonnull Set<MedicationGroup> medicationGroups)
    {
        this.medicationGroups
	    = medicationGroups.isEmpty() ? Collections.emptySet()
                : EnumSet.copyOf(medicationGroups);
        return this;
    }

    /**
      * Returns the diseases.
      *
      * @return the set of diseases; will never contain a null element
      */
    @Nonnull
    public Set<Disease> getDiseases()
    {
        return Collections.unmodifiableSet(diseases);
    }

    /**
      * Sets the diseases.
      *
      * @param diseases the new set of diseases; must not contain a null element
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setDiseases(@Nonnull Set<Disease> diseases)
    {
        this.diseases = diseases.isEmpty()
	    ? Collections.emptySet() : EnumSet.copyOf(diseases);
        return this;
    }

    /**
      * Returns the Haemoglobin Category.
      *
      * @return the Haemoglobin Category
      */
    @Nonnull
    public PathologyCategory getHaemoglobinCategory()
    {
        return haemoglobinCategory;
    }

    /**
      * Sets the Haemoglobin Category.
      *
      * @param haemoglobinCategory the category for Haemoglobin
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setHaemoglobinCategory(@Nonnull PathologyCategory haemoglobinCategory)
    {
        this.haemoglobinCategory = haemoglobinCategory;
        return this;
    }

    /**
      * Returns the Platelets Category.
      *
      * @return the Platelets Category
      */
    @Nonnull
    public PathologyCategory getPlateletsCategory()
    {
        return plateletsCategory;
    }

    /**
      * Sets the Platelets Category.
      *
      * @param plateletsCategory the category for Platelets
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
            setPlateletsCategory(@Nonnull PathologyCategory plateletsCategory)
    {
        this.plateletsCategory = plateletsCategory;
        return this;
    }

    /**
      * Returns the Alanine Aminotransferase Category.
      *
      * @return the Alanine Aminotransferase Category
      */
    @Nonnull
    public PathologyCategory getAlanineAminotransferaseCategory()
    {
        return alanineAminotransferaseCategory;
    }

    /**
      * Sets the Alanine Aminotransferase Category.
      *
      * @param alanineAminotransferaseCategory the category
      *                                        for Alanine Aminotransferase
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData setAlanineAminotransferaseCategory(
            @Nonnull PathologyCategory alanineAminotransferaseCategory)
    {
        this.alanineAminotransferaseCategory = alanineAminotransferaseCategory;
        return this;
    }

    /**
      * Returns the Gamma GT Category.
      *
      * @return the GammaGT Category
      */
    @Nonnull
    public PathologyCategory getGammaGTCategory()
    {
        return gammaGTCategory;
    }

    /**
      * Sets the Gamma GT Category.
      *
      * @param gammaGTCategory the category for Gamma GT
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
            setGammaGTCategory(@Nonnull PathologyCategory gammaGTCategory)
    {
        this.gammaGTCategory = gammaGTCategory;
        return this;
    }

    /**
      * Returns the HbA1c Category.
      *
      * @return the HbA1c Category
      */
    @Nonnull
    public PathologyCategory getHbA1cCategory()
    {
        return hba1cCategory;
    }

    /**
      * Sets the HbA1c Category.
      *
      * @param hba1cCategory the category for HbA1c
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setHbA1cCategory(@Nonnull PathologyCategory hba1cCategory)
    {
        this.hba1cCategory = hba1cCategory;
        return this;
    }

    /**
      * Returns the Bilirubin Category.
      *
      * @return the Bilirubin Category
      */
    @Nonnull
    public PathologyCategory getBilirubinCategory()
    {
        return bilirubinCategory;
    }

    /**
      * Sets the Bilirubin Category.
      *
      * @param bilirubinCategory the category for Bilirubin
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
            setBilirubinCategory(@Nonnull PathologyCategory bilirubinCategory)
    {
        this.bilirubinCategory = bilirubinCategory;
        return this;
    }

    /**
      * Returns the Cholesterol Category.
      *
      * @return the Cholesterol Category
      */
    @Nonnull
    public PathologyCategory getCholesterolCategory()
    {
        return cholesterolCategory;
    }

    /**
      * Sets the Cholesterol Category.
      *
      * @param cholesterolCategory the category for Cholesterol
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setCholesterolCategory(@Nonnull PathologyCategory cholesterolCategory)
    {
        this.cholesterolCategory = cholesterolCategory;
        return this;
    }

    /**
      * Returns the Creatinine Category.
      *
      * @return the Creatinine Category
      */
    @Nonnull
    public PathologyCategory getCreatinineCategory()
    {
        return creatinineCategory;
    }

    /**
      * Sets the Creatinine Category.
      *
      * @param creatinineCategory the category for Creatinine
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setCreatinineCategory(@Nonnull PathologyCategory creatinineCategory)
    {
        this.creatinineCategory = creatinineCategory;
        return this;
    }

    /**
      * Returns the Triglycerides Category.
      *
      * @return the Triglycerides Category
      */
    @Nonnull
    public PathologyCategory getTriglyceridesCategory()
    {
        return triglyceridesCategory;
    }

    /**
      * Sets the Triglycerides Category.
      *
      * @param triglyceridesCategory the category for Triglycerides
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setTriglyceridesCategory(@Nonnull
		PathologyCategory triglyceridesCategory)
    {
        this.triglyceridesCategory = triglyceridesCategory;
        return this;
    }

    /**
      * Returns the Albumin Creatinine Ratio Category.
      *
      * @return the Albumin Creatinine Ratio Category
      */
    @Nonnull
    public PathologyCategory getAlbuminCreatinineRatioCategory()
    {
        return albuminCreatinineRatioCategory;
    }

    /**
      * Sets the Albumin Creatinine Ratio Category.
      *
      * @param albuminCreatinineRatioCategory the category for
      *                                       Albumin Creatinine Ratio
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData setAlbuminCreatinineRatioCategory(
            @Nonnull PathologyCategory albuminCreatinineRatioCategory)
    {
        this.albuminCreatinineRatioCategory = albuminCreatinineRatioCategory;
        return this;
    }

    /**
      * Returns the LDL Category.
      *
      * @return the LDL Category
      */
    @Nonnull
    public PathologyCategory getLDLCategory()
    {
        return ldlCategory;
    }

    /**
      * Sets the LDL Category.
      *
      * @param ldlCategory the category for LDL
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setLDLCategory(@Nonnull PathologyCategory ldlCategory)
    {
        this.ldlCategory = ldlCategory;
        return this;
    }

    /**
      * Returns the eGFR Category.
      *
      * @return the eGFR Category
      */
    @Nonnull
    public PathologyCategory getEGFRCategory()
    {
        return eGFRCategory;
    }

    /**
      * Sets the eGFR Category.
      *
      * @param eGFRCategory the category for eGFR
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setEGFRCategory(@Nonnull PathologyCategory eGFRCategory)
    {
        this.eGFRCategory = eGFRCategory;
        return this;
    }

    /**
      * Returns the Blood Pressure Category.
      *
      * @return the Blood Pressure Category
      */
    @Nonnull
    public PathologyCategory getBloodPressureCategory()
    {
        return bloodPressureCategory;
    }

    /**
      * Sets the Blood Pressure Category.
      *
      * @param bloodPressureCategory the category for Blood Pressure
      *
      * @return reference to this object
      */
    public HCHRiskStratificationPRMData
	setBloodPressureCategory(@Nonnull
		PathologyCategory bloodPressureCategory)
    {
        this.bloodPressureCategory = bloodPressureCategory;
        return this;
    }

    /**
      * Returns all variables in a human readable format.
      *
      * @return all variables
      */
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString(this);
    }
}
