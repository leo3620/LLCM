import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-bloc-result',
  templateUrl: './bloc-result.component.html',
  styleUrls: ['./bloc-result.component.scss']
})
export class BlocResultComponent implements OnInit {

  @Input()
  result: number;
  constructor() { }

  ngOnInit(): void {
  }

}
