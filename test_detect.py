import urllib.request, urllib.error, json
req = urllib.request.Request('http://localhost:8082/api/detection/detect', data=json.dumps({'text': 'my name is vishnu'}).encode(), headers={'Content-Type': 'application/json'})
try:
    res = urllib.request.urlopen(req)
    print(res.read())
except urllib.error.HTTPError as e:
    print('ERROR BODY:', e.read())
