package com.example.demo700.Controllers.EventOrganaizer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Services.EventOrganaizer.MatchNameService;

@RestController
@RequestMapping("/api/match-name")
public class MatchNameController {

	@Autowired
	private MatchNameService matchNameService;

	// ✅ ADD MATCH NAME
	@PostMapping("/{userId}")
	public ResponseEntity<?> addMatchName(@RequestBody MatchName matchName, @PathVariable String userId) {

		try {

			MatchName saved = matchNameService.addMatchName(matchName, userId);
			return ResponseEntity.status(HttpStatus.CREATED).body(saved);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

		}

	}

	// ✅ UPDATE MATCH NAME
	@PutMapping("/{matchNameId}/{userId}")
	public ResponseEntity<?> updateMatchName(@RequestBody MatchName matchName, @PathVariable String matchNameId,
			@PathVariable String userId) {

		try {

			MatchName updated = matchNameService.updateMatchName(matchName, userId, matchNameId);
			return ResponseEntity.ok(updated);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

		}

	}

	// ✅ FIND BY ID
	@GetMapping("/id/{id}")
	public ResponseEntity<?> findById(@PathVariable String id) {

		try {

			return ResponseEntity.ok(matchNameService.findById(id));

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		}

	}

	// ✅ FIND BY NAME (IGNORE CASE)
	@GetMapping("/name/{name}")
	public ResponseEntity<?> findByName(@PathVariable String name) {

		try {

			return ResponseEntity.ok(matchNameService.findByNameIgnoreCase(name));

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		}

	}

	// ✅ FIND BY MATCH ID
	@GetMapping("/match/{matchId}")
	public ResponseEntity<?> findByMatchId(@PathVariable String matchId) {

		try {

			return ResponseEntity.ok(matchNameService.findByMatchId(matchId));

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		}

	}

	// ✅ SEARCH BY NAME (CONTAINING)
	@GetMapping("/search")
	public ResponseEntity<?> searchByName(@RequestParam String name) {

		try {

			List<MatchName> list = matchNameService.findByNameContainingIgnoreCase(name);
			return ResponseEntity.ok(list);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		}

	}

	// ✅ SEE ALL
	@GetMapping("/all")
	public ResponseEntity<?> seeAll() {

		try {

			return ResponseEntity.ok(matchNameService.seeAll());

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

		}

	}

	// ✅ DELETE
	@DeleteMapping("/{matchNameId}/{userId}")
	public ResponseEntity<?> removeMatchName(@PathVariable String matchNameId, @PathVariable String userId) {

		try {

			boolean deleted = matchNameService.removeMatchName(matchNameId, userId);
			return ResponseEntity.ok(deleted);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

		}

	}
}