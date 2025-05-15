package br.com.agendusp.agendusp.calendar;

import com.google.api.services.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;

@Controller
public class GoogleEventsController implements EventsController {

    private final Calendar calendarService;

    @Autowired
    public GoogleEventsController(GoogleAuthorization googleAuth) {
        // pega o Calendar autenticado
        this.calendarService = googleAuth.getCalendarService();
    }

    /**
     * Exclui um evento da agenda Google Calendar.
     *
     * @param calendarId  ID da agenda (ex.: "primary")
     * @param eventId     ID do evento a ser deletado
     * @param sendUpdates controla envio de e-mail aos participantes:
     *                    "all", "externalOnly", "none" ou "organizer"
     */
    
    @Override
    public HttpStatusCode delete(String calendarId, String eventId, String sendUpdates) {
        try {
            // monta a chamada DELETE
            CCalendar.Events.Delete deleteReq =
            calendarService.events()
                           .delete(calendarId, eventId)
                           // se não vier sendUpdates, manda "all" para notificar todo mundo
                           .setSendUpdates(sendUpdates == null || sendUpdates.isBlank() ? "all" : sendUpdates);
            // executa a remoção
            deleteReq.execute();

            // 204 No Content
            return HttpStatusCode.valueOf(204);
        } catch (Exception e) {
            e.printStackTrace();
            // 500 Internal Server Error em caso de falha
            return HttpStatusCode.valueOf(500);
        }
    }


    @Override
    public AbstractEvent get(String calendarId, String eventId, String timeZone, String alwaysIncludeEmail, String maxAttendees) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public AbstractEvent importEvent(String calendarId, AbstractEvent event) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public AbstractEvent insert(String calendarId, AbstractEvent event, String sendUpdates, String maxAttendees) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public CalendarEvents instances(String calendarId, String eventId) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public CalendarEvents list(String calendarId) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public AbstractEvent move(String calendarId, String eventId, String destination) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public AbstractEvent patch(String calendarId, String eventId, AbstractEvent event) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public AbstractEvent quickAdd(String calendarId, String text) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public AbstractEvent update(String calendarId, String eventId, AbstractEvent event) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }

    @Override
    public WatchResponse watch(String calendarId, WatchRequest watchRequest) {
        throw new UnsupportedOperationException("Não implementado ainda");
    }
}