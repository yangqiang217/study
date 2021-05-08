#!flask/bin/python
from app import app
import socket

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.connect(("8.8.8.8", 80))
print(s.getsockname()[0])
s.close()

app.config['JSON_AS_ASCII'] = False
app.run(host="0.0.0.0", debug = True, port=9999)