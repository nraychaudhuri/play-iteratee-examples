$(function () {
    $(document).ready(function() {
        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });
    
        var chart;
        $('#container').highcharts({
            chart: {
                type: 'spline',
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10,
                events: {
                    load: function() {
                        var highCharts = this
                        var webSocket = new WebSocket($("#ws-url").val());
                        webSocket.onopen = function(evt) {
                          console.log("websocket connection open")
                        }
                        
                        webSocket.onmessage = function(evt) {
                          var json = JSON.parse(evt.data)
                          console.log(json)
                          if(json.tpe == "Fast") {
                             var series = highCharts.series[0];
                             var time = (new Date()).getTime()
                          	series.addPoint([time, json.e], true, true);
                          }
                          if(json.tpe == "Slow") {
                             var series = highCharts.series[1];
                             var time = (new Date()).getTime()
                          	series.addPoint([time, json.e], true, true);
                          }
                        
                        }
                    }
                }
            },
            title: {
                text: 'Events demo'
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 150
            },
            yAxis: {
                gridLineWidth: 0,
                title: {
                    text: 'Value'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                formatter: function() {
                        return '<b>'+ this.series.name +'</b><br/>'+
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br/>'+
                        Highcharts.numberFormat(this.y, 2);
                }
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            series: [{
                name: 'Fast lane',
                data: (function() {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
    
                    for (i = -19; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: Math.random()
                        });
                    }
                    return data;
                })()
            },
            {
                name: 'Slow lane',
                data: (function() {
                    // generate an array of random data
                    var data = [],
                        time = (new Date()).getTime(),
                        i;
    
                    for (i = -19; i <= 0; i++) {
                        data.push({
                            x: time + i * 1000,
                            y: Math.random()
                        });
                    }
                    return data;
                })()
            }]
        });
    });
    
});