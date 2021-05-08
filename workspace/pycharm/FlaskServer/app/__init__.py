import time

from flask import Flask

app = Flask(__name__)
app.secret_key = str(time.time())
from app import views