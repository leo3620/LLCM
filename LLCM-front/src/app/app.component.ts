import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from "@angular/common/http";

import {medicationGroup} from './enum/MedicationGroup';
import {diseases} from './enum/Disease';
import {pathology} from './enum/Pathology';
import {pathologyCategory} from './enum/PathologyCategory';
import {alcoholStatus} from "./enum/AlcoholSatus";
import {smokingStatus} from "./enum/SmokingStatus";
import {Options} from "@angular-slider/ngx-slider";
import {BMICategory} from "./enum/BmiCategory";
import {BlocResultComponent} from './bloc-result/bloc-result.component';

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
  block: BlocResultComponent;
  medicationGroupsData = [];
  diseasesData = [];
  pathologyData = [];
  parameterString: string;
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
    this.updateChartLabel();

    this.http.post<number>("http://localhost:8080/calculate", this.submit()).subscribe(result => {
      this.result = result;

      this.labels = [...this.labels, this.parameterString];
      this.chartPercentList = [...this.chartPercentList, result * 100];
    })

    this.form.markAsPristine()
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


  /**
   * Update chart label depending on the form
   */
  updateChartLabel(): any {
    Object.keys(this.form.controls).map((key) => {
      const parsedValue = {
        [key]: this.form.get(key).value,
        changed: this.form.get(key).dirty
      }

      if (parsedValue.changed) {
        this.parameterString = key;
        switch (key) {
          case 'Diseases':
            let diseasesFormArray = this.form.get("Diseases") as FormArray;
            diseasesFormArray.controls.filter((x, index) => {
              if (x.dirty) {
                let sign = this.form.value.Diseases[index] ? '+ ' : '- '
                this.parameterString = sign + this.getDisease()[index].label;
                return true;
              }
            });
            break;
          case 'MedicationGroups':
            let medicationFormArray = this.form.get("MedicationGroups") as FormArray;
            medicationFormArray.controls.filter((x, index) => {
              if (x.dirty) {
                let sign = this.form.value.MedicationGroups[index] ? '+ ' : '- '
                this.parameterString = sign + this.getMedicine()[index].label;
                return true;
              }
            });
            break;
          case 'Pathology':
            let pathologyFormArray = this.form.get("Pathology") as FormArray;
            pathologyFormArray.controls.filter((x, index) => {
              if (x.dirty) {
                let pathology = this.getPathology()[index].label;
                let level = this.pathologyCategory.find(x => x.id === this.form.value.Pathology[index]).label
                this.parameterString = pathology + '(' + level + ')';
                return true;
              }
            });
            break;
          case 'Ethnicity': {
            this.parameterString = parsedValue.Ethnicity === "UNKNOWN" ? "Ethnie inconnue" : parsedValue.Ethnicity;
            break;
          }
          case 'IsFemale': {
            this.parameterString = parsedValue.IsFemale ? "Femme" : "Homme"
            break;
          }
          case 'Age': {
            this.parameterString = "Age: " + parsedValue.Age;
            break;
          }
          case 'AlcoholStatus': {
            this.parameterString = this.getAlcoholStatus().find(x => x.id === parsedValue.AlcoholStatus).label
            break;
          }
          case 'SmokingStatus': {
            this.parameterString = this.getSmokingStatus().find(x => x.id === parsedValue.SmokingStatus).label
            break;
          }
          case 'bmiUnknown': {
            if (parsedValue.bmiUnknown) {
              this.parameterString = "IMC Inconnu";
            } else {
              let bmi = this.getBmiCategory().find(bmi => bmi.formValue === this.minValue)
              this.parameterString = "IMC: " + bmi.min + '-' + bmi.max;
            }
            break;
          }
          default:
            break;
        }
      }
    })
  }

  /**
   * Update BMI label and fetch result
   */
  updateBMI() {
    let bmi = this.getBmiCategory().find(bmi => bmi.formValue === this.minValue)
    this.parameterString = "IMC: " + bmi.min + '-' + bmi.max;
    this.fetchResult();
  }

  /**
   * Remove last element and trigger the changes by re affecting the variable
   */
  rollback(): void {
    this.chartPercentList = this.chartPercentList.slice(0, this.chartPercentList.length - 1);
    this.labels = this.labels.slice(0, this.labels.length - 1);
  }
}
