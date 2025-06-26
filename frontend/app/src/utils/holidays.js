export async function fetchNationalHolidays(year) {
  const response = await fetch(`https://brasilapi.com.br/api/feriados/v1/${year}`);
  if (!response.ok) throw new Error("Erro ao buscar feriados");
  return response.json(); // [{date: "YYYY-MM-DD", name: "Nome"}, ...]
}