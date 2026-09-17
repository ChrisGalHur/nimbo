```javascript
const SHEET_NAME = 'Respuestas';


// ============================================================
// SERVIR PÁGINAS / API GET
// ============================================================

function doGet(e) {

  const params = e && e.parameter ? e.parameter : {};

  // ----------------------------------------------------------
  // API: obtener datos de un diagnóstico
  // Ejemplo:
  // ?id=12345
  // ----------------------------------------------------------

  if (params.id && !params.page) {

    try {

      const data = getUserData(params.id);

      if (!data) {
        return respuestaJSON({
          ok: false,
          error: 'No se ha encontrado ese diagnóstico.'
        });
      }

      return respuestaJSON({
        ok: true,
        data: data
      });

    } catch (error) {

      return respuestaJSON({
        ok: false,
        error: error.message
      });
    }
  }


  // ----------------------------------------------------------
  // PÁGINA TEST
  // ----------------------------------------------------------

  if (params.page === 'test' || !params.page) {

    return HtmlService
      .createTemplateFromFile('test')
      .evaluate()
      .setTitle('Nimbo — tu diagnóstico');
  }


  // ----------------------------------------------------------
  // DASHBOARD DEL TEST
  // ----------------------------------------------------------

  if (params.page === 'dashboard_test') {

    const userId = params.id;
    const data = getUserData(userId);

    const template = HtmlService
      .createTemplateFromFile('dashboard_test');

    template.data = data;

    return template
      .evaluate()
      .setTitle('Nimbo — tu diagnóstico');
  }


  // ----------------------------------------------------------
  // PÁGINA DESCONOCIDA
  // ----------------------------------------------------------

  return HtmlService
    .createTemplateFromFile('test')
    .evaluate()
    .setTitle('Nimbo — tu diagnóstico');
}



// ============================================================
// RECIBIR TEST
// ============================================================

function doPost(e) {

  try {

    if (!e || !e.postData || !e.postData.contents) {
      throw new Error('No se han recibido datos.');
    }

    const body = JSON.parse(e.postData.contents);

    // --------------------------------------------------------
    // Calcular puntuaciones
    // --------------------------------------------------------

    const scores = calcularScores(body);

    // --------------------------------------------------------
    // Generar identificador único
    // --------------------------------------------------------

    const userId = Utilities.getUuid();

    // --------------------------------------------------------
    // Guardar en Google Sheets
    // --------------------------------------------------------

    guardarFila(userId, body, scores);

    // --------------------------------------------------------
    // Responder
    // --------------------------------------------------------

    return respuestaJSON({
      ok: true,
      id: userId,
      scores: scores
    });

  } catch (error) {

    return respuestaJSON({
      ok: false,
      error: error.message
    });
  }
}



// ============================================================
// PROCESAR TEST INTERNAMENTE
// ============================================================

function procesarTest(respuestas) {

  const scores = calcularScores(respuestas);

  const userId = Utilities.getUuid();

  guardarFila(userId, respuestas, scores);

  return {
    ok: true,
    id: userId,
    scores: scores
  };
}



// ============================================================
// RESPUESTA JSON
// ============================================================

function respuestaJSON(objeto) {

  return ContentService
    .createTextOutput(JSON.stringify(objeto))
    .setMimeType(ContentService.MimeType.JSON);
}
```
