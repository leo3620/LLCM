import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup} from '@angular/forms';

import {medicationGroup} from './enum/MedicationGroup';
import {diseases} from './enum/Disease';
import {pathology} from './enum/Pathology';
import {pathologyCategory} from './enum/PathologyCategory';
import {HttpClient} from "@angular/common/http";
import {alcoholStatus} from "./enum/AlcoholSatus";
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
export class AppComponent implements OnInit{
  title = 'LLCM-front';

  form: FormGroup;
  result = 0;
  labels = [];
  chartPercentList = [];
  block : BlocResultComponent;
  medicationGroupsData = [];
  diseasesData = [];
  pathologyData = [];

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
      AlcoholStatus: 'NOT_RECORDED',
      MedicationGroups: new FormArray([]),
      Diseases: new FormArray([]),
      Pathology: new FormArray([]),
      Age: 20,
      IsFemale: false,
      Ethnicity: 'UNKNOWN',
    });

    this.medicationGroupsData = this.getMedicine();
    this.diseasesData = this.getDisease();
    this.pathologyData = this.getPathology();
    this.addCheckboxes();

    this.onChanges();
  }

  onChanges(): void {
    this.form.valueChanges.subscribe(val => {
      this.fetchResult();
    });
 

  }

  fetchResult(): void {
    this.http.post<number>("http://localhost:8080/calculate", this.submit()).subscribe(result => {
      this.result = result;
    this.labels.push("");
    this.chartPercentList.push(result*100);
    })

  }

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

  submit(): any {
    const selectedMedicineIds = this.form.value.MedicationGroups
      .map((checked, i) => checked ? this.medicationGroupsData[i].id : null)
      .filter(v => v !== null);

    const selectedDiseaseIds = this.form.value.Diseases
      .map((checked, i) => checked ? this.diseasesData[i].id : null)
      .filter(v => v !== null);

    const selectedPathologyIds = this.form.value.Pathology.reduce((acc, act, index) => {
      return {...acc, [pathology[index].id]: act}
    }, {});

   // const selectedPathologyIds = this.form.value.Pathology.map((value, i) => ({ [pathology[i].id] : value}));

    const finalObject = this.form.getRawValue();
    finalObject.MedicationGroups = selectedMedicineIds;
    finalObject.Diseases = selectedDiseaseIds;
    finalObject.Pathology = selectedPathologyIds;
    console.log(finalObject);
    return finalObject;
  }

}
