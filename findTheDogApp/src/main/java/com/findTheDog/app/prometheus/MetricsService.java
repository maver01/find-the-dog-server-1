package com.findTheDog.app.prometheus;

import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MetricsService {

    private final Counter imagesSentToProducerCounter;
    private final Counter imagesProcessedCounter;
    private final Counter errorProducerCounter;
    private final Timer processingTimeTimer;
    private final AtomicInteger imagesInProcessGauge;

    public MetricsService(MeterRegistry meterRegistry) {
        imagesSentToProducerCounter = Counter.builder("images_sent_to_producer_total")
                .description("Images sent to producer")
                .register(meterRegistry);

        imagesProcessedCounter = Counter.builder("images_processed_total")
                .description("Images processed")
                .register(meterRegistry);

        errorProducerCounter = Counter.builder("producer_errors_total")
                .description("Total errors in producer")
                .register(meterRegistry);

        processingTimeTimer = Timer.builder("processing_time")
                .description("Time taken to process images")
                .register(meterRegistry);

        imagesInProcessGauge = new AtomicInteger(0);
        Gauge.builder("images_in_process", imagesInProcessGauge, AtomicInteger::get)
                .description("Number of images in process before sending to producer")
                .register(meterRegistry);
    }

    public void incrementImagesSentToProducer() {
        imagesSentToProducerCounter.increment();
        imagesInProcessGauge.decrementAndGet();
    }

    public void incrementImagesProcessed() {
        imagesProcessedCounter.increment();
    }

    public void incrementErrorProducerCounter() {
        errorProducerCounter.increment();
    }

    public void recordProcessingTime(long durationInMillis) {
        processingTimeTimer.record(durationInMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public void incrementImagesInProcess() {
        imagesInProcessGauge.incrementAndGet();
    }

    public void decrementImagesInProcess() {
        imagesInProcessGauge.decrementAndGet();
    }
}