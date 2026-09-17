const SHEET_NAME = 'Respuestas';
 
// ---------- SERVIR LAS PÁGINAS ----------
 
function doGet(e) {

  const page = e.parameter.page || 'test';

  // ---------- TEST ----------
  if (page === 'test') {

    return HtmlService
      .createTemplateFromFile('test')
      .evaluate()
      .setTitle('Nimbo — tu diagnóstico');
  }


  // ---------- DASHBOARD DEL TEST ----------
  if (page === 'dashboard_test') {

    const userId = e.parameter.id;
    const data = getUserData(userId);

    const template = HtmlService
      .createTemplateFromFile('dashboard_test');

    template.data = data;

    return template
      .evaluate()
      .setTitle('Nimbo — tu diagnóstico');
  }


  // Página desconocida
  return HtmlService
    .createTemplateFromFile('test')
    .evaluate()
    .setTitle('Nimbo — tu diagnóstico');
}
 
// ---------- RECIBIR EL TEST ----------
 
function doPost(e) {
  const body = JSON.parse(e.postData.contents);
  // body esperado, ej:
  // { ingresos: 1800, gastos: 1200, ahorroMensual: 150,
  //   inversionMensual: 50, deuda: 0, fondoEmergencia: 1200, objetivo: "casa" }
 
  const scores = calcularScores(body);
  const userId = Utilities.getUuid();
 
  guardarFila(userId, body, scores);
 
  return ContentService
    .createTextOutput(JSON.stringify({ id: userId, scores: scores }))
    .setMimeType(ContentService.MimeType.JSON);
}
 
function procesarTest(respuestas) {
  const scores = calcularScores(respuestas);
  const userId = Utilities.getUuid();
  guardarFila(userId, respuestas, scores);
  return { id: userId, scores: scores };
}