import json

import fasttext
from flask import Flask, request, jsonify
from concurrent.futures import ProcessPoolExecutor

app = Flask(__name__)
model = fasttext.load_model('lid.176.ftz')


def detect_language(id, text):
    # language = language_detector.detect_language_of(text).__str__().split(".")[1]
    result = model.predict(text, k=1)
    language = result[0][0].split('__')[-1]
    return { 'id': id, 'language': language }


@app.route('/language', methods=['POST'])
def language():
    tweets = json.loads(request.data)
    print("Language detection request received")

    tasks = []
    results = {}

    with ProcessPoolExecutor() as executor:
        for tweet_id in tweets:
            future = executor.submit(detect_language, tweet_id, tweets[tweet_id])
            tasks.append(future)

        for running_task in tasks:
            result = running_task.result()
            results[result['id']] = result['language']

    return jsonify(results)


if __name__ == '__main__':
    app.run()
