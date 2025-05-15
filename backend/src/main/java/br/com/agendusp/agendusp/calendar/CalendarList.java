package br.com.agendusp.agendusp.calendar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class CalendarList {
    ArrayList<Calendar> calendars;
    private String AcessToken;

    public void setAcessToken(String AcessToken) { 
        this.AcessToken = AcessToken;
    }

    public void Insert(Calendar calendar) throws Exception {
        // 1. Validações
        if (AcessToken == null || AcessToken.isEmpty()) {
            throw new IllegalStateException("Access token não configurado");
        }
        
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar não pode ser nulo");
        }
    
        // 2. Preparar o JSON
        String jsonInput = buildCalendarJson(calendar);
        
        // 3. Configurar a requisição HTTP
        URL url = new URL("https://www.googleapis.com/calendar/v3/calendars");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + AcessToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
    
        // 4. Envia os dados
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
    
        // 5. Verifica resposta 
        int statusCode = connection.getResponseCode();
        if (statusCode >= 400) {
            try (BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                
                String errorResponse = errorReader.lines().collect(Collectors.joining());
                throw new Exception("Falha na inserção: " + statusCode + " - " + errorResponse);
            }
        }
    }

    /* private String buildCalendarJson(Calendar calendar) {    método sem usar gson 
        return String.format(
            "{\"summary\":\"%s\",\"description\":\"%s\",\"timeZone\":\"%s\"}",
            escapeJson(calendar.getSummary()),
            escapeJson(calendar.getDescription()),
            escapeJson(calendar.getTimeZone())
        );
    }
*/




}

