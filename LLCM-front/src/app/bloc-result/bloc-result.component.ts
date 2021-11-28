import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import Chart from 'chart.js/auto';



@Component({
  selector: 'app-bloc-result',
  templateUrl: './bloc-result.component.html',
  styleUrls: ['./bloc-result.component.scss']
})

export class BlocResultComponent implements OnInit {

  @Input()
  result: number;

  _labels : Array<string>;
  _chartPercentList : Array<number>;

  @Input() set chartPercentList(valeur: Array<number>) {
    this._chartPercentList =valeur;
    this.updateChart();
  }

  @Input() set labels(valeur: Array<string>) {
    this._labels =valeur;
  }

  @Output()
  rollback= new EventEmitter<any>();

  myChart: Chart;
  constructor() {
  }


  ngOnInit(): void {
    const data = {
      datasets: [{
        backgroundColor: 'rgb(63, 81, 181)',
        borderColor: 'rgb(63, 81, 181)',
        data:this._chartPercentList,
      }]
    };
    const config = {
      type: 'line',
      data: data,

      options: {
        plugins: {
          legend: false,
        },
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
              max: 100,
              min: 0,
              ticks: {
                  stepSize: 1
              }
          }
      },
      }
    };
    this.myChart = new Chart(
      document.getElementById('myChart') as HTMLCanvasElement,
      config as any
    );
    }


  updateChart(){
    if(this.myChart) {
      this.myChart.data.labels = this._labels;
      this.myChart.data.datasets[0].data = this._chartPercentList;
      this.myChart.update();
    }
  }
  flush() {
    this.rollback.emit();
  }
}
