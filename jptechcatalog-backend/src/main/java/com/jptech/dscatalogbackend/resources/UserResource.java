package com.jptech.dscatalogbackend.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jptech.dscatalogbackend.dto.UserDTO;
import com.jptech.dscatalogbackend.dto.UserInsertDTO;
import com.jptech.dscatalogbackend.dto.UserUpdateDTO;
import com.jptech.dscatalogbackend.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAlll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "firstName") String orderBy
			){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<UserDTO> listCateg = userService.findAllPaged(pageRequest);
		return ResponseEntity.ok().body(listCateg);
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable(value = "id") long id){
		
		UserDTO dto = userService.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto){
		
		UserDTO newDTO = userService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(newDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(newDTO);
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable(value = "id") Long id,@Valid @RequestBody UserUpdateDTO dto){
		
		UserDTO newDto = userService.update(id, dto);
		return ResponseEntity.ok().body(newDto);
	}
	
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id){ 
		
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
