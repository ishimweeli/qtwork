
                package com.devskiller.race;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;

        import org.json.JSONObject;
        import org.json.JSONArray;

        public class MarkingSystem {
            private final String apiKey;
            private final String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

            public MarkingSystem() {
                this.apiKey = "AIzaSyBbcSF2IOkAjxsVWzVGGNhb1Dq_flqlaG8";
                if (this.apiKey == null || this.apiKey.isEmpty()) {
                    throw new IllegalStateException("GOOGLE_API_KEY environment variable is not set");
                }
            }

            private String getAiResponse(String prompt) throws Exception {
                URL url = new URL(apiUrl + "?key=" + apiKey);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                String jsonInputString = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt.replace("\"", "\\\"") + "\"}]}]}";
                System.out.println("Request body: " + jsonInputString);

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = con.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(responseCode >= 400 ? con.getErrorStream() : con.getInputStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                if (responseCode >= 400) {
                    throw new RuntimeException("HTTP error code: " + responseCode + " Response: " + response.toString());
                }

                System.out.println("Response body: " + response.toString());
                return response.toString();
            }

            private JSONObject estimateMarks(String question, String answer, int totalMarks) throws Exception {
                String prompt = String.format(
                        "Question: %s\n" +
                                "Student's Answer: %s\n" +
                                "Total Marks Available: %d\n\n" +
                                "Please evaluate the student's answer and assign a mark out of %d. " +
                                "Provide a brief explanation for the marking decision. " +
                                "Return your response in the following JSON format:\n" +
                                "{\n" +
                                "    \"assigned_marks\": <number>,\n" +
                                "    \"explanation\": \"<explanation string>\"\n" +
                                "}",
                        question, answer, totalMarks, totalMarks
                );

                String response = getAiResponse(prompt);
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                if (candidates.length() > 0) {
                    JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                    JSONArray parts = content.getJSONArray("parts");
                    if (parts.length() > 0) {
                        String aiResponseText = parts.getJSONObject(0).getString("text");
                        // Extract JSON from the response text
                        int startIndex = aiResponseText.indexOf('{');
                        int endIndex = aiResponseText.lastIndexOf('}') + 1;
                        if (startIndex != -1 && endIndex != -1) {
                            String jsonString = aiResponseText.substring(startIndex, endIndex);
                            return new JSONObject(jsonString);
                        }
                    }
                }
                throw new RuntimeException("Unable to parse AI response");
            }

            public void markExam(List<ExamQuestion> questions) {
                int totalScore = 0;
                int totalAvailableMarks = 0;

                for (ExamQuestion q : questions) {
                    try {
                        JSONObject result = estimateMarks(q.getQuestion(), q.getAnswer(), q.getMarks());
                        int marks = result.getInt("assigned_marks");
                        String explanation = result.getString("explanation");

                        totalScore += marks;
                        totalAvailableMarks += q.getMarks();

                        System.out.println("Question: " + q.getQuestion());
                        System.out.println("Answer: " + q.getAnswer());
                        System.out.println("Marks: " + marks + "/" + q.getMarks());
                        System.out.println("Explanation: " + explanation + "\n");
                    } catch (Exception e) {
                        System.out.println("Error processing question: " + q.getQuestion());
                        e.printStackTrace();
                    }
                }

                System.out.println("Total Score: " + totalScore + "/" + totalAvailableMarks);
            }

            public static void main(String[] args) {
                MarkingSystem marker = new MarkingSystem();

                List<ExamQuestion> questions = new ArrayList<>();
                questions.add(new ExamQuestion("What is the capital of France?", "London", 2));
                questions.add(new ExamQuestion("Explain the process of photosynthesis.", "Photosynthesis is when plants make food using sunlight.", 4));
                questions.add(new ExamQuestion("Solve for x: 2x + 5 = 13", "x = 4", 3));

                marker.markExam(questions);
            }
        }

        class ExamQuestion {
            private String question;
            private String answer;
            private int marks;

            public ExamQuestion(String question, String answer, int marks) {
                this.question = question;
                this.answer = answer;
                this.marks = marks;
            }

            public String getQuestion() { return question; }
            public String getAnswer() { return answer; }
            public int getMarks() { return marks; }
        }