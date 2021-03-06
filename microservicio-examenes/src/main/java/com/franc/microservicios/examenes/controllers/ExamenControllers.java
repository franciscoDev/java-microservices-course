package com.franc.microservicios.examenes.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.franc.microservicios.commons.controllers.CommonController;
import com.franc.microservicios.commons.examenes.models.entity.Examen;
import com.franc.microservicios.commons.examenes.models.entity.Pregunta;
import com.franc.microservicios.examenes.services.ExamenService;

@RestController
public class ExamenControllers extends CommonController<Examen, ExamenService> {

	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Examen examen
									,BindingResult result
									,@PathVariable Long id
									){
		
		if( result.hasErrors())return this.validar(result);
		
		Optional<Examen> optional = service.findById(id);
		if(! optional.isPresent()) return ResponseEntity.notFound().build();
		
		Examen examenDB = optional.get();
		examenDB.setNombre(examen.getNombre());
		
		List<Pregunta> eliminadas = new ArrayList<>();
		examenDB.getPreguntas().forEach(pregunta->{
			if( examen.getPreguntas().contains(pregunta)) {
				eliminadas.add(pregunta);
			}
		});
		
		eliminadas.forEach(pregunta->{
			examenDB.removePregunta(pregunta);
		});
		
		examenDB.setPreguntas(examen.getPreguntas());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(examenDB));
	}
	
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		
		return ResponseEntity.ok(service.findByNombre(term));
	}
	
	@GetMapping("/asignaturas}")
	public ResponseEntity<?> getAsignaturas(@PathVariable String term){
		
		return ResponseEntity.ok(service.findAllAsignaturas());
	}
	
}
