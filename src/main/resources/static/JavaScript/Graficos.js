
var root;    

var templatePastel = {
            chart: {
                renderTo: '',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: ''
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage}%</b>',
                percentageDecimals: 1
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function() {
                            return '<b>'+ this.point.name +'</b><br/>' + this.y + ' ('+ Math.round(this.percentage) +' %)';
                        }
                    }
                }
            },
            series: [{
                type: 'pie',
                name: '',
                data: []
            }]
};  

var templateLineal = {
            chart: {
                renderTo: '',
                type: 'line',
                zoomType: 'x',
                spacingRight: 20
            },
            title: {
                text: ''
            },
            subtitle: {
                text: document.ontouchstart === undefined ?
                    'Clickar y arrastrar en el grafico para hace Zoom' :
                    'Arrastrar con el dedo en el grafico para hace Zoom'
            },
            xAxis: {
                type: 'datetime',
                maxZoom: 14 * 24 * 3600000, // fourteen days
                title: {
                    text: null
                }
            },
            yAxis: {
                title: {
                    text: ''
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                shared: true
            },
            legend: {
                enabled: false
            },               
            series: [{
                type: 'area',
                name: '',
                data: []            
            }]
};

var templateLinealFill = {
            chart: {
                renderTo: '',
                zoomType: 'x',
                spacingRight: 20
            },
            title: {
                text: ''
            },
            subtitle: {
                text: document.ontouchstart === undefined ?
                    'Clickar y arrastrar en el grafico para hace Zoom' :
                    'Arrastrar con el dedo en el grafico para hace Zoom'
            },
            xAxis: {
                type: 'datetime',
                maxZoom: 14 * 24 * 3600000, // fourteen days
                title: {
                    text: null
                }
            },
            yAxis: {
                title: {
                    text: ''
                },
                min: 0,
                max: 0,
                startOnTick: false,
                showFirstLabel: false
            },
            tooltip: {
                shared: true
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, 'rgba(2,0,0,0)']
                        ]
                    },
                    lineWidth: 1,
                    marker: {
                        enabled: false,
                        states: {
                            hover: {
                                enabled: true,
                                radius: 5
                            }
                        }
                    },
                    shadow: false,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    }
                }
            },    
            series: [{
                type: 'area',
                name: '',
                data: []            
            }]
};


        
     function ponerDatosBolsa(fecha){

         template = templateLineal;
         $.ajaxSetup({mimeType: "text/plain"});
         url = root + "/json/bolsa";
         if (fecha != null) url = url + "?fecha=" + fecha;

         $.getJSON(url, function(data){
            template.series[0].data = [];
            acumulado = 0;
            min = 0;
            max = 0;
            for ( var i=0, len= data.length; i< len;i++){
                var obj = data[i];
                var fecha = obj.fecha.split("/");
                acumulado = acumulado + obj.valor;
                if (acumulado > max) max = acumulado;
                if (acumulado < min) min = acumulado;
                // No se porque suma un mes mas
                var datos = [Date.UTC(fecha[2],fecha[1] - 1,fecha[0]),acumulado];
                template.series[0].data.push(datos);
            }
            
            template.chart.renderTo = 'bolsa';
            template.title.text = 'Fluctuación Bolsa';
            template.yAxis.title.text = '% de subida';
            template.yAxis.min = min - 10;
            template.yAxis.max = max + 10;
            template.series[0].name = '% fluctución';
            chart = new Highcharts.Chart(template);
            chart.setSize(600, 300)
         }
         )         
     }
     
     function ponerDatosPastel(url, clase){
         template = templatePastel;
         totalGasto = 0;
         totalIngreso = 0;
         
         $.ajaxSetup({mimeType: "text/plain"});   

         $.getJSON(url ,function(data){
             template.series[0].data = [];

            for ( var i=0, len= data.length; i< len;i++){
                var obj = data[i];
                var tipo = obj.tipo;
                var valor = obj.valor;
                var datos = [tipo, valor];
                template.series[0].data.push(datos);
            }

            template.chart.renderTo = clase;
            template.title.text = clase.toUpperCase();
            template.series[0].name = 'Porcentaje';
            chart = new Highcharts.Chart(template);
            chart.setSize(600, 300)
         }
         )         
     }

     function abrirGrafico(ruta, elem, param1, param2){

         //alert(ruta + " - " + elem + " - " + param1 + " - " + param2);

         root = ruta
         if (elem == 'graficoBolsa') ponerDatosBolsa(null);
         // param1 = idEquipo
         else if (elem == 'graficoPresupuesto'){
             tipos = new Array ("gastos", "ingresos", "balance");
             for (i=0, len= tipos.length; i < len; i++){
                 url = root + "/json/presupuesto?equipo=" + param1 + "&tipo=" + tipos[i];
                 ponerDatosPastel(url, tipos[i]);
             }
         } 
         else if (elem == 'graficoMovimientos'){
             tipos = new Array ("gastos", "ingresos", "balance");
             for (i=0, len= tipos.length; i < len; i++){
                 url = root + "/json/movimientos?equipo=" + param1 + "&tipo=" + tipos[i] + "&total=" + param2;
                 ponerDatosPastel(url, tipos[i]);
             }
         } 
         else if (elem == 'graficoPosicionesQuiniela'){
                 url = root + "/json/posicionesQuiniela?equipo=" + param1;
                 if (param2 != null) url = url + "&competicion=" + param2;
                 ponerDatosPastel(url, "posiciones");
         } 
        document.getElementById(elem).style.display = '';
    } 
		
    function cerrarGrafico(elem){
        document.getElementById(elem).style.display = 'none';    
    }		

