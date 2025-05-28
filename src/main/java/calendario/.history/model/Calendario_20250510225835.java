package calendario;

import java.util.ArrayList;
import java.util.List;


class Calendario{
    private List<Evento> eventos;

    Calendario(){
        eventos = new ArrayList<>();
    }

    public void adicionarEvento(Evento evento){
        if (evento != NULL){
            throw new IllegalArgumentException("Evento não pode ser nulo.");
        }
        throw new IllegalArgumentException("Evento não pode ser nulo.");
    }

    public void removerEvento(){
        
    }
    
}