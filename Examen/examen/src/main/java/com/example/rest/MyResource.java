package com.example.rest;

import com.example.controls.service.ExpresionService;
import com.example.models.Expresion;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("expresiones")
public class MyResource {

    private ExpresionService expresionService = new ExpresionService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertirExpresion(Expresion expresion) {
            // Convertir la expresión infija a postfija
            String infija = expresion.getExpresion1();
            String postfija = expresionService.convertirInfijoAPostfijo(expresion).getExpresion2();

            // Crear la expresión de resultado
            Expresion resultado = new Expresion(expresion.getId(), infija, postfija);
            
            // Responder con el mensaje de éxito
            return Response.status(Response.Status.CREATED)  // Código 201 para "creado"
                           .entity("Expresión registrada correctamente: " + resultado.getExpresion1() + " => " + resultado.getExpresion2())
                           .build();
        
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerExpresiones() {
        List<Expresion> expresiones = expresionService.obtenerTodasLasExpresiones();
        return Response.ok(expresiones).build();
    }

    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Expresiones API funcionando!";
    }
}
