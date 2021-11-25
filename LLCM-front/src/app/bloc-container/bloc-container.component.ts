import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-bloc-container',
  templateUrl: './bloc-container.component.html',
  styleUrls: ['./bloc-container.component.scss']
})
export class BlocContainerComponent {

  @Input()
  title: string;

}
