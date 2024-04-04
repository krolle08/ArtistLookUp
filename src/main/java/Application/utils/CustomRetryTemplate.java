package utils;

import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 *
 */
public class CustomRetryTemplate extends RetryTemplate {
    @Bean
    public CustomRetryTemplate retryTemplate() {

        // Configure retry policy
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3); // Number of retry attempts

        // Configure backoff policy
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(1000); // Initial interval in milliseconds
        backOffPolicy.setMultiplier(2); // Multiplier for exponential increase
        backOffPolicy.setMaxInterval(60000); // Maximum interval in milliseconds

        CustomRetryTemplate customRetryTemplate = new CustomRetryTemplate();
        customRetryTemplate.setRetryPolicy(retryPolicy);
        customRetryTemplate.setBackOffPolicy(backOffPolicy);

        return customRetryTemplate;
    }
}


