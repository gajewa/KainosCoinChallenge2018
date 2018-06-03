import { Component, OnInit } from '@angular/core';
import * as Chartist from 'chartist';
import { HttpClient } from '@angular/common/http';
import { Chart } from 'chart.js';

declare var $: any;

@Component({
    selector: 'dashboard-cmp',
    moduleId: module.id,
    templateUrl: 'dashboard.component.html'
})

export class DashboardComponent implements OnInit{
    btcValue: Number;
    ethValue: Number;
    ltcValue: Number;
    trend: Boolean = false;
    isMonthly: Boolean = false;
    data: any;
    originalData: any;
    week: any = 4;
    chart = [];

    constructor(private http: HttpClient) { }

    ngOnInit(){

        this.http.get('https://murmuring-reef-89774.herokuapp.com/data').subscribe(vals => {

            this.data = vals;
            console.log(vals);
            this.originalData = JSON.parse(JSON.stringify(vals)); // easiest way I found to copy an object and not referencing the other one
            this.btcValue = this.data.btcValues[this.data.btcValues.length - 1];
            this.ethValue = this.data.ethValues[this.data.ethValues.length - 1];
            this.ltcValue = this.data.ltcValues[this.data.ltcValues.length - 1];

            this.refreshChart();
        });

    }

    toggleTrend(){
        if(this.trend){
            this.trend = false;
            this.data.btcTrend = [];
            this.data.ltcTrend = [];
            this.data.ethTrend = [];
        } else {
            this.trend = true;
            this.data.btcTrend = JSON.parse(JSON.stringify(this.originalData.btcTrend[this.week]));
            this.data.ethTrend = JSON.parse(JSON.stringify(this.originalData.ethTrend[this.week]));
            this.data.ltcTrend = JSON.parse(JSON.stringify(this.originalData.ltcTrend[this.week]));

        }

        this.refreshChart();
    }

    test(){
        if(this.isMonthly){
            this.toggleToMonth();
        } else
            this.toggleToWeekly();
    }

    toggleToMonth() {

        this.data = JSON.parse(JSON.stringify(this.originalData));
        this.refreshChart();
    }

    toggleToWeekly() {
        this.updateData();
    }

    lastWeek() {
        if (this.week > 0) {
            this.week -= 1;
            if(this.trend){
                this.data.btcTrend = JSON.parse(JSON.stringify(this.originalData.btcTrend[this.week]));
                this.data.ethTrend = JSON.parse(JSON.stringify(this.originalData.ethTrend[this.week]));
                this.data.ltcTrend = JSON.parse(JSON.stringify(this.originalData.ltcTrend[this.week]));
            }
            this.updateData();
        }
    }

    nextWeek(){
        if (this.week < 4) {
            this.week += 1;
            if(this.trend){
                this.data.btcTrend = JSON.parse(JSON.stringify(this.originalData.btcTrend[this.week]));
                this.data.ethTrend = JSON.parse(JSON.stringify(this.originalData.ethTrend[this.week]));
                this.data.ltcTrend = JSON.parse(JSON.stringify(this.originalData.ltcTrend[this.week]));
           }
            this.updateData();
        }
    }



    updateData(){

        this.data.labels = this.originalData.labels.slice(this.week * 7, (this.week + 1) * 7 );
        this.data.btcValues = this.originalData.btcValues.slice(this.week * 7, (this.week + 1) * 7 );
        this.data.ethValues = this.originalData.ethValues.slice(this.week * 7, (this.week + 1) * 7 );
        this.data.ltcValues = this.originalData.ltcValues.slice(this.week * 7, (this.week + 1) * 7 );
        const msg = [];


        for (let i = 0; i < this.originalData.messages.length - 1; i++) {
            if (this.originalData.messages[i].sample < (this.week + 1 ) * 7 && this.originalData.messages[i].sample > this.week * 7) {
                msg.push(this.originalData.messages[i]);
                // console.log(this.originalData.messages[i]);
            }
        }
        this.data.messages = msg;
        console.log("ORIGINAL DATA")
        console.log(this.originalData);
        console.log("DATA")
        console.log(this.data);
        this.refreshChart();
        }

    refreshChart() {
            this.chart = new Chart('canvas', {
                type: 'line',
                data: {
                    labels: this.data.labels,
                    datasets: [
                        {
                            label: 'Ethereum',
                            data: this.data.ethValues,
                            borderColor: '#3cba9f',
                            fill: false
                        },
                        {
                            label: 'Bitcoin',
                            data: this.data.btcValues,
                            borderColor: '#ffcc00',
                            fill: false
                        },
                        {
                            label: 'LiteCoin',
                            data: this.data.ltcValues,
                            fill: false,
                        },
                        {
                            data: this.data.btcTrend,
                            label: 'BTC Trend',
                            borderColor: 'red',
                            fill: false,
                        },
                        {
                            data: this.data.ethTrend,
                            label: 'ETH Trend',
                            borderColor: 'red',
                            fill: false,
                        },
                        {
                            data: this.data.ltcTrend,
                            label: 'LTC Trend',
                            borderColor: 'red',
                            fill: false,
                        }
                    ]
                },
                options: {
                    legend: {
                        display: true
                    },
                    scales: {
                        xAxes: [{
                            display: true
                        }],
                        yAxes: [{
                            display: true,
                            type: 'logarithmic'
                        }],
                    }
                }
            });
    }



}
