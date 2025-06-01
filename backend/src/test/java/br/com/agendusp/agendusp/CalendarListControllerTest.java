package br.com.agendusp.agendusp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import br.com.agendusp.agendusp.CustomOAuth2User;
import br.com.agendusp.agendusp.documents.CalendarListResource;

@SpringBootTest
public class CalendarListControllerTest {
    public void TestDelete(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser){

    }

    // public String get(String calendarId, @AuthenticationPrincipal CustomOAuth2User customUser);

    // public String insert(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    // public String list(@AuthenticationPrincipal CustomOAuth2User customUser);

    // public String patch(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

    // public String update(CalendarListResource calendar, @AuthenticationPrincipal CustomOAuth2User customUser);

}
