package com.EducCore.EduCore.controller;

import com.EducCore.EduCore.domain.User.CourseGroup;
import com.EducCore.EduCore.domain.User.Group;
import com.EducCore.EduCore.domain.User.GroupRequestDTO;
import com.EducCore.EduCore.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    // 1. LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return ResponseEntity.ok(groupRepository.findAll());
    }

    // 2. BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        return groupRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. CRIAR GRUPO
    @PostMapping
    @Transactional
    public ResponseEntity<Group> createGroup(@RequestBody GroupRequestDTO dto) {
        try {
            Group group = new Group();
            group.setName(dto.getName());

            if (dto.getCourses() != null) {
                List<CourseGroup> courses = dto.getCourses().stream().map(cDto -> {
                    CourseGroup cg = new CourseGroup();
                    cg.setIdCourse(cDto.getIdCourse());
                    cg.setNameCourse(cDto.getNameCourse());
                    cg.setNameGroup(dto.getName());
                    cg.setGroup(group);
                    return cg;
                }).collect(Collectors.toList());

                group.setCourses(courses);
            }

            Group savedGroup = groupRepository.save(group);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO NO POST: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 4. ATUALIZAR GRUPO
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody GroupRequestDTO dto) {
        try {
            return groupRepository.findById(id).map(existingGroup -> {
                existingGroup.setName(dto.getName());

                existingGroup.getCourses().clear();
                groupRepository.saveAndFlush(existingGroup);

                if (dto.getCourses() != null) {
                    List<CourseGroup> updatedCourses = dto.getCourses().stream().map(cDto -> {
                        CourseGroup cg = new CourseGroup();
                        cg.setIdCourse(cDto.getIdCourse());
                        cg.setNameCourse(cDto.getNameCourse());
                        cg.setNameGroup(dto.getName());
                        cg.setGroup(existingGroup);
                        return cg;
                    }).collect(Collectors.toList());

                    existingGroup.getCourses().addAll(updatedCourses);
                }

                return ResponseEntity.ok(groupRepository.save(existingGroup));
            }).orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO NO PUT: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 5. DELETAR GRUPO
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        try {
            return groupRepository.findById(id).map(group -> {
                groupRepository.delete(group);
                return ResponseEntity.noContent().<Void>build();
            }).orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO NO DELETE: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}