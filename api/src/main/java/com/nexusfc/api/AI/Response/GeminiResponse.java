package com.nexusfc.api.AI.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeminiResponse {
    private List<Candidate> candidates;

    private UsageMetadata usageMetadata;

    private String modelVersion;

    @Data
    public static class Candidate {
        private Content content;

        private String finishReason;

        double avgLogprobs;
    }

    @Data
    public static class Content {
        private List<Part> parts;

        private String role;
    }

    @Data
    public static class Part {
        private String text;
    }

    @Data
    public static class UsageMetadata {
        private int promptTokenCount;

        private int candidatesTokenCount;

        private int totalTokenCount;

        private List<TokenDetails> promptTokensDetails;

        private List<TokenDetails> candidatesTokensDetails;
    }

    @Data
    public static class TokenDetails {
        private String modality;

        private int tokenCount;
    }
}
