package com.bizgenai.guardrail.rules;

import com.bizgenai.entity.Blueprint;
import com.bizgenai.entity.Category;

public interface GuardrailRule {

    boolean appliesTo(Category category, Blueprint blueprint);

    RuleResult apply(String content, Blueprint blueprint);

    int getOrder();
}
