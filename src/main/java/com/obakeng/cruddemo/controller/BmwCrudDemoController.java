package com.obakeng.cruddemo.controller;

import com.obakeng.cruddemo.exception.ResourceNotFoundException;
import com.obakeng.cruddemo.model.BmwCrudDemo;
import com.obakeng.cruddemo.repository.BmwCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class BmwCrudDemoController {

	@Autowired
	BmwCrudRepository bmwCrudRepository;


	private Sort.Direction getSortDirection(String direction) {
		if (direction.equals("asc")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("desc")) {
			return Sort.Direction.DESC;
		}

		return Sort.Direction.ASC;
	}

	@GetMapping("/sortedcars")
	public ResponseEntity<List<BmwCrudDemo>> getAllCars(@RequestParam(defaultValue = "id,desc") String[] sort) {

		try {
			List<Sort.Order> orders = new ArrayList<Sort.Order>();

			if (sort[0].contains(",")) {
				// will sort more than 2 fields
				// sortOrder="field, direction"
				for (String sortOrder : sort) {
					String[] _sort = sortOrder.split(",");
					orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
				}
			} else {
				// sort=[field, direction]
				orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
			}

			List<BmwCrudDemo> cars = bmwCrudRepository.findAll(Sort.by(orders));

			if (cars.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(cars, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/cars")
	public ResponseEntity<Map<String, Object>> getAllCarsPage(
			@RequestParam(required = false) String title,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			@RequestParam(defaultValue = "id,desc") String[] sort) {

		try {
			List<Sort.Order> orders = new ArrayList<Sort.Order>();

			if (sort[0].contains(",")) {
				// will sort more than 2 fields
				// sortOrder="field, direction"
				for (String sortOrder : sort) {
					String[] _sort = sortOrder.split(",");
					orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
				}
			} else {
				// sort=[field, direction]
				orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
			}

			List<BmwCrudDemo> cars = new ArrayList<BmwCrudDemo>();
			Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

			Page<BmwCrudDemo> pageCars;
			if (title == null)
				pageCars = bmwCrudRepository.findAll(pagingSort);
			else
				pageCars = bmwCrudRepository.findByCarContaining(title, pagingSort);

			cars = pageCars.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("cars", cars);
			response.put("currentPage", pageCars.getNumber());
			response.put("totalItems", pageCars.getTotalElements());
			response.put("totalPages", pageCars.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/cars/settled")
	public ResponseEntity<Map<String, Object>> findBySettled(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		try {
			List<BmwCrudDemo> cars = new ArrayList<BmwCrudDemo>();
			Pageable paging = PageRequest.of(page, size);

			Page<BmwCrudDemo> pageCars = bmwCrudRepository.findBySettled(true, paging);
			cars = pageCars.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("cars", cars);
			response.put("currentPage", pageCars.getNumber());
			response.put("totalItems", pageCars.getTotalElements());
			response.put("totalPages", pageCars.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/cars/{id}")
	public ResponseEntity<BmwCrudDemo> getCarById(@PathVariable("id") long id) {

		BmwCrudDemo cars = bmwCrudRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

		return new ResponseEntity<>(cars, HttpStatus.OK);

	}

	@PostMapping("/cars")
	public ResponseEntity<BmwCrudDemo> createCar(@RequestBody BmwCrudDemo bmwCrudDemo) {
		try {
			BmwCrudDemo _bmwCrudDemo = bmwCrudRepository
					.save(new BmwCrudDemo(bmwCrudDemo.getCar(), bmwCrudDemo.getDescription(), bmwCrudDemo.isSettled()));
			return new ResponseEntity<>(_bmwCrudDemo, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/cars/{id}")
	public ResponseEntity<BmwCrudDemo> updateCar(@PathVariable("id") long id, @RequestBody BmwCrudDemo bmwCrudDemo) {
		BmwCrudDemo _cars = bmwCrudRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

		_cars.setCar(bmwCrudDemo.getCar());
		_cars.setDescription(bmwCrudDemo.getDescription());
		_cars.setSettled(bmwCrudDemo.isSettled());

		return new ResponseEntity<>(bmwCrudRepository.save(_cars), HttpStatus.OK);
	}

	@DeleteMapping("/cars/{id}")
	public ResponseEntity<HttpStatus> deleteCar(@PathVariable("id") long id) {
		try {
			bmwCrudRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/cars")
	public ResponseEntity<HttpStatus> deleteAllCars() {
		try {
			bmwCrudRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}



}
