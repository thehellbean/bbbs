package com.hellbean.bbbs;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class PostController {
    private final PostRepository repository;

    PostController(PostRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/posts")
    List<Post> all() {
        return repository.findAll();
    } 

    @PostMapping("/posts")
    Post newPost(@RequestBody Post newPost) {
      return repository.save(newPost);
    }

    @GetMapping("/posts/{id}")
    Post one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }
}
