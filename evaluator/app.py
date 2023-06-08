import json

import fasttext
from flask import Flask, request, jsonify
from concurrent.futures import ProcessPoolExecutor

from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

app = Flask(__name__)
language_detector = fasttext.load_model('lid.176.ftz')
sentiment_detector = SentimentIntensityAnalyzer()


def detect_language(id, text):
    result = language_detector.predict(text, k=1)
    language = result[0][0].split('__')[-1]
    return {'id': id, 'result': language}


def detect_sentiment(id, text):
    result = sentiment_detector.polarity_scores(text)
    compound = result['compound']
    sentiment = 'Neutral'
    if compound >= 0.05:
        sentiment = 'Positive'
    if compound <= -0.05:
        sentiment = 'Negative'
    return {'id': id, 'result': sentiment}


def execute_in_parallel(tweets, function):
    tasks = []
    results = {}

    with ProcessPoolExecutor() as executor:
        for tweet_id in tweets:
            future = executor.submit(function, tweet_id, tweets[tweet_id])
            tasks.append(future)

        for running_task in tasks:
            result = running_task.result()
            results[result['id']] = result['result']

    return results


@app.route('/language', methods=['POST'])
def language():
    print("Language detection request received")

    results = execute_in_parallel(
        json.loads(request.data),
        detect_language
    )

    return jsonify(results)


@app.route('/sentiment', methods=['POST'])
def sentiment():
    print("Sentiment detection request received")

    results = execute_in_parallel(
        json.loads(request.data),
        detect_sentiment
    )

    return jsonify(results)


if __name__ == '__main__':
    app.run()
