package com.hellbean.bbbs;

class PostNotFoundException extends RuntimeException {
    PostNotFoundException(Long id) {
        super("Could not find post " + id);
    }
}
