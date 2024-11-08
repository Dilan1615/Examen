package com.example.controls.service;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import com.example.models.Expresion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.ArrayList;

public class ExpresionService {
    private static final String CARPETA_EXPRESIONES = "data";  
    private static final String ARCHIVO_EXPRESIONES = CARPETA_EXPRESIONES + "/expresiones.json"; 
    private ObjectMapper objectMapper = new ObjectMapper();

    public Expresion convertirInfijoAPostfijo(Expresion expresion) {
        String infija = expresion.getExpresion1();
        Stack<Character> pila = new Stack<>();
        String resultado = "";

        for (int i = 0; i < infija.length(); i++) {
            char c = infija.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                resultado += c;
            } else if (c == '(') {
                pila.push(c);
            } else if (c == ')') {
                while (!pila.isEmpty() && pila.peek() != '(') {
                    resultado += pila.pop();
                }
                pila.pop();
            } else {
                while (!pila.isEmpty() && obtenerPrecedencia(c) <= obtenerPrecedencia(pila.peek())) {
                    resultado += pila.pop();
                }
                pila.push(c);
            }
        }

        while (!pila.isEmpty()) {
            resultado += pila.pop();
        }

        expresion.setExpresion2(resultado);
        guardarExpresion(expresion);
        return expresion;
    }

    private int obtenerPrecedencia(char operador) {
        switch (operador) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            case '^': return 3;
            default: return 0;
        }
    }

   private void guardarExpresion(Expresion expresion) {
    List<Expresion> expresiones = obtenerTodasLasExpresiones();
    
    expresion.setId(expresiones.size() + 1);  // aumento el id solo
    expresiones.add(expresion);     
    try {
        objectMapper.writeValue(new File(ARCHIVO_EXPRESIONES), expresiones);
    } catch (IOException e) {
        System.err.println("Error al guardar las expresiones: " + e.getMessage());
    }
}

public List<Expresion> obtenerTodasLasExpresiones() {
    File archivo = new File(ARCHIVO_EXPRESIONES);
    
    if (!archivo.exists()) {
        return new ArrayList<>(); 
    }
    try {
        return objectMapper.readValue(archivo, new TypeReference<List<Expresion>>() {});
    } catch (IOException e) {
        System.err.println("Error al leer las expresiones: " + e.getMessage());
        return new ArrayList<>();
    }
}
}
