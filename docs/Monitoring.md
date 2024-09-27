# Monitoring

Disclaimer: created with AI.

## Monitoring

Prometheus is widely used for collecting and storing time-series data from servers and microservices. It supports metrics from Spring Boot, Kafka, and Python services with minimal configuration.
Grafana can be used with Prometheus to create dashboards and visualize metrics across the entire stack. It allows monitoring frontend metrics (like response times, API request counts) with browser-based tools like Web Vitals.

## Distributed tracing

Distributed tracing is a technique used to track and visualize the flow of requests as they travel through the various services and components in a distributed system, such as microservices architecture. It helps to monitor and troubleshoot performance bottlenecks, latency issues, or errors that occur as a request moves from one service to another.

How It Works:
Tracing a Request: When a request (like an API call) enters the system, it passes through multiple services, databases, message queues (like Kafka), etc. Each service logs its involvement in handling the request, including how long it took, whether there were any errors, and what the next service in the chain is.

Trace ID & Span: Each request gets a unique trace ID, and every operation along the way is logged as a span. These spans include details like:

Start time
End time
Duration of the operation
Any errors or failures
All spans from a single request share the same trace ID, allowing you to follow the request through each service.

End-to-End Visibility: Distributed tracing tools collect these spans from multiple services, combine them using the trace ID, and display a visual representation of the entire request flow. This helps in identifying where performance issues, failures, or bottlenecks occur.

Benefits of Distributed Tracing:

- Performance Optimization: Helps pinpoint bottlenecks in your services or microservices.
- Error Detection: Quickly identify where errors or failures are occurring in the system.
- Debugging Complexity: Provides insights into the dependencies between services and how they communicate.
- Improved Monitoring: When combined with monitoring tools (e.g., Prometheus), distributed tracing helps get a complete picture of your system's health and performance.

OpenTelemetry is a powerful observability framework for collecting logs, traces, and metrics across distributed systems. It can be integrated into your Java Spring Boot, Python, and even frontend (via JavaScript libraries) to gather detailed performance data and trace requests across the system.

## Prometheus

[docs](https://prometheus.io/docs/prometheus/latest/getting_started/)

## Grafana

[docs](https://grafana.com/docs/grafana/latest/setup-grafana/installation/debian/)

## Open Telemetry

## Jaeger
