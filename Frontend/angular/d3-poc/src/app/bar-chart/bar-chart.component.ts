import { AfterViewInit, Component, OnInit } from '@angular/core';
import * as d3 from 'd3';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.css']
})
export class BarChartComponent implements OnInit, AfterViewInit {

  private data = [
    {"Framework": "Vue", "Stars": "166443", "Released": "2014"},
    {"Framework": "React", "Stars": "150793", "Released": "2013"},
    {"Framework": "Angular", "Stars": "62342", "Released": "2016"},
    {"Framework": "Backbone", "Stars": "27647", "Released": "2010"},
    {"Framework": "Ember", "Stars": "21471", "Released": "2011"},
  ];

  private svg : any;
  private margin = 50;
  private width = 850 - (this.margin * 2);
  private height = 500 - (this.margin * 2);

  constructor() {
    
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.createSvg() 
    this.drawBars(this.data)
  }

  private createSvg (): void {
    this.svg =
    d3.select('figure#bar')
    .append("svg")
    .attr("width", this.width + (this.margin * 2))
    .attr("height", this.height + (this.margin * 2))
    .append("g")
    .attr("translate", `transform(${this.margin}, ${this.margin})`)

  }

  private drawBars(data: any[]): void {
    // Create the X-axis band scale
    const x = d3.scaleBand()
      .range([0, this.width])
      .domain(data.map(d => d.Framework))
      .padding(0.2);

    this.svg.append("g")
      .attr("transform", "translate(50," + this.height + ")")
      .call(d3.axisBottom(x))
      .selectAll("text")
      .attr("transform", "translate(-10,0) rotate(-45)")
      .style("text-anchor", "end");

    // Create the Y-axis band scale
    const y = d3.scaleLinear()
      .domain([0, d3.max(data.map(d=>d.Stars))])
      .range([this.height, 0]);

    // Draw the Y-axis on the DOM
    this.svg.append("g")
      .attr("transform", "translate(50, 0)")
      .call(d3.axisLeft(y));

    // Create and fill the bars
    this.svg.selectAll("bars")
    .data(data)
    .enter()
    .append("rect")
    .attr("transform", "translate(50, 0)")
    .attr("x", (d : any) => x(d.Framework))
    .attr("y", (d : any) => y(d.Stars))
    .attr("width", x.bandwidth())
    .attr("height", (d : any) => this.height - y(d.Stars))
    .attr("fill", "#d04a35");
  }

}
