package com.example.leonardooliveira.boaviagem.calendar;

import com.example.leonardooliveira.boaviagem.model.Constantes;
import com.example.leonardooliveira.boaviagem.model.Viagem;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Leonardo Oliveira on 27/07/2016.
 */
public class CalendarService {
    private Calendar calendar;
    private String nomeConta;

    public CalendarService(String nomeConta, String tokenAcesso) {
        this.nomeConta = nomeConta;
        GoogleCredential credential = new GoogleCredential();
        credential.setAccessToken(tokenAcesso);

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = new GsonFactory();

        calendar = new Calendar.Builder(transport, jsonFactory, credential)
                .setHttpRequestInitializer(credential)
                .setApplicationName(Constantes.APP_NAME)
                .build();
    }

    public String criarEvento(Viagem viagem) {
        Event event = new Event();
        event.setSummary(viagem.getDestino());

        List<EventAttendee> participantes = Arrays.asList((new EventAttendee().setEmail(nomeConta)));
        event.setAttendees(participantes);

        DateTime inicio = new DateTime(viagem.getDataChegada(), TimeZone.getDefault());
        DateTime fim = new DateTime(viagem.getDataPartida(), TimeZone.getDefault());

        event.setStart(new EventDateTime().setDateTime(inicio));
        event.setStart(new EventDateTime().setDateTime(fim));

        try {
            Event eventoCriado = calendar.events().insert(nomeConta, event).execute();
            return eventoCriado.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
