package com.bizgenai.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MockAiService implements AiService {

    private static final Logger log = LoggerFactory.getLogger(MockAiService.class);
    private final Random random = new Random();

    @Override
    public AiResponse generate(AiRequest request) {
        log.info("MockAiService generating content with {} variations", request.getVariationCount());

        // Simulate processing delay
        try {
            Thread.sleep(1000 + random.nextInt(1500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return AiResponse.error("Generation interrupted");
        }

        long startTime = System.currentTimeMillis();
        List<String> variations = new ArrayList<>();

        int variationCount = request.getVariationCount() != null ? request.getVariationCount() : 3;

        for (int i = 1; i <= variationCount; i++) {
            variations.add(generateMockVariation(i, request.getUserPrompt()));
        }

        long processingTime = System.currentTimeMillis() - startTime;

        return AiResponse.builder()
                .variations(variations)
                .totalTokens(calculateMockTokens(variations))
                .processingTimeMs(processingTime)
                .success(true)
                .build();
    }

    private String generateMockVariation(int variationNumber, String prompt) {
        String[] openings = {
                "✨ Ready to transform your business?",
                "🚀 Exciting news for you!",
                "💡 Here's something special:",
                "🌟 Don't miss out on this!",
                "🎯 Perfect timing for your business:"
        };

        String[] bodies = {
                "Our latest offering is designed with your success in mind. We've carefully crafted every detail to ensure you get the best possible results.",
                "This is your opportunity to take things to the next level. Our team has worked tirelessly to bring you something truly exceptional.",
                "We understand your needs and have created the perfect solution. Quality meets innovation in everything we do.",
                "Experience the difference that comes from true dedication to excellence. Your satisfaction is our top priority.",
                "Join thousands of satisfied customers who have already discovered the difference. It's time for your success story."
        };

        String[] closings = {
                "👉 Click the link in bio to learn more!",
                "💬 DM us for exclusive offers!",
                "🔗 Shop now - link in bio!",
                "⏰ Limited time offer - don't wait!",
                "📩 Contact us today to get started!"
        };

        int index = (variationNumber - 1) % 5;

        return String.format(
                "--- Variation %d ---\n\n%s\n\n%s\n\n%s\n\n" +
                        "[COMPANY_NAME] | [WEBSITE]\n\n" +
                        "#YourBrand #SmallBusiness #Success #Entrepreneur #Growth",
                variationNumber,
                openings[index],
                bodies[index],
                closings[index]);
    }

    private Integer calculateMockTokens(List<String> variations) {
        return variations.stream()
                .mapToInt(v -> v.split("\\s+").length)
                .sum();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
