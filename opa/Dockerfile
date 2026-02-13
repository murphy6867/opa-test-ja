# Override at build-time, e.g.: docker build --build-arg OPA_VERSION=0.xx.x .
ARG OPA_VERSION=latest
FROM openpolicyagent/opa:${OPA_VERSION}

WORKDIR /app

COPY policies /app/policies
COPY data /app/data

EXPOSE 8181

CMD ["run", "--server", "--addr=0.0.0.0:8181", "--watch", "/app/policies", "/app/data"]