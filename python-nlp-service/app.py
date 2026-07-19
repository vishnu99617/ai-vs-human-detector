from flask import Flask, request, jsonify
from transformers import GPT2Tokenizer, GPT2LMHeadModel
import torch
import nltk
from nltk.probability import FreqDist
from collections import Counter
from nltk.corpus import stopwords
import string

nltk.download('punkt')
nltk.download('punkt_tab')
nltk.download('stopwords')

# Load GPT-2 tokenizer and model
tokenizer = GPT2Tokenizer.from_pretrained('gpt2')
model = GPT2LMHeadModel.from_pretrained('gpt2')

app = Flask(__name__)

def calculate_perplexity(text):
    encoded_input = tokenizer.encode(text, add_special_tokens=False, return_tensors='pt')
    if encoded_input.numel() == 0:
        return 0
    input_ids = encoded_input[0].unsqueeze(0)

    # Truncate input to avoid model crashes on text longer than 1024 tokens
    if input_ids.size(1) > 1024:
        input_ids = input_ids[:, -1024:]

    if input_ids.size(1) <= 1:
        return 0

    with torch.no_grad():
        outputs = model(input_ids, labels=input_ids)
        loss = outputs.loss

    perplexity = torch.exp(loss)
    return perplexity.item()

def calculate_burstiness(text):
    tokens = nltk.word_tokenize(text.lower())
    if not tokens:
        return 0
    word_freq = FreqDist(tokens)
    repeated_count = sum(count > 1 for count in word_freq.values())
    burstiness_score = repeated_count / len(word_freq)
    return burstiness_score

def get_top_repeated_words(text):
    tokens = text.split()
    stop_words = set(stopwords.words('english'))
    tokens = [token.lower() for token in tokens if token.lower() not in stop_words and token.lower() not in string.punctuation]

    word_counts = Counter(tokens)
    top_words = word_counts.most_common(10)
    
    words = [word for word, count in top_words]
    counts = [count for word, count in top_words]
    
    return {"words": words, "counts": counts}

@app.route('/detect', methods=['POST'])
def detect():
    data = request.get_json()
    text = data.get('text', '')
    if not text:
        return jsonify({"error": "No text provided"}), 400
        
    perplexity = calculate_perplexity(text)
    burstiness_score = calculate_burstiness(text)
    top_words = get_top_repeated_words(text)
    
    if perplexity <= 25 and burstiness_score <= 0.20:
        result = "Likely AI Generated Content"
        status = "error"
    elif perplexity > 25 and burstiness_score > 0.20:
        result = "Likely Human Written Content"
        status = "success"
    elif perplexity > 25 and burstiness_score <= 0.20:
        result = "Possibly AI Generated Content"
        status = "warning"
    else:  
        result = "Likely Mixed AI and Human Content"
        status = "info"
        
    return jsonify({
        "perplexity": perplexity,
        "burstiness_score": burstiness_score,
        "result": result,
        "status": status,
        "top_words": top_words
    })

if __name__ == '__main__':
    app.run(port=5001, debug=True)
