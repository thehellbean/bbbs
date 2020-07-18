package com.hellbean.bbbs;

class EntityNotFoundException extends RuntimeException {
    EntityNotFoundException(Long id) {
        super("Could not find entity with id " + id);
    }
}
