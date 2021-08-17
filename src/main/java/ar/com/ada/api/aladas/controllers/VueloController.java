package ar.com.ada.api.aladas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.aladas.entities.Vuelo;
import ar.com.ada.api.aladas.entities.Reserva.EstadoReservaEnum;
import ar.com.ada.api.aladas.entities.Vuelo.EstadoVueloEnum;
import ar.com.ada.api.aladas.models.request.EstadoVueloRequest;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.services.VueloService;
import static ar.com.ada.api.aladas.services.VueloService.ValidacionVueloDataEnum;

import java.util.List;

@RestController
public class VueloController {

    @Autowired
    VueloService service;

    @PostMapping("/api/vuelos")
    public ResponseEntity<GenericResponse> postCrearVuelo(@RequestBody Vuelo vuelo) {

        GenericResponse respuesta = new GenericResponse();
        ValidacionVueloDataEnum resultadoValidacion = service.validar(vuelo);

        if (resultadoValidacion == ValidacionVueloDataEnum.OK) {
            service.crear(vuelo);

            respuesta.isOk = true;
            respuesta.id = vuelo.getVueloId();
            respuesta.message = "Vuelo creado correctamente";

            return ResponseEntity.ok(respuesta);
        } else {
            respuesta.isOk = false;
            respuesta.message = "Error(" + resultadoValidacion.toString() + ")";
            return ResponseEntity.badRequest().body(respuesta);

        }
    }

    @PutMapping("/api/vuelos/{id}/estados")
    public ResponseEntity<GenericResponse> putActualizarEstadoVuelo(@PathVariable Integer id,
            @RequestBody EstadoVueloRequest estado) {
        GenericResponse respuesta = new GenericResponse();
        Vuelo vuelo = service.buscarPorId(id);
        vuelo.setEstadoVueloId(estado.estado);
        service.actualizar(vuelo);
        respuesta.isOk = true; 
        respuesta.message = "actualizado";
        return ResponseEntity.ok(respuesta);

    }

    @GetMapping("api/vuelos/abiertos")
    public ResponseEntity<List<Vuelo>> getVuelosAbiertos(){

        return ResponseEntity.ok(service.traerVuelosAbiertos());
        
    }

}
