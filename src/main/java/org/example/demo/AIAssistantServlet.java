package org.example.demo;

import dev.langchain4j.model.ollama.OllamaChatModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;

@WebServlet("/ai-assistant")
public class AIAssistantServlet extends HttpServlet {

    private OllamaChatModel model;

    @Override
    public void init() throws ServletException {
        // Initialize the local Ollama model
        // Assuming Ollama is running on the default port 11434
        // and 'llama3' model is pulled
        model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .timeout(Duration.ofSeconds(60))
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String userMessage = request.getParameter("message");
        PrintWriter out = response.getWriter();

        if (userMessage == null || userMessage.trim().isEmpty()) {
            out.print("{\"error\": \"Empty message\"}");
            return;
        }

        try {
            // System prompt to set the persona
            String systemPrompt = "You are a helpful and empathetic Mental Wellness Assistant. " +
                    "Your goal is to provide supportive, non-clinical advice on stress management, mindfulness, and healthy habits. "
                    +
                    "Keep your responses concise and encouraging. If someone is in a crisis, advise them to seek professional help.";

            String responseText = model.generate(systemPrompt + "\n\nUser: " + userMessage);

            // Basic JSON escaping for the response
            String escapedResponse = responseText.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
            out.print("{\"reply\": \"" + escapedResponse + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\": \"Failed to connect to local AI. Ensure Ollama is running with llama3 model.\"}");
        }
    }
}
