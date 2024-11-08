import logging
from flask import Blueprint, request, render_template
import requests

router = Blueprint('router', __name__)

API_URL = 'http://localhost:8080/myapp/expresiones'

@router.route('/')
def home():
    try:
        response = requests.get(API_URL)
        response.raise_for_status()
        historial = response.json()
    except requests.exceptions.RequestException as e:
        logging.error(f"Error al conectar con la API Java: {e}")
        historial = []

    return render_template('index.html', historial=historial)

@router.route('/convertir', methods=['POST'])
def convertir_expresion():
    try:
        expresion1 = request.form.get('expresion1')
        nueva_expresion = {
            "id": 2,
            "expresion1": expresion1,
            "expresion2": ""
        }

        response = requests.post(API_URL, json=nueva_expresion)
        response.raise_for_status()
        resultado = response.json()

        historial_response = requests.get(API_URL)
        historial_response.raise_for_status()
        historial = historial_response.json()

        return render_template('index.html', resultado=resultado, historial=historial)

    except requests.exceptions.RequestException as e:
        logging.error(f"Error al conectar con la API Java: {e}")
        return render_template('index.html', historial=[])
