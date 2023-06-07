from flask import Flask, request, jsonify
from lingua import Language, LanguageDetectorBuilder

app = Flask(__name__)
language_detector = LanguageDetectorBuilder.from_languages(*Language.all()).build()


@app.route('/language')
def detect_language():
    text = request.args.get('text')
    langauge = language_detector.detect_language_of(text)
    return jsonify({
        'langauge': langauge.__str__().split(".")[1]
    })


if __name__ == '__main__':
    app.run()
