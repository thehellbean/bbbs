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
@RequestMapping("/posts")
class PostController {
    private final PostRepository repository;
    private final UserRepository userRepository;

    PostController(PostRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @GetMapping
    List<Post> all() {
        return repository.findAll();
    } 

    @PostMapping
    Post newPost(@RequestBody PostContext newPost) {
      newPost.getPost().setUser(userRepository.findById(newPost.getUserId()).get());
      return repository.save(newPost.getPost());
    }

    @GetMapping("{id}")
    EntityModel<Post> one(@PathVariable Long id) {
        Post post = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));

        return EntityModel.of(post, linkTo(methodOn(PostController.class).one(id)).withSelfRel(), linkTo(methodOn(PostController.class).all()).withRel("posts"));
    }

    @DeleteMapping("id}")
    void deletePost(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PutMapping("{id}")
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
