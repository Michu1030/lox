cmake_minimum_required(VERSION 3.10.0)

project(clox VERSION 0.1.0 LANGUAGES C)

set(SRC main.c
        chunk.c
        memory.c
        debug.c
        value.c
        vm.c
        compiler.c
        scanner.c
        object.c
        table.c
)

add_executable(clox ${SRC})