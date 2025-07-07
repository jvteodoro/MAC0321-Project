package br.com.agendusp.agendusp.dataobjects.calendarObjects;

// armazena os lembretes, que podem ser enviados por email ou em forma de pop-up
public class CalendarReminder { 
    private String method;
    private int minutes;

    public CalendarReminder(String method, int minutes) {
        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("Método não pode ser nulo ou vazio");
        }
        if (!method.equals("email") && !method.equals("popup")) {
            throw new IllegalArgumentException("Método inválido: " + method);
        }
        if (minutes < 0) {
            throw new IllegalArgumentException("Minutos não pode ser negativo");
        } else if (minutes > 40320) {
            throw new IllegalArgumentException("Minutos não pode ser maior que 40320");
        }
        this.method = method;
        this.minutes = minutes;
    }

    public String getMethod() {
        return method;
    }

    public int getMinutes() {
        return minutes;
    }
}
