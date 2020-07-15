package com.hellbean.bbbs;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("/posts/{id}")
    Post replacePost(@RequestBody Post newPost, @PathVariable Long id) {
        return repository.findById(id).map(post -> { 
            post.setTitle(newPost.getTitle());
            post.setContent(newPost.getContent()); 
            return repository.save(post); 
        }).orElseGet(() -> {
            newPost.setId(id);
            return repository.save(newPost);
        });
    }
}
