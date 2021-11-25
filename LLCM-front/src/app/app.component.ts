import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from "@angular/common/http";

import {medicationGroup} from './enum/MedicationGroup';
import {diseases} from './enum/Disease';
import {pathology} from './enum/Pathology';
import {pathologyCategory} from './enum/PathologyCategory';
import {alcoholStatus} from "./enum/AlcoholSatus";
import {smokingStatus} from "./enum/SmokingStatus";
import {LabelType, Options} from "@angular-slider/ngx-slider";
import {BMICategory} from "./enum/BmiCategory";
import { BlocResultComponent } from './bloc-result/bloc-result.component';

interface Food {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'LLCM-front';

  form: FormGroup;
  result = 0;
  labels = [];
  chartPercentList = [];
  block : BlocResultComponent;
  medicationGroupsData = [];
  diseasesData = [];
  pathologyData = [];

  minValue: number = 1;
  maxValue: number = 2;

  options: Options = {
    floor: 1,
    ceil: 6,
    step: 1,
    showTicks: true,
    stepsArray: [
      {value: 1, legend: '7'},
      {value: 2, legend: '25'},
      {value: 3, legend: '30'},
      {value: 4, legend: '35'},
      {value: 5, legend: '40'},
      {value: 6, legend: '..200'},
    ],
    minRange: 1,
    maxRange: 1,
    animate: false,
    pushRange: true,
    translate: (): string => {
      return '';
    }
  };

  get medicineFormArray(): FormArray {
    return this.form.controls.MedicationGroups as FormArray;
  }

  get diseaseFormArray(): FormArray {
    return this.form.controls.Diseases as FormArray;
  }

  get pathologyFormArray(): FormArray {
    return this.form.controls.Pathology as FormArray;
  }

  get pathologyCategory(): any[] {
    return pathologyCategory;
  }

  constructor(private fb: FormBuilder, private http: HttpClient) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      BMICategory: 1,
      AlcoholStatus: 'NOT_RECORDED',
      SmokingStatus: 'UNKNOWN',
      MedicationGroups: new FormArray([]),
      Diseases: new FormArray([]),
      Pathology: new FormArray([]),
      Age: 20,
      IsFemale: false,
      Ethnicity: 'UNKNOWN',
      bmiUnknown: true,
    });

    this.medicationGroupsData = this.getMedicine();
    this.diseasesData = this.getDisease();
    this.pathologyData = this.getPathology();
    this.addCheckboxes();

    this.onChanges();
    this.fetchResult();
  }

  onChanges(): void {
    this.form.valueChanges.subscribe(val => {
      this.fetchResult();
    });
 

  }

  /**
   * Fetch result from the backend
   */
  fetchResult(): void {
    this.http.post<number>("http://localhost:8080/calculate", this.submit()).subscribe(result => {
      this.result = result;
    this.labels.push("");
    this.chartPercentList.push(result*100);
    })
  }

  /**
   * Add formControl for each kind of thing (haha)
   */
  private addCheckboxes(): void {
    this.medicationGroupsData.forEach(() => this.medicineFormArray.push(new FormControl(false)));
    this.pathologyData.forEach(() => this.pathologyFormArray.push(new FormControl('NO_TEST_HISTORY')));
    this.diseasesData.forEach(() => this.diseaseFormArray.push(new FormControl(false)));
  }

  getMedicine(): any {
    return medicationGroup;
  }

  getDisease(): any {
    return diseases;
  }

  getPathology(): any {
    return pathology;
  }

  getAlcoholStatus(): any {
    return alcoholStatus;
  }

  getSmokingStatus(): any {
    return smokingStatus;
  }

  getBmiCategory(): any {
    return BMICategory;
  }

  /**
   * Transform our form to fit with what the algo wants
   */
  submit(): any {
    const finalObject = this.form.getRawValue();

    const selectedMedicineIds = this.form.value.MedicationGroups
    .map((checked, i) => checked ? this.medicationGroupsData[i].id : null)
    .filter(v => v !== null);

    const selectedDiseaseIds = this.form.value.Diseases
    .map((checked, i) => checked ? this.diseasesData[i].id : null)
    .filter(v => v !== null);

    const selectedPathologyIds = this.form.value.Pathology.reduce((acc, act, index) => {
      return {...acc, [pathology[index].id]: act}
    }, {});

    if (this.form.value.bmiUnknown) {
      finalObject.BMICategory = 'NOT_RECORDED';
    } else {
      finalObject.BMICategory = this.getBmiCategory().find(bmi => bmi.formValue === this.minValue).id;
    }

    finalObject.MedicationGroups = selectedMedicineIds;
    finalObject.Diseases = selectedDiseaseIds;
    finalObject.Pathology = selectedPathologyIds;
    delete finalObject.bmiUnknown;
    return finalObject;
  }
}
