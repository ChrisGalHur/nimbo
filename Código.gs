function doPost(e) {

  try {

    // Recibir datos enviados desde GitHub Pages
    var datos = JSON.parse(e.postData.contents);


    // Abrir la hoja
    var hoja = SpreadsheetApp
      .getActiveSpreadsheet()
      .getSheetByName("Respuestas");


    // Crear un ID único para este diagnóstico
    var id = Utilities.getUuid();


    // Guardar los datos
    hoja.appendRow([

      new Date(),

      id,

      datos.ingresos,

      datos.gastos,

      datos.ahorroMensual,

      datos.inversionMensual,

      datos.deuda,

      datos.fondoEmergencia

    ]);


    // Responder al test
    return ContentService

      .createTextOutput(

        JSON.stringify({

          ok: true,

          id: id

        })

      )

      .setMimeType(
        ContentService.MimeType.JSON
      );


  } catch(error) {


    return ContentService

      .createTextOutput(

        JSON.stringify({

          ok: false,

          error: error.message

        })

      )

      .setMimeType(
        ContentService.MimeType.JSON
      );

  }

}