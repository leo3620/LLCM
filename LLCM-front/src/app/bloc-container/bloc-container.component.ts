import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-bloc-container',
  templateUrl: './bloc-container.component.html',
  styleUrls: ['./bloc-container.component.scss']
})
export class BlocContainerComponent implements OnInit {

  constructor() { }

  @Input()
  title: string;

  ngOnInit(): void {
  }

}
