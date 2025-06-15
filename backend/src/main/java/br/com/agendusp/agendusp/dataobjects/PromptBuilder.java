package br.com.agendusp.agendusp.dataobjects;
import java.util.List;

public class PromptBuilder {
    public static String getPromptParaInformeSemana(String personName, List<String> compromissos, List<String> enquetes, List<String> eventosCancelados, String diaInicial) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Você é uma inteligência artificial que gera relatórios de compromissos semanais.\n");
        prompt.append("Sua tarefa é criar um informe formal e claro com base nos compromissos da semana.\n");
        prompt.append("Considere o seguinte contexto:\n\n");

        prompt.append("- Nome da pessoa: ").append(personName).append("\n");
        prompt.append("- Contexto: A pessoa deseja receber um resumo dos compromissos agendados para a semana do dia " + diaInicial + ".\n");
        prompt.append("\nCompromissos:\n");
        if (compromissos == null || compromissos.isEmpty()) {
            prompt.append("Não há compromissos!\n");
        }
        else {
            for (String compromisso : compromissos) {
                prompt.append("- ").append(compromisso).append("\n");
            }
        }
        prompt.append("\nResultado de enquetes:\n");
        if (enquetes == null || enquetes.isEmpty()) {
            prompt.append("Não há enquetes!\n");
        }
        else {
            for (String enquete : enquetes) {
                prompt.append("- ").append(enquete).append("\n");
            }
        }
        prompt.append("\nEventos cancelados:\n");
        if (eventosCancelados == null || eventosCancelados.isEmpty()) {
            prompt.append("Não há enquetes!\n");
        }
        else {
            for (String evento : eventosCancelados) {
                prompt.append("- ").append(evento).append("\n");
            }
        }
        prompt.append("- Linguagem: Seja objetivo, use um tom profissional e evite repetições desnecessárias.\n");

        prompt.append("Por favor, enfatize informações relevantes como sobreposição de horários, eventos recentemente cancelados e " +
        "enquetes recentes de horário para reunião.\n");


        return prompt.toString();
    }

    public static String getPromptParaInformeDia(String personName, List<String> compromissos, List<String> enquetes, List<String> eventosCancelados, String dia) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Você é uma inteligência artificial que gera relatórios de compromissos diários.\n");
        prompt.append("Sua tarefa é criar um informe formal e claro com base nos compromissos do dia " + dia + ".\n");
        prompt.append("Considere o seguinte contexto:\n\n");

        prompt.append("- Nome da pessoa: ").append(personName).append("\n");
        prompt.append("- Contexto: A pessoa deseja receber um resumo das informações e dos compromissos agendados para o dia " + dia + ".\n");
        prompt.append("\nCompromissos:\n");
        if (compromissos == null || compromissos.isEmpty()) {
            prompt.append("Não há compromissos!\n");
        }
        else {
            for (String compromisso : compromissos) {
                prompt.append("- ").append(compromisso).append("\n");
            }
        }
        prompt.append("\nResultado de enquetes:\n");
        if (enquetes == null || enquetes.isEmpty()) {
            prompt.append("Não há enquetes!\n");
        }
        else {
            for (String enquete : enquetes) {
                prompt.append("- ").append(enquete).append("\n");
            }
        }
        prompt.append("\nEventos cancelados:\n");
        if (eventosCancelados == null || eventosCancelados.isEmpty()) {
            prompt.append("Não há enquetes!\n");
        }
        else {
            for (String evento : eventosCancelados) {
                prompt.append("- ").append(evento).append("\n");
            }
        }

        prompt.append("\n- Linguagem: Seja objetivo, use um tom profissional e evite repetições desnecessárias.\n");
        
        prompt.append("Por favor, enfatize informações relevantes como sobreposição de horários, eventos recentemente cancelados e " +
        "enquetes recentes de horário para reunião.\n");

        return prompt.toString();
    }
}
