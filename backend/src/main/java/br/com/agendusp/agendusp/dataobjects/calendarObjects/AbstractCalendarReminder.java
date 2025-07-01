package br.com.agendusp.agendusp.dataobjects.calendarObjects;

public class AbstractCalendarReminder {
    String method;
    int minutes;

    public AbstractCalendarReminder(String method, int minutes) {
        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("Método não pode ser nulo ou vazio");
        }
        // if (minutes == null) {
        // throw new IllegalArgumentException("Minutos não pode ser nulo");
        // }
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
}
