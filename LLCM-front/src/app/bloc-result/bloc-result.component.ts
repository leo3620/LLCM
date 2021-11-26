import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import Chart from 'chart.js/auto';



@Component({
  selector: 'app-bloc-result',
  templateUrl: './bloc-result.component.html',
  styleUrls: ['./bloc-result.component.scss']
})

export class BlocResultComponent implements OnInit {

  @Input()
  result: number;
  @Input()
  labels : Array<string>;
  @Input()
  chartPercentList : Array<number>;
  myChart: Chart;
  constructor() {
  }


  ngOnInit(): void {
    const data = {
      labels: this.labels,
      datasets: [{
        backgroundColor: 'rgb(63, 81, 181)',
        borderColor: 'rgb(63, 81, 181)',
        data:this.chartPercentList,
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
        layout: {
          padding: {
            bottom: -15
          }
        }
      }
    };
    this.myChart = new Chart(
      document.getElementById('myChart') as HTMLCanvasElement,
      config as any
    );
    document.getElementById("myBtn").addEventListener("click", this.flush);
    }

  ngOnChanges(changes: SimpleChanges): void
  {
    console.log(this.labels)
    this.myChart.update();
  }

  flush() {    
    this.myChart.clear();
  }
}