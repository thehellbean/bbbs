package com.hellbean.bbbs;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<User> all() {
        return repository.findAll();
    } 

    @PostMapping
    User newUser(@RequestBody User newUser) {
      return repository.save(newUser);
    }

    @GetMapping("{id}")
    EntityModel<User> one(@PathVariable Long id) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));

        return EntityModel.of(user, linkTo(methodOn(UserController.class).one(id)).withSelfRel(), linkTo(methodOn(UserController.class).all()).withRel("users"));
    }

    @DeleteMapping("{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        return repository.findById(id).map(user -> { 
            user.setName(newUser.getName());
            return repository.save(user); 
        }).orElseGet(() -> {
            newUser.setId(id);
            return repository.save(newUser);
        });
    }
}
