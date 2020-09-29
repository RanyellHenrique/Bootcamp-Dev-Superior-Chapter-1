package com.bootcamp.BootcampChapter1.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bootcamp.BootcampChapter1.dto.ClientDTO;
import com.bootcamp.BootcampChapter1.entities.Client;
import com.bootcamp.BootcampChapter1.repositories.ClientRepository;
import com.bootcamp.BootcampChapter1.services.exceptions.DataBaseException;
import com.bootcamp.BootcampChapter1.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found " + id));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO objDto) {
		Client entity = new Client();
		copyDtoToEntity(objDto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO objDto) {
		try {
			Client entity = repository.getOne(id);
			copyDtoToEntity(objDto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Entity not found");
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Entity not found " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
		
	}
	
	private void copyDtoToEntity(ClientDTO objDto, Client entity) {
		entity.setBirthDate(objDto.getBirthDate());
		entity.setChildren(objDto.getChildren());
		entity.setIncome(objDto.getIncome());
		entity.setName(objDto.getName());
		entity.setCpf(objDto.getCpf());
		
	}

}
