var root;  
var tab;

function exportToXL(eSrc) {
    var oExcel; 
    var oExcelSheet; 
    var oWkBooks;
    var cols; 
    oExcel = new ActiveXObject('Excel.Application');
    oWkBooks = oExcel.Workbooks.Add;
    oExcelSheet = oWkBooks.Worksheets(1);
    oExcelSheet.Activate();
    if (eSrc.tagName != 'TABLE') {
        alert('No ha sido posible exportar la tabla a excell');
    }
    cols = Math.ceil(eSrc.cells.length / eSrc.rows.length);
    for (var i = 0; i < eSrc.cells.length; i ++)
    {
        var c, r;
        r = Math.ceil((i+1) / cols);
        c = (i+1)-((r-1)*cols)
        if (eSrc.cells(i).tagName == 'TH') { 
            oExcel.ActiveSheet.Cells(r,c).Font.Bold = true;
            oExcel.ActiveSheet.Cells(r,c).Interior.Color = 14474460; 
        }
        if (eSrc.cells(i).childNodes.length > 0 && eSrc.cells(i).childNodes(0).tagName == "B") 
            oExcel.ActiveSheet.Cells(r,c).Font.Bold = true;
        oExcel.ActiveSheet.Cells(r,c).Value = eSrc.cells(i).innerText;
    }
    oExcelSheet.Application.Visible = true;
}


function ponerDatos(idEq){
    
    $.ajaxSetup({ 
        scriptCharset: "utf-8" , 
        contentType: "application/json; charset=utf-8"
    });
    
    url = root + "/json/auditoria?equipo=" + idEq;
    
    if (tab != null)
        document.getElementById("cuerpo").removeChild(tab);
    
    tab = document.createElement("table");
    tab.setAttribute("width", "100%");
    tab.setAttribute("id", "tablaDatos");
    tr = document.createElement("tr");
    tab.appendChild(tr);
    th = document.createElement("th");
    th.innerHTML = "Fecha";    
    th.setAttribute("width", "15%");
    tr.appendChild(th);
    th = document.createElement("th");
    th.setAttribute("width", "70%");
    th.innerHTML = "Concepto";
    tr.appendChild(th);
    th = document.createElement("th");
    th.setAttribute("width", "15%");
    th.innerHTML = "Cantidad";
    tr.appendChild(th);
    
    $.getJSON(url, function(data){
        for ( var i=0, len= data.length; i< len;i++){
            var obj = data[i];
            tr = document.createElement("tr");            
            td = document.createElement("td");
            td.innerHTML = obj.fecha;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = obj.clase;
            tr.appendChild(td);
            td = document.createElement("td");
            td.innerHTML = obj.cantidad;
            tr.appendChild(td);
            tab.appendChild(tr);
           }
       }
   )
    
     document.getElementById("cuerpo").appendChild(tab);
       
}
     
 
function mostrarDatos(ruta, idEq){    
    
    root = ruta;
    ponerDatos(idEq);
    document.getElementById("ventanaDetalle").style.display = '';
} 

function exportarDatos(ruta, idEq){
    root = ruta;
    ponerDatos(idEq);
    exportToXL(document.getElementById("tablaDatos"));
}
		
function cerrarVentana(){
    document.getElementById("ventanaDetalle").style.display = 'none';    
}		

