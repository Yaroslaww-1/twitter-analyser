FROM python:3.10-slim AS builder

EXPOSE 5000

WORKDIR /app

COPY requirements.txt /app
RUN --mount=type=cache,target=/root/.cache/pip \
    pip3 install -r requirements.txt

COPY . /app

ENTRYPOINT ["python3"]
CMD ["app.py", "--host=0.0.0.0"]