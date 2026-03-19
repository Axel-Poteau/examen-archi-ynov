package com.coworking.memberservice.controller;

import com.coworking.memberservice.model.Member;
import com.coworking.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Members", description = "Gestion des membres de la plateforme")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @Operation(summary = "Lister tous les membres")
    public List<Member> getAll() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un membre par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Membre trouvé"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé")
    })
    public Member getById(@Parameter(description = "ID du membre") @PathVariable Long id) {
        return memberService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau membre")
    @ApiResponse(responseCode = "201", description = "Membre créé avec succès")
    public ResponseEntity<Member> create(@RequestBody Member member) {
        return new ResponseEntity<>(memberService.create(member), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un membre")
    public Member update(@PathVariable Long id, @RequestBody Member member) {
        return memberService.update(id, member);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un membre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Membre supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "ID du membre") @PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/suspend")
    @Operation(summary = "Suspendre un membre")
    public void suspend(@PathVariable Long id) {
        memberService.suspend(id);
    }

    @PatchMapping("/{id}/unsuspend")
    @Operation(summary = "Réactiver un membre suspendu")
    public void unsuspend(@PathVariable Long id) {
        memberService.unsuspend(id);
    }
}
