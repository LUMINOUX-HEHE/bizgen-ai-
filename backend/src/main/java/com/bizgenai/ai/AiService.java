package com.bizgenai.ai;

public interface AiService {

    AiResponse generate(AiRequest request);

    boolean isAvailable();
}
