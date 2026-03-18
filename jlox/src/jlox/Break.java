package jlox;

class Break extends RuntimeException {
    Break() {
        super(null, null, false, false);
    }
}
