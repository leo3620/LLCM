import { Component } from '@angular/core';
import {FormGroup} from "@angular/forms";

interface Food {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'LLCM-front';

  foods: Food[] = [
    {value: 'steak-0', viewValue: 'Steak'},
    {value: 'pizza-1', viewValue: 'Pizza'},
    {value: 'tacos-2', viewValue: 'Tacos'},
  ];

  // form: FormGroup;

  constructor() {
    /*this.form = this.fb.group({
      Age: "60",
      IsFemale: false,
      Ethnicity: "NON_ATSI",
      BMICategory: "BMI_7_TO_25",
      SmokingStatus: "NON_SMOKER",
      AlcoholStatus: "NON_DRINKER",
      IRSADDecile: "TEN",
      MedicationGroups: [
        "STATINS",
        "ANTI_COAGULANTS",
        "ANTI_DEPRESSANTS",
        "ANTI_PSYCHOTICS",
        "ANTI_INFLAMMATORY",
        "STEROIDS"
      ],
      Diseases: [

      ],
      Pathology:this.fb.group({
        HaemoglobinCategory: ['LOW'],
        PlateletsCategory:  ['LOW'],
        AlanineAminotransferaseCategory:  ['LOW'],
        GammaGTCategory:  ['LOW'],
        HbA1cCategory:  ['LOW'],
        BilirubinCategory:  ['LOW'],
        CholesterolCategory:  ['LOW'],
        CreatinineCategory:  ['LOW'],
        TriglyceridesCategory:  ['LOW'],
        AlbuminCreatinineRatioCategory: ['LOW'],
        LDLCategory:  ['LOW'],
        EGFRCategory: ['LOW'],
        BloodPressureCategory:  ['LOW']
      }),
    });

     */
  }



}
