// ---------- CÁLCULO DE PUNTUACIONES ----------
// Ejemplo simplificado — ajusta los umbrales a tu criterio real.
 
function calcularScores(d) {
  const ingresos = clamp((d.ingresos / 2500) * 100, 0, 100);
  const gastos = clamp(100 - ((d.gastos / d.ingresos) * 100 - 50), 0, 100);
  const ahorro = clamp((d.ahorroMensual / (d.ingresos * 0.2)) * 100, 0, 100);
  const inversion = clamp((d.inversionMensual / (d.ingresos * 0.1)) * 100, 0, 100);
  const mesesGastos = d.fondoEmergencia / d.gastos;
  const emergencia = clamp((mesesGastos / 3) * 100, 0, 100);
  const deudas = clamp(100 - (d.deuda / d.ingresos) * 100, 0, 100);
 
  const total = Math.round(
    ingresos * 0.15 + gastos * 0.2 + ahorro * 0.2 +
    inversion * 0.15 + emergencia * 0.2 + deudas * 0.1
  );
 
  return {
    total: total,
    ingresos: Math.round(ingresos),
    gastos: Math.round(gastos),
    ahorro: Math.round(ahorro),
    inversion: Math.round(inversion),
    emergencia: Math.round(emergencia),
    deudas: Math.round(deudas)
  };
}

function clamp(n, min, max) {
  return Math.max(min, Math.min(max, isFinite(n) ? n : 0));
}

// ---------- GUARDAR Y LEER DE LA HOJA ----------
 
function guardarFila(userId, respuestas, scores) {
  const sheet = getSheet();
  sheet.appendRow([
    new Date(), userId,
    respuestas.ingresos, respuestas.gastos, respuestas.ahorroMensual,
    respuestas.inversionMensual, respuestas.deuda, respuestas.fondoEmergencia,
    scores.total, scores.ingresos, scores.gastos, scores.ahorro,
    scores.inversion, scores.emergencia, scores.deudas
  ]);
}
 
function getUserData(userId) {
  const sheet = getSheet();
  const rows = sheet.getDataRange().getValues();
  const headers = rows[0];
  const row = rows.find(r => r[1] === userId);
  if (!row) return null;
 
  const obj = {};
  headers.forEach((h, i) => obj[h] = row[i]);
  return obj;
}
 
function getSheet() {
  const ss = SpreadsheetApp.getActiveSpreadsheet();
  let sheet = ss.getSheetByName(SHEET_NAME);
  if (!sheet) {
    sheet = ss.insertSheet(SHEET_NAME);
    sheet.appendRow([
      'Timestamp', 'UserId', 'Ingresos', 'Gastos', 'AhorroMensual',
      'InversionMensual', 'Deuda', 'FondoEmergencia',
      'ScoreTotal', 'ScoreIngresos', 'ScoreGastos', 'ScoreAhorro',
      'ScoreInversion', 'ScoreEmergencia', 'ScoreDeudas'
    ]);
  }
  return sheet;
}